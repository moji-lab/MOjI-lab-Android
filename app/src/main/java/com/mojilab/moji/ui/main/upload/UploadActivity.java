package com.mojilab.moji.ui.main.upload;

import android.app.Activity;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.os.Bundle;
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
import com.mojilab.moji.ui.main.upload.add.AddActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.ui.main.upload.change.ChangeOrderActivity;
import com.mojilab.moji.ui.main.upload.tag.TagActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.post.PostResponse;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import java.util.ArrayList;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

    static final int TAG_ACTIVITY = 333;
    static final int LOC_ACTIVITY = 444;
    static final int ADD_ACTIVITY = 555;
    static final int CHANGE_ACTIVITY = 666;

    final String TAG = "UploadAct ::";

    NetworkService networkService;

    SQLiteDatabase database;
    DatabaseHelper helper;

    CourseTable courseTable;

    ActivityUploadBinding binding;
    UploadViewModel viewModel;

    CourseRecyclerviewAdapter courseRecyclerviewAdapter;
    private ArrayList<CourseData> courseDataArrayList = new ArrayList<>();


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

        courseTable = new CourseTable(this);

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

    //다이어 로그 띄우기

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
        if(courseTable.getCount()==0){
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
                finish();
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }

    public void clickCompleteBtn() {

        //코스 추가 하고 돌아 왔을 때
        //장소 선택 했을 때

        if(courseDataArrayList == null)
            return;

        if (courseDataArrayList.size() > 0 && binding.etUploadActWriteLocation.getText().length() >0) {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.RED);

            binding.tvUploadActCompleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    postUploadResponse();
                    finish();
                }
            });


        } else {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.GRAY);
        }
    }

    public void setCourseRecyclerView() {


        if (courseDataArrayList != null)
            courseDataArrayList.clear();

        courseDataArrayList = courseTable.selectData();

        if(courseDataArrayList == null)
            return;

        Log.e("course.mainAddress",courseDataArrayList.get(0).mainAddress);

        RecyclerView mRecyclerView = binding.rvUploadActCourseList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        courseRecyclerviewAdapter = new CourseRecyclerviewAdapter(courseDataArrayList, this);
        courseRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(courseRecyclerviewAdapter);

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
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);

        PostUploadData postUploadData = settingPostData();

        Call<PostResponse> postUploadResponse = networkService.postUpboard("eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9.eyJpc3MiOiJtb2ppIiwidXNlcl9JZHgiOjMxfQ.pQCy6cFP8YR_q2qyTTRfnAGT4WdEI_a_h2Mgz6HaszY",postUploadData);
        postUploadResponse.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    Log.v(TAG, " Success");

                } else {
                    Log.v(TAG, "실패 메시지 = " + response.message());
                    Toast.makeText(getApplicationContext(), "업로드 통신 실패", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG);
            }
        });
    }

    public PostUploadData settingPostData(){
        //InfoData
        Boolean open = !binding.ivUploadActAlarmTag.isSelected(); //선택되면 closed임 따라서 !연산자 붙여줘야함
        String mainAddress = binding.etUploadActWriteLocation.getText().toString();
        //검색통신 하기 전까지
        //임의데이터
        String subAddress = "서울특별시";
        ArrayList<Integer> share = new ArrayList<>();
        //태그
        share.add(30);
        InfoData infoData = new InfoData(open,mainAddress,subAddress,share);


        //CourseData
        ArrayList<CourseData> courseDataArrayList = courseTable.selectData();
        ArrayList<CourseUploadData> courseUploadDataArrayList = new ArrayList<>();
        PhotosData photosData;
        for(int i = 0; i< courseDataArrayList.size();i++){
            CourseData courseDataItem = courseDataArrayList.get(i);

            ArrayList<PhotosData> photosDataArrayList = new ArrayList<>();

            for(int j = 0; j<courseDataItem.photos.size();j++){

                boolean isShared;

                if(courseDataItem.share.get(j)==1){
                    isShared = true;
                }
                else
                    isShared = false;

                photosData = new PhotosData(courseDataItem.photos.get(j),isShared);
                photosDataArrayList.add(photosData) ;
            }

            //tagList 넣어야함
            //임의의 값
            ArrayList<String> tagArrayList = new ArrayList<>();
            tagArrayList.add("전어축제");

            courseUploadDataArrayList.add(new CourseUploadData(courseDataItem,photosDataArrayList,tagArrayList));
        }

        PostUploadData postUploadData = new PostUploadData(infoData,courseUploadDataArrayList);

        return postUploadData;
    }


}
