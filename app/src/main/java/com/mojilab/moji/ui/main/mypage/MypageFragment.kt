package com.mojilab.moji.ui.main.mypage

import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
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
import com.mojilab.moji.ui.login.LoginActivity
import com.mojilab.moji.ui.main.mypage.notice.NoticeActivity
import com.mojilab.moji.ui.main.mypage.profilemodify.ProfileEditActivity
import com.mojilab.moji.util.adapter.ContentsPagerAdapter
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.get.GetMypageRecordData
import com.mojilab.moji.util.network.get.GetMypageRecordResponse
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import retrofit2.Call
import retrofit2.Response
import com.mojilab.moji.ui.main.MainActivity
import android.app.Activity
import android.os.Parcelable
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.app.ActivityCompat
import androidx.core.app.ActivityCompat.finishAffinity


class MypageFragment : Fragment()  {

    companion object{
        lateinit var mypageFragment : MypageFragment
    }

    private var mContentPagerAdapter: ContentsPagerAdapter? = null
    var recordNum : Int = 0
    var scrabNum : Int = 0
    lateinit var networkService : NetworkService
    lateinit var myPageRecordData: GetMypageRecordData
    var profileImg : String = ""
    lateinit var mContext : Context

    val TAG = "MypageFragment"
    lateinit var v : View


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        v = inflater.inflate(com.mojilab.moji.R.layout.fragment_mypage, container, false)
        var c = resources.getColor(android.R.color.holo_orange_light)
        mContext = context!!
        mypageFragment = this

        v.my_page_loading_progress.setIndeterminate(true)
        v.my_page_loading_progress.getIndeterminateDrawable().setColorFilter(c, PorterDuff.Mode.MULTIPLY)

        // 프로필 수정 화면으로 이동
        v.btn_edit_profile_mypage.setOnClickListener {
            var intent = Intent(mContext, ProfileEditActivity::class.java)
            intent.putExtra("profileImg", profileImg)
            intent.putExtra("nickname", myPageRecordData.nickname)
            startActivityForResult(intent, 28)
        }

        // 알림 화면으로 이동
        v.btn_alarm_mypage.setOnClickListener {
            var intent = Intent(mContext, NoticeActivity::class.java)
            startActivityForResult(intent, 29)
        }

