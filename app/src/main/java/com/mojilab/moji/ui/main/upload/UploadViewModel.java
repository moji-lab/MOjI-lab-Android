package com.mojilab.moji.ui.main.upload;

import androidx.annotation.VisibleForTesting;
import androidx.lifecycle.MutableLiveData;
import com.mojilab.moji.base.BaseViewModel;

public class UploadViewModel extends BaseViewModel<UploadNavigator> {
    public MutableLiveData<Boolean> isClosed = new MutableLiveData<>();

    @VisibleForTesting
    public void init(){
        isClosed.setValue(false);
    }

    public void changeStatus(){
        isClosed.setValue(!isClosed.getValue());
    }

    public void callAddCourseActivity(){
        getNavigator().callAddCourseActivity();
    }

    public void callAddActivity(){
        getNavigator().callAddActivity();
    }

    public void callChangeOrderActivity(){
        getNavigator().callChangeOrderActivity();
    }

    public void callTagActivity(){
        getNavigator().callTagActivity();
    }
}
