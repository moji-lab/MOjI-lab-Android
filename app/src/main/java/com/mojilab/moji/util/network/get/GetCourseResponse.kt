package com.mojilab.moji.util.network.get

data class GetCourseResponse (
    var status : Int,
    var message : String,
    var data : GetCourseData
)