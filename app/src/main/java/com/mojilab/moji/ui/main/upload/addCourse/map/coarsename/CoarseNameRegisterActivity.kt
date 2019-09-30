package com.mojilab.moji.ui.main.upload.addCourse.map.coarsename

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.mojilab.moji.R
import kotlinx.android.synthetic.main.activity_coarse_name_register.*

class CoarseNameRegisterActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_coarse_name_register)

        var receivedLat : Double = intent.getDoubleExtra("lat", 0.0);
        var receivedLng : Double = intent.getDoubleExtra("lng", 0.0);
        var coarseName : String = ""

        // 백버튼
        btn_back_map_name_register.setOnClickListener {
            finish()
        }

        // 위치 보러가기 버튼
        btn_back_map_name_register.setOnClickListener {
            finish()
        }

        // 완료 버튼
        btn_confirm_name_register.setOnClickListener{
            coarseName = edit_coarse_name_name_register.text.toString()
        }
    }
}
