package com.mojilab.moji.ui.main.mypage.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.mojilab.moji.R

class ItemTagAdapter(private var tagDatas: ArrayList<String>) : RecyclerView.Adapter<ItemTagViewHolder>(){

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemTagViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.rv_item_tag, parent, false)
        return ItemTagViewHolder(mainView)
    }

    override fun getItemCount(): Int = tagDatas.size

    override fun onBindViewHolder(holder: ItemTagViewHolder, position: Int) {
        holder.recordTag.text = tagDatas[position]
    }
}