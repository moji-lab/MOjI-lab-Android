package com.mojilab.moji.ui.main.mypage.myrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class MyRecordFragment : Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_myrecord, container, false)

        return v;
    }

}
