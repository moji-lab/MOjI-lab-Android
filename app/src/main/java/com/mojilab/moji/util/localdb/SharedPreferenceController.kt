package com.mojilab.moji.util.localdb

import android.content.Context

object SharedPreferenceController{
    private val USER_NAME = "MYKEY"
    private val myAuth = "jwt"

    private  val USER = "MYAUTOKEY"
    private val myAutoAuth = "myAuth"

    private  val userId ="userId"
    private val  UserId = "UserId"

    private val userNickname = "userName"
    private val UserNickname = "UserNickname"

    private val imagePath = "imagePath"
    private val ImagePath = "ImagePath"

    private val userPicture = "userPicture"
    private val UserPicture = "UserPicture"


    // 유저의 토큰으로 모든 보드 접근
    fun setAuthorization(context: Context, authorization : String){
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(myAuth, authorization)
        editor.commit()
    }

    fun getAuthorization(context: Context) : String {
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        return pref.getString(myAuth, "")
    }



    fun setAutoAuthorization(context: Context, authorization : String){
        val pref = context.getSharedPreferences(USER, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(myAutoAuth, authorization)
        editor.commit()
    }
    fun getAutoAuthorization(context: Context) : String {
        val pref = context.getSharedPreferences(USER, Context.MODE_PRIVATE)
        return pref.getString(myAutoAuth, "")
    }
    //userId 저장
    fun setUserId(context: Context, User_Id : Int){
        val pref = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putInt(UserId, User_Id)
        editor.commit()
    }
    //userId 가져오기
    fun getUserId(context: Context) : Int {
        val pref = context.getSharedPreferences(userId, Context.MODE_PRIVATE)
        return pref.getInt(UserId, -1)
    }

    // 유저의 닉네임 저장
    fun setUserNickname(context: Context, nickname : String){
        val pref = context.getSharedPreferences(userNickname, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(UserNickname, nickname)
        editor.commit()
    }

    fun getUserNickname(context: Context) : String {
        val pref = context.getSharedPreferences(userNickname, Context.MODE_PRIVATE)
        return pref.getString(UserNickname, "")
    }

    // 코스 사진 경로 저장
    fun setCourseImagePath(context: Context, coarseImagePath : String){
        val pref = context.getSharedPreferences(imagePath, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(ImagePath, coarseImagePath)
        editor.commit()
    }

    fun getCourseImagePath(context: Context) : String {
        val pref = context.getSharedPreferences(imagePath, Context.MODE_PRIVATE)
        return pref.getString(ImagePath, "")
    }


    fun clearUserNickname(context: Context){
        val pref = context.getSharedPreferences(userNickname, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }

    //로그아웃시 모드 접근하는 토큰 초기화
    fun clearSPC(context: Context){
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
    //로그아웃시 자동로그인을 위한 토큰 초기화
    fun AutoclearSPC(context: Context){
        val pref = context.getSharedPreferences(USER, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }

    // set User piacture
    fun setUserPicture(context: Context, authorization: String) {
        val pref = context.getSharedPreferences(userPicture, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(UserPicture, authorization)
        editor.commit()
    }

    fun getUserPicture(context: Context): String {
        val pref = context.getSharedPreferences(userPicture, Context.MODE_PRIVATE)
        return pref.getString(UserPicture, "")
    }

    fun clearUserPicture(context: Context) {
        val pref = context.getSharedPreferences(userPicture, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }



}