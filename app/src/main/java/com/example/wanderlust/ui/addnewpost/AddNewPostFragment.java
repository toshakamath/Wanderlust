package com.example.wanderlust.ui.addnewpost;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.R;

import java.io.FileNotFoundException;
import java.util.ArrayList;

public class AddNewPostFragment extends Fragment {

    private AddNewPostViewModel addNewPostViewModel;

    private static final int PICK_IMAGE_MULTIPLE = 1;

    private static final String TAG = "BlogUploadFragment";

    private EditText blogTitle, blogLocation, blogText;
    private Button uploadImgButton, clear, submit;
    private boolean successFlag = false;

    ArrayList<Bitmap> imageList = new ArrayList<>();

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addNewPostViewModel = ViewModelProviders.of(this).get(AddNewPostViewModel.class);
        addNewPostViewModel.instantiateDBHelper(getContext());
        View root = inflater.inflate(R.layout.fragment_addnewpost, container, false);

        blogTitle = (EditText) root.findViewById(R.id.blogTitle);
        blogLocation = (EditText) root.findViewById(R.id.blogLocation);
        blogText = (EditText) root.findViewById(R.id.blogText);

        uploadImgButton = (Button) root.findViewById(R.id.uploadImgButton);
        clear = (Button) root.findViewById(R.id.clear);
        submit = (Button) root.findViewById(R.id.submit);

        uploadImgButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i(TAG, "uploadImgButton.setOnClickListener : called");

                Intent galleryIntent = new Intent();
                galleryIntent.setType("image/*");
                galleryIntent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(galleryIntent, "Select Images"), PICK_IMAGE_MULTIPLE);
            }
        });

        addNewPostViewModel.getBlogTitle().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                blogTitle.setText(s);
            }
        });

        addNewPostViewModel.getBlogText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(String s) {
                blogText.setText(s);
            }
        });

        addNewPostViewModel.getSuccessFlag().observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                successFlag = true;
            }
        });

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPostViewModel.clearData();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addNewPostViewModel.getBlogText().setValue(blogText.getText().toString());
                addNewPostViewModel.getBlogTitle().setValue(blogTitle.getText().toString());
                addNewPostViewModel.getBlogImages().setValue(imageList);
                addNewPostViewModel.submitForm();
                if(successFlag) {
                    Toast.makeText(getContext(), "Data was successfully uploaded to database!", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return root;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.i(TAG, "onActivityResult : called");
        if (requestCode == PICK_IMAGE_MULTIPLE) {
            if (resultCode == Activity.RESULT_OK) {
                Bitmap bitmap = null;
                if (data.getClipData() != null) {

                    Log.i(TAG, "Multiple Image : called");

                    int countClipData = data.getClipData().getItemCount();

                    for(int i = 0; i < countClipData; i++) {
                        Uri currImage = data.getClipData().getItemAt(i).getUri();
                        try {
                            bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(currImage));
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        }
                        imageList.add(bitmap);
                    }

                } else if (data.getData() != null) {
                    Log.i(TAG, "Single Image : called");

                    Uri currImage = data.getData();
                    try {
                        bitmap = BitmapFactory.decodeStream(getActivity().getApplicationContext().getContentResolver().openInputStream(currImage));
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                    imageList.add(bitmap);
//                    Log.i(TAG, "currImage : " + currImage.toString());
                }
                Toast.makeText(getContext(), "Images Uploaded Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }
}