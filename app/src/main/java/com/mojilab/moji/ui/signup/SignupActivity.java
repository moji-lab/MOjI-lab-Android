package com.mojilab.moji.ui.signup;

import android.content.Intent;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivitySignupBinding;
import com.mojilab.moji.ui.login.LoginActivity;

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> implements SignupNavigator {

    ActivitySignupBinding binding;
    SignupViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_signup;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();

        viewModel = ViewModelProviders.of(this).get(SignupViewModel.class);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void callActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
