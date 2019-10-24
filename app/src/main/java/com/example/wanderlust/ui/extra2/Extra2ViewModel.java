package com.example.wanderlust.ui.extra2;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class Extra2ViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public Extra2ViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is extra2 fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}