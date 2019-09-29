package com.mojilab.moji.ui.main.upload.add

import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.mojilab.moji.R
import kotlinx.android.synthetic.main.activity_test.*
import java.io.FileNotFoundException
import java.io.IOException
import java.io.InputStream

class TestActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test)

        var input : InputStream? = contentResolver.openInputStream(Uri.parse("/storage/emulated/0/DCIM/Camera/20190919_075610_HDR.jpg"))

        Glide.with(this).load("/storage/emulated/0/DCIM/Camera/20190919_075610_HDR.jpg").into(test_iv)



    }
}
