package com.mojilab.moji.ui.main.mypage;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mojilab.moji.R;

public class MypageFragment extends Fragment {
    ViewPager viewPager;

    public MypageFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_mypage, container, false);
        return v;
    }
}
