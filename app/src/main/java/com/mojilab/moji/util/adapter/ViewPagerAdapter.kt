package com.mojilab.moji.util.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentStatePagerAdapter
import com.mojilab.moji.ui.main.mypage.friendshare.FriendShareFragment
import com.mojilab.moji.ui.main.mypage.myrecord.MyRecordFragment
import com.mojilab.moji.ui.main.mypage.myscrab.MyScrabFragment

class ViewPagerAdapter(fm : FragmentManager, private val num_fragment : Int) : FragmentStatePagerAdapter(fm) {

    companion object{
        private var myRecordFragment : MyRecordFragment? = null
        private var friendShareFragment: FriendShareFragment? = null
        private var myScrabFragment : MyScrabFragment? = null

        @Synchronized
        fun getMyRecordFragment() : MyRecordFragment{
            if(myRecordFragment == null){
                myRecordFragment = MyRecordFragment()
            }
            return myRecordFragment!!
        }
        @Synchronized
        fun getFriendShareFragment() : FriendShareFragment{
            if(friendShareFragment == null){
                friendShareFragment = FriendShareFragment()
            }
            return friendShareFragment!!
        }
        @Synchronized
        fun getMyScrabFragment() : MyScrabFragment{
            if(myScrabFragment == null){
                myScrabFragment = MyScrabFragment()
            }
            return myScrabFragment!!
        }

    }

    override fun getItem(position: Int): Fragment {
        return when(position){
            0 -> getMyRecordFragment()
            1 -> getFriendShareFragment()
            2 -> getMyScrabFragment()
            else -> null
        }!!
    }

    override fun getCount(): Int {
        return num_fragment
    }
}