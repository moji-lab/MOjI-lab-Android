package com.mojilab.moji.ui.login;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
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
import com.mojilab.moji.util.network.post.PostLoginResponse;
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
        if(SharedPreferenceController.INSTANCE.getUserEmail(getApplicationContext()).isEmpty()){
            Log.v(TAG, "자동로그인 토큰이 없음");
            Log.v(TAG, "자동로그인 토큰이 없음"+SharedPreferenceController.INSTANCE.getUserEmail(getApplicationContext()));
        }else {
            Log.v(TAG, "자동로그인 토큰이 있음");
            Log.v(TAG, "자동로그인 토큰이 있음"+SharedPreferenceController.INSTANCE.getUserEmail(getApplicationContext()));
            Log.v(TAG, "자동로그인 토큰이 있음"+SharedPreferenceController.INSTANCE.getUserPassword(getApplicationContext()));
            postAutoLogin();
        }
    }
    @Override
    public void callSignupActivity() {
        startActivity(new Intent(getApplicationContext(), SignupActivity.class));
    }

    @Override
    public void callMainActivity() {


        // 로그인 시도
        if(viewModel.email.get() != null && viewModel.passwd.get() != null && !viewModel.email.get().equals("") && !viewModel.passwd.get().equals("")){
            binding.loginConfirmBtn.setEnabled(false);
            Log.v(TAG, "Login 시도& 버튼 막아놓음");
            postLogin();
        }
        // email만 공백
        else if((viewModel.email.get() == null || viewModel.email.get().equals("")) && viewModel.passwd.get() != null){
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_LONG).show();
        }
        // 비밀번호만 공백
        else if(viewModel.email.get() != null && (viewModel.passwd.get() == null || viewModel.passwd.get().equals(""))){
            Toast.makeText(getApplicationContext(), "패스워드를 입력해주세요", Toast.LENGTH_LONG).show();
        }
        // 둘 다 공백
        else{
            Toast.makeText(getApplicationContext(), "이메일과 패스워드를 입력해주세요", Toast.LENGTH_LONG).show();
        }
    }

    // 로그인 통신
    public void postLogin() {
        NetworkService networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        LoginData postLogin = new LoginData(viewModel.email.get(), viewModel.passwd.get());
        Call<PostLoginResponse> postSignupResponse = networkService.postLogin(postLogin);
        postSignupResponse.enqueue(new Callback<PostLoginResponse>() {
            @Override
            public void onResponse(Call<PostLoginResponse> call, Response<PostLoginResponse> response) {

                Log.v(TAG, "Login 통신 성공 & 버튼 원상복구");
                if(response.body().getMessage().equals("로그인 성공")&&binding.cbLoginCheck.isChecked()){
                    Log.v(TAG, "Login Success 자동로그인");
                    binding.loginConfirmBtn.setEnabled(false);
                    // 토큰 내부DB 저장
                    Log.v(TAG, "토큰 값 = " + response.body().getData());
                    SharedPreferenceController.INSTANCE.setAuthorization(getApplicationContext(), response.body().getData().getToken());
                    SharedPreferenceController.INSTANCE.setUserNickname(getApplicationContext(), response.body().getData().getNickname());
                    SharedPreferenceController.INSTANCE.setUserId(getApplicationContext(), response.body().getData().getUserIdx());

                    SharedPreferenceController.INSTANCE.setUserEmail(getApplicationContext(),viewModel.email.get());
                    SharedPreferenceController.INSTANCE.setUserPassword(getApplicationContext(),viewModel.passwd.get());
                    Log.v(TAG, "Login Success 자동로그인 email 토큰"+SharedPreferenceController.INSTANCE.getUserEmail(getApplicationContext()));
                    //로그인 시 유저사진이 NULL이 아닐경 우 초기화 및 새로 저장
                    if(response.body().getData().getProfileUrl() !=null){
                        SharedPreferenceController.INSTANCE.clearUserPicture(getApplicationContext());
                        SharedPreferenceController.INSTANCE.setUserPicture(getApplicationContext(),response.body().getData().getProfileUrl());
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }else if(response.body().getMessage().equals("로그인 성공")){
                    Log.v(TAG, "Login 자동로그인 아님");
                    binding.loginConfirmBtn.setEnabled(false);
                    // 토큰 내부DB 저장
                    Log.v(TAG, "토큰 값 = " + response.body().getData());
                    SharedPreferenceController.INSTANCE.setAuthorization(getApplicationContext(), response.body().getData().getToken());
                    SharedPreferenceController.INSTANCE.setUserNickname(getApplicationContext(), response.body().getData().getNickname());
                    SharedPreferenceController.INSTANCE.setUserId(getApplicationContext(), response.body().getData().getUserIdx());

                    SharedPreferenceController.INSTANCE.clearUserEmail(getApplicationContext());
                    SharedPreferenceController.INSTANCE.clearUserPassword(getApplicationContext());


                    //로그인 시 유저사진이 NULL이 아닐경 우 초기화 및 새로 저장
                    if(response.body().getData().getProfileUrl() !=null){
                        SharedPreferenceController.INSTANCE.clearUserPicture(getApplicationContext());
                        SharedPreferenceController.INSTANCE.setUserPicture(getApplicationContext(),response.body().getData().getProfileUrl());
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);
                }
                else{
                    binding.loginConfirmBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "이메일 또는 패스워드가 틀렸습니다", Toast.LENGTH_LONG).show();
                    Log.v(TAG, "실패 메시지 = " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PostLoginResponse> call, Throwable t) {
                binding.loginConfirmBtn.setEnabled(true);
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
            }
        });
    }

    // 자동로그인 통신
    public void postAutoLogin() {
        NetworkService networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        LoginData postLogin = new LoginData(SharedPreferenceController.INSTANCE.getUserEmail(getApplicationContext()), SharedPreferenceController.INSTANCE.getUserPassword(getApplicationContext()));
        Call<PostLoginResponse> postSignupResponse = networkService.postLogin(postLogin);
        postSignupResponse.enqueue(new Callback<PostLoginResponse>() {
            @Override
            public void onResponse(Call<PostLoginResponse> call, Response<PostLoginResponse> response) {

                Log.v(TAG, "자동Login 토큰통신 성공 & 버튼 원상복구");
                if(response.body().getMessage().equals("로그인 성공")){
                    Log.v(TAG, "AutoLogin Success");
                    binding.loginConfirmBtn.setEnabled(false);
                    // 토큰 내부DB 저장
                    Log.v(TAG, "토큰 값 = " + response.body().getData());
                    SharedPreferenceController.INSTANCE.setAuthorization(getApplicationContext(), response.body().getData().getToken());
                    SharedPreferenceController.INSTANCE.setUserNickname(getApplicationContext(), response.body().getData().getNickname());
                    SharedPreferenceController.INSTANCE.setUserId(getApplicationContext(), response.body().getData().getUserIdx());

                    //로그인 시 유저사진이 NULL이 아닐경 우 초기화 및 새로 저장
                    if(response.body().getData().getProfileUrl() !=null){
                        SharedPreferenceController.INSTANCE.clearUserPicture(getApplicationContext());
                        SharedPreferenceController.INSTANCE.setUserPicture(getApplicationContext(),response.body().getData().getProfileUrl());
                    }
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    Toast.makeText(getApplicationContext(), "자동 로그인 되었습니다", Toast.LENGTH_SHORT).show();
                    startActivity(intent);
                }
                else{
                    binding.loginConfirmBtn.setEnabled(true);
                    Toast.makeText(getApplicationContext(), "이메일 또는 패스워드가 틀렸습니다", Toast.LENGTH_LONG).show();
                    Log.v(TAG, "실패 메시지 = " + response.message());
                }
            }

            @Override
            public void onFailure(Call<PostLoginResponse> call, Throwable t) {
                binding.loginConfirmBtn.setEnabled(true);
                Log.v(TAG, "토큰 AutoLogin 실패");
            }
        });
    }

}
