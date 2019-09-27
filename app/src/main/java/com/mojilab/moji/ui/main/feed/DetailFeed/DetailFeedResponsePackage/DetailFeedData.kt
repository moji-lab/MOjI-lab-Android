package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage

data class DetailFeedData(
    val _id: String?,
    val courseList: ArrayList<CourseData?>,
    val likeCount: Int?,
    val liked: Boolean?,
    val scraped: Boolean?,
    val user: UserData?,
    val writeTime: String?
)