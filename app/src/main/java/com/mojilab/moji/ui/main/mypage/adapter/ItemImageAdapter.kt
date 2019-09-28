package com.mojilab.moji.ui.main.mypage.adapter

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.mypage.data.PhotoData

class ItemImageAdapter(var context : Context, var boadrdId : String, private var imageDatas: ArrayList<PhotoData>, var requestManager : RequestManager) : RecyclerView.Adapter<ItemImageViewHolder>(){

    var mContext = context
    var boardIdx = boadrdId

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemImageViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record_image, parent, false)
        return ItemImageViewHolder(mainView)
    }

    override fun getItemCount(): Int = imageDatas.size

    override fun onBindViewHolder(holder: ItemImageViewHolder, position: Int) {
        // 코스 사진 있는 경우
        if(imageDatas.size > 0){
            requestManager.load(imageDatas[position].photoUrl).into(holder.recordImage)
            holder.recordNum.text = (position+1).toString()
            if(position == 0){
                holder.leftLine.visibility = View.GONE
            }
            else if(position == imageDatas.size-1){
                holder.rightLine.visibility = View.GONE
            }
        }

        holder.recordImage.setOnClickListener {
            var intent = Intent(mContext, DetailFeedActivity::class.java)
            intent.putExtra("boardIdx", boardIdx)
            mContext.startActivity(intent)
        }

    }
}