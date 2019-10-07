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

    private val userPicture = "userPicture"
    private val UserPicture = "UserPicture"

    private val pictureUrl = "pictureUrl"
    private val PictureUrl = "PictureUrl"

    //자동로그인 위한 이메일 패스워드
    private val email = "email"
    private val Email = "Email"
    private val Password = "Password"
    private val password = "password"


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
    fun clearAuthorization(context: Context){
        val pref = context.getSharedPreferences(USER_NAME, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }


    //자동로그인
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

    // 유저의 닉네임 저장
    fun setPictureUrl(context: Context, coarseUrl : String){
        val pref = context.getSharedPreferences(pictureUrl, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(PictureUrl, coarseUrl)
        editor.commit()
    }

    fun getPictureUrl(context: Context) : String {
        val pref = context.getSharedPreferences(pictureUrl, Context.MODE_PRIVATE)
        return pref.getString(PictureUrl, "")
    }

    fun clearUserNickname(context: Context){
        val pref = context.getSharedPreferences(userNickname, Context.MODE_PRIVATE)
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

    // set User Email
    fun setUserEmail(context: Context, authorization: String) {
        val pref = context.getSharedPreferences(email, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(Email, authorization)
        editor.commit()
    }

    fun getUserEmail(context: Context): String {
        val pref = context.getSharedPreferences(email, Context.MODE_PRIVATE)
        return pref.getString(Email, "")
    }

    fun clearUserEmail(context: Context) {
        val pref = context.getSharedPreferences(email, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }
    // set User Password
    fun setUserPassword(context: Context, authorization: String) {
        val pref = context.getSharedPreferences(password, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.putString(Password, authorization)
        editor.commit()
    }

    fun getUserPassword(context: Context): String {
        val pref = context.getSharedPreferences(password, Context.MODE_PRIVATE)
        return pref.getString(Password, "")
    }

    fun clearUserPassword(context: Context) {
        val pref = context.getSharedPreferences(password, Context.MODE_PRIVATE)
        val editor = pref.edit()
        editor.clear()
        editor.commit()
    }

}