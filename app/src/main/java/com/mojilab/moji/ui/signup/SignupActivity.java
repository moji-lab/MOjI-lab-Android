package com.mojilab.moji.ui.signup;

import android.content.Intent;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
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

        binding.signupEmailEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.signupEmailEdit.setBackgroundResource(R.drawable.edit_circle_focus_on_background);
                // 중복 검사 초기화
                if(emailCheck){
                    binding.ivEmailValidSignup.setVisibility(View.GONE);
                    binding.tvDuplicateEmailCheckSignup.setVisibility(View.VISIBLE);
                    emailCheck = false;
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_off_circle_button);
                }


            }
        });

        binding.signupPasswdEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.signupPasswdEdit.setBackgroundResource(R.drawable.edit_circle_focus_on_background);
                if(emailCheck && nicknameCheck && equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())){
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_on_circle_button);
                }
                else{
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_off_circle_button);
                }
            }
        });

        binding.signupPasswdCheckEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                binding.signupPasswdCheckEdit.setBackgroundResource(R.drawable.edit_circle_focus_on_background);
                if(emailCheck && nicknameCheck && equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())){
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_on_circle_button);
                }
                else{
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_off_circle_button);
                }
            }
        });

        binding.signupNicknameEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                // 중복 검사 초기화
                if(nicknameCheck){
                    binding.ivNicknameValidSignup.setVisibility(View.GONE);
                    binding.tvDuplicateNicknameCheckSignup.setVisibility(View.VISIBLE);

                    nicknameCheck = false;
                    binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_off_circle_button);
                }

            }
        });
    }

    @Override
    public void callActivity() {

        // 회원가입 시도
        if (emailCheck && nicknameCheck && equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())) {
            postSignup();
        }
        // 회원가입 불가
        else {
            // 이메일 중복 검사 통과 && 비밀번호 일치 => 통과
            if (!emailCheck) {
                Toast.makeText(getApplicationContext(), "이메일 중복 검사를 눌러주세요", Toast.LENGTH_LONG).show();
                binding.signupPasswdCheckEdit.clearFocus();
                binding.signupNicknameEdit.clearFocus();
                binding.signupEmailEdit.requestFocus();
                binding.signupEmailEdit.setBackgroundResource(R.drawable.edit_circle_refocus_background);
            }

            // 비밀번호 일치 검사 fail
            else if (!equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())) {
                binding.signupEmailEdit.clearFocus();
                binding.signupNicknameEdit.clearFocus();
                binding.signupPasswdCheckEdit.requestFocus();
                binding.signupPasswdCheckEdit.setBackgroundResource(R.drawable.edit_circle_refocus_background);
                Toast.makeText(getApplicationContext(), "비밀번호 확인을 다시 입력해주세요.", Toast.LENGTH_LONG).show();
            }
            // 닉네임 중복 검사 fail
            else if(!nicknameCheck){
                binding.signupEmailEdit.clearFocus();
                binding.signupPasswdCheckEdit.clearFocus();
                binding.signupNicknameEdit.requestFocus();
                binding.signupNicknameEdit.setBackgroundResource(R.drawable.edit_circle_refocus_background);
                Toast.makeText(getApplicationContext(), "닉네임 중복 검사를 눌러주세요", Toast.LENGTH_LONG).show();
            }
        }

    }
    @Override
    public void emailCheck() {
        // 이메일 공백 X
        if(viewModel.email.get() != null && !viewModel.email.get().equals("")){
            if(emailCheckPattern(viewModel.email.get())){
                getEmailDuplicateCheck();
            }
            else{
                Toast.makeText(getApplicationContext(), "이메일을 정확히 입력해주세요", Toast.LENGTH_LONG).show();
            }
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
            if(viewModel.nickname.get().length() < 2){
                Toast.makeText(getApplicationContext(), "두글자 이상으로 입력해주세요.", Toast.LENGTH_LONG).show();
            }
            else{
                // 닉네임 중복 X
                getNicknameDuplicateCheck();
            }
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
                if (response.body().getStatus() == 201) {
                    Log.v(TAG, "Signup Success");
                    Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
                    startActivity(intent);
                } else {

                    Log.v(TAG, "상태코드 = " + response.body().getStatus());
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
                    emailCheck = true;
                    if(nicknameCheck && equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())){
                        binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_on_circle_button);
                    }
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
                    nicknameCheck = true;
                    if(emailCheck && equalPasswd(viewModel.passwd.get(), viewModel.passwdCheck.get())){
                        binding.signupConfirmBtn.setBackgroundResource(R.drawable.confirm_focus_on_circle_button);
                    }

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

        if(password == null || passwordCheck == null || password.equals("") || passwordCheck.equals("")){
            result = false;
        }
        else{
            // 비밀번호가 일치하면
            if (password.equals(passwordCheck)) {
                result = true;
            }
            // 불일치하면
            else {
                result = false;
            }
        }

        return result;
    }
}
