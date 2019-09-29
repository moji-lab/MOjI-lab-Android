package com.mojilab.moji.util.network.get

import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData

data class GetCommentResponce (
    var status : String,
    var message : String,
    var data : ArrayList<DetailCommentData?>
)