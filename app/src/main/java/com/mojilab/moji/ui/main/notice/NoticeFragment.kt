package com.mojilab.moji.ui.main.notice

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.ui.main.notice.adapter.NoticeAdapter
import com.mojilab.moji.ui.main.notice.data.NoticeData
import kotlinx.android.synthetic.main.fragment_notice.view.*

class NoticeFragment : Fragment()  {

    lateinit var noticeDatas : ArrayList<NoticeData>
    lateinit var noticeAdapter: NoticeAdapter
    lateinit var requestManager: RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_notice, container, false)

        requestManager = Glide.with(this)
        setRecyclerview(v)

        return v;
    }

    fun setRecyclerview(v : View){

        noticeDatas = ArrayList<NoticeData>()
        for (i in 0..2){
            noticeDatas.add(NoticeData("https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg/220px-IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg", "아유", "         님이 기록하기에서 회원님을 태그했습니다.", "10분 전"))
            noticeDatas.add(NoticeData("https://i.pinimg.com/originals/5d/9f/ab/5d9fab1988b0d5f9cbe5f049ce4c4165.jpg", "보영", "         님이 기록하기에서 회원님을 태그했습니다.", "25분 전"))
            noticeDatas.add(NoticeData("https://img4.daumcdn.net/thumb/R658x0.q70/?fname=https://t1.daumcdn.net/news/201906/13/moneytoday/20190613143031970hbtp.jpg", "수지", "         님이 기록하기에서 회원님을 태그했습니다.", "1시간 전"))
            noticeDatas.add(NoticeData(null, "제민", "         님이 기록하기에서 회원님을 태그했습니다.", "14:30"))
            noticeDatas.add(NoticeData(null, "무현", "         님이 기록하기에서 회원님을 태그했습니다.", "15:20"))
            noticeDatas.add(NoticeData(null, "승희", "         님이 기록하기에서 회원님을 태그했습니다.", "20:50"))
        }
        noticeAdapter = NoticeAdapter(noticeDatas, requestManager)

        v.rv_notice_content_notice.adapter = noticeAdapter
        v.rv_notice_content_notice.layoutManager = LinearLayoutManager(context)
    }
}