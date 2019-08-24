package com.jem.moji.viewmodel;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jem.moji.ui.login.LoginActivity;
import com.jem.moji.util.actinteface.CallAnotherActivityNavigator;

public class LoginViewModel extends ViewModel {

    private CallAnotherActivityNavigator navigator;

    // 변경이 있으면 MutableLiveData
    public MutableLiveData<String> email = new MutableLiveData<>();
    public MutableLiveData<String> passwd = new MutableLiveData<>();

    // 초기값 설정
    public LoginViewModel(CallAnotherActivityNavigator navigator){
        email.setValue(null);
        passwd.setValue(null);
        this.navigator = navigator;
    }

    public void insertData() {
        email.setValue(email.getValue());
    }

    public void insertPasswd(){
        passwd.setValue(passwd.getValue());
    }

    public void callActivity(){
        navigator.callActivity();
    }

}
