package com.example.wanderlust.ui.settings;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.Doa.UserObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;
import com.example.wanderlust.ui.personalblogs.PersonalBlogs;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

public class SettingsFragment extends Fragment {

    private static final String TAG = "SettingsFragment";

    private SettingsViewModel settingsViewModel;

    private TextView accName;
    private TextView accEmail;
    private Button personalBlogs;
    private Button updatePassword;
    private EditText newPassword;

    private FirebaseFirestore db;

    public View onCreateView(@NonNull final LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView : " + "called");
        settingsViewModel =
                ViewModelProviders.of(this).get(SettingsViewModel.class);
        View root = inflater.inflate(R.layout.fragment_settings, container, false);

        accName = (TextView) root.findViewById(R.id.accName);
        accEmail = (TextView) root.findViewById(R.id.accEmail);
        personalBlogs = (Button) root.findViewById(R.id.personalBlogs);
        updatePassword = (Button) root.findViewById(R.id.updatePassword);
        newPassword = (EditText) root.findViewById(R.id.newPassword);

        db = FirebaseFirestore.getInstance();

        final UserObject userObject = ((MainActivity) getActivity()).getUserData();

//        Log.i(TAG, userObject.toString());

//        Log.i(TAG, "accountHolderId ---> " + userObject.getUserId());
//        Log.i(TAG, "accountHolderName ---> " + userObject.getName());
        accName.setText(userObject.getName());
        accEmail.setText(userObject.getEmail());

        personalBlogs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getActivity(), PersonalBlogs.class);
                intent.putExtra("userID", userObject.getUserId());
                intent.putExtra("userName", userObject.getName());
                intent.putExtra("userEmail", userObject.getEmail());
                startActivity(intent);
            }
        });

        updatePassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                DocumentReference docRef = db.collection("user_table").document(userObject.getUserId());

                docRef.update("password", newPassword.getText().toString()).
                        addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()) {
                            Toast.makeText(getContext(), "Password Update Was Successful!", Toast.LENGTH_LONG).show();

                        } else  {
                            Toast.makeText(getContext(), "Password Update Failed!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }
        });

        return root;
    }

}