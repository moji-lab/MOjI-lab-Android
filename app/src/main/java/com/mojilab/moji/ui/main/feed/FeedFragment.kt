package com.mojilab.moji.ui.main.feed

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.InputType
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.RequestManager
import com.google.gson.JsonObject
import com.google.gson.JsonParser
import com.mojilab.moji.data.SignupData
import com.mojilab.moji.ui.login.LoginActivity
import com.mojilab.moji.ui.main.feed.SearchFeed.Course
import com.mojilab.moji.ui.main.feed.SearchFeed.SearchFeedResponse
import com.mojilab.moji.ui.main.mypage.adapter.FeedItemAdapter
import com.mojilab.moji.ui.main.mypage.data.FeedData
import com.mojilab.moji.util.adapter.SearchRecyclerViewAdapter
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetRandromFeedResponse
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostLikeData
import kotlinx.android.synthetic.main.fragment_feed.*
import kotlinx.android.synthetic.main.fragment_feed.view.*
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class FeedFragment : Fragment()  {
    lateinit var searchingUserRecyclerViewAdapter: SearchRecyclerViewAdapter
    var directorySearchData = ArrayList<Course>()
    companion object{
        lateinit var feedFragment : FeedFragment
    }

    lateinit var recordAdapter : FeedItemAdapter
    lateinit var requestManager : RequestManager
    lateinit var networkService : NetworkService
    lateinit var myFeedDatas: ArrayList<FeedData>
    lateinit var SearchFeedDatas: ArrayList<FeedData>
    lateinit var userName : String
    val TAG = "FeedFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_feed, container, false)
        feedFragment = this
        requestManager = Glide.with(this)

        searchingUserRecyclerViewAdapter = SearchRecyclerViewAdapter(context!!, directorySearchData)
        v.rv_feed_Search_feed.layoutManager = GridLayoutManager(v.context, 2)
        v.rv_feed_Search_feed.adapter = searchingUserRecyclerViewAdapter
        v.rv_feed_Search_feed.setNestedScrollingEnabled(false)

        setRecyclerview(v)
        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        edt_fragment_feed_text.inputType = InputType.TYPE_CLASS_TEXT
        edt_fragment_feed_text.imeOptions = EditorInfo.IME_ACTION_SEARCH
        val imm = context?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager?
        iv_feed_btn_backbutton.setOnClickListener {
            rv_feed_Search_feed.visibility=View.GONE
            iv_feed_btn_backbutton.visibility=View.GONE
            rv_feed_content_feed.visibility=View.VISIBLE
            rl_feed_notfound.visibility=View.GONE
            rl_feed_feed_number.visibility=View.GONE
            edt_fragment_feed_text.text=null
        }
        btn_search_feed.setOnClickListener {
            if (edt_fragment_feed_text.text.toString() == "") {
                Toast.makeText(context,"검색어를 입력해주세요",Toast.LENGTH_SHORT).show()
                imm!!.hideSoftInputFromWindow(edt_fragment_feed_text.getWindowToken(), 0)
            }else{
            searchPost() // 검색 통신 넣어야함
            iv_feed_btn_backbutton.visibility=View.VISIBLE
            rv_feed_content_feed.visibility=View.GONE
            imm!!.hideSoftInputFromWindow(edt_fragment_feed_text.getWindowToken(), 0)
            //통신 했을때 값이 없으면 rl_feed_notfound visible
            }
        }
        edt_fragment_feed_text.setOnEditorActionListener({ textView, action, event ->
            var handled = false
            if (action == EditorInfo.IME_ACTION_SEARCH) {
                if (edt_fragment_feed_text.text.toString() == "") {
                    iv_feed_btn_backbutton.visibility=View.VISIBLE
                    rv_feed_content_feed.visibility=View.GONE
                    Toast.makeText(context,"검색어를 입력해주세요",Toast.LENGTH_SHORT).show()
                } else
                    searchPost()
                imm!!.hideSoftInputFromWindow(edt_fragment_feed_text.getWindowToken(), 0)
            }
            handled
        })
    }
    fun setRecyclerview(v : View){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val getRandomFeedResonse = networkService.getRandomFeedResonse(token)

        getRandomFeedResonse.enqueue(object : retrofit2.Callback<GetRandromFeedResponse>{

            override fun onResponse(call: Call<GetRandromFeedResponse>, response: Response<GetRandromFeedResponse>) {
                if (response.isSuccessful) {
                    myFeedDatas = response.body()!!.data!!
                    Log.v(TAG, "랜덤피드 통신 성공 = " + myFeedDatas.toString())

                    // 피드 데이터가 있을 경우
                    if(myFeedDatas.size != 0){
                        recordAdapter = FeedItemAdapter(activity!!, context!!, myFeedDatas, requestManager)

                        v.rv_feed_content_feed.adapter = recordAdapter
                        v.rv_feed_content_feed.layoutManager = LinearLayoutManager(context)
                        v.rv_feed_content_feed.setNestedScrollingEnabled(false)
                    }
                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<GetRandromFeedResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }
    fun searchPost(){
        var jsonObject = JSONObject()
        jsonObject.put("keyword", edt_fragment_feed_text.text.toString())
//Gson 라이브러리의 Json Parser을 통해 객체를 Json으로!
        val gsonObject = JsonParser().parse(jsonObject.toString()) as JsonObject
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val postsearch = networkService.postSearches("application/json",token,gsonObject)

        postsearch.enqueue(object : retrofit2.Callback<SearchFeedResponse>{

            override fun onResponse(call: Call<SearchFeedResponse>, response: Response<SearchFeedResponse>) {
                if (response.isSuccessful) {
                    Log.v(TAG, "피드 검색 통신 성공 ")
                  if(response.body()!!.status ==200){
                      iv_feed_btn_backbutton.visibility=View.VISIBLE
                      var temp: ArrayList<Course> = response.body()!!.data!!.courses
                      if (temp != null) {
                          rv_feed_Search_feed.visibility=View.VISIBLE
                          var position = searchingUserRecyclerViewAdapter.itemCount
                          searchingUserRecyclerViewAdapter.dataList?.clear()
                          searchingUserRecyclerViewAdapter.notifyItemInserted(searchingUserRecyclerViewAdapter.itemCount)
                          searchingUserRecyclerViewAdapter.dataList?.addAll(temp)
                          searchingUserRecyclerViewAdapter.notifyDataSetChanged()

                      }
                      // 피드 데이터가 있을 경우
                      tv_feed_feed_count.text="총 게시물 "+response.body()!!.data!!.courses!!.size.toString()+"개"
                      rl_feed_feed_number.visibility=View.VISIBLE
                      Toast.makeText(context!!,response.body()!!.message!!,Toast.LENGTH_LONG).show()
                      rl_feed_notfound.visibility=View.GONE
                      rv_feed_content_feed.visibility=View.GONE
                  }else if(response.body()!!.status ==404){
                      searchingUserRecyclerViewAdapter.dataList?.clear()
                      searchingUserRecyclerViewAdapter.notifyItemInserted(searchingUserRecyclerViewAdapter.itemCount)
                      searchingUserRecyclerViewAdapter.notifyDataSetChanged()
                      iv_feed_btn_backbutton.visibility=View.VISIBLE
                      rl_feed_feed_number.visibility=View.GONE
                      rl_feed_notfound.visibility=View.VISIBLE
                      rv_feed_content_feed.visibility=View.GONE
                      //검색 정보가 없음
                  }else if(response.body()!!.status ==600){
                      searchingUserRecyclerViewAdapter.dataList?.clear()
                      searchingUserRecyclerViewAdapter.notifyItemInserted(searchingUserRecyclerViewAdapter.itemCount)
                      searchingUserRecyclerViewAdapter.notifyDataSetChanged()
                      iv_feed_btn_backbutton.visibility=View.VISIBLE
                      Toast.makeText(context!!,"알 수 없는 오류입니다.",Toast.LENGTH_LONG).show()
                      rl_feed_notfound.visibility=View.VISIBLE
                      rv_feed_content_feed.visibility=View.GONE
                      //검색 정보가 없음
                  }


                }
                else{
                    Log.v(TAG, "통신 실패 = " + response.message().toString())
                }
            }
            override fun onFailure(call: Call<SearchFeedResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }
}