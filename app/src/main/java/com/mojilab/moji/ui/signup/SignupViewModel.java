package com.mojilab.moji.ui.signup;

import android.view.View;
import android.widget.EditText;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseViewModel;

public class SignupViewModel extends BaseViewModel<SignupNavigator> {

    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPass;
    private View.OnFocusChangeListener onFocusPassCheck;
    private View.OnFocusChangeListener onFocusNickname;
    private boolean isFocus;

    @VisibleForTesting
    public void init(){
        onFocusEmail = new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if(focused){
                    isFocus = true;
                    imgload(et, R.drawable.edit_circle_focus_on_background);
                }else{
                    isFocus = false;
                    imgload(et, R.drawable.edit_circle_focus_off_background);
                }

                // 나중에 쓸수도
                if(et.getText().length() > 0 && !focused){
                }
            }
        };

        onFocusPass = new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if(focused){
                    isFocus = true;
                    imgload(et, R.drawable.edit_circle_focus_on_background);
                }else{
                    isFocus = false;
                    imgload(et, R.drawable.edit_circle_focus_off_background);
                }

            }
        };

        onFocusPassCheck = new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if(focused){
                    isFocus = true;
                    imgload(et, R.drawable.edit_circle_focus_on_background);
                }else{
                    isFocus = false;
                    imgload(et, R.drawable.edit_circle_focus_off_background);
                }

            }
        };

        onFocusNickname = new View.OnFocusChangeListener(){

            @Override
            public void onFocusChange(View view, boolean focused) {
                EditText et = (EditText) view;
                if(focused){
                    isFocus = true;
                    imgload(et, R.drawable.edit_circle_focus_on_background);
                }else{
                    isFocus = false;
                    imgload(et, R.drawable.edit_circle_focus_off_background);
                }

            }
        };
    }

    @BindingAdapter({"imgRes"})
    public static void imgload(EditText editText, int resid){
        editText.setBackgroundResource(resid);
    }

    // 화면 전환
    public void callActivity(){
        getNavigator().callActivity();
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener(){
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPassOnFocusChangeListener(){
        return onFocusPass;
    }

    public View.OnFocusChangeListener getPassCheckOnFocusChangeListener(){
        return onFocusPassCheck;
    }
    public View.OnFocusChangeListener getNicknameOnFocusChangeListener(){
        return onFocusNickname;
    }

    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener){
        if(editText.getOnFocusChangeListener() == null){
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }
}