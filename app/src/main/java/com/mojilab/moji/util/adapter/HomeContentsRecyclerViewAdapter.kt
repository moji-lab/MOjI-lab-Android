package com.mojilab.moji.util.adapter

import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.mojilab.moji.R
import com.mojilab.moji.databinding.ActivityMainBinding
import com.mojilab.moji.ui.main.MainActivity
import com.mojilab.moji.ui.main.MainViewModel
import com.mojilab.moji.ui.main.home.HomeData.HomeRecyclerViewContentsData
import com.mojilab.moji.ui.main.map.MapFragment
import com.mojilab.moji.ui.main.mypage.MypageFragment.Companion.mypageFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.mojilab.moji.ui.main.home.HomeFragment
import kotlinx.android.synthetic.main.fragment_home.*


class HomeContentsRecyclerViewAdapter(var ctx: Context, var dataList: ArrayList<HomeRecyclerViewContentsData>) :
    RecyclerView.Adapter<HomeContentsRecyclerViewAdapter.Holder>() {
    override fun onCreateViewHolder(viewgroup: ViewGroup, position: Int): Holder {
        val view: View = LayoutInflater.from(ctx).inflate(com.mojilab.moji.R.layout.rv_item_home_homecontents, viewgroup, false)

        return Holder(view)
    }

    override fun getItemCount(): Int {
        return dataList.size
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {

        holder.cv_item_home_hotcity_allview.setOnClickListener {
         /*   val transaction =  (ctx as MainActivity).getSupportFragmentManager().beginTransaction()
            val mapview = MapFragment()
            val bundle = Bundle()
            bundle.putString("keword", "#"+dataList[position].city.toString())
            mapview.setArguments(bundle)
            transaction.replace(com.mojilab.moji.R.id.main_fragment_container, mapview)
            transaction.commit()*/

            (ctx as MainActivity).callMapFragmentWithBundle("#"+dataList[position].city.toString())
            HomeFragment.keyword =dataList[position].city.toString()
                            //지도로 프래그먼트 전환
        }

        holder.iv_rv_home_city_name.text=dataList[position].city.toString()

        Glide.with(ctx)
            .load(dataList[position].thumbnail)
            .into(holder.iv_rv_home_city_img)
    }

    inner class Holder(itemView: View) : RecyclerView.ViewHolder(itemView) {



        var cv_item_home_hotcity_allview = itemView.findViewById(com.mojilab.moji.R.id.cv_item_home_hotcity_allview) as CardView
        var iv_rv_home_city_name=itemView.findViewById(com.mojilab.moji.R.id.iv_rv_home_city_name) as TextView
        var iv_rv_home_city_img=itemView.findViewById(com.mojilab.moji.R.id.iv_rv_home_city_img) as ImageView
    }

}