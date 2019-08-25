package com.mojilab.moji.ui.register;

import com.mojilab.moji.base.BaseViewModel;

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {

    // 화면 전환
    public void callActivity(){
        getNavigator().callActivity();
    }
}
