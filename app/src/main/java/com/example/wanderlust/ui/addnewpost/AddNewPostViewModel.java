package com.example.wanderlust.ui.addnewpost;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.Doa.BlogObject;
import com.google.android.gms.maps.model.LatLng;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;

public class AddNewPostViewModel extends ViewModel {

    private static final String TAG = "AddNewPostViewModel";

    private MutableLiveData<String> blogTitle;
    private MutableLiveData<String> blogText;
    private MutableLiveData<ArrayList<Bitmap>> blogImages;
    private MutableLiveData<String> locationName;
    private MutableLiveData<String> locationAddress;
    private MutableLiveData<LatLng> locationLatLong;
    private ArrayList<byte[]> imageBlob;
    private MutableLiveData<Boolean> successFlag;

    DbInstance dbInstance;

    public AddNewPostViewModel(){
        blogTitle = new MutableLiveData<>();
        blogText = new MutableLiveData<>();
        blogImages = new MutableLiveData<>();
        locationName = new MutableLiveData<>();
        locationAddress = new MutableLiveData<>();
        locationLatLong = new MutableLiveData<>();
        successFlag = new MutableLiveData<>();
        successFlag.setValue(false);
    }

    public void instantiateDBHelper(Context context) {
        dbInstance = new DbInstance(context);
    }

    public MutableLiveData<ArrayList<Bitmap>> getBlogImages() {
        return blogImages;
    }

    public MutableLiveData<String> getBlogTitle() {
        return blogTitle;
    }

    public MutableLiveData<String> getBlogText() {
        return blogText;
    }

    public MutableLiveData<Boolean> getSuccessFlag() {
        return successFlag;
    }

    public MutableLiveData<String> getLocationName() {
        return locationName;
    }

    public MutableLiveData<String> getLocationAddress() {
        return locationAddress;
    }

    public MutableLiveData<LatLng> getLocationLatLong() {
        return locationLatLong;
    }

    public void clearData() {
        blogTitle.setValue("");
        blogText.setValue("");
        blogImages.setValue(null);
        locationAddress.setValue("");
        locationAddress.setValue("");
        locationLatLong.setValue(null);
    }

    public void submitForm() {
        Log.i(TAG, "submitForm : called");

        // Calling sqlite from here
        boolean success = false;
        convertBitmapToByteArray(blogImages.getValue());
        BlogObject blog = new BlogObject(null, "1", blogTitle.getValue(), locationName.getValue(), blogText.getValue(), imageBlob, locationLatLong.getValue().latitude, locationLatLong.getValue().longitude, 0, "");
        try {
            long ID = dbInstance.addBlogToTable(blog);
            Log.i(TAG, "ID: " + ID);
            blog.setBlogId(Long.toString(ID));
            dbInstance.addBlogPicsToTable(blog);
            success = true;
        } catch (Exception e){
            Log.e(TAG, e.getMessage());
        }
        if(success) {
            getSuccessFlag().setValue(true);
        }
    }

    public void convertBitmapToByteArray(ArrayList<Bitmap> imageList) {
        Log.i(TAG, "convertUriToByteArray : called");

        imageBlob = new ArrayList<>();
        for(Bitmap img : imageList) {
            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
            img.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
            imageBlob.add(outputStream.toByteArray());
        }
    }

}