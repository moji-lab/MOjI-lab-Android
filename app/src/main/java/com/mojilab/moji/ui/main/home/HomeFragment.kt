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
import android.graphics.Color
import android.os.Handler
import com.mojilab.moji.ui.main.MainActivity
import android.graphics.Color.parseColor
import android.graphics.PorterDuff









/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment()  {

    companion object{
        lateinit var homeFragment : HomeFragment
        var keyword =""
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
        val c = resources.getColor(R.color.holo_orange_light)
        loading_progress.setIndeterminate(true)
        loading_progress.getIndeterminateDrawable().setColorFilter(c, PorterDuff.Mode.MULTIPLY)


/*        Handler().postDelayed(Runnable {
            //loading progress bar
            loading_progress.visibility = View.VISIBLE
            getDetailfeed()

        }, 200)*/


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

        loading_progress.visibility = View.VISIBLE
            getDetailfeed()


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
                    loading_progress.visibility = View.GONE
                    if(response.body()!!.status==200){
                       // Toast.makeText(context,"피드 조회 성공", Toast.LENGTH_LONG).show()
                        tv_home_name.text=response.body()!!.data?.nickName?.toString()+" 님, \n어디로 떠날까요?"
                        tv_home_hashtah.text="#"+response.body()!!.data.hotCategoryKeyword

                        //요즘 핫한 축제 5개 고정
                        AloneDataList.clear()
                        AloneDataList.add(HomeRecyclerViewContentsData("https://img1.daumcdn.net/thumb/R720x0/?fname=https://t1.daumcdn.net/liveboard/dailylife/82301ad97d054aac9b8ff0f3beb8229d.jpg","#"+response.body()!!.data.hotKeywords[0]))
                        AloneDataList.add(HomeRecyclerViewContentsData("https://blog.hmgjournal.com/images_n/contents/171024_season01.png","#"+response.body()!!.data.hotKeywords[1]))
                        AloneDataList.add(HomeRecyclerViewContentsData("http://www.seoulilbo.com/news/photo/201901/350028_165856_3412.jpg","#"+response.body()!!.data.hotKeywords[2]))
                        AloneDataList.add(HomeRecyclerViewContentsData("https://img.seoul.co.kr/img/upload/2019/07/21/SSI_20190721163549_V.jpg","#"+response.body()!!.data.hotKeywords[3]))
                        AloneDataList.add(HomeRecyclerViewContentsData("http://recipe1.ezmember.co.kr/cache/recipe/2015/04/15/5cab4fbd17279e8e4cde992f90e149b21.jpg","#"+response.body()!!.data.hotKeywords[4]))
                        homeContentsRecyclerViewAdapter = HomeContentsRecyclerViewAdapter(context!!, AloneDataList)
                        rv_home_hotcity.adapter = homeContentsRecyclerViewAdapter
                        rv_home_hotcity.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)

                        //이런 여행은 어때? 5개 고정
                        homeRecyclerViewContentsDataList.clear()
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://t1.daumcdn.net/cfile/tistory/99F1203B5A94A89615","#"+response.body()!!.data.recommendKeywords[0]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("http://health.chosun.com/site/data/img_dir/2019/03/29/2019032901549_0.jpg","#"+response.body()!!.data.recommendKeywords[1]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://s3-ap-northeast-2.amazonaws.com/www.sktinsight.com/wp-content/uploads/2018/09/%EC%9D%B8%EA%B5%AC%EC%87%BC%ED%81%AC_%EC%84%9C%EC%9A%B8%EC%9D%B8%EA%B5%AC_3.jpg","#"+response.body()!!.data.recommendKeywords[2]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("http://www.polinews.co.kr/data/photos/201711/art_1510300371.jpg","#"+response.body()!!.data.recommendKeywords[3]))
                        homeRecyclerViewContentsDataList.add(HomeRecyclerViewContentsData("https://japan-magazine.jnto.go.jp/jnto2wm/wp-content/uploads/1506_fireworks_main.jpg","#"+response.body()!!.data.recommendKeywords[4]))
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
