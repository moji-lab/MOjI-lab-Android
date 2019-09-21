package com.mojilab.moji.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import com.mojilab.moji.ui.main.home.HomeFragment
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mojilab.moji.ui.main.mypage.friendshare.FriendShareFragment
import com.mojilab.moji.ui.main.mypage.myrecord.MyRecordFragment
import com.mojilab.moji.ui.main.mypage.myscrab.MyScrabFragment

class ContentsPagerAdapter(fm: FragmentManager, private val mPageCount: Int) :
    FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {

        when (position) {
            0 -> {
                return MyRecordFragment()
            }

            1 -> {
                return FriendShareFragment()
            }

            2 -> {
                return MyScrabFragment()
            }

            else ->
                return null!!
        }
    }

    override fun getCount(): Int {
        return mPageCount
    }
}