package com.mojilab.moji.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.DetailFeedRecyclerViewData
import android.app.Activity
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayout
import com.mojilab.moji.R


class DetailFeedRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<DetailFeedRecyclerViewData>) :
    RecyclerView.Adapter<DetailFeedRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(com.mojilab.moji.R.layout.rv_item_detail_feed_course, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.tv_item_detail_place.text=dataList[position].city
        holder.vp_item_viewpager.adapter=SliderFeedPagerAdapter((ctx as AppCompatActivity).supportFragmentManager,dataList[position].RecyclerViewItem_Img.size,dataList[position].RecyclerViewItem_Img)
        holder.vp_item_viewpager.offscreenPageLimit=2
        holder.tl_item_detail_indicator.setupWithViewPager(holder.vp_item_viewpager)
        //여기서 dataList[position].img 배열 viewpager로 넘기기
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        var tv_item_detail_place = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_place) as TextView
        var vp_item_viewpager = itemView.findViewById(com.mojilab.moji.R.id.vp_item_viewpager) as ViewPager
        var tl_item_detail_indicator=itemView.findViewById(R.id.tl_item_detail_indicator) as TabLayout
    }

}