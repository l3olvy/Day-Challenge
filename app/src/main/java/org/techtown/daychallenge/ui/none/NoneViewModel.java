package org.techtown.daychallenge.ui.none;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class NoneViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public NoneViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is none fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}