package com.example.wanderlust.ui.addnewpost;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.Doa.UserObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.model.Place;
import com.google.android.libraries.places.api.net.PlacesClient;
import com.google.android.libraries.places.widget.AutocompleteSupportFragment;
import com.google.android.libraries.places.widget.listener.PlaceSelectionListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.UUID;

public class AddNewPostFragment extends Fragment {

    private AddNewPostViewModel addNewPostViewModel;

    private static final int PICK_IMAGE_MULTIPLE = 1;

    private static final String TAG = "BlogUploadFragment";

    private EditText blogTitle, blogLocation, blogText;
    private Button uploadImgButton, clear, submit;
    private boolean successFlag = false;
    private String locationName;
    private String locationAddress;
    private LatLng locationLatLng;
    private String userID;

    Uri imgUri;

    PlacesClient placesClient;
    List<Place.Field> placeFields;
    AutocompleteSupportFragment autocompleteSupportFragment;

    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private StorageReference storageReference = FirebaseStorage.getInstance().getReference("images");

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        addNewPostViewModel = ViewModelProviders.of(this).get(AddNewPostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addnewpost, container, false);

        UserObject userObject = ((MainActivity) getActivity()).getUserData();

        userID = userObject.getUserId();

        blogTitle = (EditText) root.findViewById(R.id.blogTitle);
        blogText = (EditText) root.findViewById(R.id.blogText);

        uploadImgButton = (Button) root.findViewById(R.id.uploadImgButton);
        clear = (Button) root.findViewById(R.id.clear);
        submit = (Button) root.findViewById(R.id.submit);

        Places.initialize(getContext(), getResources().getString(R.string.GOOGLE_API_KEY));
        placesClient = Places.createClient(getContext());
        placeFields = Arrays.asList(Place.Field.ID, Place.Field.NAME, Place.Field.ADDRESS, Place.Field.LAT_LNG);

        setupAutoCompleteFragment();

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

        clear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                blogTitle.setText("");
                blogText.setText("");
                imgUri = null;
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                submitForm();
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
                if (data.getClipData() != null) {

                    Log.i(TAG, "Multiple Image : called");

                    Toast.makeText(getContext(), "Cannot upload multiple images", Toast.LENGTH_SHORT).show();

//                    int countClipData = data.getClipData().getItemCount();
//
//                    for(int i = 0; i < countClipData; i++) {
//                        Uri currImage = data.getClipData().getItemAt(i).getUri();
//                        imageList.add(currImage);
//                    }

                } else if (data.getData() != null) {
                    Log.i(TAG, "Single Image : called");

                    Uri currImage = data.getData();
                    imgUri = currImage;
//                    Log.i(TAG, "currImage : " + currImage.toString());
                }
                Toast.makeText(getContext(), "Images Uploaded Successfully!", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void setupAutoCompleteFragment() {
        autocompleteSupportFragment = (AutocompleteSupportFragment) getChildFragmentManager().findFragmentById(R.id.blogLocation);

        autocompleteSupportFragment.setPlaceFields(placeFields);

        autocompleteSupportFragment.setOnPlaceSelectedListener(new PlaceSelectionListener() {
            @Override
            public void onPlaceSelected(@NonNull Place place) {
                locationName = place.getName();
                locationAddress = place.getAddress();
                locationLatLng = place.getLatLng();
            }

            @Override
            public void onError(@NonNull Status status) {
                Toast.makeText(getContext(), "Some error occured!", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String getFileExtension(Uri uri){
        ContentResolver contentResolver = getContext().getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadImages(Uri mImageUri) {
        final StorageReference fileReference = storageReference.child(System.currentTimeMillis()
                + "." + getFileExtension(mImageUri));

        fileReference.putFile(mImageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                        Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_LONG).show();
                        taskSnapshot.getMetadata().getReference().getDownloadUrl().addOnCompleteListener(new OnCompleteListener<Uri>() {
                            @Override
                            public void onComplete(@NonNull Task<Uri> task) {
                                if (task.isSuccessful()) {
                                    // Task completed successfully
                                    Uri uri = task.getResult();
                                    Log.i(TAG, "URI -> " + uri);

                                    UUID blogId = UUID.randomUUID();
                                    BlogObject blogObject = new BlogObject(blogId.toString(), userID,
                                            blogTitle.getText().toString(), locationName, blogText.getText().toString(), locationLatLng.latitude,
                                            locationLatLng.longitude, 0, new ArrayList<String>(), uri.toString());

                                    db.collection("blog_table").document(blogId.toString()).set(blogObject);
                                    Intent i = new Intent(getContext(), MainActivity.class);
                                    startActivity(i);

                                } else {
                                    // Task failed with an exception
                                    Exception exception = task.getException();
                                    Log.i(TAG, "exception -> " + exception);
                                }
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void submitForm() {
        if (imgUri != null) {
            uploadImages(imgUri);
        }
        else {
            Toast.makeText(getContext(), "No file selected", Toast.LENGTH_SHORT).show();
        }

    }
}