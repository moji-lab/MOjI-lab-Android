package com.mojilab.moji.ui.main.mypage.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.data.RecordData
import com.mojilab.moji.util.adapter.RecyclerviewItemDeco

class MypageItemAdapter(var context : Context, private var recordDatas: ArrayList<RecordData>, var requestManager : RequestManager, var tabPosition : Int) : RecyclerView.Adapter<MypageItemViewHolder>(){

    lateinit var recordImageAdapter: ItemImageAdapter
    lateinit var mContext: Context
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageItemViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        mContext = context

        return MypageItemViewHolder(mainView)
    }

    override fun getItemCount(): Int = recordDatas.size

    override fun onBindViewHolder(holder: MypageItemViewHolder, position: Int) {
        recordImageAdapter = ItemImageAdapter(
            recordDatas[position].recordImg!!,
            requestManager
        )

        //  나의 기록 탭
        if(tabPosition == 0){
            holder.shareToggle.visibility = View.GONE
        }
        // 친구 공유 탭
        else if(tabPosition == 1){
            holder.moreBtn.visibility = View.GONE
        }

        requestManager.load(recordDatas[position].profileImgUrl).into(holder.profileImage)
        holder.profileName.text = recordDatas[position].name
        holder.recordDate.text = recordDatas[position].date

        holder.coarse.text = recordDatas[position].coarse
        holder.coarseContent.text = recordDatas[position].carseContent

        recyclerviewItemDeco = RecyclerviewItemDeco(context!!, 0)
        if (recyclerviewItemDeco != null) {
            holder.recordImagesRv.removeItemDecoration(recyclerviewItemDeco!!)
        }
        holder.recordImagesRv.addItemDecoration(recyclerviewItemDeco!!);
        holder.recordImagesRv.adapter = recordImageAdapter
        holder.recordImagesRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        holder.likeNum.text = recordDatas[position].likeNum.toString()
        holder.commentNum.text = recordDatas[position].commentNum.toString()

        holder.favoriteBtn.setOnClickListener {
            if(holder.favoriteBtn.isSelected){
                holder.favoriteBtn.isSelected = false
            }
            else{
                holder.favoriteBtn.isSelected = true
            }
        }
        holder.scrabBtn.setOnClickListener {
            if(holder.scrabBtn.isSelected){
                holder.scrabBtn.isSelected = false
            }
            else{
                holder.scrabBtn.isSelected = true
            }
        }
    }
}