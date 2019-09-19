package com.mojilab.moji.ui.main.home


import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.mojilab.moji.ui.main.home.HomeData.HotCityData
import com.mojilab.moji.ui.main.home.HomeData.OtherCityData
import com.mojilab.moji.util.adapter.HotCityRecyclerViewAdapter
import com.mojilab.moji.util.adapter.OtherCityRecyclerViewAdapter
import kotlinx.android.synthetic.main.fragment_home.*

/**
 * A simple [Fragment] subclass.
 */
class HomeFragment : Fragment()  {
    lateinit var hotCityRecyclerViewAdapter: HotCityRecyclerViewAdapter
    var hotCityDataList: ArrayList<HotCityData> = ArrayList()

    lateinit var Top5CityRecyclerViewAdapter: OtherCityRecyclerViewAdapter
    var Top5DataList: ArrayList<OtherCityData> = ArrayList()

    lateinit var AloneCityRecyclerViewAdapter: OtherCityRecyclerViewAdapter
    var AloneDataList: ArrayList<OtherCityData> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(com.mojilab.moji.R.layout.fragment_home, container, false)

    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        hotCityDataList.add(HotCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","일산"))
        hotCityDataList.add(HotCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","안산"))
        hotCityDataList.add(HotCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","서울"))
        hotCityDataList.add(HotCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","대구"))
        hotCityDataList.add(HotCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","광주"))
        hotCityRecyclerViewAdapter = HotCityRecyclerViewAdapter(context!!, hotCityDataList)
        rv_home_hotcity.adapter = hotCityRecyclerViewAdapter
        rv_home_hotcity.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)

        AloneDataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","오산"))
        AloneDataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","수원"))
        AloneDataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","파주"))
        AloneDataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","여주"))
        AloneDataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","전주"))
        Top5CityRecyclerViewAdapter = OtherCityRecyclerViewAdapter(context!!, AloneDataList)
        rv_home_topPlaying.adapter = Top5CityRecyclerViewAdapter
        rv_home_topPlaying.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)


        Top5DataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","인천"))
        Top5DataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","강원"))
        Top5DataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","부산"))
        Top5DataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","포항"))
        Top5DataList.add(OtherCityData("https://project-youngwoo.s3.ap-northeast-2.amazonaws.com/program_03.jpg","울진"))
        AloneCityRecyclerViewAdapter = OtherCityRecyclerViewAdapter(context!!, Top5DataList)
        rv_home_alonePlaying.adapter = Top5CityRecyclerViewAdapter
        rv_home_alonePlaying.layoutManager = LinearLayoutManager(context!!,LinearLayoutManager.HORIZONTAL,false)
    }


}
