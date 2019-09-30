package com.mojilab.moji.ui.main.map;

import android.hardware.input.InputManager;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import androidx.databinding.DataBindingUtil;
import com.mojilab.moji.R;
import com.mojilab.moji.databinding.ActivityMapSearchBinding;

public class MapSearchActivity extends AppCompatActivity {

    ActivityMapSearchBinding binding;
    InputMethodManager imm;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map_search);

        binding =  DataBindingUtil.setContentView(this, R.layout.activity_map_search);
        binding.setActivity(this);

        imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
        imm.toggleSoftInput(InputMethodManager.SHOW_FORCED, InputMethodManager.HIDE_IMPLICIT_ONLY);
        binding.etMapSearchActSearchLocation.requestFocus();

        setClickListener();
    }

    public void setClickListener(){
        binding.etMapSearchActSearchLocation.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {

                    binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                    binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
                    setData();

                    return true;
                }
                return false;
            }
        });

        binding.ivMapSearchActSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                binding.llMapSearchActHelpComment.setVisibility(View.GONE);
                binding.llMapSearchActRvContainer.setVisibility(View.VISIBLE);
                setData();

            }
        });

        binding.ivMapSearchActBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                finish();
            }
        });
    }

    public void setData(){

    }
}
