package com.mojilab.moji.ui.main.upload.add;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;
import com.mojilab.moji.base.BaseViewModel;

public class AddViewModel extends BaseViewModel<AddNavigator> {

    public MutableLiveData<Boolean> isSubmit = new MutableLiveData<>();

    @VisibleForTesting
    public void init(){
        isSubmit.setValue(false);
    }

    public void callAddCourseActivity(){
        getNavigator().callAddCourseActivity();
    }
    public void accessCameraGallery(){ getNavigator().accessCameraGallery();}
}
