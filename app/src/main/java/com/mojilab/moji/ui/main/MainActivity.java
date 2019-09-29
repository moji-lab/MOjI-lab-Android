package com.mojilab.moji.ui.main;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.ViewModelProviders;

import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityMainBinding;
import com.mojilab.moji.ui.main.feed.FeedFragment;
import com.mojilab.moji.ui.main.home.HomeFragment;
import com.mojilab.moji.ui.main.map.MapFragment;
import com.mojilab.moji.ui.main.map.TMapFragment;
import com.mojilab.moji.ui.main.mypage.MypageFragment;
import com.mojilab.moji.ui.main.upload.UploadActivity;
import com.skt.Tmap.TMapView;

import java.util.Map;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> implements MainNavigator {

    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 555;
    ActivityMainBinding binding;
    MainViewModel viewModel;
    Fragment nowFrag;
    MapFragment mapFragment;

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
                // 싱글톤 (메모리 낭비 방지)
                if(HomeFragment.homeFragment == null){
                    HomeFragment.homeFragment = new HomeFragment();
                }
                nowFrag = HomeFragment.homeFragment;
                break;

            case "map" :
                // 싱글톤 (메모리 낭비 방지)
                mapFragment = com.mojilab.moji.ui.main.map.MapFragment.getMapFragment();
                nowFrag = mapFragment;
                break;

            case "feed" :
                // 싱글톤 (메모리 낭비 방지)
                if(FeedFragment.feedFragment == null){
                    FeedFragment.feedFragment = new FeedFragment();
                }
                nowFrag = FeedFragment.feedFragment;
                break;

            case "mypage" :
                // 싱글톤 (메모리 낭비 방지)
                if(MypageFragment.mypageFragment == null){
                    MypageFragment.mypageFragment = new MypageFragment();
                }
                nowFrag = MypageFragment.mypageFragment;
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
        callFragment("feed");
    }

    @Override
    public void callMypageFragment() {
        binding.mainHomelBtn.setImageResource(R.drawable.tab_1_home);
        binding.mainMapBtn.setImageResource(R.drawable.tab_2_explore);
        binding.mainAlarmBtn.setImageResource(R.drawable.tab_4_alarm);
        binding.mainMypageBtn.setImageResource(R.drawable.tab_5_mypage_active);
        callFragment("mypage");
    }

    // 백버튼 클릭 시
    @Override
    public void onBackPressed() {
        AlertDialog.Builder dialog = new AlertDialog.Builder(this);
        dialog.setMessage("모지를 종료할까요?");
        dialog.setPositiveButton("확인", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                moveTaskToBack(true);
                finish();
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        dialog.setNegativeButton("아니요", null);
        dialog.show();
    }
}
