package com.mojilab.moji.ui.main.upload.addCourse;

import android.app.Activity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import com.mojilab.moji.R;
import com.mojilab.moji.data.LocationData;
import com.mojilab.moji.data.UploadImgData;
import com.mojilab.moji.databinding.ActivityAddCourseBinding;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.main.upload.add.AddActivity;
import com.mojilab.moji.ui.main.upload.add.UploadImgRecyclerviewAdapter;

import java.util.ArrayList;

public class AddCourseActivity extends AppCompatActivity {

    ActivityAddCourseBinding binding;

    InputMethodManager imm;

    LocationRecyclerviewAdapter locationRecyclerviewAdapter;
    private ArrayList<LocationData> locationDataArrayList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_add_course);
        binding.setActivity(this);
        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);

        binding.etAddCourseActSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    binding.llAddCourseActHelpComment.setVisibility(View.GONE);
                    binding.llAddCourseActRvContainer.setVisibility(View.VISIBLE);
                    setData();

                    return true;
                }
                return false;
            }
        });

        binding.ivAddCourseActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.llAddCourseActHelpComment.setVisibility(View.GONE);
                binding.llAddCourseActRvContainer.setVisibility(View.VISIBLE);
                setData();

            }
        });

        binding.ivAddCourseActBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    public void setData(){

        if(locationDataArrayList !=null){
            locationDataArrayList.clear();
        }
        locationDataArrayList.add(new LocationData("승희집","경기도 안양시 만안구 석수2동", 1.1f, 1.2f));
        locationDataArrayList.add(new LocationData("롯데월드","경기도 안양시 만안구 석수2동", 1.1f, 1.2f));
        locationDataArrayList.add(new LocationData("제민집","서울특별시 슈가집", 1.1f, 1.2f));
        locationDataArrayList.add(new LocationData("뭐이씨","서울특별시 목1동", 1.1f, 1.2f));
        locationDataArrayList.add(new LocationData("다예집","경기도 안양시 만안구 석수2동", 1.1f, 1.2f));

        RecyclerView mRecyclerView = binding.rvAddCourseActList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        locationRecyclerviewAdapter = new LocationRecyclerviewAdapter(locationDataArrayList, this);
        locationRecyclerviewAdapter.notifyDataSetChanged();
        mRecyclerView.setAdapter(locationRecyclerviewAdapter);

        locationRecyclerviewAdapter.setOnItemClickListener(new LocationRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View v, int position, String mainAddress) {

                if(getIntent().getIntExtra("add",0)==10){
                    Intent intent = new Intent(getApplicationContext(), AddActivity.class);
                    intent.putExtra("main",mainAddress);
                    setResult(Activity.RESULT_OK,intent);
                }else {
                    Intent intent = new Intent(getApplicationContext(), UploadActivity.class);
                    intent.putExtra("main",mainAddress);
                    setResult(Activity.RESULT_OK,intent);
                }
                finish();
            }
        });

    }
}
