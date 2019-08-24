package com.jem.moji.ui.login;

import android.content.Intent;
import android.util.Log;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.ViewModelProviders;
import com.jem.moji.R;
import com.jem.moji.databinding.ActivityLoginBinding;
import com.jem.moji.ui.register.RegisterActivity;
import com.jem.moji.ui.splash.SplashActivity;
import com.jem.moji.util.actinteface.CallAnotherActivityNavigator;
import com.jem.moji.viewmodel.LoginViewModel;

public class LoginActivity extends AppCompatActivity implements CallAnotherActivityNavigator {

    private LoginViewModel viewModel = new LoginViewModel(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ActivityLoginBinding binding = DataBindingUtil.setContentView(this, R.layout.activity_login);


        binding.setLifecycleOwner(this);
//       final LoginViewModel viewModel = ViewModelProviders.of(this).get(LoginViewModel.class);
        binding.setViewModel(viewModel);
    }

    @Override
    public void callActivity() {
        startActivity(new Intent(getApplicationContext(), RegisterActivity.class));
    }
}
