package com.mojilab.moji.ui.main.mypage.adapter

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.data.LoginData
import com.mojilab.moji.data.PostNoticeData
import com.mojilab.moji.ui.main.MainActivity
import com.mojilab.moji.ui.main.mypage.data.RecordData
import com.mojilab.moji.util.adapter.RecyclerviewItemDeco
import com.mojilab.moji.util.adapter.RecyclerviewTagItemDeco
import com.mojilab.moji.util.bottomsheet.BottomsheetFragment
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MypageItemAdapter(var activity : FragmentActivity, var context : Context, private var feedDatas: ArrayList<RecordData>, var requestManager : RequestManager) : RecyclerView.Adapter<MypageItemViewHolder>(){

    lateinit var recordImageAdapter: ItemImageAdapter
    lateinit var mContext: Context
    lateinit var mActivity : FragmentActivity
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    val TAG = "MypageItemAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageItemViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        mContext = context
        mActivity = activity

        return MypageItemViewHolder(mainView)
    }

    override fun getItemCount(): Int = feedDatas.size

    override fun onBindViewHolder(holder: MypageItemViewHolder, position: Int) {

        // 더보기 버튼 클릭시
        holder.moreBtn.setOnClickListener {
            val bottomSheetDialogFragment = BottomsheetFragment()
            bottomSheetDialogFragment.show(mActivity.supportFragmentManager, bottomSheetDialogFragment.tag)
        }

        requestManager.load(feedDatas[position].profileUrl).into(holder.profileImage)
        holder.profileName.text = feedDatas[position].nickName
        holder.recordDate.text = feedDatas[position].date

        holder.coarse.text = feedDatas[position].mainAddress
        holder.coarseContent.text = feedDatas[position].place

        // 이미지 리사이클러뷰
        recordImageAdapter = ItemImageAdapter(
            feedDatas[position].photoList!!,
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

        // 이미 좋아요 클릭했다면
        if(feedDatas[position].liked) holder.favoriteBtn.isSelected = true
        else holder.favoriteBtn.isSelected = false;

        // 이미 스크랩 했다면
        if(feedDatas[position].scraped) holder.scrabBtn.isSelected = true
        else holder.scrabBtn.isSelected = false

        // 좋아요 버튼 이벤트
        holder.favoriteBtn.setOnClickListener {
            if(holder.favoriteBtn.isSelected){
                holder.favoriteBtn.isSelected = false
            }
            else{
                holder.favoriteBtn.isSelected = true
            }
        }

        // 스크랩 버튼 이벤트
        holder.scrabBtn.setOnClickListener {
            if(holder.scrabBtn.isSelected){
                holder.scrabBtn.isSelected = false
            }
            else{
                holder.scrabBtn.isSelected = true
            }
        }
    }

    // 알림 보내기
    fun postNotice() {
        val networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token = SharedPreferenceController.getAuthorization(mContext);
        var noticeData : PostNoticeData = PostNoticeData("김모지님이 회원님의 게시물을 좋아합니다.")
        Log.v(TAG, "토큰 값 = " + token)
        val postSignupResponse = networkService.postNotice(token, noticeData)
        postSignupResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                Log.v(TAG, "받은 값 = " + response.toString())
                if (response.body()!!.status == 201) {
                    Log.v(TAG, "Post Notice Success")
                    Toast.makeText(mContext, "알림 post 성공", Toast.LENGTH_LONG)
                } else {
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = ")
            }
        })
    }
}