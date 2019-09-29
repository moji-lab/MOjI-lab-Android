package com.mojilab.moji.ui.main.feed.SearchFeed

import com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage.DetailCommentData

data class CourseX(
    val _id: String?,
    val boardIdx: String?,
    val comments: ArrayList<DetailCommentData?>,
    val content: String?,
    val lat: String?,
    val lng: String?,
    val mainAddress: String?,
    val order: Int?,
    val photos: ArrayList<Photo?>?,
    val subAddress: String?,
    val tagInfo: ArrayList<String?>?,
    val userIdx: Int?,
    val visitTime: String?
)