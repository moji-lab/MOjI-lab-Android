package com.mojilab.moji.ui.main.upload;

import android.app.Activity;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
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

    static final int TAG_ACTIVITY = 333;
    static final int LOC_ACTIVITY = 444;
    static final int ADD_ACTIVITY = 555;
    static final int CHANGE_ACTIVITY = 666;

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
        startActivityForResult(new Intent(getApplicationContext(), AddCourseActivity.class), LOC_ACTIVITY);
    }

    @Override
    public void callAddActivity() {
        startActivityForResult(new Intent(getApplicationContext(), AddActivity.class), ADD_ACTIVITY);
    }

    @Override
    public void callChangeOrderActivity() {
        startActivityForResult(new Intent(getApplicationContext(), ChangeOrderActivity.class), CHANGE_ACTIVITY);
    }

    @Override
    public void callTagActivity() {

        startActivityForResult(new Intent(getApplicationContext(), TagActivity.class), TAG_ACTIVITY);
    }

    public void clickCompleteBtn() {
        //처음 들어올 때와
        //코스 추가 하고 돌아 올 때 실행

        if(courseDataArrayList == null)
            return;

        if (courseDataArrayList.size() > 0 && binding.etUploadActWriteLocation.getText().length() >0) {
            Log.e("str", binding.etUploadActWriteLocation.getText().toString());
            binding.tvUploadActCompleteBtn.setTextColor(Color.RED);

            binding.tvUploadActCompleteBtn.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //통신
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
}
