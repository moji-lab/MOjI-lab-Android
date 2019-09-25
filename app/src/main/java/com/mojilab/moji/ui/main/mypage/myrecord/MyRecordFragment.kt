package com.mojilab.moji.ui.main.mypage.myrecord

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.mojilab.moji.ui.main.mypage.adapter.MypageItemAdapter
import com.mojilab.moji.ui.main.mypage.data.RecordData
import kotlinx.android.synthetic.main.fragment_myrecord.view.*

class MyRecordFragment : Fragment()  {

    lateinit var recordAdapter : MypageItemAdapter
    lateinit var recordDatas : ArrayList<RecordData>
    lateinit var requestManager: RequestManager
    lateinit var imageDatas : ArrayList<String>
    lateinit var tagDatas : ArrayList<String>

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_myrecord, container, false)

        // Glide
        requestManager = Glide.with(this)
        setRecyclerview(v)

        return v;
    }

    fun setRecyclerview(v : View){

        imageDatas = ArrayList<String>()
        recordDatas = ArrayList<RecordData>()
        tagDatas = ArrayList<String>()
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")

        tagDatas.add("#카페")
        tagDatas.add("#속초")

        recordDatas.add(RecordData("https://i.pinimg.com/originals/5d/9f/ab/5d9fab1988b0d5f9cbe5f049ce4c4165.jpg", "박보영", "2019년 9월 20일",imageDatas, "춘천", "닭갈비거리-막국수", 10, 4, tagDatas))
        imageDatas = ArrayList<String>()
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        recordDatas.add(RecordData("https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg/220px-IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg", "아이유", "2019년 9월 20일",imageDatas, "춘천", "닭갈비거리-막국수", 10, 4, tagDatas))
        imageDatas = ArrayList<String>()

        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        recordDatas.add(RecordData("https://i.pinimg.com/originals/5d/9f/ab/5d9fab1988b0d5f9cbe5f049ce4c4165.jpg", "박보영", "2019년 9월 20일",imageDatas, "춘천", "닭갈비거리-막국수", 10, 4, tagDatas))
        imageDatas = ArrayList<String>()

        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        recordDatas.add(RecordData("https://upload.wikimedia.org/wikipedia/commons/thumb/e/eb/IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg/220px-IU_at_%22Real%22_VIP_premiere%2C_27_June_2017_04.jpg", "아이유", "2019년 9월 20일",imageDatas, "춘천", "닭갈비거리-막국수", 10, 4, tagDatas))
        imageDatas = ArrayList<String>()
        tagDatas = ArrayList<String>()

        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        recordDatas.add(RecordData("https://i.pinimg.com/originals/5d/9f/ab/5d9fab1988b0d5f9cbe5f049ce4c4165.jpg", "박보영", "2019년 9월 20일",imageDatas, "춘천", "닭갈비거리-막국수", 10, 4, tagDatas))


        recordAdapter = MypageItemAdapter(context!!, recordDatas, requestManager)

        v.rv_record_myrecord.adapter = recordAdapter
        v.rv_record_myrecord.layoutManager = LinearLayoutManager(context)
        v.rv_record_myrecord.setNestedScrollingEnabled(false)
    }

}
