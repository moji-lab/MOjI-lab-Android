package com.mojilab.moji.ui.main.mypage

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Color
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.google.android.material.tabs.TabLayout
import com.mojilab.moji.R
import com.mojilab.moji.ui.main.mypage.profileedit.ProfileEditActivity
import com.mojilab.moji.util.adapter.ContentsPagerAdapter
import kotlinx.android.synthetic.main.custom_tab.view.*
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import kotlinx.android.synthetic.main.fragment_mypage.view.img_profile_mypage
import okhttp3.MediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileNotFoundException
import java.io.InputStream


class MypageFragment : Fragment()  {

    private var mContentPagerAdapter: ContentsPagerAdapter? = null
    var recordNum : Int = 0

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_mypage, container, false)
        controlContentHeight(v, 0)
        addTab(v)

        // 프로필 수정 화면으로 이동
        v.btn_edit_profile_mypage.setOnClickListener {
            var intent = Intent(context, ProfileEditActivity::class.java)
            startActivity(intent)
        }

        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
    }

    fun addTab(v :View){
        mContentPagerAdapter = ContentsPagerAdapter(
            activity!!.getSupportFragmentManager(), v.tl_container_mypage.getTabCount()
        )
        v.vp_container_mypage.setAdapter(mContentPagerAdapter)

        v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("나의 기록"))
        v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("친구와 공유"))
        v.tl_container_mypage.addTab(v.tl_container_mypage.newTab().setText("스크랩한 글"))

        mContentPagerAdapter = ContentsPagerAdapter(
            activity!!.supportFragmentManager, v.tl_container_mypage.getTabCount()
        )

        v.vp_container_mypage.adapter = mContentPagerAdapter
        v.vp_container_mypage.addOnPageChangeListener(

            TabLayout.TabLayoutOnPageChangeListener(v.tl_container_mypage)
        )

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
            recordNum = 5
            heightNum = (445 * recordNum + 40).toFloat()
        }
        else if(tabNo == 1){
            recordNum = 5
            heightNum = (445 * recordNum + 40).toFloat()
        }else{
            recordNum = 3
            heightNum = (130 * recordNum + 40).toFloat()
        }

        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightNum, resources.displayMetrics)
                .toInt()
        val paramlinear = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)
        v.rl_vpcontent_mypage.setLayoutParams(paramlinear)
    }

}
