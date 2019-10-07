package com.mojilab.moji.ui.main.upload.addCourse.map.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.data.TourData

class TourMapAdapter(private var tourDatas: ArrayList<TourData>, var requestManager: RequestManager) : RecyclerView.Adapter<TourMapViewHolder>(){

    private lateinit var onItemClick : View.OnClickListener

    fun setOnItemClickListener(I : View.OnClickListener) {
        onItemClick = I
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TourMapViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_bottom_tourist_map, parent, false)
        mainView.setOnClickListener(onItemClick)
        return TourMapViewHolder(mainView)
    }

    override fun getItemCount(): Int = tourDatas.size

    override fun onBindViewHolder(holder: TourMapViewHolder, position: Int) {
        requestManager.load(tourDatas[position].imgUrl).into(holder.tourImage)
        holder.tourTitle.text = tourDatas[position].title
        holder.tourAddress.text = tourDatas[position].address
    }
}