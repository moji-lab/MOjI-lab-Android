package com.mojilab.moji.ui.main.upload.add;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.DatePicker;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.data.CourseTagData;
import com.mojilab.moji.data.HashTagData;
import com.mojilab.moji.data.PhotoPath;
import com.mojilab.moji.data.RegisteredTagData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.ui.main.upload.tag.TagRecyclerviewAdapter;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetHashTagResponse;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.StringTokenizer;

public class AddActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    StringBuilder sb;

    ArrayList<String> coursePicPaths;
    ArrayList<String> courseTags;
    Double lat, lng;
    String insertKeyword;

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    Uri data;
    private  MultipartBody.Part profileImage = null;

    static final int ADDRESS_ACTIVITY = 123;
    final String TAG = "AddActivity ::";
    String inputText;

    SQLiteDatabase database;
    DatabaseHelper helper;

    AddActivity addActivity;
    String location;

    CourseData courseData = new CourseData();
    CourseTable courseTable;
    ArrayList<Integer> shares;

    ActivityAddBinding binding;
    AddViewModel viewModel;

    NetworkService networkService;

    UploadImgRecyclerviewAdapter uploadImgRecyclerviewAdapter;
    private ArrayList<UploadImgData> uploadImgDataArrayList = new ArrayList<>();

    HashTagRecyclerviewAdapter hashTagRecyclerviewAdapter;
    private ArrayList<HashTagData> hashTagDataArrayList = new ArrayList<>();

    int ACCESSGALLERY = 2018;

    @Override
    public int getLayoutId() {
        return R.layout.activity_add;
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("코스추가를 종료하시면, 작성중이던 데이터가 삭제됩니다. 그래도 종료하시겠습니까?");
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                finish();
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        coursePicPaths = new ArrayList<String>();
        courseTags = new ArrayList<String>();
        shares = new ArrayList<>();

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);

        addActivity = this;
        //내부db
        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        courseTable = new CourseTable(this);

        //통신
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        binding.etAddActTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                inputText = binding.etAddActTag.getText().toString();
                String[] array = inputText.split(" ");
                Log.v(TAG, "글자 = " + array[array.length-1].replace("#", ""));

                getSearchResponse(array[array.length-1].replace("#", ""));
                binding.rlAddActHashTagListContainer.setVisibility(View.VISIBLE);

            }
        });

    // 확인 버튼
        binding.rlAddActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUploadData();
            }
        });

        binding.ivAddActCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    @Override
    public void callAddCourseActivity() {
        Intent intent = new Intent(getApplicationContext(), AddCourseActivity.class);
        intent.putExtra("add", 10);
        startActivityForResult(intent, ADDRESS_ACTIVITY);
    }

    public void accessCameraGallery() {

        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(MediaStore.Images.Media.CONTENT_TYPE);
/*
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            intent = Intent.ACTION_OPEN_DOCUMENT;
        } else {
            intent = Intent.ACTION_PICK;
        }*/
        intent.setData(MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);

        Log.e("accessCameraGallery", intent.getData().toString());

        startActivityForResult(intent, ACCESSGALLERY);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.e("onActivityResult", "들어왔능가2" + data);

        if (requestCode == REQ_CODE_SELECT_IMAGE) {
            if (resultCode == Activity.RESULT_OK) {
                try {
                    Uri selectedImage;
                    selectedImage = data.getData();
                    Log.v(TAG,"처음 uri = " + selectedImage.toString());
                    SharedPreferenceController.INSTANCE.setPictureUrl(getApplicationContext(), selectedImage.toString());

                    Uri tempUri = Uri.parse(SharedPreferenceController.INSTANCE.getPictureUrl(getApplicationContext()));
                    BitmapFactory.Options options = new BitmapFactory.Options();

                    InputStream input = null; // here, you need to get your context.
                    try {
                        input = getContentResolver().openInputStream(tempUri);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }

                    Bitmap bitmap = BitmapFactory.decodeStream(input, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                    RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                    File img = new File(getRealPathFromURI(getApplicationContext(),tempUri)); // 가져온 파일의 이름을 알아내려고 사용합니다
                    String path = getRealPathFromURI(getApplicationContext(),tempUri);
                    Uri testUri = getUriFromPath(path);
                    Log.v(TAG, "테스트uri = " + tempUri.toString());
                    Log.v(TAG, "사진 url = " + getRealPathFromURI(getApplicationContext(),tempUri).toString());
                    String picPath = getRealPathFromURI(getApplicationContext(),tempUri).toString();
                    SharedPreferenceController.INSTANCE.setPictureUrl(getApplicationContext(),getRealPathFromURI(getApplicationContext(),tempUri).toString());
//                    SharedPreferenceController.INSTANCE.setPictureUrl(getApplicationContext(),getRealPathFromURI(getApplicationContext(),selectedImage).toString());


                    // 코스 사진 절대 경로 배열에 추가
                    coursePicPaths.add(picPath);
                    shares.add(1);

                    Log.v("asfd","디비 값 = " + (helper.getResult()));
                    profileImage = MultipartBody.Part.createFormData("profileImage", img.getName(), photoBody);

//                    String savedUrl = SharedPreferenceController.INSTANCE.getPictureUrl(getApplicationContext());
//                    Log.v(TAG, "db에서 갖고온 사진 uri = " + savedUrl);
//                    Uri tempUri = Uri.parse(savedUrl);
//                    Log.v(TAG,"저장 uri = " + tempUri.toString());
                    setCourseRecyclerView(data.getData().toString());

                    // 선택한 이미지를 해당 이미지뷰에 적용
                    Log.v(TAG, "선택 이미지 =  " + img.getName());

                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }

        if (requestCode == ACCESSGALLERY) {
            Log.e("onActivityResult", "들어왔능가1" + data);
            Log.e("onActivityResult", "들어왔능가0" + data);

            if (data != null) {
                //사진 선택하지 않고 나왔을 때 처리
                if (data.getClipData() != null) {
                    int count = data.getClipData().getItemCount();
                    for (int i = 0; i < count; i++) {
                        Uri imageUri = data.getClipData().getItemAt(i).getUri();
                        Log.e("test transform origin :", imageUri.toString());
                        Log.e("URI0:", imageUri.toString());
                        //Log.e("URI1:", "+++" + getRealPathFromURI(imageUri) + "+++");
                        //File imgFile = new File(getRealPathFromURI(imageUri));

                        setCourseRecyclerView(imageUri.toString());
                    }
                }
            }
            return;

        }

        if (requestCode == ADDRESS_ACTIVITY) {
            if(data == null){
                return;
            }
            location = data.getStringExtra("main");
            lat = data.getDoubleExtra("lat", 0.0);
            lng = data.getDoubleExtra("lng", 0.0);
            binding.etAddActWriteLocation.setText(location);
            binding.ivAddActLocSelector.setSelected(true);
        }
    }

    // 이미지 파일을 확장자까지 표시해주는 메소드
    public String getRealPathFromURI(Context context, Uri contentUri) {
        Cursor cursor = null;
        try {
            String[] proj = {MediaStore.Images.Media.DATA};
            cursor = context.getContentResolver().query(contentUri, proj, null, null, null);
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            return cursor.getString(column_index);
        } finally {
            if (cursor != null) {
                cursor.close();
            }
        }
    }

    public Uri getUriFromPath(String path){

//        String fileName= "file:///sdcard/DCIM/Camera/2013_07_07_12345.jpg";
        Uri fileUri = Uri.parse(path);
        String filePath = fileUri.getPath();

        Cursor c = getContentResolver().query( MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                null, "_data = '" + filePath + "'", null, null );
        c.moveToNext();
        int id = c.getInt( c.getColumnIndex( "_id" ) );
        Uri uri = ContentUris.withAppendedId( MediaStore.Images.Media.EXTERNAL_CONTENT_URI, id );
        return uri;

    }

    @Override
    public void callDatePicker() {
        Calendar cal = Calendar.getInstance();

        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH);
        int day = cal.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog dialog = new DatePickerDialog(this, listener, year, month, day);
        dialog.show();
    }

    private DatePickerDialog.OnDateSetListener listener = new DatePickerDialog.OnDateSetListener() {
        @Override
        public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {

            String yearValue = String.valueOf(year);
            String monthValue = String.valueOf((monthOfYear+1));
            String dayValue = String.valueOf(dayOfMonth);

            // 10보다 작은 경우 앞에 0추가
            if((monthOfYear+1) < 10) monthValue = "0" + monthValue;
            if(dayOfMonth < 10) dayValue = "0" + dayValue;

            binding.etAddActSelectDate.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
            courseData.visitTime = yearValue + "-" + monthValue + "-" + dayValue;

        }
    };

    public void storeUploadData() {

        if (binding.etAddActContents.getText().length() == 0 ||
                uploadImgDataArrayList.size() == 0 ||
                binding.etAddActWriteLocation.getText().length() == 0 ||
                binding.etAddActSelectDate.getText().length() == 0) {
            Toast.makeText(this, "모든 양식을 채워야 저장이 가능합니다.", Toast.LENGTH_SHORT).show();
            return;
        }

        // 내부 DB에 태그 데이터 한 번에 저장
        CourseTagData tagData;
        for (String tag : courseTags){
            tagData = new CourseTagData(0, tag);
            helper.insertTag(tagData);
        }

        courseData.mainAddress = location;
        courseData.subAddress = location;
        courseData.lat = lat;
        courseData.lng = lng;

        courseData.content = binding.etAddActContents.getText().toString();

        //해시태그
        String str = binding.etAddActTag.getText().toString();
        courseData.photos = new ArrayList<>();
        courseData.share = new ArrayList<>();

        // 내부 DB에 사진 경로들 한 번에 저장
        PhotoPath tempPhotoPath;
        for (String photoPath : coursePicPaths){
            tempPhotoPath = new PhotoPath(0, photoPath, 1);
            helper.insertPhoto(tempPhotoPath);
            courseData.photos.add(photoPath);
        }

        for (Integer sharesNum : shares) {

            //잠기면 true 1
            //안잠기만 false 0
            // 일단 전부 1로
            Log.v(TAG, "확인확인");
            courseData.share.add(sharesNum);
        }

        courseData.order = courseTable.getCount() + 1; //데이터 개수 조회 한 후, 삽입
        //해시태그 !!!
        courseData.tag = str;

        //데이터 insert
        courseTable.insertData(courseData);



        Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void setCourseRecyclerView(String testImg) {

        binding.rvAddActImgList.setVisibility(View.VISIBLE);
        UploadImgData uploadImgData = new UploadImgData(0, false, true, testImg);
        // 안잠기면 0
//        courseData.share.add(0);

        uploadImgDataArrayList.add(uploadImgData);

        RecyclerView mRecyclerView = binding.rvAddActImgList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        uploadImgRecyclerviewAdapter = new UploadImgRecyclerviewAdapter(uploadImgDataArrayList, this);
        mRecyclerView.setAdapter(uploadImgRecyclerviewAdapter);

        uploadImgRecyclerviewAdapter.setOnItemClickListener(new UploadImgRecyclerviewAdapter.setOnItemClickListener() {

            @Override
            public void onItemClick(View v, int position) {
                Log.e("이미지 어뎁터리스너", "삭제 버튼 클릭 포지션 = " + position);
                coursePicPaths.remove(position);
                shares.remove(position);
            }
        });

        uploadImgRecyclerviewAdapter.setOnLockItemClickListener(new UploadImgRecyclerviewAdapter.setOnLockItemClickListener() {

            @Override
            public void onLockItemClick(View v, int position, boolean isLocked) {
                if(isLocked){
                    Log.v(TAG, "잠금 O");
                    shares.set(position, 0);
                }
                else{
                    Log.v(TAG, "잠금 X");
                    shares.set(position, 1);
                }
            }

        });

    }


    public void setHashTagRecyclerView(ArrayList<HashTagData> hashTagDataArrayList) {

        RecyclerView mRecyclerView = binding.rvAddActHashTagList;
        if(hashTagDataArrayList.size()>0) Log.v(TAG, "해시 태그 데이터 = " + hashTagDataArrayList.get(0));
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        Log.v(TAG, "여기1");

        hashTagRecyclerviewAdapter = new HashTagRecyclerviewAdapter(hashTagDataArrayList, this);
        hashTagRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(hashTagRecyclerviewAdapter);

        hashTagRecyclerviewAdapter.setOnItemClickListener(new HashTagRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, HashTagData hashTagData) {
                binding.etAddActTag.setText(binding.etAddActTag.getText().toString().substring(0, binding.etAddActTag.getText().toString().length() - insertKeyword.length()-1));
                binding.etAddActTag.append("#" + hashTagData.tagInfo + " ");
                binding.etAddActTag.requestFocus();

                binding.etAddActTag.setSelection(binding.etAddActTag.length());
            }
        });
    }

    public void getSearchResponse(final String keyword) {

        insertKeyword = keyword;

        Call<GetHashTagResponse> getHashTagResponse = networkService.getHashTagResponse(keyword);

        getHashTagResponse.enqueue(new Callback<GetHashTagResponse>() {
            @Override
            public void onResponse(Call<GetHashTagResponse> call, Response<GetHashTagResponse> response) {
                Log.v(TAG, "해시태 조회 성공"+response.toString());
                if (response.body().getStatus() == 200) {
                    Log.v(TAG, "해시태 조회 성공");

                    setHashTagRecyclerView(response.body().getData());

                } else if (response.body().getStatus() == 404) {
                    Log.v(TAG, "해시태그를 찾을 수 없습니다."+keyword);

                    ArrayList<HashTagData> hashTagDataArrayList = new ArrayList<>();

                    setHashTagRecyclerView(hashTagDataArrayList);

                } else {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                }
            }

            @Override
            public void onFailure(Call<GetHashTagResponse> call, Throwable t) {
                Log.v(TAG+"::", t.toString());

            }
        });
    }

    // 방 배경 이미지 변경
    public void changeImage(){
        Intent intent = new Intent(Intent.ACTION_PICK);
        intent.setType(android.provider.MediaStore.Images.Media.CONTENT_TYPE);
        intent.setData(android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // intent.setType("image/*");
        startActivityForResult(intent, REQ_CODE_SELECT_IMAGE);
    }

}