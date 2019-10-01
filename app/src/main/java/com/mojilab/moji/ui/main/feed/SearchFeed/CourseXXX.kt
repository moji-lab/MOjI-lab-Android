package com.mojilab.moji.ui.main.feed.SearchFeed

data class CourseXXX(
    val _id: String,
    val boardIdx: String,
    val comments: List<Any>,
    val content: String,
    val lat: String,
    val lng: String,
    val mainAddress: String,
    val order: Int,
    val photos: List<PhotoX>,
    val subAddress: String,
    val tagInfo: List<String>,
    val userIdx: Int,
    val visitTime: String
)