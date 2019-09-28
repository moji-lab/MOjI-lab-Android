package com.mojilab.moji.util.network.get

import com.mojilab.moji.ui.main.mypage.data.FeedData

data class GetRandromFeedResponse (
    var status : Int,
    var message : String,
    var data : ArrayList<FeedData>?
)