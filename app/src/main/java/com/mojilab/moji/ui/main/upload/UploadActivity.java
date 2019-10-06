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

import com.google.gson.Gson;
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
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.StringTokenizer;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

    static final int TAG_ACTIVITY = 333;
    static final int LOC_ACTIVITY = 444;
    static final int ADD_ACTIVITY = 555;
    static final int CHANGE_ACTIVITY = 666;

    ArrayList<MultipartBody.Part> course_pictures;
    final String TAG = "UploadAct ::";

    NetworkService networkService;

    SQLiteDatabase database;
    DatabaseHelper helper;

    CourseTable coursesTable;

    ActivityUploadBinding binding;
    UploadViewModel viewModel;

    CourseRecyclerviewAdapter coursesRecyclerviewAdapter;

    ArrayList<CourseData> coursesDataArrayList = new ArrayList<>();
    ArrayList<PhotosData> photosDataArrayList;
    ArrayList<String> coursesIdxArrayList = new ArrayList<>();
    ArrayList<String> tagArrayList; //TAG INFO DATA

    @Override
    public int getLayoutId() {
        return R.layout.activity_upload;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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


       /* if (coursesDataArrayList.size() > 0 && binding.etUploadActWriteLocation.getText().length() >0) {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.RED);



        } else {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.GRAY);
        }*/
    }

    public void setCourseRecyclerView() {


        if (coursesDataArrayList != null)
            coursesDataArrayList.clear();

        coursesDataArrayList = coursesTable.selectData();

        if(coursesDataArrayList == null)
            return;

        Log.e("courses.mainAddress",coursesDataArrayList.get(0).mainAddress);

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

                if (data.getStringExtra("idxList") != null) {

                    Log.e("데이터", data.toString());
                    Log.e("데이터 detailed", data.getStringExtra("idxList"));

                    String str = data.getStringExtra("idxList");
                    String[] array = str.split(",");

                    int[] idxList = new int[array.length];

                    for (int i = 0; i < idxList.length; i++) {
                        idxList[i] = Integer.parseInt(array[i]);
                        Log.e("idxList,,,", idxList[i] + "");
                    }

                    if (idxList != null) {
                        Log.e("TAG_ACTIVITY,,,", idxList.length + "");
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


    // 게시글 등록
    public void postUploadResponse() {

        ArrayList<Integer> share = new ArrayList<>();
        ArrayList<String> tagInfo = new ArrayList<>();
        tagInfo.add("가을");
        share.add(31);

        Info info = new Info(true, "춘천시" , "춘천시" , share);
        ArrayList<Courses> courses = new ArrayList<>();
        courses.add(new Courses("제민이집", "서울시", "2019-10-06", "성공", tagInfo, 1, 1.1, 1.2));

        Attach attach = new Attach(info, courses);
        Gson gson = new Gson();
        String json = gson.toJson(attach);

        RequestBody requestBody = RequestBody.create(MediaType.parse("application/json"), gson.toJson(attach));
        ArrayList<PhotosData> photosData = new ArrayList<>();


        LinkedHashMap<String, RequestBody> mp= new LinkedHashMap<>();
        LinkedHashMap<String, MultipartBody.Part> pictureMap = new LinkedHashMap<>();



        RequestBody openRb = RequestBody.create(MediaType.parse("text/plain"), "true");
        mp.put("info.open", openRb);

        RequestBody mainRb = RequestBody.create(MediaType.parse("text/plain"), "춘천시");
        mp.put("info.mainAddress", mainRb);

        RequestBody subRb = RequestBody.create(MediaType.parse("text/plain"), "춘천시");
        mp.put("info.subAddress", subRb);

        RequestBody shareRb = RequestBody.create(MediaType.parse("text/plain"), "31");
        mp.put("info.share[0]", shareRb);


        for(int i=0;i<1;i++)
        {
            RequestBody manAddressRb = RequestBody.create(MediaType.parse("text/plain"), "춘천시");
            mp.put("courses["+i+"].mainAddress", manAddressRb);

            RequestBody subAddressRb = RequestBody.create(MediaType.parse("text/plain"), "춘천시");
            mp.put("courses["+i+"].subAddress", subAddressRb);

            RequestBody visitTimeRb = RequestBody.create(MediaType.parse("text/plain"), "2019-10-06");
            mp.put("courses["+i+"].visitTime", visitTimeRb);

            RequestBody contentRb = RequestBody.create(MediaType.parse("text/plain"), "확인");
            mp.put("courses["+i+"].content", contentRb);

            RequestBody tagInfoRb = RequestBody.create(MediaType.parse("text/plain"), "가을");
            mp.put("courses["+i+"].tagInfo[0]", tagInfoRb);

            RequestBody orderRb = RequestBody.create(MediaType.parse("text/plain"), "1");
            mp.put("courses["+i+"].order", orderRb);

            RequestBody latRb = RequestBody.create(MediaType.parse("text/plain"), "1.1");
            mp.put("courses["+i+"].lat", latRb);

            RequestBody lngRb = RequestBody.create(MediaType.parse("text/plain"), "1.2");
            mp.put("courses["+i+"].lng", lngRb);

            RequestBody photosRepresentRb = RequestBody.create(MediaType.parse("text/plain"), "true");
            mp.put("courses["+i+"].photos[0].represent", photosRepresentRb);


        }

/*        RequestBody requestBody = RequestBody.create(
                MediaType.parse("multipart/form-data"), // notice I'm using "multipart/form-data"
                json
        );*/


        Log.v(TAG, "게시글 등록 리스폰스");
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        PostUploadData postUploadData = settingPostData();

        Log.v(TAG, "보내기전 최종 info 메인 주소 = " + postUploadData.info.mainAddress);
        Log.v(TAG, "보내기전 최종 info 상세주소 = " + postUploadData.info.subAddress);
        Log.v(TAG, "보내기전 최종 info open = " + postUploadData.info.open);
        for (int i=0; i<postUploadData.info.share.size(); i++){
            Log.v(TAG, "보내기전 최종 info 친구 공유, "  + i + "번쨰 = " + postUploadData.info.share.get(i));
        }

        for(int i = 0; i<postUploadData.courses.size(); i++){
            Log.v(TAG, "보내기전 최종 " + i + "번째");
            Log.v(TAG, "보내기전 최종 courses 메인 주소 = " +postUploadData.courses.get(i).mainAddress);
            Log.v(TAG, "보내기전 최종 courses 상세 주소 = " + postUploadData.courses.get(i).subAddress);
            Log.v(TAG, "보내기전 최종 courses 시간 = " + postUploadData.courses.get(i).visitTime);
            Log.v(TAG, "보내기전 최종 courses 상세 내용 = " + postUploadData.courses.get(i).content);
            for (int j=0; j<postUploadData.courses.get(i).tagInfo.size(); j++){
                Log.v(TAG, "보내기전 최종 courses 태그  = " + j + "번째 " + postUploadData.courses.get(i).tagInfo.get(j));
            }
            Log.v(TAG, "보내기전 최종 courses 정렬 = " + postUploadData.courses.get(i).order);
            Log.v(TAG, "보내기전 최종 courses 위도 = " + postUploadData.courses.get(i).lat);
            Log.v(TAG, "보내기전 최종 courses 경도 = " + postUploadData.courses.get(i).lng);

            for (int j=0; j<photosDataArrayList.size(); j++){
                Log.v(TAG, "보내기전 최종 courses 사진 멀티파트 = " + j + "번째 " + photosDataArrayList.get(j).photo);
                Log.v(TAG, "보내기전 최종 courses 사진 visible = " + j + "번째 " + photosDataArrayList.get(j).represent);
            }

        }

/*
        String info_json = "\"info\":"  + gson.toJson(postUploadData.info);
        String courses_json = "\"courses\":" + gson.toJson(postUploadData.courses);
        Log.v(TAG, "info json = " + info_json);
        Log.v(TAG, "코스 json = " + courses_json);
*/

//        RequestBody info = RequestBody.create(MediaType.parse("text/plain"), info_json);
//        MultipartBody.Part info = MultipartBody.Part.createFormData("info", "info", rq_info);

//        MultipartBody.Part info = MultipartBody.Part.createFormData("info", info_json);

//        RequestBody courses = RequestBody.create(MediaType.parse("text/plain"), courses_json);
//        MultipartBody.Part courses = MultipartBody.Part.createFormData("courses", "courses", rq_courses);
//        MultipartBody.Part courses = MultipartBody.Part.createFormData("courses", courses_json);


/*    Map<String, RequestBody> map = new HashMap<>();
    map.put("info", rq_info);
    map.put("courses", rq_courses)*/;

        String token = SharedPreferenceController.INSTANCE.getAuthorization(getApplicationContext());

        Call<PostUploadResponse> postUploadResponse = networkService.postUpboard(token, mp, course_pictures);
        postUploadResponse.enqueue(new Callback<PostUploadResponse>() {
            @Override
            public void onResponse(Call<PostUploadResponse> call, Response<PostUploadResponse> response) {
                Log.v(TAG, "업로드 = " + response.toString());
                Log.v(TAG, "업로드 에러 = " + response.errorBody().toString());
                Log.v(TAG, "업로드 통신 = " + response.body().toString());
                if (response.isSuccessful()) {
                    Log.v(TAG, " 업로드 성공");
                    if(response.body() != null){
                       coursesIdxArrayList = response.body().getData();
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
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG);
            }
        });
    }

    //태그통신
    public PostUploadData settingPostData(){
        Log.v(TAG, "세팅");
        //InfoData
//        Boolean open = !binding.ivUploadActAlarmTag.isSelected(); //선택되면 closed임 따라서 !연산자 붙여줘야함
        Boolean open = binding.switchUploadActOpen.isChecked();
        String mainAddress = binding.etUploadActWriteLocation.getText().toString();
        //검색통신 하기 전까지
        //임의데이터
        String subAddress = binding.etUploadActWriteLocation.getText().toString();
        ArrayList<Integer> share = new ArrayList<>();
        //태그
        share.add(37);
        InfoData infoData = new InfoData(open,mainAddress,subAddress,share);

        //CourseData
        ArrayList<CourseData> coursesDataArrayList = coursesTable.selectData();
        ArrayList<CourseUploadData> coursesUploadDataArrayList = new ArrayList<>();

        PhotosData photosData;
        for(int i = 0; i< coursesDataArrayList.size();i++){

            CourseData coursesDataItem = coursesDataArrayList.get(i);

            // 문자열 자르기
            String tagStr = coursesDataArrayList.get(i).tag;
            tagArrayList = new ArrayList<>();
            StringTokenizer st = new StringTokenizer(tagStr, " ");
            String keyword;
            while(st.hasMoreTokens()){
                // # 제거
                keyword = st.nextToken();
                keyword = keyword.replace("#", "");
                Log.v(TAG, "태그 결과 = " + keyword);
                tagArrayList.add(keyword);
            }

            //이미지
            photosDataArrayList = new ArrayList<>();
            Log.v(TAG, "여기1");

            for(int j = 0; j<coursesDataItem.photos.size();j++){

                Log.v(TAG, "여기 2 = " + j);
                boolean isShared;

                if(coursesDataItem.share.get(j)==1){
                    isShared = true;
                }
                else
                    isShared = false;

                BitmapFactory.Options options = new BitmapFactory.Options();

                Log.v(TAG, "여기 3");
                InputStream input = new InputStream() {
                    @Override
                    public int read() throws IOException {
                        Log.v(TAG, "여기 4");
                        return 0;
                    }
                };
                Log.e("test transform String :", coursesDataItem.photos.get(j));
                Log.e("test transform Uri :", Uri.parse(coursesDataItem.photos.get(j)).toString());

                Uri pictureUri = getUriFromPath(coursesDataItem.photos.get(j));

                try {
                    Log.v(TAG, "여기 8");
                    input = getContentResolver().openInputStream(pictureUri);
                    Log.v(TAG, "여기 9");
                    Log.e("정상","input 완료");
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                    System.out.println("Error: " + e.getMessage());
                    Log.e("에러","input 완료");
                }

                Bitmap bitmap = BitmapFactory.decodeStream(input, null, options); // InputStream 으로부터 Bitmap 을 만들어 준다.
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.JPEG, 20, baos);
                RequestBody photoBody = RequestBody.create(MediaType.parse("image/jpg"), baos.toByteArray());
                File img = new File(coursesDataItem.photos.get(j)); // 가져온 파일의 이름을 알아내려고 사용합니다

                course_pictures = new ArrayList<>();
                course_pictures.add(MultipartBody.Part.createFormData("courses[0].photos[0].photo", img.getName(), photoBody));

                Log.v(TAG, "코스 postion = " + i);
                Log.v(TAG, "마지막 메인 주소 = " + coursesDataArrayList.get(i).mainAddress);
                Log.v(TAG, "마지막 상세 주소 = " + coursesDataArrayList.get(i).subAddress);
                Log.v(TAG, "마지막 시간 = " + coursesDataArrayList.get(i).visitTime);
                Log.v(TAG, "마지막 상세 내용 = " + coursesDataArrayList.get(i).content);
                Log.v(TAG, "마지막 태그 = " + coursesDataArrayList.get(i).tag);
                Log.v(TAG, "마지막 정렬 = " + coursesDataArrayList.get(i).order);
                Log.v(TAG, "마지막 위도 = " + coursesDataArrayList.get(i).lat);
                Log.v(TAG, "마지막 경도 = " + coursesDataArrayList.get(i).lng);
                Log.v(TAG, "마지막 사진 이름 = " + j + "번째 = " + img.getName());

                RequestBody represent = RequestBody.create(MediaType.parse("text.plain"), String.valueOf(isShared));
                photosData = new PhotosData(course_pictures.get(0), represent);
                photosDataArrayList.add(photosData) ;
            }

            coursesUploadDataArrayList.add(new CourseUploadData(coursesDataItem, tagArrayList));
        }

        for(int i = 0; i<coursesUploadDataArrayList.size(); i++){
            Log.v(TAG, "최종 " + i + "번째");
            Log.v(TAG, "최종 메인 주소 = " + coursesUploadDataArrayList.get(i).mainAddress);
            Log.v(TAG, "최종 상세 주소 = " + coursesUploadDataArrayList.get(i).subAddress);
            Log.v(TAG, "최종 시간 = " + coursesUploadDataArrayList.get(i).visitTime);
            Log.v(TAG, "최종 상세 내용 = " + coursesUploadDataArrayList.get(i).content);
            for (int j=0; j<coursesUploadDataArrayList.get(i).tagInfo.size(); j++){
                Log.v(TAG, "최종 태그  = " + j + "번째 " + coursesUploadDataArrayList.get(i).tagInfo.get(j));
            }
            Log.v(TAG, "최종 정렬 = " + coursesUploadDataArrayList.get(i).order);
            Log.v(TAG, "최종 위도 = " + coursesUploadDataArrayList.get(i).lat);
            Log.v(TAG, "최종 경도 = " + coursesUploadDataArrayList.get(i).lng);

            for (int j=0; j<photosDataArrayList.size(); j++){
                Log.v(TAG, "최종 사진 멀티파트 = " + j + "번째 " + photosDataArrayList.get(j).photo);
                Log.v(TAG, "최종 사진 visible = " + j + "번째 " + photosDataArrayList.get(j).represent);
            }
        }
        PostUploadData postUploadData = new PostUploadData(infoData,coursesUploadDataArrayList);

        return postUploadData;
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
