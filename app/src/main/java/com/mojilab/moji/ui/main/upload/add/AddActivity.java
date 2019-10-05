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
import android.os.Build;
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
import com.mojilab.moji.data.HashTagData;
import com.mojilab.moji.data.PostHashTagsData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetHashTagResponse;
import com.mojilab.moji.util.network.post.PostResponse;

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

public class AddActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    private static final int REQ_CODE_SELECT_IMAGE = 100;
    Uri data;
    private  MultipartBody.Part profileImage = null;

    static final int ADDRESS_ACTIVITY = 123;
    final String TAG = "AddActivity ::";

    SQLiteDatabase database;
    DatabaseHelper helper;

    String location;

    CourseData courseData = new CourseData();
    CourseTable courseTable;

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

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);

        //내부db
        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        courseTable = new CourseTable(this);

        //통신
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);


        // 확인 버튼
        binding.rlAddActAddBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                storeUploadData();
            }
        });

        setWatcherEvent();
        test();
    }

    public void test() {

        Log.e("test", binding.etAddActSelectDate.getText().toString() + "/" + binding.etAddActTag.getText() + "/" + binding.etAddActContents.getText());

/*        if(binding.etAddActSelectDate.getText() != null &&
                 binding.etAddActContents.getText() != null && binding.etAddActTag.getText() != null){
            viewModel.isSubmit.setValue(true);
            binding.rlAddActAddBtn.setSelected(true);
            Log.e("null","?");
        }else {
            binding.rlAddActAddBtn.setSelected(false);
            Log.e("not null","?");
        }*/

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
                    SharedPreferenceController.INSTANCE.setPictureUrl(getApplicationContext(),getRealPathFromURI(getApplicationContext(),tempUri).toString());
//                    SharedPreferenceController.INSTANCE.setPictureUrl(getApplicationContext(),getRealPathFromURI(getApplicationContext(),selectedImage).toString());

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




/*    public String getRealPathFromURI(Uri contentUri) {
        Cursor cursor = null;

        String[] proj = new String[]{MediaStore.Images.Media.DATA};
        cursor = this.getContentResolver().query(contentUri, proj, null, null, null);
        int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }*/

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
            binding.etAddActSelectDate.setText(year + "년 " + (monthOfYear+1) + "월 " + dayOfMonth + "일");
            courseData.visitTime = year + "-" + (monthOfYear+1) + "-" + dayOfMonth;
        }
    };

    public void storeUploadData() {

/*
        if (binding.etAddActContents.getText().length() == 0 ||
                uploadImgDataArrayList.size() == 0 ||
                binding.etAddActWriteLocation.getText().length() == 0 ||
                binding.etAddActSelectDate.getText().length() == 0) {

            Toast.makeText(this, "모든 양식을 채워야 저장이 가능합니다.", Toast.LENGTH_SHORT).show();

            return;
        }
*/

        courseData.mainAddress = location;
        courseData.subAddress = "경기도 안양시 만안구";
        courseData.lat = (float) 1.3;
        courseData.log = (float) 3.5;

        courseData.content = binding.etAddActContents.getText().toString();

        //해시태그
        String str = binding.etAddActTag.getText().toString();
        str = str.replace("#", " ");
        courseData.tag = str;


        courseData.photos = new ArrayList<>();
        courseData.share = new ArrayList<>();

        for (int i = 0; i < uploadImgDataArrayList.size(); i++) {

            courseData.photos.add(uploadImgDataArrayList.get(i).image.toString());
            //잠기면 true 1
            //안잠기만 false 0
            if (uploadImgDataArrayList.get(i).lock)
                courseData.share.add(1);
            else
                courseData.share.add(0);
        }

        courseData.order = courseTable.getCount() + 1; //데이터 개수 조회 한 후, 삽입

        //데이터 insert
        courseTable.insertData(courseData);

        //해시태그 !!!


        Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
        setResult(Activity.RESULT_OK, intent);

        finish();
    }

    public void setCourseRecyclerView(String testImg) {

        binding.rvAddActImgList.setVisibility(View.VISIBLE);

        UploadImgData uploadImgData = new UploadImgData(0, false, true, testImg);

        uploadImgDataArrayList.add(uploadImgData);

        RecyclerView mRecyclerView = binding.rvAddActImgList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this, RecyclerView.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        uploadImgRecyclerviewAdapter = new UploadImgRecyclerviewAdapter(uploadImgDataArrayList, this);
        mRecyclerView.setAdapter(uploadImgRecyclerviewAdapter);
    }


    public void setFocusedEvent() {
        binding.etAddActTag.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean focus) {
                Log.e("test", String.valueOf(focus));
                if (focus) {
                    scrollToEnd(); //스크롤 처리
                    binding.rlAddActHashTagListContainer.setVisibility(View.VISIBLE);
                    if(binding.etAddActTag.getText().length() > 0){
                        String keyword = binding.etAddActTag.getText().toString();
                        keyword = keyword.replace("#","");
                        getSearchResponse(keyword);
                    }
                } else
                    binding.rlAddActHashTagListContainer.setVisibility(View.GONE); // 얘를 어칸담
            }
        });
    }

    public void setWatcherEvent() {
        binding.etAddActTag.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.v(TAG, " onTextChanged");

            }

            @Override
            public void afterTextChanged(Editable editable) {
                scrollToEnd(); //스크롤 처리
            }
        });
    }

    public void setHashTagRecyclerView(ArrayList<HashTagData> hashTagDataArrayList) {

        RecyclerView mRecyclerView = binding.rvAddActHashTagList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        hashTagRecyclerviewAdapter = new HashTagRecyclerviewAdapter(hashTagDataArrayList, this);
        hashTagRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(hashTagRecyclerviewAdapter);

        hashTagRecyclerviewAdapter.setOnItemClickListener(new HashTagRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, HashTagData hashTagData) {
                binding.etAddActTag.setText("#" + hashTagData.tagInfo);
            }
        });
    }

    public void scrollToEnd() {
        binding.scrollAddAct.post(new Runnable() {
            @Override
            public void run() {
                binding.scrollAddAct.fullScroll(View.FOCUS_DOWN);
            }

        });

        Log.v(TAG, "afterTextChanged");

        binding.rlAddActHashTagListContainer.setVisibility(View.VISIBLE);

        if (binding.etAddActTag.isFocusable())
        {
            if(binding.etAddActTag.getText().length() > 0){
                Log.v(TAG, "afterTextChanged - r길이 0 이상");
                String keyword = binding.etAddActTag.getText().toString();
                keyword = keyword.replace("#","");
                getSearchResponse(keyword);
            }
        }
    }


    public void getSearchResponse(final String keyword) {

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
