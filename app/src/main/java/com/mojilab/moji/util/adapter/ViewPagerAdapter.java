package com.mojilab.moji.util.adapter;

import android.view.MotionEvent;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.ArrayList;
import java.util.List;

public class ViewPagerAdapter extends FragmentPagerAdapter
{

    private final List<Fragment> mFragmentList = new ArrayList<>();

    public void addFragment(Fragment fragment){
        mFragmentList.add(fragment);
    }

    public ViewPagerAdapter(FragmentManager fm){
        super(fm);
    }
    @Override
    public Fragment getItem(int position) { return mFragmentList.get(position); }

    @Override
    public int getCount() {
        return mFragmentList.size() ;
    }


}