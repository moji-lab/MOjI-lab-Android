package com.mojilab.moji.ui.main.mypage

import android.graphics.Color
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import android.widget.LinearLayout
import android.util.TypedValue
import androidx.core.content.ContextCompat
import com.google.android.material.tabs.TabLayout
import com.mojilab.moji.R
import com.mojilab.moji.util.adapter.ContentsPagerAdapter
import kotlinx.android.synthetic.main.custom_tab.view.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*


class MypageFragment : Fragment()  {

    private var mContentPagerAdapter: ContentsPagerAdapter? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        val v= inflater.inflate(com.mojilab.moji.R.layout.fragment_mypage, container, false)

        var recordNum : Int = 7
        var heightNum : Float = (410 * recordNum).toFloat()

        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightNum, resources.displayMetrics)
                .toInt()
        val paramlinear = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)

        v.rl_vpcontent_mypage.setLayoutParams(paramlinear)
        addTab(v)
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
            }
            override fun onTabUnselected(tab: TabLayout.Tab) {
            }
            override fun onTabReselected(tab: TabLayout.Tab) {
            }
        })
    }

}
