package com.mojilab.moji.ui.main.mypage.myrecord

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.adapter.FeedItemAdapter
import com.mojilab.moji.ui.main.mypage.data.FeedData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetMypageRecordResponse
import kotlinx.android.synthetic.main.fragment_myrecord.view.*
import retrofit2.Call
import retrofit2.Response

class MyRecordFragment : Fragment()  {

    lateinit var recordAdapter : FeedItemAdapter
    lateinit var requestManager: RequestManager
    lateinit var networkService : NetworkService
    lateinit var myFeedDatas: ArrayList<FeedData>
    lateinit var mActivity : FragmentActivity
    lateinit var mContext : Context
    var userID : Int = 0

    val TAG = "MyRecordFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_myrecord, container, false)

        mActivity = activity!!
        mContext = context!!
        userID = SharedPreferenceController.getUserId(context!!)
        myRecordFragment = this
        // Glide
        requestManager = Glide.with(this)
        getMypageData(v, 0)

        return v;
    }

    fun getMypageData(v : View, flag : Int){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val getMypageRecordResponse = networkService.getMypageRecordData(token)

        getMypageRecordResponse.enqueue(object : retrofit2.Callback<GetMypageRecordResponse>{

            override fun onResponse(call: Call<GetMypageRecordResponse>, response: Response<GetMypageRecordResponse>) {
                if (response.isSuccessful) {
                    Log.v(TAG, "나의 기록 통신 성공")
                    myFeedDatas = response.body()!!.data.feedList

                    // 피드 데이터가 있을 경우
                    if(myFeedDatas.size != 0){

                        v.tv_record_count_myrecord.text = "총 게시물 " + myFeedDatas.size.toString() + "개"

                        // 프로필 사진 변경 X
                            recordAdapter = FeedItemAdapter(userID, mActivity, mContext!!, myFeedDatas, requestManager, 0)

                            v.rv_record_myrecord.adapter = recordAdapter
                            v.rv_record_myrecord.layoutManager = LinearLayoutManager(context)
                            v.rv_record_myrecord.setNestedScrollingEnabled(false)
                    }else{
                        v.tv_record_count_myrecord.text = "총 게시물 "  + "0개"
                    }
                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<GetMypageRecordResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }
    
    companion object{
        lateinit var myRecordFragment : MyRecordFragment
    }
}
