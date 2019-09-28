package com.mojilab.moji.ui.main.feed.DetailFeed.Comment

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetCoarseCommentResponce
import com.mojilab.moji.util.network.get.GetProfileImgResponse
import com.mojilab.moji.util.network.get.GetUserDataResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostCoarseCommentData
import com.mojilab.moji.util.network.post.data.PostLikeData
import kotlinx.android.synthetic.main.activity_detail_comment.*
import org.jetbrains.anko.ctx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailCommentActivity : AppCompatActivity() {
    lateinit var detailCommnetRecyclerViewAdapter: DetailCommnetRecyclerViewAdapter
    var detailCommentDataList: ArrayList<DetailCommentData?> = ArrayList()
    val TAG = "DetailCommentActivity"
    lateinit var networkService : NetworkService
    lateinit var requestManager: RequestManager
    lateinit var profileImgUrls : ArrayList<String>
    var coarseId : String = ""
    var boardId : String = ""
    var flag : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_detail_comment)
        profileImgUrls = ArrayList<String>()

        // 피드에서 들어올 경우
        if(intent.getIntExtra("flag", 0) == 0){
            boardId = intent.getStringExtra("boardId")
        }
        // 코스에서 들어올 경우
        else{
            coarseId = intent.getStringExtra("coarseId")

            getCoarseComment(coarseId)
        }

        requestManager = Glide.with(this)

        iv_detail_comment_back_btn.setOnClickListener {
            finish()
        }

        iv_detail_comment_comfirm.setOnClickListener {
            postCoarseComment();
        }
    }

    // 코스 댓글 작성
    fun postCoarseComment() {
        var token : String = SharedPreferenceController.getAuthorization(applicationContext);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postCoarseCommentData = PostCoarseCommentData(coarseId, "Hi Hi Hi")

        val postCoarseCommentResponse = networkService.postCoarseComment(token, postCoarseCommentData)
        postCoarseCommentResponse.enqueue(object : Callback<PostResponse> {
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

    fun getCoarseComment(coarseId : String){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getCoarseCommentResponce = networkService.getCoarseCommentResonse(token, coarseId)

        getCoarseCommentResponce.enqueue(object : retrofit2.Callback<GetCoarseCommentResponce>{

            override fun onResponse(call: Call<GetCoarseCommentResponce>, response: Response<GetCoarseCommentResponce>) {
                if (response.isSuccessful) {
                    if(response.body()!!.data != null){
                        detailCommentDataList = response.body()!!.data
                        Log.v(TAG, "코스 댓글 통신 성공 = " + detailCommentDataList.toString())
                        // 댓글 데이터가 있을 경우
                        getProfileImg()
                    }
                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<GetCoarseCommentResponce>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

    // 사용자 이미지 주소 가져오기
    fun getProfileImg(){
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            for (i in 0.. detailCommentDataList.size-1){
                var getUserDataResponse = networkService.getUserData(detailCommentDataList[i]!!.userIdx.toString()) // 네트워크 서비스의 getContent 함수를 받아옴

                getUserDataResponse.enqueue(object : Callback<GetUserDataResponse> {
                    override fun onResponse(call: Call<GetUserDataResponse>?, response: Response<GetUserDataResponse>?) {
                        if(response!!.body()!!.status == 200) {
                            Log.v(ContentValues.TAG, "프로필 이미지 = " + response.body()!!.data.photoUrl)
                            detailCommentDataList[i]!!.profileImgUrl = response.body()!!.data.photoUrl
                            detailCommentDataList[i]!!.userName = response.body()!!.data.nickname

                            // 마지막 프로필 이미지 저장
                            if(i == detailCommentDataList.size-1){
                                detailCommnetRecyclerViewAdapter = DetailCommnetRecyclerViewAdapter(applicationContext, detailCommentDataList, requestManager)

                                detailCommnetRecyclerViewAdapter = detailCommnetRecyclerViewAdapter
                                rv_detail_comment.adapter = detailCommnetRecyclerViewAdapter
                                rv_detail_comment.layoutManager = LinearLayoutManager(ctx,LinearLayoutManager.VERTICAL,false)
                            }
                        }
                        else{
                            Log.v(TAG, "서버 코드 = " + response!!.body()!!.status)
                        }
                    }
                    override fun onFailure(call: Call<GetUserDataResponse>?, t: Throwable?) {
                    }
                })
            }

        } catch (e: Exception) {
        }

    }
}