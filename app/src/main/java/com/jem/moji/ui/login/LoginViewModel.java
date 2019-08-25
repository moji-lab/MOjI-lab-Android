package com.jem.moji.ui.login;

import androidx.lifecycle.MutableLiveData;
import com.jem.moji.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private LoginNavigator navigator;

    // 변경이 있으면 MutableLiveData
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> passwd = new MutableLiveData<>();


    // 초기값 설정
    public LoginViewModel(){
        email.setValue(null);
        passwd.setValue(null);
    }

    // 화면 전환
    public void callActivity(){
        getNavigator().callActivity();
    }

}
