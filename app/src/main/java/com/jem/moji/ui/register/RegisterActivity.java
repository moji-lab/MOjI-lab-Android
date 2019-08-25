package com.jem.moji.ui.register;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import androidx.lifecycle.ViewModelProviders;
import com.jem.moji.R;
import com.jem.moji.base.BaseActivity;
import com.jem.moji.databinding.ActivityRegisterBinding;
import com.jem.moji.ui.login.LoginActivity;

public class RegisterActivity extends BaseActivity<ActivityRegisterBinding, RegisterViewModel> implements RegisterNavigator {

    ActivityRegisterBinding binding;
    RegisterViewModel viewModel;

    @Override
    public int getLayoutId() {
        return R.layout.activity_register;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel.class);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
    }

    @Override
    public void callActivity() {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
    }
}
