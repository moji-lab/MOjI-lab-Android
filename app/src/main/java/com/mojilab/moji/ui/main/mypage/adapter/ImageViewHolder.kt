package com.mojilab.moji.ui.main.mypage.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class ImageViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var recordImage : ImageView = itemView!!.findViewById(R.id.iv_record_image_item)
    var recordNum : TextView = itemView!!.findViewById(R.id.tv_coarse_order_item)
    var leftLine : View = itemView!!.findViewById(R.id.view_connect_left_item)
    var rightLine : View = itemView!!.findViewById(R.id.view_connect_right_item)
}