package com.mojilab.moji.ui.main;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityMainBinding;
import com.mojilab.moji.ui.main.alarm.AlarmFragment;
import com.mojilab.moji.ui.main.home.HomeFragment;
import com.mojilab.moji.ui.main.map.MapFragment;
import com.mojilab.moji.ui.main.mypage.MypageFragment;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.mojilab.moji.ui.signup.SignupActivity;
import com.mojilab.moji.util.adapter.ViewPagerAdapter;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    Fragment nowFrag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);

        callFragment("home");

    }


    public void callFragment(String frag) {

        switch (frag) {
            case "home" :
                nowFrag = new HomeFragment();
                break;
            case "map" :
                nowFrag = new MapFragment();
                break;
            case "alarm" :
                nowFrag = new AlarmFragment();
                break;
            case "mypage" :
                nowFrag = new MypageFragment();
                break;
        }
        FragmentManager fm = getSupportFragmentManager();
        FragmentTransaction transaction = fm.beginTransaction();
        transaction.replace(com.mojilab.moji.R.id.main_fragment_container, nowFrag);
        transaction.commit();
    }


    @Override
    public void callUploadActivity() {
        startActivity(new Intent(getApplicationContext(), UploadActivity.class));
    }

    @Override
    public void callHomeFragment() {
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home_active);
        binding.mainMapBtn.setImageResource(R.drawable.tab_2_explore);
        binding.mainAlarmBtn.setImageResource(R.drawable.tab_4_alarm);
        binding.mainMypageBtn.setImageResource(R.drawable.tab_5_mypage);
        callFragment("home");

    }

    @Override
    public void callMapFragment(){
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home);
        binding.mainMapBtn.setImageResource(R.drawable.tab_2_explore_active);
        binding.mainAlarmBtn.setImageResource(R.drawable.tab_4_alarm);
        binding.mainMypageBtn.setImageResource(R.drawable.tab_5_mypage);
        callFragment("map");
    }

    @Override
    public void callAlarmFragment() {
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home);
        binding.mainMapBtn.setImageResource(R.drawable.tab_2_explore);
        binding.mainAlarmBtn.setImageResource(R.drawable.tab_4_alarm_active);
        binding.mainMypageBtn.setImageResource(R.drawable.tab_5_mypage);
        callFragment("alarm");
    }

    @Override
    public void callMypageFragment() {
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home);
        binding.mainMapBtn.setImageResource(R.drawable.tab_2_explore);
        binding.mainAlarmBtn.setImageResource(R.drawable.tab_4_alarm);
        binding.mainMypageBtn.setImageResource(R.drawable.tab_5_mypage_active);
        callFragment("mypage");
    }
}
