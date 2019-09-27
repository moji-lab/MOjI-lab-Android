package com.mojilab.moji.ui.main.feed

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
import kotlinx.android.synthetic.main.fragment_feed.view.*

class FeedFragment : Fragment()  {
    lateinit var imageDatas : ArrayList<String>
    lateinit var recordDatas : ArrayList<RecordData>
    lateinit var tagDatas : ArrayList<String>
    lateinit var recordAdapter : MypageItemAdapter
    lateinit var requestManager : RequestManager

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_feed, container, false)

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

        imageDatas = ArrayList<String>()
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas = ArrayList<String>()

        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas = ArrayList<String>()

        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")
        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas = ArrayList<String>()
        tagDatas = ArrayList<String>()

        imageDatas.add("https://img.insight.co.kr/static/2019/04/03/700/4cbsd123234t969i68d5.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/993A3F335C6398F203")
        imageDatas.add("https://www.vviptravel.com/wp-content/uploads/2019/05/lotte-world-theme-park-castle-800x575.jpg")
        imageDatas.add("https://cdn.pixabay.com/photo/2018/11/29/05/00/han-river-3845034__340.jpg")
        imageDatas.add("https://t1.daumcdn.net/cfile/tistory/9942B3395A3501C304")


        recordAdapter = MypageItemAdapter(activity!!, context!!, recordDatas, requestManager)

        v.rv_feed_content_feed.adapter = recordAdapter
        v.rv_feed_content_feed.layoutManager = LinearLayoutManager(context)
        v.rv_feed_content_feed.setNestedScrollingEnabled(false)
    }
}