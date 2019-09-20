package com.mojilab.moji.ui.main;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
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

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 555;
    ActivityMainBinding binding;
    MainViewModel viewModel;
    Fragment nowFrag;

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        viewModel.setNavigator(this);
        binding.setViewModel(viewModel);
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home_active);
        callHomeFragment();

        if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            // Should we show an explanation?
            if (shouldShowRequestPermissionRationale(
                    Manifest.permission.READ_EXTERNAL_STORAGE)) {
                // Explain to the user why we need to read the contacts
            }

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

            // MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE is an
            // app-defined int constant that should be quite unique

            return;
        }

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
