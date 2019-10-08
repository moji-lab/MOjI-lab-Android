package com.mojilab.moji.util.bottomsheet

import android.app.Dialog
import android.content.ContentValues.TAG
import android.content.Intent
import android.util.Log
import android.view.View
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojilab.moji.ui.main.feed.DetailFeed.updatelock.UpdateLockActivity
import com.mojilab.moji.util.network.NetworkService
import kotlinx.android.synthetic.main.bottom_edit_sheet_add_more.view.*


class EditBottomsheetFragment : BottomSheetDialogFragment() {

    lateinit var networkService : NetworkService
    var position : Int = 0
    var courseID : String = ""

    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog!!, style)
        val contentView = View.inflate(context, com.mojilab.moji.R.layout.bottom_edit_sheet_add_more, null)
        dialog?.setContentView(contentView)
        val mArgs = arguments
        courseID = mArgs!!.getString("courseID")
        Log.v(TAG, "받아온 코스 id = " + courseID)

        // 코스 수정 버튼 클릭 시
        contentView.rl_edit_add_more.setOnClickListener {
            var intent = Intent(context, UpdateLockActivity::class.java)
            intent.putExtra("courseID", courseID)
            startActivity(intent)
        }
    }
}