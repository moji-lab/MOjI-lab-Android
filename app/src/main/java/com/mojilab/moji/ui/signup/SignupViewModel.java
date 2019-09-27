package com.mojilab.moji.ui.signup;

import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.Bindable;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import androidx.databinding.ObservableInt;

import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseViewModel;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class SignupViewModel extends BaseViewModel<SignupNavigator> {

    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> passwd = new ObservableField<>();
    public final ObservableField<String> passwdCheck = new ObservableField<>();
    public final ObservableField<String> nickname = new ObservableField<>();

    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPass;
    private View.OnFocusChangeListener onFocusPassCheck;
    private View.OnFocusChangeListener onFocusNickname;
    ObservableInt listVisibility = new ObservableInt(GONE);
    private boolean isFocus;

    boolean nicknameCheck;

    public TextWatcher emailWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    email.set(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

    public TextWatcher passwdWatcher() {
        return new TextWatcher() {

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    passwd.set(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public TextWatcher passwdCheckWatcher() {
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                    passwdCheck.set(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        };
    }

    public TextWatcher nicknameWatcher(){
        return new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                nickname.set(charSequence.toString());
            }

            @Override
            public void afterTextChanged(Editable editable) {
            }
        };
    }

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

    public void emailCheck(){getNavigator().emailCheck();}

    public void nicknameCheck(){ getNavigator().nicknameCheck(); }

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