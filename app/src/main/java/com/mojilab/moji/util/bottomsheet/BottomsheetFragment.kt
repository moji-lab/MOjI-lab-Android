package com.mojilab.moji.util.bottomsheet

import android.app.Dialog
import android.view.View
import android.widget.Toast
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.mojilab.moji.R
import kotlinx.android.synthetic.main.bottom_sheet_add_more.view.*

class BottomsheetFragment : BottomSheetDialogFragment() {
    override fun setupDialog(dialog: Dialog, style: Int) {
        super.setupDialog(dialog!!, style)
        val contentView = View.inflate(context, R.layout.bottom_sheet_add_more, null)
        dialog?.setContentView(contentView)

        contentView.iv_review_add_more.setOnClickListener {
            Toast.makeText(context, "수정 토스트 출력", Toast.LENGTH_LONG).show()
        }

        contentView.iv_delete_add_more.setOnClickListener {
            Toast.makeText(context, "삭제 토스트 출력", Toast.LENGTH_LONG).show()
        }
    }
}