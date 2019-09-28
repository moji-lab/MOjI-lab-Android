package com.mojilab.moji.ui.main.mypage.myscrab

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.ui.main.mypage.data.FeedData
import com.mojilab.moji.util.adapter.RecyclerviewItemDeco
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetMypageRecordResponse
import kotlinx.android.synthetic.main.fragment_myscrab.view.*
import retrofit2.Call
import retrofit2.Response

class MyScrabFragment : Fragment()  {

    lateinit var myScrabAdapter : MyScrabAdapter
    lateinit var requestManager: RequestManager
    lateinit var recyclerviewItemDeco : RecyclerviewItemDeco
    lateinit var networkService : NetworkService
    lateinit var myScrapDatas: ArrayList<FeedData>
    lateinit var mContext : Context
    val TAG = "MyScrapFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_myscrab, container, false)

        mContext = context!!
        // Glide
        requestManager = Glide.with(this)
        getScrapData(v)
        return v;
    }

    fun getScrapData(v : View){

        recyclerviewItemDeco = RecyclerviewItemDeco(context!!, 2)
        if (recyclerviewItemDeco != null) {
            v.rv_scrab_content_myscrab.removeItemDecoration(recyclerviewItemDeco!!)
        }
        v.rv_scrab_content_myscrab.addItemDecoration(recyclerviewItemDeco!!);

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val getMypageRecordResponse = networkService.getMyScrapData(token)

        getMypageRecordResponse.enqueue(object : retrofit2.Callback<GetMypageRecordResponse>{

            override fun onResponse(call: Call<GetMypageRecordResponse>, response: Response<GetMypageRecordResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.data.feedList != null){
                        myScrapDatas = response.body()!!.data.feedList

                        Log.v(TAG, "나의 스크랩 통신 성공 = " + myScrapDatas.toString())

                        // 스크랩 데이터가 있을 경우
                        if(myScrapDatas.size != 0){
                            v.tv_scrap_count_myscrab.text = "총 게시물 " + myScrapDatas.size.toString() + "개"
                            myScrabAdapter = MyScrabAdapter(mContext!!, myScrapDatas, requestManager)

                            v.rv_scrab_content_myscrab.adapter = myScrabAdapter
                            v.rv_scrab_content_myscrab.layoutManager = GridLayoutManager(context, 3)
                            v.rv_scrab_content_myscrab.setNestedScrollingEnabled(false)
                        }
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

}