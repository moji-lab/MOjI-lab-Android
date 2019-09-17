package com.mojilab.moji.ui.main.upload;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityUploadBinding;

public class UploadActivity extends BaseActivity<ActivityUploadBinding, UploadViewModel> implements UploadNavigator {

    ActivityUploadBinding binding;
    UploadViewModel viewModel;

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
}
