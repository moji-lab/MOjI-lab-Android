package com.mojilab.moji.ui.main.home


import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mojilab.moji.ui.login.LoginActivity
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.home.HomeData.HomeRecyclerViewContentsData
import com.mojilab.moji.util.adapter.HomeContentsRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment()  {
    lateinit var homeContentsRecyclerViewAdapter: HomeContentsRecyclerViewAdapter
    var homeRecyclerViewContentsDataList: ArrayList<HomeRecyclerViewContentsData> = ArrayList()

    lateinit var AloneCityRecyclerViewAdapter: HomeContentsRecyclerViewAdapter
    var AloneDataList: ArrayList<HomeRecyclerViewContentsData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.mojilab.moji.R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //첫번째
        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://t1.daumcdn.net/liveboard/dailylife/2b9ea3473a9e4e868b17bec5815983d5.jpg","#불꽃놀이"))
        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://news.samsung.com/kr/wp-content/uploads/2017/03/%ED%91%B8%EB%93%9C%ED%8F%AC%EC%BB%A4%EC%8A%A44%ED%8E%B804.jpg","#삼겹살축제"))
        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://steemitimages.com/DQmZQcAQYCxHKLtomTLdjkMBqiAc5yZqq4dKwK6wZNvSeq4/44.jpg","#술마시고싶다"))
        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://post-phinf.pstatic.net/MjAxODA5MDVfNDcg/MDAxNTM2MTI4NjI1Mjg5.gA42RDRKg46XwyT9mAb9jQ4g-M6G_RUm3eYPScDmO94g.a5Y9z--DthGq59YR9ye2h8YkT_kSRvjxGLoZKP_tVHog.JPEG/%EB%B6%88%EA%BD%83%EC%B6%95%EC%A0%9C%EB%AA%85%EB%8B%B901.jpg?type=w1200","#맥주축제"))
        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://t1.daumcdn.net/liveboard/dailylife/2b9ea3473a9e4e868b17bec5815983d5.jpg","#곱창먹기축제"))
        homeContentsRecyclerViewAdapter = HomeContentsRecyclerViewAdapter(context!!, homeRecyclerViewContentsDataList)
        rv_home_hotcity.adapter = homeContentsRecyclerViewAdapter
        rv_home_hotcity.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)

        //두번째꺼
        AloneDataList.add(HomeRecyclerViewContentsData("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg","#술여행"))
        AloneDataList.add(HomeRecyclerViewContentsData("https://post-phinf.pstatic.net/MjAxODA0MDNfMjgy/MDAxNTIyNjgxNjQzMTc2.9zObByVQ-Az9SuNbnhDA34JAkBHBgBL0zh2xjibG8cIg.s9M1q3XTHMUBXLY1RuDZ7h40YZGu8RpXAEcTk4lKCxog.JPEG/bjsn-20171130-195451-000-resize.jpg?type=w1200","#알파고여행"))
        AloneDataList.add(HomeRecyclerViewContentsData("https://news.samsung.com/kr/wp-content/uploads/2017/02/%EB%89%B4%EC%8A%A4%EB%A3%B8%EC%A3%BC%EC%B9%98%EC%9D%9823%ED%8E%B801.jpg","#회사취직여행"))
        AloneDataList.add(HomeRecyclerViewContentsData("https://japan-magazine.jnto.go.jp/jnto2wm/wp-content/uploads/1506_fireworks_main.jpg","#캠퍼스투어"))
        AloneDataList.add(HomeRecyclerViewContentsData("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg","#사옥투어"))
        AloneCityRecyclerViewAdapter = HomeContentsRecyclerViewAdapter(context!!, AloneDataList)
        rv_home_topPlaying.adapter = AloneCityRecyclerViewAdapter
        rv_home_topPlaying.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)




        // 디테일 보드 가는 테스트 나중에 지워야함
        tv_home_name.setOnClickListener {
            var intent = Intent(context, DetailFeedActivity::class.java)
            startActivity(intent)

        }

    }


}
