package com.example.wanderlust.ui.extra1;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.wanderlust.R;

public class Extra1Fragment extends Fragment {

    private Extra1ViewModel extra1ViewModel;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        extra1ViewModel =
                ViewModelProviders.of(this).get(Extra1ViewModel.class);
        View root = inflater.inflate(R.layout.fragment_extra1, container, false);
        final TextView textView = root.findViewById(R.id.text_extra1);
        extra1ViewModel.getText().observe(this, new Observer<String>() {
            @Override
            public void onChanged(@Nullable String s) {
                textView.setText(s);
            }
        });
        return root;
    }
}