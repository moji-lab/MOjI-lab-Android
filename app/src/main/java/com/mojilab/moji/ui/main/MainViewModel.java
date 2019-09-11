package com.mojilab.moji.ui.main;

import androidx.viewpager.widget.ViewPager;
import com.mojilab.moji.base.BaseViewModel;

public class MainViewModel extends BaseViewModel<MainNavigator> {

    // 화면 전환 -> 회원가입 페이지
    public void callUploadActivity(){
        getNavigator().callUploadActivity();
    }

    public void callHomeFragment() { getNavigator().callHomeFragment();}

    public void callMapFragment() { getNavigator().callMapFragment();}

    public void callAlarmFragment() { getNavigator().callAlarmFragment();}

    public void callMypageFragment() { getNavigator().callMypageFragment();}
}
