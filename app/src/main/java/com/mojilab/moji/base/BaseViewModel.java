package com.mojilab.moji.base;

import androidx.lifecycle.ViewModel;
import io.reactivex.disposables.CompositeDisposable;

import java.lang.ref.WeakReference;

public class BaseViewModel<N> extends ViewModel {


    private CompositeDisposable compositeDisposable;
    private WeakReference<N> navigator;

    @Override
    protected void onCleared() {
//        compositeDisposable.dispose();
//        super.onCleared();
    }

    public void setNavigator(N navigator) {
        this.navigator = new WeakReference<>(navigator);
    }

    public N getNavigator() {
        return navigator.get();
    }
}
