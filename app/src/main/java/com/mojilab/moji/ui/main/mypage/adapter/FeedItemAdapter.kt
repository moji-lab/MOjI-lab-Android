package com.mojilab.moji.ui.main.mypage.adapter

import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojilab.moji.R
import com.mojilab.moji.data.PostNoticeData
import com.mojilab.moji.ui.main.feed.DetailFeed.Comment.DetailCommentActivity
import com.mojilab.moji.ui.main.mypage.data.FeedData
import com.mojilab.moji.ui.main.mypage.data.PhotoData
import com.mojilab.moji.util.adapter.RecyclerviewItemDeco
import com.mojilab.moji.util.bottomsheet.BottomsheetFragment
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import com.mojilab.moji.util.network.post.data.PostScrapData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*
import kotlin.collections.ArrayList
import android.os.Bundle



class FeedItemAdapter(var userID : Int, var activity : FragmentActivity, var context : Context, private var feedDatas: ArrayList<FeedData>, var requestManager : RequestManager) : RecyclerView.Adapter<FeedItemViewHolder>(){

    lateinit var recordImageAdapter: ItemImageAdapter
    lateinit var mContext: Context
    lateinit var mActivity : FragmentActivity
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    lateinit var networkService : NetworkService
    val TAG = "FeedItemAdapter"
    var recevierId : Int = 0

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): FeedItemViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(com.mojilab.moji.R.layout.item_record, parent, false)
        mContext = context
        mActivity = activity

        return FeedItemViewHolder(mainView)
    }

    override fun getItemCount(): Int = feedDatas.size

    override fun onBindViewHolder(holder: FeedItemViewHolder, position: Int) {

        Log.v("imgData" , "받아온 데이터 = " + feedDatas[position]!!.toString())
        Log.v("asdf","유저id = " + userID)
        Log.v("asdf","비교값id = " + feedDatas[position].userIdx)
        if(userID != feedDatas[position].userIdx){
            holder.moreBtn.visibility = View.GONE
        }

        recevierId = feedDatas[position].userIdx
        // 더보기 버튼 클릭시
        holder.moreBtn.setOnClickListener {
            val bottomSheetDialogFragment = BottomsheetFragment()
            val args = Bundle()
            args.putString("boardID", feedDatas[position].boardIdx)
            bottomSheetDialogFragment.setArguments(args);
            bottomSheetDialogFragment.show(mActivity.supportFragmentManager, bottomSheetDialogFragment.tag)
        }

        requestManager.load(feedDatas[position].profileUrl).into(holder.profileImage)
        holder.profileName.text = feedDatas[position].nickName

        // 날짜
        holder.recordDate.text = feedDatas[position].date.substring(0, 10)

        holder.coarse.text = feedDatas[position].mainAddress
        holder.coarseContent.text = feedDatas[position].place

        var photoList : ArrayList<PhotoData> = feedDatas[position].photoList!!
        photoList = feedDatas[position].photoList
        Collections.reverse(photoList);
        // 이미지 리사이클러뷰
        recordImageAdapter = ItemImageAdapter(
            mContext,
            feedDatas[position].boardIdx,
            photoList,
            requestManager
        )

        recyclerviewItemDeco = RecyclerviewItemDeco(context!!, 0)
        if (recyclerviewItemDeco != null) {
            holder.recordImagesRv.removeItemDecoration(recyclerviewItemDeco!!)
        }
        holder.recordImagesRv.addItemDecoration(recyclerviewItemDeco!!);
        holder.recordImagesRv.adapter = recordImageAdapter
        holder.recordImagesRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        holder.likeNum.text = feedDatas[position].likeCount.toString()
        holder.commentNum.text = feedDatas[position].commentCount.toString()

        holder.chatBtn.setOnClickListener{
            var intent : Intent = Intent(context, DetailCommentActivity::class.java)
            intent.putExtra("flag", 0)
            intent.putExtra("boardId", feedDatas[position].boardIdx)
            intent.putExtra("profileImgUrl", feedDatas[position].profileUrl)
            context.startActivity(intent)
        }

        // 이미 좋아요 클릭했다면
        if(feedDatas[position].liked) holder.favoriteBtn.isSelected = true
        else holder.favoriteBtn.isSelected = false;

        // 이미 스크랩 했다면
        if(feedDatas[position].scraped) holder.scrabBtn.isSelected = true
        else holder.scrabBtn.isSelected = false

        // 좋아요 버튼 이벤트
        holder.favoriteBtn.setOnClickListener {
            // 좋아요가 눌러있다면
            if(holder.favoriteBtn.isSelected){
                holder.favoriteBtn.isSelected = false
                // 좋아요 -1 TextView 변경
                holder.likeNum.text = (Integer.parseInt(holder.likeNum.text as String)-1).toString()
            }
            // 좋아요가 눌러있지 않다면
            else{
                holder.favoriteBtn.isSelected = true
                // 좋아요 +1 TextView 변경
                holder.likeNum.text = (Integer.parseInt(holder.likeNum.text as String)+1).toString()
                if(userID != feedDatas[position].userIdx) seondNotice()
            }
            postLike(position)
        }

        // 스크랩 버튼 이벤트
        holder.scrabBtn.setOnClickListener {
            if(holder.scrabBtn.isSelected){
                holder.scrabBtn.isSelected = false
                deleteScrap(position)
            }
            else{
                holder.scrabBtn.isSelected = true
                postScrap(position)
            }
        }
    }


    // 좋아요
    fun postLike(position : Int) {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postLikeData = PostLikeData(feedDatas.get(position).boardIdx)

        val postFeedLikeResponse = networkService.postLike(token, postLikeData)
        postFeedLikeResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }
    
    // 스크랩 ON
    fun postScrap(position : Int) {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postScrapData = PostScrapData(feedDatas.get(position).boardIdx)

        val postSignupResponse = networkService.postScrap(token, postScrapData)
        postSignupResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

    // 스크랩 OFF
    fun deleteScrap(position : Int) {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postScrapData = PostScrapData(feedDatas.get(position).boardIdx)

        val postSignupResponse = networkService.deleteScrap(token, postScrapData)
        postSignupResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }

            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }


    // 알림 보내기
    fun seondNotice() {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        var nickname : String = SharedPreferenceController.getUserNickname(context!!)
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postNoticeData = PostNoticeData(recevierId, nickname + "님이 회원님의 게시물을 좋아합니다.")

        val postNoticeResponse = networkService.postNotice(token, postNoticeData)
        postNoticeResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.v(TAG, "알림 데이터 = " + response.body().toString())
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "알림 메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

}