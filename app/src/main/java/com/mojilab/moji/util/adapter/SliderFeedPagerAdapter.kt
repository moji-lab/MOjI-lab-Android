package com.mojilab.moji.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mojilab.moji.ui.main.home.HomeFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mojilab.moji.ui.main.mypage.friendshare.FriendShareFragment
import com.mojilab.moji.ui.main.mypage.myrecord.MyRecordFragment
import com.mojilab.moji.ui.main.mypage.myscrab.MyScrabFragment
import android.os.Bundle
import com.mojilab.moji.ui.main.feed.DetailFeed.SliderFeedFragment


class SliderFeedPagerAdapter(fm:FragmentManager,val num_fragment: Int, var img : ArrayList<String?>): FragmentStatePagerAdapter(fm!!){
    override fun getCount(): Int {
        return num_fragment
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

    override fun getItem(p0: Int): Fragment {   //TabLayout , ViewPager가 보여주고자 하는 View의 번호
        var fragment : SliderFeedFragment = SliderFeedFragment() // 보여질 Fragment는 모두 같은 클래스를 상속
        var bundle : Bundle = Bundle(1) // 한 개 Bundle에 담길 데이터의 개수 지정
        when(p0){


            0->bundle.putString("background_url",img[0])
            1->bundle.putString("background_url",img[1])
            2->bundle.putString("background_url",img[2])
            3->bundle.putString("background_url",img[3])
            4->bundle.putString("background_url",img[4])
            5->bundle.putString("background_url",img[5])
            6->bundle.putString("background_url",img[6])
            7->bundle.putString("background_url",img[7])

        }

        fragment.arguments=bundle
        return  fragment
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}