package com.mojilab.moji.ui.main.mypage.myrecord.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.myrecord.data.RecordData

class RecordImageAdapter(private var imageDatas: ArrayList<String>, var requestManager : RequestManager) : RecyclerView.Adapter<RecordImageViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordImageViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record_image, parent, false)
        return RecordImageViewHolder(mainView)
    }

    override fun getItemCount(): Int = imageDatas.size

    override fun onBindViewHolder(holder: RecordImageViewHolder, position: Int) {
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