package com.mojilab.moji.ui.signup;

import com.mojilab.moji.base.BaseViewModel;

public class SignupViewModel extends BaseViewModel<SignupNavigator> {

    // 화면 전환
    public void callActivity(){
        getNavigator().callActivity();
    }
}
