package com.mojilab.moji.ui.main.home
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.mojilab.moji.ui.login.LoginActivity
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedActivity
import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.GetDetailFeedResponse
import com.mojilab.moji.ui.main.home.HomeData.HomeFragmentResponse
import com.mojilab.moji.ui.main.home.HomeData.HomeRecyclerViewContentsData
import com.mojilab.moji.ui.main.mypage.notice.NoticeActivity
import com.mojilab.moji.util.adapter.DetailFeedRecyclerViewAdapter
import com.mojilab.moji.util.adapter.HomeContentsRecyclerViewAdapter
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import kotlinx.android.synthetic.main.activity_detail_feed.*
import kotlinx.android.synthetic.main.fragment_home.*
import retrofit2.Call
import retrofit2.Response
import android.R
import com.mojilab.moji.ui.main.MainActivity



/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment()  {

    companion object{
        lateinit var homeFragment : HomeFragment
        var keyword =" "
    }

    lateinit var networkService : NetworkService
    lateinit var homeContentsRecyclerViewAdapter: HomeContentsRecyclerViewAdapter
    var homeRecyclerViewContentsDataList: ArrayList<HomeRecyclerViewContentsData> = ArrayList()

    lateinit var AloneCityRecyclerViewAdapter: HomeContentsRecyclerViewAdapter
    var AloneDataList: ArrayList<HomeRecyclerViewContentsData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        homeFragment = this
        return inflater.inflate(com.mojilab.moji.R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        var bundle = Bundle()
        iv_home_btn_bell.setOnClickListener {
            var intent = Intent(context, NoticeActivity::class.java)
            startActivity(intent)
        }
        getDetailfeed()
        tv_home_hashtag1.setOnClickListener {
            (context as MainActivity).callMapFragmentWithBundle(tv_home_hashtag1.text.toString())
            keyword=tv_home_hashtag1.text.toString()
        }
        tv_home_hashtag2.setOnClickListener {
            (context as MainActivity).callMapFragmentWithBundle(tv_home_hashtag2.text.toString())
            keyword=tv_home_hashtag2.text.toString()
        }
        tv_home_hashtag3.setOnClickListener {
            (context as MainActivity).callMapFragmentWithBundle(tv_home_hashtag3.text.toString())
            keyword=tv_home_hashtag3.text.toString()
        }
        tv_home_hashtag4.setOnClickListener {
            (context as MainActivity).callMapFragmentWithBundle(tv_home_hashtag4.text.toString())
            keyword=tv_home_hashtag4.text.toString()
        }
        tv_home_hashtag5.setOnClickListener {
            (context as MainActivity).callMapFragmentWithBundle(tv_home_hashtag5.text.toString())
            keyword=tv_home_hashtag5.text.toString()
        }

    }
    fun getDetailfeed(){
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        val getHomeFragmentResponse = networkService.getHomeFragmentResponse(SharedPreferenceController.getAuthorization(context!!))

        getHomeFragmentResponse.enqueue(object : retrofit2.Callback<HomeFragmentResponse>{
            override fun onFailure(call: Call<HomeFragmentResponse>, t: Throwable) {
                Toast.makeText(context,"피드 조회 실패", Toast.LENGTH_LONG).show()
            }

            override fun onResponse(call: Call<HomeFragmentResponse>, response: Response<HomeFragmentResponse>) {
                if (response.isSuccessful) {
                    if(response.body()!!.status==200){
                       // Toast.makeText(context,"피드 조회 성공", Toast.LENGTH_LONG).show()
                        tv_home_name.text=response.body()!!.data.nickName.toString()+" 님, \n어디로 떠날까요?"
                        tv_home_hashtah.text="#"+response.body()!!.data.hotCategoryKeyword

                        //이런 여행은 어때요 5개 고정
                        AloneDataList.clear()
                        AloneDataList.add(HomeRecyclerViewContentsData("http://www.hinoky.com/files/attach/filebox/377/001/1377.jpg","#"+response.body()!!.data.hotKeywords[0]))
                        AloneDataList.add(HomeRecyclerViewContentsData("https://blog.hmgjournal.com/images_n/contents/171024_season01.png","#"+response.body()!!.data.hotKeywords[1]))
                        AloneDataList.add(HomeRecyclerViewContentsData("http://img.insight.co.kr/static/2017/08/24/700/o1970r670o9gfyx65u94.jpg",response.body()!!.data.hotKeywords[2]))
                        AloneDataList.add(HomeRecyclerViewContentsData("https://blog.hmgjournal.com/images_n/contents/180404_food01.jpg",response.body()!!.data.hotKeywords[3]))
                        AloneDataList.add(HomeRecyclerViewContentsData("http://recipe1.ezmember.co.kr/cache/recipe/2015/04/15/5cab4fbd17279e8e4cde992f90e149b21.jpg",response.body()!!.data.hotKeywords[4]))
                        homeContentsRecyclerViewAdapter = HomeContentsRecyclerViewAdapter(context!!, AloneDataList)
                        rv_home_hotcity.adapter = homeContentsRecyclerViewAdapter
                        rv_home_hotcity.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)

                        //이런 여행은 어때? 5개 고정
                        homeRecyclerViewContentsDataList.clear()
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("http://img.insight.co.kr/static/2017/08/24/700/o1970r670o9gfyx65u94.jpg","#"+response.body()!!.data.recommendKeywords[0]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("http://www.hinoky.com/files/attach/filebox/377/001/1377.jpg","#"+response.body()!!.data.recommendKeywords[1]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://news.samsung.com/kr/wp-content/uploads/2017/02/%EB%89%B4%EC%8A%A4%EB%A3%B8%EC%A3%BC%EC%B9%98%EC%9D%9823%ED%8E%B801.jpg",response.body()!!.data.recommendKeywords[2]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://japan-magazine.jnto.go.jp/jnto2wm/wp-content/uploads/1506_fireworks_main.jpg",response.body()!!.data.recommendKeywords[3]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://cdn.crowdpic.net/detail-thumb/thumb_d_9251380BC9F989860802C79579E5D8A2.jpg",response.body()!!.data.recommendKeywords[4]))
                        AloneCityRecyclerViewAdapter = HomeContentsRecyclerViewAdapter(context!!, homeRecyclerViewContentsDataList)
                        rv_home_topPlaying.adapter = AloneCityRecyclerViewAdapter
                        rv_home_topPlaying.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)
                        tv_home_hashtag1.text="#"+response.body()!!.data.topKeywords[0]
                        tv_home_hashtag2.text="#"+response.body()!!.data.topKeywords[1]
                        tv_home_hashtag3.text="#"+response.body()!!.data.topKeywords[2]
                        tv_home_hashtag4.text="#"+response.body()!!.data.topKeywords[3]
                        tv_home_hashtag5.text="#"+response.body()!!.data.topKeywords[4]



                    }
                }
            }
        })
    }

}
