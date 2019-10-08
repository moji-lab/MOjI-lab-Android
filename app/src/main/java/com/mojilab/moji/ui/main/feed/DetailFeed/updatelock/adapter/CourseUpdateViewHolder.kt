package com.mojilab.moji.ui.main.feed.DetailFeed.updatelock.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class CourseUpdateViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var courseImg : ImageView = itemView!!.findViewById(R.id.iv_update_img_item_img)
    var courseLock : ImageView = itemView!!.findViewById(R.id.iv_update_act_item_btn_lock)
    var courseBoss : TextView = itemView!!.findViewById(R.id.tv_update_img_item_boss)
}