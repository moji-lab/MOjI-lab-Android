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

class RecordAdapter(private var homeSongData: ArrayList<RecordData>, var requestManager : RequestManager) : RecyclerView.Adapter<RecordViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecordViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        return RecordViewHolder(mainView)
    }

    override fun getItemCount(): Int = homeSongData.size

    override fun onBindViewHolder(holder: RecordViewHolder, position: Int) {

    }
}