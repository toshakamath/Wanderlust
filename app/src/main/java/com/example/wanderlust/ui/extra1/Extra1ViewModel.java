package com.example.wanderlust.ui.extra1;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Extra1ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Extra1ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is extra1 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}