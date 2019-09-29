package com.mojilab.moji.util.bottomsheet

import android.app.Dialog
import android.content.ContentValues.TAG
import android.util.Log
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojilab.moji.R
import com.mojilab.moji.util.localdb.SharedPreferenceController
import com.mojilab.moji.util.network.ApiClient
import com.mojilab.moji.util.network.NetworkService
import com.mojilab.moji.util.network.post.PostResponse
import com.mojilab.moji.util.network.post.data.PostScrapData
import kotlinx.android.synthetic.main.bottom_sheet_add_more.view.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import android.os.Bundle


class BottomsheetFragment : BottomSheetDialogFragment() {

    lateinit var networkService : NetworkService
    var boardId : String =""

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog!!, style)
        val contentView = View.inflate(context, com.mojilab.moji.R.layout.bottom_sheet_add_more, null)
        dialog?.setContentView(contentView)
        val mArgs = arguments
        boardId = mArgs!!.getString("boardID")
        Log.v("df", "값 = " + boardId)
        contentView.iv_review_add_more.setOnClickListener {
            Toast.makeText(context, "수정 토스트 출력", Toast.LENGTH_LONG).show()
        }

        contentView.iv_delete_add_more.setOnClickListener {
            Toast.makeText(context, "삭제 토스트 출력", Toast.LENGTH_LONG).show()
            // deleteBoard();
        }
    }

    // 보드 삭제
    fun deleteBoard() {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        val deleteBoardResponse = networkService.deleteBoard(token, boardId)
        deleteBoardResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
                    Log.v(TAG,  "보드 삭제 메시지 = " + response.body()!!.message)
                } else {
                    Log.v(TAG, "보드 삭제 상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "보드 삭제 실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "보드 삭제 서버 연결 실패 = " + t.toString())
            }
        })
    }

}