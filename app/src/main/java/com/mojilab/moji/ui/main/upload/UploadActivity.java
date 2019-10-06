package com.mojilab.moji.ui.main.upload;

import android.app.Activity;
import android.content.ContentUris;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.*;
import com.mojilab.moji.databinding.ActivityUploadBinding;
import com.mojilab.moji.ui.main.MainActivity;
import com.mojilab.moji.ui.main.upload.add.AddActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.ui.main.upload.change.ChangeOrderActivity;
import com.mojilab.moji.ui.main.upload.tag.TagActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.post.PostResponse;
import com.mojilab.moji.util.network.post.PostUploadResponse;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.io.*;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.StringTokenizer;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

    static final int TAG_ACTIVITY = 333;
    static final int LOC_ACTIVITY = 444;
    static final int ADD_ACTIVITY = 555;
    static final int CHANGE_ACTIVITY = 666;

    // 멀티 파트 사진 배열
    ArrayList<MultipartBody.Part> course_pictures;
    final String TAG = "UploadAct ::";

    NetworkService networkService;

    // 사진 uri
    Uri pictureUri;

    // 친구 태그 리스트
    ArrayList<Integer> share;

    SQLiteDatabase database;
    DatabaseHelper helper;
    LinkedHashMap<String, RequestBody> map;

    // multipart를 포함한 사진 데이터
    PhotosData photosData;
    CourseTable coursesTable;

    ActivityUploadBinding binding;
    UploadViewModel viewModel;

    // info 데이터
    InfoData infoData;

    // info + 코스 데이터
    PostUploadData postUploadData;

    // 내부 DB에서 가져오는 코스 데이터 저장용 리스트(photo값 = string)
    ArrayList<CourseData> coursesDataArrayList;

    // 사진 객체를 제외한 코스 업로드용 객체 배열
    ArrayList<CourseUploadData> coursesUploadDataArrayList;

    // 코스 리사이클러뷰
    CourseRecyclerviewAdapter coursesRecyclerviewAdapter;

    // 업로드용 사진 객체 배열
    ArrayList<PhotosData> photosUploadDataArrayList;
    ArrayList<String> coursesIdxArrayList = new ArrayList<>();
    ArrayList<String> tagArrayList; //TAG INFO DATA

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        share = new ArrayList<>();

        // 해시맵
        map = new LinkedHashMap<>();

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(UploadViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setUploadViewModel(viewModel);

        helper = new DatabaseHelper(this);
        database = helper.getWritableDatabase();

        coursesTable = new CourseTable(this);

        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        binding.ivUploadActAlarmTag.setSelected(true);
        binding.rlUploadActAlarmContainer.setVisibility(View.GONE);

        setCourseRecyclerView();

        binding.ivUploadActCloseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    // 사진 경로로부터 uri 얻는 메서드
    public Uri getUriFromPath(String path){
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
    public void callAddCourseActivity() {
        startActivityForResult(new Intent(getApplicationContext(), AddCourseActivity.class), LOC_ACTIVITY);
    }

    @Override
    public void callAddActivity() {
        startActivityForResult(new Intent(getApplicationContext(), AddActivity.class), ADD_ACTIVITY);
    }

    @Override
    public void callChangeOrderActivity() {
        if(coursesTable.getCount()==0){
            Toast.makeText(this, "데이터를 입력 한 후, 순셔변경 메뉴를 이용 하실 수 있습니다.", Toast.LENGTH_SHORT).show();
            return ;
        }
        startActivityForResult(new Intent(getApplicationContext(), ChangeOrderActivity.class), CHANGE_ACTIVITY);
    }

    @Override
    public void callTagActivity() {

        startActivityForResult(new Intent(getApplicationContext(), TagActivity.class), TAG_ACTIVITY);
    }

    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("기록하기를 종료하시면, 등록한 코스 이외의 데이터가 삭제됩니다. 그래도 종료하시겠습니까?");
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    public void  clickCompleteBtn() {

        //코스 추가 하고 돌아 왔을 때
        //장소 선택 했을 때
        if(coursesDataArrayList == null)
            return;

        binding.tvUploadActCompleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.v(TAG, "확인 누름");
                if(binding.etUploadActWriteLocation.getText().toString().equals("") || binding.etUploadActWriteLocation.getText() == null){
                    Toast.makeText(getApplicationContext(), "지역을 입력해주세요" , Toast.LENGTH_LONG).show();
                }
                else{
                    postUploadResponse();
                    finish();
                }
            }
        });

       if (coursesDataArrayList.size() > 0 && binding.etUploadActWriteLocation.getText().length() >0) {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.RED);


        } else {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.GRAY);
        }
    }

    public void setCourseRecyclerView() {

        if (coursesDataArrayList != null)
            coursesDataArrayList.clear();

        coursesDataArrayList = coursesTable.selectData();

        if(coursesDataArrayList == null)
            return;


        RecyclerView mRecyclerView = binding.rvUploadActCourseList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        coursesRecyclerviewAdapter = new CourseRecyclerviewAdapter(coursesDataArrayList, this);
        coursesRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(coursesRecyclerviewAdapter);

        clickCompleteBtn();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAG_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                // 재초기화
                share = new ArrayList<>();

                if (data.getStringExtra("idxList") != null) {

                    String str = data.getStringExtra("idxList");
                    String[] array = str.split(",");

                    int[] idxList = new int[array.length];
                    int myUserId = SharedPreferenceController.INSTANCE.getUserId(getApplicationContext());

                    for (int i = 0; i < idxList.length; i++) {
                        idxList[i] = Integer.parseInt(array[i]);
                        // 친구 태그 추가
                        // 나랑 다른 id 모두 추가
                        if(idxList[i] != myUserId) share.add(idxList[i]);
                    }

                    if (idxList != null) {
                        binding.tvUploadActAlarmTagCnt.setText(idxList.length + "");
                        binding.rlUploadActAlarmContainer.setVisibility(View.VISIBLE);
                        //통신에도 넣어야 함
                    } else
                        Log.e("TAG_ACTIVITY,,,", "널값!");
                }
            }
        }

        if (requestCode == ADD_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                setCourseRecyclerView();
            }
        }

        if (requestCode == LOC_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                binding.etUploadActWriteLocation.setText(data.getStringExtra("main"));
                binding.ivUploadActLocSelector.setSelected(true);

                clickCompleteBtn();
            }
        }

        if (requestCode == CHANGE_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                setCourseRecyclerView();
            }
        }
    }

    // DB에서 저장된 코스 데이터 가져오기
    public PostUploadData settingPostData(){

        // 사진 객체를 제외한 코스 업로드용 객체 배열 초기화
        coursesUploadDataArrayList = new ArrayList<>();

        // 업로드용 사진 배열 초기화
        photosUploadDataArrayList = new ArrayList<>();

        // 사진 멀티 파트 저장
        course_pictures = new ArrayList<>();

        //InfoData
        Boolean open = binding.switchUploadActOpen.isChecked();
        String mainAddress = binding.etUploadActWriteLocation.getText().toString();
        String subAddress = binding.etUploadActWriteLocation.getText().toString();

        // info 데이터 설정
        infoData = new InfoData(open,mainAddress,subAddress, share);

        // 코스 데이터 item
        CourseData coursesDataItem;

        // 코스 개수만큼
        for(int i = 0; i< coursesDataArrayList.size();i++){

            coursesDataItem = coursesDataArrayList.get(i);

            // 문자열 자르기
            String tagStr = coursesDataArrayList.get(i).tag;
            tagArrayList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(tagStr, " ");
            String keyword;
            while(st.hasMoreTokens()){
                // # 제거
                keyword = st.nextToken();
                keyword = keyword.replace("#", "");
                tagArrayList.add(keyword);
            }

            // 각 코스 안에 있는 사진 개수만큼
            for(int j = 0; j<coursesDataItem.photos.size();j++){
                boolean isShared;
                if(coursesDataItem.share.get(j)==1) isShared = true;
                else isShared = false;

                BitmapFactory.Options options = new BitmapFactory.Options();
                InputStream input = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        return 0;
                    }
                };

                // 경로로부터 uri 받아오기
                pictureUri = getUriFromPath(coursesDataItem.photos.get(j));
                try {
                    input = getContentResolver().openInputStream(pictureUri);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                Bitmap bitmap = BitmapFactory.decodeStream(input, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                File img = new File(coursesDataItem.photos.get(j)); // 가져온 파일의 이름을 알아내려고 사용합니다

                MultipartBody.Part photoPart = MultipartBody.Part.createFormData("courses[" + i + "].photos[" + j + "].photo", img.getName(), photoBody);
                // 사진 정보 추가
                course_pictures.add(photoPart);

                RequestBody represent = RequestBody.create(MediaType.parse("text.plain"), String.valueOf(isShared));
                map.put("courses["+i+"].photos[" + j + "].represent", represent);

                photosData = new PhotosData(photoPart, represent);
                photosUploadDataArrayList.add(photosData) ;
            }

            coursesUploadDataArrayList.add(new CourseUploadData(coursesDataItem, tagArrayList));
        }

        PostUploadData postUploadData = new PostUploadData(infoData,coursesUploadDataArrayList);

        return postUploadData;
    }

    // 게시글 저장
    public LinkedHashMap saveHashMap(){
        RequestBody Rb;
        // info 데이터
        Rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postUploadData.info.open));
        map.put("info.open", Rb);

        Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.info.mainAddress);
        map.put("info.mainAddress", Rb);

        Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.info.subAddress);
        map.put("info.subAddress", Rb);

        for (int i=0; i<postUploadData.info.share.size(); i++){
            Rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postUploadData.info.share.get(i)));
            map.put("info.share[" + i + "]", Rb);
        }
        // 코스 개수만큼 반복
        for(int i=0;i<postUploadData.courses.size();i++) {
            Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.courses.get(i).mainAddress);
            map.put("courses["+i+"].mainAddress", Rb);

            Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.courses.get(i).subAddress);
            map.put("courses["+i+"].subAddress", Rb);

            Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.courses.get(i).visitTime);
            map.put("courses["+i+"].visitTime", Rb);

            Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.courses.get(i).content);
            map.put("courses["+i+"].content", Rb);

            for (int j = 0; j < postUploadData.courses.get(i).tagInfo.size(); j++) {
                Rb = RequestBody.create(MediaType.parse("text/plain"), postUploadData.courses.get(i).tagInfo.get(j));
                map.put("courses["+i+"].tagInfo[" + j + "]", Rb);
            }
            Rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postUploadData.courses.get(i).order));
            map.put("courses["+i+"].order", Rb);

            Rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postUploadData.courses.get(i).lat));
            map.put("courses["+i+"].lat", Rb);

            Rb = RequestBody.create(MediaType.parse("text/plain"), String.valueOf(postUploadData.courses.get(i).lng));
            map.put("courses["+i+"].lng", Rb);

        }
        return map;
    }

    // 게시글 등록
    public void postUploadResponse() {

        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        // 반환값 저장
        postUploadData = settingPostData();

        Log.v(TAG, "여기 2 = " + postUploadData.toString());
        String token = SharedPreferenceController.INSTANCE.getAuthorization(getApplicationContext());

        Log.v(TAG, "여기 3 사진 멀티파트 크기 = " + course_pictures.toString());
        Log.v(TAG, "여기 4 save 반환값 = " + saveHashMap().toString());
        Call<PostUploadResponse> postUploadResponse = networkService.postUpboard(token, saveHashMap(), course_pictures);
        postUploadResponse.enqueue(new Callback<PostUploadResponse>() {
            @Override
            public void onResponse(Call<PostUploadResponse> call, Response<PostUploadResponse> response) {
                Log.v(TAG, "업로드 = " + response.toString());
                if (response.isSuccessful()) {
                    Log.v(TAG, " 업로드 성공");
                    if(response.body() != null){
                       coursesIdxArrayList = response.body().getData().getCourseIdx();
                        Log.v(TAG, " 업로드 성공2");
                       //태그정보, 코스정보
                       postHashTagResponse(tagArrayList, coursesIdxArrayList);
                    }
                } else {
                    Log.v(TAG, "업로드 실패 메시지 = " + response.message());
                    Toast.makeText(getApplicationContext(), "업로드 통신 실패", Toast.LENGTH_LONG);
                }
            }
            @Override
            public void onFailure(Call<PostUploadResponse> call, Throwable t) {
                Log.v(TAG, "업로드 서버 연결 실패 = " + t.toString());
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG);
            }
        });
    }

    // 해시태그 등록 통신
    public void postHashTagResponse(ArrayList<String> tagInfo, ArrayList<String> coursesIdxList) {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        ArrayList<HashTagData> hashTagDataArrayList = new ArrayList<>();
        //코스아이디 리스트 캐수만큼
        for(int i =0; i<coursesIdxList.size();i++){
            hashTagDataArrayList.add(new HashTagData(tagInfo.get(i)));

            //리퀘스트바디 양식에 맞춰서
            PostHashTagsData postHashTagsData = new PostHashTagsData(coursesIdxList.get(i),hashTagDataArrayList);
            Call<PostResponse> postHashTagResponse = networkService.postHashTag(postHashTagsData);
            postHashTagResponse.enqueue(new Callback<PostResponse>() {
                @Override
                public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                    if (response.isSuccessful()) {
                        Log.v(TAG, "기록 데이터 삽입 Success");

                    } else {
                        Log.v(TAG, "해시태그 실패");
                        Log.v(TAG, "실패 메시지 = " + response.message());
                        Toast.makeText(getApplicationContext(), "해시태그 통신 실패", Toast.LENGTH_LONG);
                    }
                }

                @Override
                public void onFailure(Call<PostResponse> call, Throwable t) {
                    Log.v(TAG, "서버 연결 실패 = " + t.toString());
                    Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG);
                }
            });
        }
    }
}
