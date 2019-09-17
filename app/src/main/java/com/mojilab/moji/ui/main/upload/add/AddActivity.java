package com.mojilab.moji.ui.main.upload.add;

import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import com.bumptech.glide.Glide;
import com.mojilab.moji.R;

public class AddActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //ImageView iv = findViewById(R.id.iv_add_act_upload_img);
        //Glide.with(getApplication()).load("https://upload.wikimedia.org/wikipedia/commons/c/cb/IU_at_Golden_Disk_Awards_on_January_10%2C_2018_%281%29.jpg").into(iv);
    }
}
