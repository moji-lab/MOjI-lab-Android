package com.mojilab.moji.util.bottomsheet

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.DialogInterface
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
import androidx.appcompat.app.AlertDialog
import com.mojilab.moji.ui.main.feed.FeedFragment
import com.mojilab.moji.ui.main.mypage.myrecord.MyRecordFragment
import org.jetbrains.anko.backgroundResource


class BottomsheetFragment : BottomSheetDialogFragment() {

    lateinit var networkService : NetworkService
    var boardId : String = ""
    var openCheck : String = ""
    var position : Int = 0

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog!!, style)
        val contentView = View.inflate(context, com.mojilab.moji.R.layout.bottom_sheet_add_more, null)
        dialog?.setContentView(contentView)
        val mArgs = arguments
        boardId = mArgs!!.getString("boardID")
        openCheck = mArgs!!.getString("openCheck")
        position = mArgs!!.getInt("position")
        Log.v(TAG, "체크 = " + openCheck)

        if(openCheck.equals("공개")){
            contentView.tv_open_check_add_more.text = "비공개로 전환"
            contentView.iv_review_add_more.setBackgroundResource(R.drawable.btn_lock)
        }
        else{
            contentView.tv_open_check_add_more.text = "공개로 전환"
            contentView.iv_review_add_more.setBackgroundResource(R.drawable.btn_unlock)
        }

        // 공개/비공개 변환 버튼 클릭 시
        contentView.rl_change_mode_add_more.setOnClickListener {
            putFeedOpenChange()
        }

        // 삭제 버튼 클릭 시
        contentView.rl_delete_add_more.setOnClickListener {
            val dialog = AlertDialog.Builder(context!!)
            dialog.setMessage("정말로 삭제할까요?")
            dialog.setPositiveButton(
                "확인"
            ) { dialogInterface, i ->
                deleteBoard();
                dismiss()
            }
            dialog.setNegativeButton("아니요", null)
            dialog.show()
        }
    }

    // 피드 공개 / 비공개 변경
    fun putFeedOpenChange() {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)
        Log.v("asdf", "보드 id = " + boardId)

        val putOpenChangeResponse = networkService.putOpenChange(token, boardId)
        putOpenChangeResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
                    Log.v(TAG,  "공개 비공개 응답 메시지 = " + response.body()!!.message)
                    dismiss()
                } else {
                    Log.v(TAG, "상태코드 = " + response.body()!!.status)
                    Log.v(TAG, "실패 메시지 = " + response.message())
                }
            }
            override fun onFailure(call: Call<PostResponse>, t: Throwable) {
                Log.v(TAG, "서버 연결 실패 = " + t.toString())
            }
        })
    }

    // 피드 삭제
    fun deleteBoard() {
        var token : String = SharedPreferenceController.getAuthorization(context!!);
        networkService = ApiClient.getRetrofit().create(NetworkService::class.java)

        val deleteBoardResponse = networkService.deleteBoard(token, boardId)
        deleteBoardResponse.enqueue(object : Callback<PostResponse> {
            override fun onResponse(call: Call<PostResponse>, response: Response<PostResponse>) {
                if (response.body()!!.status == 204) {
                    Log.v(TAG,  "보드 삭제 메시지 = " + response.body()!!.message)
                    MyRecordFragment.myRecordFragment.recordAdapter.notifyItemRemoved(position)
                    MyRecordFragment.myRecordFragment.recordAdapter.notifyItemRangeRemoved(position, 1)
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