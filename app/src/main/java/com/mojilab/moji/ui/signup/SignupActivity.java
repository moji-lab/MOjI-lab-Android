package com.mojilab.moji.ui.signup;

import android.content.Intent;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import androidx.lifecycle.ViewModelProviders;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.data.SignupData;
import com.mojilab.moji.databinding.ActivitySignupBinding;
import com.mojilab.moji.ui.login.LoginActivity;
import com.mojilab.moji.util.network.ApiClient;
import com.mojilab.moji.util.network.NetworkService;
import com.mojilab.moji.util.network.get.GetDuplicateCheckResponse;
import com.mojilab.moji.util.network.post.PostResponse;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SignupActivity extends BaseActivity<ActivitySignupBinding, SignupViewModel> implements SignupNavigator {

    ActivitySignupBinding binding;
    SignupViewModel viewModel;
    final String TAG = "SingupActivity";
    NetworkService networkService;
    boolean emailCheck;
    boolean nicknameCheck;

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
        viewModel.init();
        binding.setViewModel(viewModel);
    }

    @Override
    public void callActivity() {

        // 공백 발견
        if (viewModel.email.get() != null || viewModel.nickname.get() != null || viewModel.passwd.get() != null
                || viewModel.passwdCheck.get() != null || !viewModel.email.get().equals("") || !viewModel.nickname.get().equals("")
                || !viewModel.passwd.get().equals("") || !viewModel.passwdCheck.get().equals("")) {
            Toast.makeText(getApplicationContext(), "모두 입력해주세요", Toast.LENGTH_LONG).show();
        }
        // 회원가입 시도
        else {

            // 이메일 정규식 검사 && 비밀번호 일치 => 통과
            if (emailCheckPattern(viewModel.email.get()) || equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())) {
                postSignup();
            }
            // 이메일 정규식 검사 fail
            else if (!emailCheckPattern(viewModel.email.get())) {
                Toast.makeText(getApplicationContext(), "이메일을 정확히 입력해주세요", Toast.LENGTH_LONG).show();
            }

            // 비밀번호 일치 검사 fail
            if (!equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())) {
                Toast.makeText(getApplicationContext(), "비밀번호 확인을 다시 입력해주세요.", Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    public void emailCheck() {
        // 이메일 공백 X
        if(viewModel.email.get() != null && !viewModel.email.get().equals("")){
            getEmailDuplicateCheck();
        }
        // 이메일 공백
        else{
            Toast.makeText(getApplicationContext(), "이메일을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void nicknameCheck(){
        // 닉네임 공백 X
        if(viewModel.nickname.get() != null && !viewModel.nickname.get().equals("")){
            // 닉네임 중복 X
            getNicknameDuplicateCheck();
        }
        // 닉네임 공백
        else{
            Toast.makeText(getApplicationContext(), "닉네임을 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    // 회원가입 통신
    public void postSignup() {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        SignupData postSignup = new SignupData(viewModel.email.get(), viewModel.nickname.get(), viewModel.passwd.get());
        Call<PostResponse> postSignupResponse = networkService.postSignup(postSignup);
        postSignupResponse.enqueue(new Callback<PostResponse>() {
            @Override
            public void onResponse(Call<PostResponse> call, Response<PostResponse> response) {
                if (response.isSuccessful()) {
                    Log.v(TAG, "Signup Success");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {
                    Log.v(TAG, "실패 메시지 = " + response.message());
                    Toast.makeText(getApplicationContext(), "회원가입 실패", Toast.LENGTH_LONG);
                }
            }

            @Override
            public void onFailure(Call<PostResponse> call, Throwable t) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
                Toast.makeText(getApplicationContext(), "서버 연결 실패", Toast.LENGTH_LONG);
            }
        });
    }

    // 이메일 중복 체크
    public void getEmailDuplicateCheck() {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        Call<GetDuplicateCheckResponse> getEmailCheckResponse = networkService.getEmailDuplicateCheck(viewModel.email.get());
        getEmailCheckResponse.enqueue(new Callback<GetDuplicateCheckResponse>() {
            @Override
            public void onResponse(Call<GetDuplicateCheckResponse> call, Response<GetDuplicateCheckResponse> response) {
                if(response.body().getStatus() == 200){
                    Log.v(TAG, "Email Valid Check Success");
                    Toast.makeText(getApplicationContext(), "사용 가능 합니다", Toast.LENGTH_LONG).show();
                    binding.tvDuplicateEmailCheckSignup.setVisibility(View.GONE);
                    binding.ivEmailValidSignup.setVisibility(View.VISIBLE);

                }
                else if(response.body().getStatus() == 400){
                    Log.v(TAG, "실패 메시지 = " + response.message());
                    Toast.makeText(getApplicationContext(), "중복된 이메일입니다", Toast.LENGTH_LONG).show();
                } else {
                    Log.v(TAG, "데이터 통신 실패 = " + response.message().toString());
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GetDuplicateCheckResponse> call, Throwable t) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
            }
        });
    }

    // 닉네임 중복 체크
    public void getNicknameDuplicateCheck() {
        networkService = ApiClient.INSTANCE.getRetrofit().create(NetworkService.class);
        Call<GetDuplicateCheckResponse> getNicknameDuplicateResponse = networkService.getNicknameDuplicateCheck(viewModel.nickname.get());
        getNicknameDuplicateResponse.enqueue(new Callback<GetDuplicateCheckResponse>() {
            @Override
            public void onResponse(Call<GetDuplicateCheckResponse> call, Response<GetDuplicateCheckResponse> response) {
                if (response.body().getStatus() == 200) {
                    Log.v(TAG, "Nickname Valid Check Success");
                    Toast.makeText(getApplicationContext(), "사용 가능 합니다", Toast.LENGTH_LONG).show();
                    binding.tvDuplicateNicknameCheckSignup.setVisibility(View.GONE);
                    binding.ivNicknameValidSignup.setVisibility(View.VISIBLE);
                }
                else if(response.body().getStatus() == 400){
                    Toast.makeText(getApplicationContext(), "중복된 닉네임입니다", Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(getApplicationContext(), "에러", Toast.LENGTH_LONG).show();
                }
            }
            @Override
            public void onFailure(Call<GetDuplicateCheckResponse> call, Throwable t) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString());
            }
        });
    }

    public boolean emailCheckPattern(String email) {
        String emailPattern = "^[_a-zA-Z0-9-\\.]+@[\\.a-zA-Z0-9-]+\\.[a-zA-Z]+$";
        boolean result;

        Matcher match = Pattern.compile(emailPattern).matcher(email);
        if (match.find()) {
            result = true;
        } else {
            result = false;
        }
        return result;
    }

    public boolean equalPasswd(String password, String passwordCheck) {
        boolean result;

        // 비밀번호가 일치하면
        if (password.equals(passwordCheck)) {
            result = true;
        }
        // 불일치하면
        else {
            result = false;
        }
        return result;
    }
}
