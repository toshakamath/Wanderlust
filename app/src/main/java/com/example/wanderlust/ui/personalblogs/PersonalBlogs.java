package com.example.wanderlust.ui.personalblogs;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.example.wanderlust.Adapters.PersonalBlogAdapter;
import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class PersonalBlogs extends AppCompatActivity {

    private static final String TAG = "PersonalBlogs";

//    public static final String EXTRA_NAME = "com.example.wanderlust.NAME";
//    public static final String EXTRA_EMAIL = "com.example.wanderlust.EMAIL";
//    public static final String EXTRA_ID = "com.example.wanderlust.ID";

    private RecyclerView recyclerView;
    private PersonalBlogAdapter personalBlogAdapter;
    private LinearLayoutManager linearLayoutManager;
//    private ImageButton home;

    private FirebaseFirestore db;
    private ArrayList<BlogObject> allPersonalBlogs;
    private String userID;
    private String userName;
    private String userEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_personal_blogs);

        Log.i(TAG, "onCreate :  called");

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

//        home = (ImageButton) findViewById(R.id.home_button);

        allPersonalBlogs = new ArrayList<>();
        db = FirebaseFirestore.getInstance();

        userID = getIntent().getStringExtra("userID");
        userName = getIntent().getStringExtra("userName");
        userEmail = getIntent().getStringExtra("userEmail");

        recyclerView = (RecyclerView) findViewById(R.id.personalBlogRecyclerView);
        recyclerView.setHasFixedSize(true);

        linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);

        personalBlogAdapter = new PersonalBlogAdapter(allPersonalBlogs, this);
        recyclerView.setAdapter(personalBlogAdapter);

        getPersonalBlogs();

//        home.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                intent.putExtra(EXTRA_ID, userID);
//                intent.putExtra(EXTRA_NAME, userName);
//                intent.putExtra(EXTRA_EMAIL, userEmail);
//                startActivity(intent);
//            }
//        });
    }

    private void getPersonalBlogs() {
        db.collection("blog_table").whereEqualTo("userId", userID).
                get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
            @Override
            public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                for(QueryDocumentSnapshot documentSnapshot : queryDocumentSnapshots){
                    long blogLikes = (long) documentSnapshot.get("blogLikes");
                    BlogObject blog = new BlogObject((String) documentSnapshot.get("blogId"), (String) documentSnapshot.get("userId"), (String) documentSnapshot.get("blogTitle"),
                            (String)documentSnapshot.get("blogLocation"), (String)documentSnapshot.get("blogText"), (Double) documentSnapshot.get("blogLat"),
                            (Double) documentSnapshot.get("blogLong"), (int) blogLikes, (ArrayList<String>) documentSnapshot.get("blogReviews"),
                            (String) documentSnapshot.get("blogPicUrl"));

                    allPersonalBlogs.add(blog);
                }
//                personalBlogAdapter = new PersonalBlogAdapter(allPersonalBlogs);
//                recyclerView.setAdapter(personalBlogAdapter);
                personalBlogAdapter.notifyDataSetChanged();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getParent(), "No Blogs To Display", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
