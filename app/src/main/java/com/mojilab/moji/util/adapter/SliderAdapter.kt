package com.mojilab.moji.util.adapter

import android.content.Context
import android.os.Bundle
import android.view.View
import com.bumptech.glide.Glide
import com.github.islamkhsh.CardSliderAdapter
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.FeedViewPagerData
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.PhotoData
import kotlinx.android.synthetic.main.fragment_slider_feed.view.*

class SliderAdapter(var ctx: Context, items : ArrayList<PhotoData?>) : CardSliderAdapter<PhotoData?>(items) {

    override fun bindView(position: Int, itemContentView: View, item: PhotoData?) {

        item?.run {
            if(represent){
                Glide.with(ctx).load(photoUrl).into(itemContentView.iv_detail_vp_img)
            }
        }
    }
    override fun getItemContentLayout(position: Int) = R.layout.fragment_slider_feed
}