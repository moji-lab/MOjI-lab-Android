package com.mojilab.moji.ui.main;

import android.os.Bundle;

import androidx.lifecycle.ViewModelProviders;
import androidx.viewpager.widget.ViewPager;

import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseActivity;
import com.mojilab.moji.databinding.ActivityMainBinding;
import com.mojilab.moji.ui.main.alarm.AddFragment;
import com.mojilab.moji.ui.main.alarm.AlarmFragment;
import com.mojilab.moji.ui.main.home.HomeFragment;
import com.mojilab.moji.ui.main.map.MapFragment;
import com.mojilab.moji.ui.main.mypage.MypageFragment;
import com.mojilab.moji.util.adapter.ViewPagerAdapter;

public class MainActivity extends BaseActivity<ActivityMainBinding, MainViewModel> {

    ActivityMainBinding binding;
    MainViewModel viewModel;
    ViewPagerAdapter adapter = new ViewPagerAdapter(getSupportFragmentManager());

    @Override
    public int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = getViewDataBinding();
        viewModel = ViewModelProviders.of(this).get(MainViewModel.class);
        binding.setViewModel(viewModel);

        setViewPager(binding.mainContainerVp);
        setTabLayout();

    }

    public void setViewPager(ViewPager viewPager) {
        adapter.addFragment(new HomeFragment());
        adapter.addFragment(new MapFragment());
        adapter.addFragment(new AddFragment());
        adapter.addFragment(new AlarmFragment());
        adapter.addFragment(new MypageFragment());
        viewPager.setAdapter(adapter);
    }
    private final void setTabLayout() {

//        View bottomNavigationLayout = LayoutInflater.from(this).inflate(R.layout.main_bottom_tab_bar, null);
//        RelativeLayout homeRl = (RelativeLayout) bottomNavigationLayout.findViewById(R.id.bottom_home_rl);
//        RelativeLayout mapRl = (RelativeLayout) bottomNavigationLayout.findViewById(R.id.bottom_map_rl);
//        RelativeLayout addRl = (RelativeLayout) bottomNavigationLayout.findViewById(R.id.bottom_add_rl);
//        RelativeLayout alarmRl = (RelativeLayout) bottomNavigationLayout.findViewById(R.id.bottom_alarm_rl);
//        RelativeLayout mypageRl = (RelativeLayout) bottomNavigationLayout.findViewById(R.id.bottom_mypage_rl);

        binding.mainContainerTl.setupWithViewPager(binding.mainContainerVp);
        binding.mainContainerTl.getTabAt(0).setIcon(R.drawable.tab_1_home);
        binding.mainContainerTl.getTabAt(1).setIcon(R.drawable.tab_2_explore);
        binding.mainContainerTl.getTabAt(2).setIcon(R.drawable.tab_3_add);
        binding.mainContainerTl.getTabAt(3).setIcon(R.drawable.tab_4_alarm);
        binding.mainContainerTl.getTabAt(4).setIcon(R.drawable.tab_5_mypage);

    }
}
