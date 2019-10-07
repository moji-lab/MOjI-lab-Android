package com.mojilab.moji.util.network.get

data class GetTourItems (
    var items : GetTourItem,
    var numOfRows : Int,
    var pageNo : Int,
    var totalCount : Int
)