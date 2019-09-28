package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage

data class DetailCommentData(
    var profileImgUrl : String?,
    var userName : String,
    val content: String?,
    val userIdx: Int?,
    val writeTime :String?
)