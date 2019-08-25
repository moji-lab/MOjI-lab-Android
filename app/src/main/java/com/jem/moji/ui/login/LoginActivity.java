package com.jem.moji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.jem.moji.R;
import com.jem.moji.base.BaseActivity;
import com.jem.moji.databinding.ActivityLoginBinding;
import com.jem.moji.ui.register.RegisterActivity;

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
    public void callActivity() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }

}
