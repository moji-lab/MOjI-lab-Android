package com.mojilab.moji.ui.main.upload.add;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityAddBinding;
import com.mojilab.moji.ui.main.upload.addCourse.AddCourseActivity;

public class AddActivity extends BaseActivity<ActivityAddBinding, AddViewModel> implements AddNavigator {

    ActivityAddBinding binding;
    AddViewModel viewModel;

    @Override
    public int getLayoutId() { return R.layout.activity_add; }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(AddViewModel.class);
        viewModel.setNavigator(this);
        viewModel.init();
        binding.setAddViewModel(viewModel);
    }

    @Override
    public void callAddCourseActivity() {
        startActivity(new Intent(getApplicationContext(), AddCourseActivity.class));
    }
}
