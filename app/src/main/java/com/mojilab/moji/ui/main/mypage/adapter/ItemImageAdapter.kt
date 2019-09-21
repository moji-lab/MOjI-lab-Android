package com.mojilab.moji.ui.main.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R

class ItemImageAdapter(private var imageDatas: ArrayList<String>, var requestManager : RequestManager) : RecyclerView.Adapter<ImageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record_image, parent, false)
        return ImageViewHolder(mainView)
    }

    override fun getItemCount(): Int = imageDatas.size

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        requestManager.load(imageDatas[position]).into(holder.recordImage)
        holder.recordNum.text = (position+1).toString()
        if(position == 0){
            holder.leftLine.visibility = View.GONE
        }
        else if(position == imageDatas.size-1){
            holder.rightLine.visibility = View.GONE
        }
    }
}