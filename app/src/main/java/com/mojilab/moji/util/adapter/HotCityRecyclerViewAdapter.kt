package com.mojilab.moji.util.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.home.HomeData.HotCityData

class HotCityRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<HotCityData>) :
    RecyclerView.Adapter<HotCityRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(R.layout.rv_item_home_hotcity, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.iv_rv_home_city_name.text=dataList[position].city.toString()

        Glide.with(ctx)
            .load(dataList[position].thumbnail)
            .into(holder.iv_rv_home_city_img)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var cv_item_home_hotcity_allview = itemView.findViewById(R.id.cv_item_home_hotcity_allview) as CardView
        var iv_rv_home_city_name=itemView.findViewById(R.id.iv_rv_home_city_name) as TextView
        var iv_rv_home_city_img=itemView.findViewById(R.id.iv_rv_home_city_img) as ImageView
    }

}