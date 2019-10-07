package com.mojilab.moji.util.network.get

data class GetTourDetail (
    var addr1 : String,
    var addr2 : String,
    var areacode : Int,
    var cat1 : String,
    var cat2 : String,
    var cat3 : String,
    var contentid : Int,
    var contenttypeid : Int,
    var createdtime : Long,
    var dist : Int,
    var firstimage : String?,
    var firstimage2 : String?,
    var mapx : Double,
    var mapy : Double,
    val mlevel : Int,
    var modifiedtime : Long,
    var readcount : Int,
    var sigungucode : Int,
    var tel : String,
    var title : String
)