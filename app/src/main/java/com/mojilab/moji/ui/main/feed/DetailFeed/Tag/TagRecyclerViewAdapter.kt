package com.mojilab.moji.ui.main.feed.DetailFeed.Tag

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.github.islamkhsh.CardSliderViewPager
import com.mojilab.moji.R
import com.mojilab.moji.util.adapter.SliderAdapter



class TagRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<String?>) :
    RecyclerView.Adapter<TagRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(com.mojilab.moji.R.layout.rv_item_tag, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.tv_tag_value_item.text="#"+dataList[position]
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_tag_value_item =itemView.findViewById(R.id.tv_tag_value_item) as TextView


    }




}
