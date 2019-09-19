package com.mojilab.moji.ui.main.upload;

import android.content.Intent;
import android.os.Bundle;
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

import java.util.ArrayList;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

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

        setCourseRecyclerView();

    }

    @Override
    public void callAddCourseActivity() {
        startActivity(new Intent(getApplicationContext(), AddCourseActivity.class));
    }

    @Override
    public void callAddActivity() {
        startActivity(new Intent(getApplicationContext(), AddActivity.class));
    }

    @Override
    public void callChangeOrderActivity() {
        startActivity(new Intent(getApplicationContext(), ChangeOrderActivity.class));
    }

    @Override
    public void callTagActivity() {
        startActivity(new Intent(getApplicationContext(), TagActivity.class));
    }

    public void setCourseRecyclerView(){

        ArrayList<String> imgList = new ArrayList<>();
        CourseData courseData = new CourseData(0,1,"승희집","2019년09월04일",imgList,"승희네 집을 갔다왔다. 승희네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.");
        CourseData courseData1 = new CourseData(1,2,"누리집","2019년09월05일",imgList,"누리네 집을 갔다왔다. 누리네 집에는 삼월이라는 강아지가 있다. 삼월이는 이빨 구조가 다른 강아지와 달라 혀가 항상 오른쪽으로 살짝 튀어나와있다. 그것이 매력포인트다. 나는 원래 강아지를 그리 좋아하지 않았다. 삼월이와 일주일을 살고 난 후, 강아지를 좋아하게 되었다.");
        CourseData courseData2 = new CourseData(2,3,"제민집","2019년09월06일",imgList,"제민이네 집을 갔다왔다. 제민이네 집에는 슈가라는 강아지가 있다. 슈가는 조그맣고 겁쟁이라고한다. 근데 안보여준다 으악!!");
        CourseData courseData3 = new CourseData(3,4,"무돌집","2019년09월07일",imgList,"무돌이네 집을 갔다왔다. 무돌이네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.");
        CourseData courseData4 = new CourseData(4,5,"영우집","2019년09월08일",imgList,"영우네 집을 갔다왔다. 영우네 집에는 10년 넘게 키운 난이 있는데, 한번도 꽃이 핀 적이 없다.. 그런데 올해 처음으로 난에 꽃이 피었다. 아빠는 신나서 꽃에서 꿀 향기가 난다며 좋아하신다.");

        courseDataArrayList.add(courseData);
        courseDataArrayList.add(courseData1);
        courseDataArrayList.add(courseData2);
        courseDataArrayList.add(courseData3);
        courseDataArrayList.add(courseData4);

        RecyclerView mRecyclerView = binding.rvUploadActCourseList;
        LinearLayoutManager mLinearLayoutManager = new LinearLayoutManager(getApplicationContext());
        mRecyclerView.setLayoutManager(mLinearLayoutManager);

        courseRecyclerviewAdapter = new CourseRecyclerviewAdapter(courseDataArrayList, getApplicationContext());
        mRecyclerView.setAdapter(courseRecyclerviewAdapter);
    }
}