        // 로그아웃
        v.btn_signout_profile_mypage.setOnClickListener {

            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage("로그아웃 할까요?")
            dialog.setPositiveButton(
                "확인"
            ) { dialogInterface, i ->
                SharedPreferenceController.clearUserEmail(mContext)
                SharedPreferenceController.clearUserNickname(mContext)
                SharedPreferenceController.clearUserPassword(mContext)
                SharedPreferenceController.clearUserPicture(mContext)
                SharedPreferenceController.clearAuthorization(mContext)
                var intent = Intent(mContext, LoginActivity::class.java)
                startActivity(intent)
                activity!!.finish()
            }
            dialog.setNegativeButton("아니요", null)
            dialog.show()
        }
        v.my_page_loading_progress.visibility = View.VISIBLE
        //loading progress bar
        getMypageData(v, 0)
        saveState()
        return v;
    }


    fun saveState(): Parcelable? {

        return null

    }
    fun addTab(v :View, flag : Int){
        mContentPagerAdapter = ContentsPagerAdapter( activity!!.getSupportFragmentManager(), v.tl_container_mypage.getTabCount() )
        //error
        v.vp_container_mypage.setAdapter(mContentPagerAdapter)

        if(flag == 0){
            v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("나의 기록"))
            v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("스크랩한 글"))
        }

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
                if(tab.position==1){
                    resizeMyscrab(scrabNum)
                }else{
                    resizeMyrecode(recordNum)
                }
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

    private fun resizeMyrecode(item_size: Int) {
        val params = vp_container_mypage.getLayoutParams()
//        기존
//        params.height = 1150 * item_size
        // 다예꺼 늘리기...
        params.height = 2000 * item_size
        vp_container_mypage.setLayoutParams(params)
    }
    private fun resizeMyscrab(item_size: Int) {
        val params = vp_container_mypage.getLayoutParams()
        // 기존
//        params.height = 600 * (item_size/3+1)
        // 다예꺼 늘리기...
        params.height = 900 * (item_size/3+1)
        vp_container_mypage.setLayoutParams(params)
    }

    // 각 탭에 맞게 탭 레이아웃 크기 조절
    fun controlContentHeight(v: View, tabNum: Int) {
        var tabNo = tabNum
        var heightNum : Float = 0f
        if(tabNo == 0){
            heightNum = (300 * (recordNum+1) + 40).toFloat()
        }
        else{
            heightNum = (200 * (scrabNum+1) + 40).toFloat()
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
            Handler().postDelayed({
                Glide.with(mContext!!).load(SharedPreferenceController.getUserPicture(mContext)).error(com.mojilab.moji.R.drawable.profile_iu).into(iv_profile_mypage)
            }, 500)// 0.5초 정도 딜

        }
        // 알림 화면에서 돌아왔을 때
        else if(requestCode == 29){

        }

    }

    fun getMypageData(v : View, flag : Int){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(mContext!!)
        val getMypageRecordResponse = networkService.getMypageRecordData(token)

        getMypageRecordResponse.enqueue(object : retrofit2.Callback<GetMypageRecordResponse>{

            override fun onResponse(call: Call<GetMypageRecordResponse>, response: Response<GetMypageRecordResponse>) {
                if (response.isSuccessful) {
                    my_page_loading_progress.visibility = View.GONE
                    // 처음 들어왔을 때만 탭 추가
                    if(flag == 0) addTab(v,0)
                    // 탭 추가 X
                    if(flag == 1) addTab(v,1)

                    myPageRecordData = response.body()!!.data
                    Log.v(TAG, "통신 성공 = " + myPageRecordData.toString())
                    if(myPageRecordData.profileUrl != null){
                        v.iv_profile_mypage.visibility = View.VISIBLE
                        v.rl_default_proflle_img_mypage.visibility = View.GONE
                        profileImg = myPageRecordData.profileUrl
                        Glide.with(mContext!!).load(profileImg).error(com.mojilab.moji.R.drawable.profile_iu).into(v.iv_profile_mypage)
                    }
                    else{
                        v.rl_default_proflle_img_mypage.visibility = View.VISIBLE
                        v.iv_profile_mypage.visibility = View.GONE
                        v.tv_profile_name_mypage.text = myPageRecordData.nickname.substring(0, 2)
                    }
                    v.tv_nickname_mypage.text = myPageRecordData.nickname

                    recordNum = myPageRecordData.boardCount;
                    if(recordNum == 0) recordNum = 1
                    resizeMyrecode(recordNum)

                    scrabNum = myPageRecordData.scrapCount;
                    if(scrabNum == 0) scrabNum = 1
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
    fun getMypagePicture(flag : Int){

        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        var token : String = SharedPreferenceController.getAuthorization(mContext!!)
        val getMypageRecordResponse = networkService.getMypageRecordData(token)

        getMypageRecordResponse.enqueue(object : retrofit2.Callback<GetMypageRecordResponse>{

            override fun onResponse(call: Call<GetMypageRecordResponse>, response: Response<GetMypageRecordResponse>) {
                if (response.isSuccessful) {
                   // my_page_loading_progress.visibility = View.GONE

                    if(myPageRecordData.profileUrl != null){
                        iv_profile_mypage.visibility = View.VISIBLE
                        rl_default_proflle_img_mypage.visibility = View.GONE
                        profileImg = myPageRecordData.profileUrl
                        Glide.with(mContext!!).load(profileImg).error(com.mojilab.moji.R.drawable.profile_iu).into(iv_profile_mypage)
                    }
                    else{
                        rl_default_proflle_img_mypage.visibility = View.VISIBLE
                        iv_profile_mypage.visibility = View.GONE
                        tv_profile_name_mypage.text = response.body()!!.data.nickname.substring(0, 2)
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
