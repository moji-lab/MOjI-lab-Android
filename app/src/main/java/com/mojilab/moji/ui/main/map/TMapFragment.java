package com.mojilab.moji.ui.main.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.mojilab.moji.R;
import com.skt.Tmap.TMapView;

public class TMapFragment extends Fragment {

    private TMapFragment() {
    }

    private static TMapFragment tmapFragment = null;

    public static TMapFragment getMapFragment(){
        if(tmapFragment == null) tmapFragment = new TMapFragment();
        return tmapFragment;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View v = inflater.inflate(R.layout.fragment_tmap, container, false);

        LinearLayout linearLayoutTmap = (LinearLayout)v.findViewById(R.id.linearLayoutTmap);
        TMapView tMapView = new TMapView(getContext());

        tMapView.setSKTMapApiKey("39e3c1c8-26b9-4afe-96e3-d68fe892aa84");
        linearLayoutTmap.addView( tMapView );
        return v;
    }
}
