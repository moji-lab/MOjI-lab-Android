package com.mojilab.moji.ui.main.mypage

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.util.TypedValue
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.notice.NoticeActivity
import com.mojilab.moji.ui.main.mypage.profilemodify.ProfileEditActivity
import com.mojilab.moji.util.adapter.ContentsPagerAdapter
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetMypageRecordData
import com.mojilab.moji.util.network.get.GetMypageRecordResponse
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import retrofit2.Call
import retrofit2.Response

class MypageFragment : Fragment()  {

    private var mContentPagerAdapter: ContentsPagerAdapter? = null
    var recordNum : Int = 0
    var scrabNum : Int = 0
    lateinit var networkService : NetworkService
    lateinit var myPageRecordData: GetMypageRecordData

    val TAG = "MypageFragment"

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_mypage, container, false)

        getMypageData(v)
        // 프로필 수정 화면으로 이동
        v.btn_edit_profile_mypage.setOnClickListener {
            var intent = Intent(context, ProfileEditActivity::class.java)
            startActivityForResult(intent, 28)
        }

        // 알림 화면으로 이동
        v.btn_alarm_mypage.setOnClickListener {
            var intent = Intent(context, NoticeActivity::class.java)
            startActivityForResult(intent, 29)
        }

        return v;
    }

    fun addTab(v :View){
        mContentPagerAdapter = ContentsPagerAdapter(
            activity!!.getSupportFragmentManager(), v.tl_container_mypage.getTabCount()
        )
        v.vp_container_mypage.setAdapter(mContentPagerAdapter)

        v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("나의 기록"))
        v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("스크랩한 글"))

        mContentPagerAdapter = ContentsPagerAdapter(
            activity!!.supportFragmentManager, v.tl_container_mypage.getTabCount()
        )

        v.vp_container_mypage.adapter = mContentPagerAdapter

        v.vp_container_mypage.addOnPageChangeListener(
            TabLayout.TabLayoutOnPageChangeListener(v.tl_container_mypage)
        )

        // 탭 선택할 때
        v.tl_container_mypage.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                v.vp_container_mypage.currentItem = tab.position
                // 각 탭에 맞게 크기 조절
                controlContentHeight(v, tab.position)
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    // 각 탭에 맞게 탭 레이아웃 크기 조절
    fun controlContentHeight(v: View, tabNum: Int) {
        var tabNo = tabNum
        var heightNum : Float = 0f
        if(tabNo == 0){
            heightNum = (445 * recordNum + 40).toFloat()
        }
        else{
            heightNum = (130 * scrabNum + 40).toFloat()
        }

        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightNum, resources.displayMetrics)
                .toInt()
        val paramlinear = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        v.rl_vpcontent_mypage.setLayoutParams(paramlinear)
    }

    // 다시 돌아왔을 때
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        // 프로필수정 화면에서 돌아왔을 때
        if(requestCode == 28) {
            var confirmFlag = data!!.getIntExtra("confirmFlag", 0)

            // 확인 버튼으로 돌아왔을 때
            if (confirmFlag == 1) {
                // 이미지뷰만 서버에서 다시 받아오기
            }
            // 뒤로가기 버튼으로 돌아왔을 때
            else {
                // 아무고토 안해도 된다.
            }
        }
        // 알림 화면에서 돌아왔을 때
        else if(requestCode == 29){

        }
    }

    fun getMypageData(v : View){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(context!!)
        val getMypageRecordResponse = networkService.getMypageRecordData(token)

        getMypageRecordResponse.enqueue(object : retrofit2.Callback<GetMypageRecordResponse>{

            override fun onResponse(call: Call<GetMypageRecordResponse>, response: Response<GetMypageRecordResponse>) {
                if (response.isSuccessful) {
                    addTab(v)
                    Log.v(TAG, "통신 성공")
                    myPageRecordData = response.body()!!.data

                    if(myPageRecordData.profileUrl != null){
                        v.iv_profile_mypage.visibility = View.VISIBLE
                        v.rl_default_proflle_img_mypage.visibility = View.GONE
                        Glide.with(context!!).load(myPageRecordData.profileUrl).error(R.drawable.profile_iu).into(v.iv_profile_mypage)
                    }
                    else{
                        v.rl_default_proflle_img_mypage.visibility = View.VISIBLE
                        v.iv_profile_mypage.visibility = View.GONE
                        v.tv_profile_name_mypage.text = myPageRecordData.nickname.substring(0, 2)
                    }
                    v.tv_nickname_mypage.text = myPageRecordData.nickname

                    recordNum = myPageRecordData.boardCount;
                    if(recordNum == 0) recordNum = 1
                    controlContentHeight(v, 0)
                    scrabNum = myPageRecordData.scrapCount;
                    if(scrabNum == 0) scrabNum = 1
                 /*   // 피드 데이터 있을 경우
                    if(myPageRecordData.feedList.size >= 0){
                    }*/
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
