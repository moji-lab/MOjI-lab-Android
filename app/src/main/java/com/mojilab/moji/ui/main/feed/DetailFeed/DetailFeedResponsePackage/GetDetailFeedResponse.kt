package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage

data class GetDetailFeedResponse(
    val data: DetailFeedData?,
    val message: String?,
    val status: Int?
)