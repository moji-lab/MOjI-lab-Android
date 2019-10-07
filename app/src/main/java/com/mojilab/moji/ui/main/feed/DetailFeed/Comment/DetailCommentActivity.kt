package com.mojilab.moji.ui.main.feed.DetailFeed.Comment

import android.content.ContentValues
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetCommentResponce
import com.mojilab.moji.util.network.get.GetUserDataResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostCoarseCommentData
import kotlinx.android.synthetic.main.activity_detail_comment.*
import org.jetbrains.anko.ctx
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.feed.FeedFragment
import com.mojilab.moji.ui.main.mypage.myrecord.MyRecordFragment
import com.mojilab.moji.util.network.post.data.PostFeedCommentData


class DetailCommentActivity : AppCompatActivity() {
    lateinit var detailCommnetRecyclerViewAdapter: DetailCommnetRecyclerViewAdapter
    var detailCommentDataList: ArrayList<DetailCommentData?> = ArrayList()
    val TAG = "DetailCommentActivity"
    lateinit var networkService : NetworkService
    lateinit var requestManager: RequestManager
    lateinit var profileImgUrls : ArrayList<String>
    var coarseId : String = ""
    var boardId : String = ""
    var userID : Int = 0
    var profileImgUrl : String = ""
    var randomFeedFlag : Int = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(com.mojilab.moji.R.layout.activity_detail_comment)
        profileImgUrls = ArrayList<String>()
        requestManager = Glide.with(this)

        // 1인 경우 : 피드탭에서 입장, 0인 경우 : 나의기록에서 입장
        randomFeedFlag = intent.getIntExtra("randomFeedFlag", 0)

        var flag = intent.getIntExtra("flag", 0)
        // 피드에서 들어올 경우
        if(flag == 0){
            boardId = intent.getStringExtra("boardId")
            profileImgUrl = intent.getStringExtra("profileImgUrl")//이거 안쓰는중

            if(SharedPreferenceController.getUserPicture(this) != "") {
                Log.v(TAG, "피드에서 들어와서 프로필이미지가 NULL이 아님"+SharedPreferenceController.getUserPicture(this))
                requestManager.load(SharedPreferenceController.getUserPicture(this)).into(cv_detail_comment_mypicture)
                rl_detail_comment_default_proflle_img_comment.visibility=View.GONE
            }else{
                Log.v(TAG, "피드에서 들어와서 프로필이미지가 NUll이라서 텍스트 넣음")
                tv_detail_comment_profile_name_comment.text=SharedPreferenceController.getUserNickname(this).substring(0,2)
            }
            getFeedComment(boardId)
        }
        // 코스에서 들어올 경우
        else{
            userID = intent.getIntExtra("userID", 0)
            getMyProfileImg()
            coarseId = intent.getStringExtra("coarseId")
            getCoarseComment(coarseId)
        }

        iv_detail_comment_back_btn.setOnClickListener {
            // 랜덤 피드 통해서 입장
            if(randomFeedFlag == 1) FeedFragment.feedFragment.recordAdapter.notifyDataSetChanged()
            // 나의 기록하기 통해서 입장
            else if(randomFeedFlag == 0) MyRecordFragment.myRecordFragment.recordAdapter.notifyDataSetChanged()
            // 보드 상세페이지 통해서 입장
            else DetailFeedActivity.detailFeedActivity.DetailFeedRecyclerViewAdapter.notifyDataSetChanged()

            finish()
        }

