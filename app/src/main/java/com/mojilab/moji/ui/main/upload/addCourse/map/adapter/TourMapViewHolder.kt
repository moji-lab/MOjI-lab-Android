package com.mojilab.moji.ui.main.upload.addCourse.map.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class TourMapViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var tourImage : ImageView = itemView!!.findViewById(R.id.iv_tourist_img_map)
    var tourTitle : TextView = itemView!!.findViewById(R.id.tv_tourist_title_map)
    var tourAddress : TextView = itemView!!.findViewById(R.id.tv_tourist_address_map)
}