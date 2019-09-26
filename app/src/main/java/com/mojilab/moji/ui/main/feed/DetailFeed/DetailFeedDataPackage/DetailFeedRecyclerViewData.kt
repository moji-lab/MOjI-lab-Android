package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedDataPackage

data class DetailFeedRecyclerViewData (
    var city : String?,
    var date: String?,
    var RecyclerViewItem_Img: ArrayList<FeedViewPagerData?>,
    var hashTag : String?,
    var heartNum : String?,
    var commentNum : String
)