        iv_detail_comment_comfirm.setOnClickListener {
            // 피드
            if(flag == 0){
                Log.v(TAG, "피드 댓글 POST 시작")
                postFeedComment()
            }
            // 코스
            else if(flag == 1){
                Log.v(TAG, "코스 댓글 POST 시작")
                postCoarseComment();
            }
        }
    }

    // 피드 댓글 작성
    fun postFeedComment() {
        var token : String = SharedPreferenceController.getAuthorization(applicationContext);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postFeedCommentData = PostFeedCommentData(boardId, edit_comment_content_detail.text.toString())

        val postCoarseCommentResponse = networkService.postFeedComment(token, postFeedCommentData)
        postCoarseCommentResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                    // 갱신해야함
                    edit_comment_content_detail.setText("")
                    getFeedComment(boardId)
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

    // 코스 댓글 작성
    fun postCoarseComment() {
        var token : String = SharedPreferenceController.getAuthorization(applicationContext);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val postCoarseCommentData = PostCoarseCommentData(coarseId, edit_comment_content_detail.text.toString())

        val postCoarseCommentResponse = networkService.postCoarseComment(token, postCoarseCommentData)
        postCoarseCommentResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 201) {
                    Log.v(TAG,  "메시지 = " + response.body()!!.message)
                    // 갱신해야함
                    edit_comment_content_detail.setText("")
                    getCoarseComment(coarseId)
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

    // 피드 댓글 데이터 가져오기
    fun getFeedComment(feedId : String){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getFeedCommentResponce = networkService.getFeedCommentResonse(token, feedId)
        Log.v("Asdf","피드 = " + feedId)

        getFeedCommentResponce.enqueue(object : retrofit2.Callback<GetCommentResponce>{

            override fun onResponse(call: Call<GetCommentResponce>, response: Response<GetCommentResponce>) {
                if (response.isSuccessful) {
                    if(response.body()!!.data != null){
                        detailCommentDataList = response.body()!!.data
                        Log.v(TAG, "피드 댓글 통신 성공 = " + detailCommentDataList.toString())
                        // 댓글 데이터가 있을 경우
                        getProfileImg()
                    }
                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<GetCommentResponce>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

    // 코스 댓글 데이터 가져오기
    fun getCoarseComment(coarseId : String){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getCoarseCommentResponce = networkService.getCoarseCommentResonse(token, coarseId)

        getCoarseCommentResponce.enqueue(object : retrofit2.Callback<GetCommentResponce>{

            override fun onResponse(call: Call<GetCommentResponce>, response: Response<GetCommentResponce>) {
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
            override fun onFailure(call: Call<GetCommentResponce>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

    // 사용자 이미지 주소 가져오기
    fun getProfileImg(){
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            for (i in 0.. detailCommentDataList.size-1){
                var getUserDataResponse = networkService.getUserData(detailCommentDataList[i]?.userIdx!!) // 네트워크 서비스의 getContent 함수를 받아옴

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
                                rv_detail_comment.scrollToPosition(detailCommentDataList.size-1)
                                val imm =
                                    getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                                imm.hideSoftInputFromWindow(edit_comment_content_detail.getWindowToken(), 0);
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

    // 본인이미지 주소 가져오기
    fun getMyProfileImg(){
        try {
            networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

            var getUserDataResponse = networkService.getUserData(userID) // 네트워크 서비스의 getContent 함수를 받아옴

            getUserDataResponse.enqueue(object : Callback<GetUserDataResponse> {
                override fun onResponse(call: Call<GetUserDataResponse>?, response: Response<GetUserDataResponse>?) {
                    if(response!!.body()!!.status == 200) {
                        Log.v(ContentValues.TAG, "내 프로필 이미지 = " + response.body()!!.data.photoUrl)
                       // requestManager.load(response.body()!!.data.photoUrl).into(cv_detail_comment_mypicture)
                        if(SharedPreferenceController.getUserPicture(this@DetailCommentActivity) != "") {
                            Log.v(TAG, "코스에서 들어와서 프로필이미지가 NULL이 아님"+SharedPreferenceController.getUserPicture(this@DetailCommentActivity))
                            requestManager.load(SharedPreferenceController.getUserPicture(this@DetailCommentActivity)).into(cv_detail_comment_mypicture)
                            rl_detail_comment_default_proflle_img_comment.visibility=View.GONE
                        }else{
                            Log.v(TAG, "코스에서 들어와서 프로필이미지가 NUll이라서 텍스트 넣음")
                            tv_detail_comment_profile_name_comment.text=SharedPreferenceController.getUserNickname(this@DetailCommentActivity).substring(0,2)
                        }
                    }
                    else{
                        Log.v(TAG, "서버 코드 = " + response!!.body()!!.status)
                    }
                }
                override fun onFailure(call: Call<GetUserDataResponse>?, t: Throwable?) {
                }
            })

        } catch (e: Exception) {
        }

    }

    override fun onBackPressed() {
        // 랜덤 피드 통해서 입장
        if(randomFeedFlag == 1) FeedFragment.feedFragment.recordAdapter.notifyDataSetChanged()
        // 나의 기록하기 통해서 입장
        else if(randomFeedFlag == 0) MyRecordFragment.myRecordFragment.recordAdapter.notifyDataSetChanged()
        // 보드 상세페이지 통해서 입장
        else DetailFeedActivity.detailFeedActivity.DetailFeedRecyclerViewAdapter.notifyDataSetChanged()

        super.onBackPressed()
    }
}