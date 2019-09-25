package com.mojilab.moji.ui.main.upload;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.CourseData;
import com.mojilab.moji.databinding.ActivityUploadBinding;
import com.mojilab.moji.ui.main.upload.add.AddActivity;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;
import com.mojilab.moji.ui.main.upload.change.ChangeOrderActivity;
import com.mojilab.moji.ui.main.upload.tag.TagActivity;
import com.mojilab.moji.util.localdb.CourseTable;
import com.mojilab.moji.util.localdb.DatabaseHelper;

import java.util.ArrayList;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

    static final int TAG_ACTIVITY= 333;
    static final int LOC_ACTIVITY= 444;
    static final int ADD_ACTIVITY= 555;
    static final int CHANGE_ACTIVITY= 666;

    SQLiteDatabase database;
    DatabaseHelper helper;

    CourseData courseData = new CourseData();
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

        binding.ivUploadActAlarmTag.setSelected(true);
        binding.rlUploadActAlarmContainer.setVisibility(View.GONE);

        setCourseRecyclerView();

    }

    @Override
    public void callAddCourseActivity() {
        startActivityForResult(new Intent(getApplicationContext(), AddCourseActivity.class),LOC_ACTIVITY);
    }

    @Override
    public void callAddActivity() {
        startActivityForResult(new Intent(getApplicationContext(), AddActivity.class),ADD_ACTIVITY);
    }

    @Override
    public void callChangeOrderActivity() {
        startActivityForResult(new Intent(getApplicationContext(), ChangeOrderActivity.class),CHANGE_ACTIVITY);
    }

    @Override
    public void callTagActivity() {

        startActivityForResult(new Intent(getApplicationContext(), TagActivity.class),TAG_ACTIVITY);
    }

    public void setCourseRecyclerView(){

/*
        ArrayList<String> imgList = new ArrayList<>();
        imgList.add("https://images.otwojob.com/product/x/U/6/xU6PzuxMzIFfSQ9.jpg/o2j/resize/852x622%3E");
        imgList.add("https://t1.daumcdn.net/cfile/tistory/21266735579D869932");
        imgList.add("https://t1.daumcdn.net/cfile/tistory/21266735579D869932");
        imgList.add("https://images.otwojob.com/product/x/U/6/xU6PzuxMzIFfSQ9.jpg/o2j/resize/852x622%3E");

        ArrayList<Integer> share = new ArrayList<>();
        share.add(1);
        share.add(0);
        share.add(0);
        share.add(1);

        CourseData courseData = new CourseData("승희집","경기도 안양시 석수동","2019-09-04","승희네 집을 갔다왔다. 승희네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.",1,1,1, imgList,share);
        CourseData courseData1 = new CourseData("누리집","서울특별시","2019-09-05","누리네 집을 갔다왔다. 누리네 집에는 삼월이라는 강아지가 있다. 삼월이는 이빨 구조가 다른 강아지와 달라 혀가 항상 오른쪽으로 살짝 튀어나와있다. 그것이 매력포인트다. 나는 원래 강아지를 그리 좋아하지 않았다. 삼월이와 일주일을 살고 난 후, 강아지를 좋아하게 되었다.",2,1,1, imgList,share);
        CourseData courseData2 = new CourseData("제민집","서울특별시","2019-09-06","제민이네 집을 갔다왔다. 제민이네 집에는 슈가라는 강아지가 있다. 슈가는 조그맣고 겁쟁이라고한다. 근데 안보여준다 으악!!",3,1,1, imgList,share);
        CourseData courseData3 = new CourseData("무돌집","인천광역시","2019-09-07","무돌이네 집을 갔다왔다. 승희네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.",4,1,1, imgList,share);
        CourseData courseData4 = new CourseData("영우집","서울특별시","2019-09-08","영우네 집을 갔다왔다. 승희네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.",5,1,1, imgList,share);
*/
        if(courseDataArrayList != null)
            courseDataArrayList.clear();
        courseDataArrayList = courseTable.selectData();

        if(courseDataArrayList == null)
            return;

/*        courseDataArrayList.add(courseData);
        courseDataArrayList.add(courseData1);
        courseDataArrayList.add(courseData2);
        courseDataArrayList.add(courseData3);
        courseDataArrayList.add(courseData4);*/

        RecyclerView mRecyclerView = binding.rvUploadActCourseList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        courseRecyclerviewAdapter = new CourseRecyclerviewAdapter(courseDataArrayList, this);
        courseRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(courseRecyclerviewAdapter);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == TAG_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {

                if(data.getStringExtra("idxList") != null){

                    Log.e("데이터",data.toString());
                    Log.e("데이터 detailed",data.getStringExtra("idxList"));

                    String str = data.getStringExtra("idxList");
                    String [] array = str.split(",");

                    int [] idxList = new int[array.length];

                    for(int i = 0 ; i<idxList.length ; i++){
                        idxList[i] = Integer.parseInt(array[i]);
                        Log.e("idxList,,,",idxList[i]+"");
                    }

                    if(idxList !=null){
                        Log.e("TAG_ACTIVITY,,,",idxList.length+"");
                        binding.tvUploadActAlarmTagCnt.setText(idxList.length+"");
                        binding.rlUploadActAlarmContainer.setVisibility(View.VISIBLE);
                    }else
                        Log.e("TAG_ACTIVITY,,,","널값!");
                }
            }
        }

        if (requestCode == ADD_ACTIVITY) {
            if (resultCode == Activity.RESULT_OK) {
                setCourseRecyclerView();
            }
        }
    }
}
