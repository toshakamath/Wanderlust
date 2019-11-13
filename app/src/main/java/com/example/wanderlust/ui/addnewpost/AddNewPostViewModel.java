package com.example.wanderlust.ui.addnewpost;

import android.net.Uri;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;

public class AddNewPostViewModel extends ViewModel {

    private static final String TAG = "AddNewPostViewModel";

    private MutableLiveData<String> blogTitle;
    private MutableLiveData<String> blogText;
    private MutableLiveData<ArrayList<Uri>> blogImages;
    private MutableLiveData<String> locationName;
    private MutableLiveData<String> locationAddress;
    private MutableLiveData<LatLng> locationLatLong;
    private ArrayList<byte[]> imageBlob;
    private MutableLiveData<Boolean> successFlag;
    private MutableLiveData<String> userID;

    public AddNewPostViewModel(){
        blogTitle = new MutableLiveData<>();
        blogText = new MutableLiveData<>();
        blogImages = new MutableLiveData<>();
        locationName = new MutableLiveData<>();
        locationAddress = new MutableLiveData<>();
        locationLatLong = new MutableLiveData<>();
        userID = new MutableLiveData<>();
        successFlag = new MutableLiveData<>();
        successFlag.setValue(false);
    }

    public MutableLiveData<String> getUserID() {
        return userID;
    }

    public MutableLiveData<ArrayList<Uri>> getBlogImages() {
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

//    public void submitForm() {
//        Log.i(TAG, "submitForm : called");
//
//        // Calling sqlite from here
//        boolean success = false;



//            dbInstance.addBlogPicsToTable(blog);
//            success = true;
//        } catch (Exception e){
//            Log.e(TAG, e.getMessage());
//        }
//        if(success) {
//            getSuccessFlag().setValue(true);
//        }
    }


//    public void convertBitmapToByteArray(ArrayList<Bitmap> imageList) {
//        Log.i(TAG, "convertUriToByteArray : called");
//
//        imageBlob = new ArrayList<>();
//        for(Bitmap img : imageList) {
//            ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
//            img.compress(Bitmap.CompressFormat.JPEG, 0, outputStream);
//            imageBlob.add(outputStream.toByteArray());
//        }
//    }

