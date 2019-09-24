package com.mojilab.moji.ui.main.feed.DetailFeed


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.bumptech.glide.Glide

import com.mojilab.moji.R
import kotlinx.android.synthetic.main.fragment_slider_feed.*

/**
 * A simple [Fragment] subclass.
 */
class SliderFeedFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_slider_feed, container, false)
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

       var image=arguments!!.getString("background_url")



        Glide.with(this).load(image).into(iv_detail_vp_img)

    }
}
