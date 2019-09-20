package com.mojilab.moji.ui.main.mypage

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.mojilab.moji.R
import com.mojilab.moji.util.adapter.ViewPagerAdapter
import kotlinx.android.synthetic.main.fragment_mypage.*
import kotlinx.android.synthetic.main.fragment_mypage.view.*
import android.widget.LinearLayout
import android.util.TypedValue



class MypageFragment : Fragment()  {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val v= inflater.inflate(R.layout.fragment_mypage, container, false)

        var recordNum : Int = 7
        var heightNum : Float = (410 * recordNum).toFloat()

        val height =
            TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, heightNum, resources.displayMetrics)
                .toInt()
        val paramlinear = LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height)

        v.rl_vpcontent_mypage.setLayoutParams(paramlinear)
        configureMainTab(v)
        return v;
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

    }

    fun configureMainTab(v : View){
        v.vp_container_mypage.adapter = ViewPagerAdapter(activity!!.supportFragmentManager, 3);
        v.vp_container_mypage.offscreenPageLimit = 2
        v.tl_container_mypage.setupWithViewPager(v.vp_container_mypage)

        val navCategoryMainLayout : View = (activity!!.getSystemService(android.content.Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater)
            .inflate(R.layout.navigation_mypage_tab, null, false)

        v.tl_container_mypage.getTabAt(0)!!.customView = navCategoryMainLayout.findViewById(R.id.tv_myrecord_nav) as TextView
        v.tl_container_mypage.getTabAt(1)!!.customView = navCategoryMainLayout.findViewById(R.id.tv_friendshare_nav) as TextView
        v.tl_container_mypage.getTabAt(2)!!.customView = navCategoryMainLayout.findViewById(R.id.tv_myscrab_nav) as TextView
    }

}
