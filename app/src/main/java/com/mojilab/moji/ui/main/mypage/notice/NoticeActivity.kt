package com.mojilab.moji.ui.main.mypage.notice

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.MainActivity
import com.mojilab.moji.ui.main.mypage.notice.adapter.NoticeAdapter
import com.mojilab.moji.data.NoticeData
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetNoticeDataResponse
import kotlinx.android.synthetic.main.activity_notice.*
import retrofit2.Call
import retrofit2.Response

class NoticeActivity : AppCompatActivity() {

    lateinit var noticeDatas : ArrayList<NoticeData>
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var requestManager: RequestManager
    lateinit var networkService : NetworkService
    val TAG = "NoticeActivity"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notice)

        requestManager = Glide.with(this)
        getNoticeData();

        btn_back_notice.setOnClickListener {
            var intent = Intent(applicationContext, MainActivity::class.java)
            setResult(29, intent)
            finish()
        }
    }


    fun getNoticeData(){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(applicationContext)
        val getNoticeDataResponse = networkService.getNoticeData(token)

        getNoticeDataResponse.enqueue(object : retrofit2.Callback<GetNoticeDataResponse>{
            override fun onFailure(call: Call<GetNoticeDataResponse>, t: Throwable) {
            }

            override fun onResponse(call: Call<GetNoticeDataResponse>, response: Response<GetNoticeDataResponse>) {
                // 알림 데이터 있을 경우만

                if(response.code() == 200){
                    if (response.body()!!.status == 200) {
                        Log.v(TAG, "알림 데이터 = " + response.body()!!.toString())

                        if(response.body()!!.data.size >= 0){
                            noticeDatas = response.body()!!.data
                            noticeAdapter = NoticeAdapter(noticeDatas, requestManager)

                            rv_notice_content_notice.adapter = noticeAdapter
                            rv_notice_content_notice.layoutManager = LinearLayoutManager(applicationContext)
                        }
                    }
                    // 알림 데이터가 없는 경우
                    else if (response.body()!!.status == 200) {
                        Log.v(TAG, "알림 데이터 하나도 없음")
                    }
                    // 다른 에러들
                    else{
                        Log.v(TAG, "서버 상태 코드 = " + response.body()!!.status)
                    }
                }
                else{
                    Log.v(TAG, "알림 조회 에러 = " + response.code())
                }

            }
        })
    }


    /*// 리사이클러뷰 연결
    fun setRecyclerview(){
        noticeDatas = ArrayList<NoticeData>()
        for (i in 0..2){
            noticeDatas.add(
                NoticeData(
                    "https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg/220px-IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg",
                    "아유",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "10분 전"
                )
            )
            noticeDatas.add(
                NoticeData(
                    "https://i.pinimg.com/originals/5d/9f/ab/5d9fab1988b0d5f9cbe5f049ce4c4165.jpg",
                    "보영",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "25분 전"
                )
            )
            noticeDatas.add(
                NoticeData(
                    "https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/201906/13/moneytoday/20190613143031970hbtp.jpg",
                    "수지",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "1시간 전"
                )
            )
            noticeDatas.add(
                NoticeData(
                    null,
                    "제민",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "14:30"
                )
            )
            noticeDatas.add(
                NoticeData(
                    null,
                    "무현",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "15:20"
                )
            )
            noticeDatas.add(
                NoticeData(
                    null,
                    "승희",
                    "         님이 기록하기에서 회원님을 태그했습니다.",
                    "20:50"
                )
            )
        }
        noticeAdapter = NoticeAdapter(noticeDatas, requestManager)

        rv_notice_content_notice.adapter = noticeAdapter
        rv_notice_content_notice.layoutManager = LinearLayoutManager(applicationContext)
    }
*/
}
