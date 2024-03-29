package com.example.wanderlust.ui.home;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.BlogView;
import com.example.wanderlust.Adapters.BlogAdapter;
import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.Doa.BlogObject;

import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;
import com.example.wanderlust.ui.auth.Login;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private HomeViewModel homeViewModel;
    private RecyclerView recycleCards;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);

        recycleCards = (RecyclerView) root.findViewById(R.id.recycle_cards);

        final DbInstance dbHelper = new DbInstance(getActivity());
        final ArrayList<BlogObject> blogData = dbHelper.getAllBlogData();
        if(blogData != null){

            for(int i=0; i<blogData.size(); i++){
                Log.i("Tag: "+i, blogData.get(i).getBlogId());
                Log.i("Tag: "+i, blogData.get(i).getBlogTitle());
                Log.i("Tag: "+i, blogData.get(i).getBlogText());
                Log.i("Tag: "+i, blogData.get(i).getBlogLocation());
                Log.i("Tag: "+i, blogData.get(i).getBlogReviews());
            }

            recycleCards.setAdapter(new BlogAdapter(blogData,new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent i = new Intent(getActivity(), BlogView.class);
                    startActivity(i);
                }
            }));

            recycleCards.setLayoutManager(new LinearLayoutManager(getContext()));

        }

//        blogData.clear();
//        blogData.addAll(dbHelper.getAllBlogData());

        return root;

    }
}