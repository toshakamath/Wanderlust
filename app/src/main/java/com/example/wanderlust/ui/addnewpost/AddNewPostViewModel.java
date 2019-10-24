package com.example.wanderlust.ui.addnewpost;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class AddNewPostViewModel extends ViewModel {

    private MutableLiveData<String> mText;

    public AddNewPostViewModel() {
        mText = new MutableLiveData<>();
        mText.setValue("This is add new post fragment");
    }

    public LiveData<String> getText() {
        return mText;
    }
}