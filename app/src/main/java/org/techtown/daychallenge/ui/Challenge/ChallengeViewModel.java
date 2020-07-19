package org.techtown.daychallenge.ui.Challenge;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ChallengeViewModel extends ViewModel {
    private MutableLiveData<String> mText;

    public ChallengeViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is challenge fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}
