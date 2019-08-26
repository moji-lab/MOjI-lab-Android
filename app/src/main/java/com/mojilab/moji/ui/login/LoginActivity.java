package com.mojilab.moji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityLoginBinding;
import com.mojilab.moji.ui.main.MainActivity;
import com.mojilab.moji.ui.signup.SignupActivity;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    LoginViewModel viewModel;
    ActivityLoginBinding binding;

    @Override
    public int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = getViewDataBinding();

        viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void callSignupActivity() {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    @Override
    public void callMainActivity() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

}
