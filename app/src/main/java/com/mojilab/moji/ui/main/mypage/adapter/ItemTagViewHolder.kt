package com.mojilab.moji.ui.main.mypage.adapter

import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class ItemTagViewHolder(itemView: View?) : RecyclerView.ViewHolder(itemView!!) {
    var recordTag : TextView = itemView!!.findViewById(R.id.tv_tag_value_item)
}