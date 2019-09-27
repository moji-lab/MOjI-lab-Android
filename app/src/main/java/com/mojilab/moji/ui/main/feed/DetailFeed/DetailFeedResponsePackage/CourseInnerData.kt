package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage

data class CourseInnerData(
    val _id: String?,
    val boardIdx: String?,
    val comments: ArrayList<DetailCommentData?>,
    val content: String?,
    val lat: String?,
    val lng: String?,
    val mainAddress: String?,
    val order: Int?,
    val photos: ArrayList<PhotoData?>,
    val subAddress: String?,
    val tagInfo: ArrayList<String?>,
    val userIdx: Int?,
    val visitTime: String?
)