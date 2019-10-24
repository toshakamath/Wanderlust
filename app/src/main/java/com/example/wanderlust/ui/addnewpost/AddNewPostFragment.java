package com.example.wanderlust.ui.addnewpost;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.R;

public class AddNewPostFragment extends Fragment {

    private AddNewPostViewModel addNewPostViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        addNewPostViewModel =
                ViewModelProviders.of(this).get(AddNewPostViewModel.class);
        View root = inflater.inflate(R.layout.fragment_addnewpost, container, false);
        final TextView textView = root.findViewById(R.id.text_addnewpost);
        addNewPostViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}