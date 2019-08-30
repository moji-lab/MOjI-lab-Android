package com.mojilab.moji.ui.login;

import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import androidx.annotation.VisibleForTesting;
import androidx.databinding.BindingAdapter;
import androidx.databinding.ObservableField;
import com.mojilab.moji.R;
import com.mojilab.moji.base.BaseViewModel;

public class LoginViewModel extends BaseViewModel<LoginNavigator> {

    private LoginNavigator navigator;
    private View.OnFocusChangeListener onFocusEmail;
    private View.OnFocusChangeListener onFocusPassword;
    public final ObservableField<String> email = new ObservableField<>();
    public final ObservableField<String> passwd = new ObservableField<>();
    private boolean isFocus;

    // 변경이 있으면 MutableLiveData
//    public MutableLiveData<String> email = new MutableLiveData<>();
//    public MutableLiveData<String> passwd = new MutableLiveData<>();

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
                if(et.getText().length() > 0 && focused){
                    Log.v("asdf", "asdf");
                }
            }
        };

        onFocusPassword = new View.OnFocusChangeListener(){
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

    // TextWatcher Listener Setting
    public TextWatcher emailWatcher() {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                email.set(charSequence.toString());
            }

            @Override public void afterTextChanged(Editable editable) {
            }
        };
    }

    public TextWatcher passwdWatcher() {
        return new TextWatcher() {
            @Override public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {}

            @Override public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                passwd.set(charSequence.toString());
            }

            @Override public void afterTextChanged(Editable editable) {
            }
        };
    }

    // 초기값 설정
    public LoginViewModel(){
    }

    @BindingAdapter({"imgRes"})
    public static void imgload(EditText editText, int resid){
        editText.setBackgroundResource(resid);
    }

    @BindingAdapter({"imgRes"})
    public static void imgload(Button btn, int resid){
        btn.setBackgroundResource(resid);
    }

    // 화면 전환 -> 회원가입 페이지
    public void callSignupActivity(){
        getNavigator().callSignupActivity();
    }
    // 화면 전환 -> 메인화면 페이지
    public void callMainActivity(){
        getNavigator().callMainActivity();
    }

    public View.OnFocusChangeListener getEmailOnFocusChangeListener(){
        return onFocusEmail;
    }

    public View.OnFocusChangeListener getPasswordOnFocusChangeListener(){
        return onFocusPassword;
    }


    @BindingAdapter("onFocus")
    public static void bindFocusChange(EditText editText, View.OnFocusChangeListener onFocusChangeListener){
        if(editText.getOnFocusChangeListener() == null){
            editText.setOnFocusChangeListener(onFocusChangeListener);
        }
    }

}
