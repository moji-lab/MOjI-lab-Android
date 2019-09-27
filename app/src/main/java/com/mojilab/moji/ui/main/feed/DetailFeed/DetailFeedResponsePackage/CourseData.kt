package com.mojilab.moji.ui.main.feed.DetailFeed.DetailFeedResponsePackage

data class CourseData(
    val course: CourseInnerData?,
    val likeCount: Int?,
    val liked: Boolean?,
    val scrapCount: Int?,
    val scraped: Boolean?
)