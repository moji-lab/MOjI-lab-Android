package com.mojilab.moji.ui.login;

import android.util.Log;
import android.view.View;
import android.widget.EditText;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.lifecycle.MutableLiveData;
import com.mojilab.moji.base.BaseViewModel;
import kotlin.jvm.JvmStatic;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private LoginNavigator navigator;
    private View.OnFocusChangeListener onFocusEmail;

    // 변경이 있으면 MutableLiveData
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> passwd = new MutableLiveData<>();

    @VisibleForTesting
    public void init(){
        onFocusEmail = new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean focused) {
                if(focused){
                    Log.v("asdf"," focus on");
                }else{
                    Log.v("asdf", "focus off");
                }
                EditText et = (EditText) view;
                if(et.getText().length() > 0 && !focused){
                    Log.v("asdf","확인");
                }
            }
        };
    }

    // 초기값 설정
    public LoginViewModel(){
        email.setValue(null);
        passwd.setValue(null);
    }

    // 화면 전환 -> 회원가입 페이지
    public void callSignupActivity(){
        getNavigator().callSignupActivity();
    }
    // 화면 전환 -> 메인화면 페이지
    public void callMainActivity(){
        getNavigator().callMainActivity();
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener(){
        return onFocusEmail;
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener){
        if(editText.getOnFocusChangeListener() == null){
            Log.v("asdf","adsf");
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

}
