package com.example.wanderlust.ui.home;



import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.Adapters.BlogAdapter;
import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class HomeFragment extends Fragment {

    private static final String TAG = "HomeFragment";
    private HomeViewModel homeViewModel;
    private RecyclerView recycleCards;
    private BlogAdapter blogAdapter;

    private FirebaseFirestore db;
    private ArrayList<BlogObject> allBlogs;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        homeViewModel =
                ViewModelProviders.of(this).get(HomeViewModel.class);
        View root = inflater.inflate(R.layout.fragment_home, container, false);
//        final TextView textView = root.findViewById(R.id.text_home);
//        homeViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });
        allBlogs = new ArrayList<>();

        recycleCards = (RecyclerView) root.findViewById(R.id.recycle_cards);
        recycleCards.setHasFixedSize(true);
        recycleCards.setLayoutManager(new LinearLayoutManager(getContext()));

        db = FirebaseFirestore.getInstance();

        getAllBlogData();

//            recycleCards.setAdapter(blogAdapter);
//            recycleCards.setLayoutManager(new LinearLayoutManager(getContext()));
//
////        blogData.clear();
////        blogData.addAll(dbHelper.getAllBlogData());

        return root;

    }

    private void getAllBlogData() {
        db.collection("blog_table").get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    long blogLikes = (long) documentSnapshot.get("blogLikes");
                    BlogObject blog = new BlogObject((String) documentSnapshot.get("blogId"), (String) documentSnapshot.get("userId"), (String) documentSnapshot.get("blogTitle"),
                    (String)documentSnapshot.get("blogLocation"), (String)documentSnapshot.get("blogText"), (Double) documentSnapshot.get("blogLat"),
                            (Double) documentSnapshot.get("blogLong"), (int) blogLikes, (ArrayList<String>) documentSnapshot.get("blogReviews"),
                            (String) documentSnapshot.get("blogPicUrl"));

                    allBlogs.add(blog);
                }
                blogAdapter = new BlogAdapter(allBlogs);
                recycleCards.setAdapter(blogAdapter);
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "No Blogs To Display", Toast.LENGTH_SHORT).show();
            }
        });
    }
}