package com.mojilab.moji.ui.main.alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.mojilab.moji.R;

public class AddFragment extends Fragment {
    ViewPager viewPager;

    public AddFragment(){

    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState){
        View v = inflater.inflate(R.layout.fragment_alarm, container, false);
        return v;
    }
}
