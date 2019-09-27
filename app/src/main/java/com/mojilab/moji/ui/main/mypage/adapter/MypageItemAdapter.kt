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

class MypageItemAdapter(var activity : FragmentActivity, var context : Context, private var recordDatas: ArrayList<RecordData>, var requestManager : RequestManager) : RecyclerView.Adapter<MypageItemViewHolder>(){

    lateinit var recordImageAdapter: ItemImageAdapter
    lateinit var recordTagAdapter: ItemTagAdapter
    lateinit var mContext: Context
    lateinit var mActivity : FragmentActivity
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    lateinit var rvTagItemDeco : RecyclerviewTagItemDeco
    val TAG = "MypageItemAdapter"

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MypageItemViewHolder {
        val mainView : View = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_record, parent, false)
        mContext = context
        mActivity = activity

        return MypageItemViewHolder(mainView)
    }

    override fun getItemCount(): Int = recordDatas.size

    override fun onBindViewHolder(holder: MypageItemViewHolder, position: Int) {

 /*       holder.moreBtn.setOnClickListener {
            val bottomSheetDialogFragment = BottomsheetFragment()
            bottomSheetDialogFragment.show(mActivity.supportFragmentManager, bottomSheetDialogFragment.tag)
        }

        requestManager.load(recordDatas[position].profileImgUrl).into(holder.profileImage)
        holder.profileName.text = recordDatas[position].name
        holder.recordDate.text = recordDatas[position].date

        holder.coarse.text = recordDatas[position].coarse
        holder.coarseContent.text = recordDatas[position].carseContent

        // 이미지 리사이클러뷰
        recordImageAdapter = ItemImageAdapter(
            recordDatas[position].recordImg!!,
            requestManager
        )

        recyclerviewItemDeco = RecyclerviewItemDeco(context!!, 0)
        if (recyclerviewItemDeco != null) {
            holder.recordImagesRv.removeItemDecoration(recyclerviewItemDeco!!)
        }
        holder.recordImagesRv.addItemDecoration(recyclerviewItemDeco!!);
        holder.recordImagesRv.adapter = recordImageAdapter
        holder.recordImagesRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)


        // 태그 리사이클러뷰
        if(recordDatas[position].tags!!.size == 0){
            holder.tagsRv.visibility = View.GONE
        }
        else{
            holder.tagsRv.visibility = View.VISIBLE
        }

        recordTagAdapter = ItemTagAdapter(
            recordDatas[position].tags!!
        )

        rvTagItemDeco = RecyclerviewTagItemDeco(context!!)
        if (rvTagItemDeco != null) {
            holder.tagsRv.removeItemDecoration(rvTagItemDeco!!)
        }
        holder.tagsRv.addItemDecoration(rvTagItemDeco!!);
        holder.tagsRv.adapter = recordTagAdapter
        holder.tagsRv.layoutManager = LinearLayoutManager(mContext, LinearLayoutManager.HORIZONTAL, false)

        holder.likeNum.text = recordDatas[position].likeNum.toString()
        holder.commentNum.text = recordDatas[position].commentNum.toString()

        holder.favoriteBtn.setOnClickListener {
            if(holder.favoriteBtn.isSelected){
                holder.favoriteBtn.isSelected = false
            }
            else{
                holder.favoriteBtn.isSelected = true
               // postNotice()
            }
        }
        holder.scrabBtn.setOnClickListener {
            if(holder.scrabBtn.isSelected){
                holder.scrabBtn.isSelected = false
            }
            else{
                holder.scrabBtn.isSelected = true
            }
        }*/
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