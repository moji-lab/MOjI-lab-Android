package com.jem.moji.ui.register;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.jem.moji.base.BaseViewModel;

public class RegisterViewModel extends BaseViewModel<RegisterNavigator> {

    // 화면 전환
    public void callActivity(){
        getNavigator().callActivity();
    }
}
