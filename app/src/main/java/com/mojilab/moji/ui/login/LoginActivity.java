package com.mojilab.moji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.LoginData;
import com.mojilab.moji.data.SignupData;
import com.mojilab.moji.databinding.ActivityLoginBinding;
import com.mojilab.moji.ui.main.MainActivity;
import com.mojilab.moji.ui.signup.SignupActivity;
import com.mojilab.moji.util.localdb.SharedPreferenceController;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.post.PostResponse;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends BaseActivity<ActivityLoginBinding, LoginViewModel> implements LoginNavigator {

    LoginViewModel viewModel;
    ActivityLoginBinding binding;
    final String TAG = "LoginActivity";

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
        viewModel.init();
        binding.setViewModel(viewModel);

    }
    @Override
    public void callSignupActivity() {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    @Override
    public void callMainActivity() {

        // 로그인 시도
        if(viewModel.email.get() != null && viewModel.passwd.get() != null && !viewModel.email.get().equals("") && !viewModel.passwd.get().equals("")){
            postLogin();
        }
        // email만 공백
        else if((viewModel.email.get() == null || viewModel.email.get().equals("")) && viewModel.passwd.get() != null){
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
        }
        // 비밀번호만 공백
        else if(viewModel.email.get() != null && (viewModel.passwd.get() == null || viewModel.passwd.get().equals(""))){
            Toast.makeText(getApplicationContext(), "비밀번호를 입력해주세요", Toast.LENGTH_LONG).show();
        }
        // 둘 다 공백
        else{
            Toast.makeText(getApplicationContext(), "둘 다 입력해주세요", Toast.LENGTH_LONG).show();
        }
    }

    // 회원가입 통신
    public void postLogin() {
        NetworkService networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        LoginData postLogin = new LoginData(viewModel.email.get(), viewModel.passwd.get());
        Call<PostResponse> postSignupResponse = networkService.postLogin(postLogin);
        postSignupResponse.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if(response.body().getMessage().equals("로그인 성공")){
                    Log.v(TAG, "Login Success");

                    // 토큰 내부DB 저장
                    Log.v(TAG, "토큰 값 = " + response.body().getData());
                    SharedPreferenceController.INSTANCE.setAuthorization(getApplicationContext(), response.body().getData());
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(getApplicationContext(), "입력한 값이 틀렸습니다", Toast.LENGTH_LONG).show();
                    Log.v(TAG, "실패 메시지 = " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
            }
        });
    }

}
