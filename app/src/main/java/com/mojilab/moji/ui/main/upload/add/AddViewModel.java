package com.mojilab.moji.ui.main.upload.add;

import androidx.annotation.VisibleForTesting;
import com.mojilab.moji.base.BaseViewModel;

public class AddViewModel extends BaseViewModel<AddNavigator> {
    @VisibleForTesting
    public void init(){
    }

    public void callAddCourseActivity(){
        getNavigator().callAddCourseActivity();
    }
}
