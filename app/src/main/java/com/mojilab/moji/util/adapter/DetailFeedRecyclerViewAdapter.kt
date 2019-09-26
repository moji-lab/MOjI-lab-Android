package com.mojilab.moji.util.adapter
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage.DetailFeedRecyclerViewData
import com.github.islamkhsh.CardSliderViewPager
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
        holder.vp_item_viewpager.adapter=SliderAdapter(ctx,dataList[position].RecyclerViewItem_Img)
        holder.tv_item_detail_real_number.text=(position+1).toString()
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var tv_item_detail_real_number = itemView.findViewById(R.id.tv_item_detail_real_number) as TextView
        var tv_item_detail_place = itemView.findViewById(com.mojilab.moji.R.id.tv_item_detail_place) as TextView
        var vp_item_viewpager = itemView.findViewById(com.mojilab.moji.R.id.vp_item_viewpager) as CardSliderViewPager

    }




}


