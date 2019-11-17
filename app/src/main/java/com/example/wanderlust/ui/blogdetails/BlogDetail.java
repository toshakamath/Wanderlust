package com.example.wanderlust.ui.blogdetails;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.wanderlust.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class BlogDetail extends AppCompatActivity {

    private static final String TAG = "BlogDetail";

    private TextView currBlogTitle;
    private TextView currBlogLocation;
    private ImageView currBlogImg;
    private TextView currBlogDesc;
    private TextView currBlogLikes;
    private ImageView heartRed;
    private ImageView heartWhite;
    private GestureDetector gestureDetector;
    private Heart heart;
    private String blogId;
    private FirebaseFirestore db;
    private GestureDetector bookMarkGestureDetector;
    private ImageView bookMarkBlack;
    private ImageView bookMarkWhite;
    private BookMark bookMark;
    private Boolean bookmarkedByCurrUser;
    private String userId;
    private ArrayList<String> bookmarkedBlogIds;
    private ImageView addCommentButton;
    private EditText addComment;
    private Button submitComment;
    private ArrayList<String> blogComments;
    private TextView displayBlogComments;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_detail);

        this.getSupportActionBar().setDisplayOptions(ActionBar.DISPLAY_SHOW_CUSTOM);
        getSupportActionBar().setDisplayShowCustomEnabled(true);
        getSupportActionBar().setCustomView(R.layout.custom_action_bar);

        currBlogTitle = (TextView) findViewById(R.id.currBlogTitle);
        currBlogLocation = (TextView) findViewById(R.id.currBlogLocation);
        currBlogDesc = (TextView) findViewById(R.id.currBlogDesc);
        currBlogLikes = (TextView) findViewById(R.id.currBlogLikes);
        currBlogImg = (ImageView) findViewById(R.id.currBlogImg);
        heartRed = (ImageView) findViewById(R.id.image_heart_red);
        heartWhite = (ImageView) findViewById(R.id.image_heart);
        bookMarkBlack = (ImageView) findViewById(R.id.image_bookmark_black);
        bookMarkWhite = (ImageView) findViewById(R.id.image_bookmark);
        addCommentButton = (ImageView) findViewById(R.id.image_comment);
        addComment = (EditText) findViewById(R.id.addComment);
        submitComment = (Button) findViewById(R.id.submitComment);
        displayBlogComments = (TextView) findViewById(R.id.displayBlogComments);

        addCommentButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.setVisibility(View.VISIBLE);
                submitComment.setVisibility(View.VISIBLE);
            }
        });

        submitComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addComment.setVisibility(View.GONE);
                submitComment.setVisibility(View.GONE);

                //Call db from here
                String blogString = addComment.getText().toString();
                addComment.setText("");
                insertCommentInDB(blogString);
            }
        });

        db = FirebaseFirestore.getInstance();

        bookmarkedBlogIds = new ArrayList<>();

        blogComments = new ArrayList<>();

        gestureDetector = new GestureDetector(this, new GestureListener());

        bookMarkGestureDetector = new GestureDetector(this, new bookMarkGestureListener());

        heartRed.setVisibility(View.GONE);
        heartWhite.setVisibility(View.VISIBLE);
        heart = new Heart(heartRed, heartWhite);

        bookMark = new BookMark(bookMarkBlack, bookMarkWhite);

        getIncomingIntents();

        toggleHeart();
        checkIfBookmarkedByCurrUser();
    }

    private void insertCommentInDB(String blogString) {

        Log.i(TAG, "insertCommentInDB : called");

        blogComments.add(blogString);
//        Log.i(TAG, "blogComments :" + blogComments.size());
        DocumentReference documentReference = db.collection("blog_table").document(blogId);

        documentReference.update("blogReviews", blogComments).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if(task.isSuccessful()){
                    //Called get comments method
                    getAllBlogComments();
                    Toast.makeText(getBaseContext(), "Blog Comment saved Successfully!", Toast.LENGTH_LONG).show();
                } else{
                    Toast.makeText(getBaseContext(), "Failure to save Blog Comment!", Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    private void getAllBlogComments() {
        DocumentReference docRef = db.collection("blog_table").document(blogId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null) {
                        blogComments = (ArrayList<String>) documentSnapshot.get("blogReviews");
                    }
                    Log.i(TAG, "blogComments.size() : "+ blogComments.size());
                    setComments(blogComments);
                }

            }
        });
    }

    private void getIncomingIntents() {
        Log.i(TAG, "getIncomingIntents : checking for incoming intents");

        if(getIntent().hasExtra("userId")) {
            userId = getIntent().getStringExtra("userId");
            Log.i(TAG, "userId: " + userId);
        }

        if(getIntent().hasExtra("blogId") && getIntent().hasExtra("blogTitle") && getIntent().hasExtra("blogLocation") && getIntent().hasExtra("blogPicUrl")
                && getIntent().hasExtra("blogLikes") && getIntent().hasExtra("blogDesc") && getIntent().hasExtra("blogReviews")) {

            Log.i(TAG, "getIncomingIntents : found intent extras");

            blogId = getIntent().getStringExtra("blogId");
            String blogTitle = getIntent().getStringExtra("blogTitle");
            String blogLocation = getIntent().getStringExtra("blogLocation");
            String blogPicUrl = getIntent().getStringExtra("blogPicUrl");
            String blogDesc = getIntent().getStringExtra("blogDesc");
            String blogLikes = getIntent().getStringExtra("blogLikes");
            ArrayList<String> blogReviews = getIntent().getStringArrayListExtra("blogReviews");

//            Log.i(TAG, "blogId : " + blogId);
//            Log.i(TAG, "blogTitle : " + blogTitle);
//            Log.i(TAG, "blogLikes : " + blogLikes);
//            Log.i(TAG, "blogPicUrl : " + blogPicUrl);
//            Log.i(TAG, "blogReviews : " + blogReviews.size());
            setData(blogTitle, blogLocation, blogDesc, blogPicUrl, blogLikes, blogReviews);
        }
    }

    private void setComments(ArrayList<String> blogReviews) {
        blogComments = blogReviews;
        int blogReviewsLength = blogReviews.size();
        String reviewString = "";
        if(blogReviewsLength == 1){
            reviewString = "Comments: " + blogReviews.get(0);
        }
        else if(blogReviewsLength == 2){
            reviewString = "Comments: " + blogReviews.get(0) + " and " + blogReviews.get(1);
        }
        else if(blogReviewsLength == 3){
            reviewString = "Comments: " + blogReviews.get(0) + " , " + blogReviews.get(1) + " and " + blogReviews.get(2);
        }
        else if(blogReviewsLength == 4){
            reviewString = "Comments: " + blogReviews.get(0) + " , " + blogReviews.get(1) + " , " + blogReviews.get(2) + " and " + blogReviews.get(3);
        }
        else if(blogReviewsLength > 4){
            reviewString = "Comments: " + blogReviews.get(0) + " , " + blogReviews.get(1) + " , " + blogReviews.get(2) + " , " + blogReviews.get(3)
                    + " and " + (blogReviewsLength - 4) + " others";
        }
        Log.d(TAG, "setData: reviews string: " + reviewString);
        displayBlogComments.setText(reviewString);
    }

    private void setData(String blogTitle, String blogLocation, String blogDesc, String blogPicUrl, String blogLikes, ArrayList<String> blogReviews) {

        currBlogTitle.setText(blogTitle);
        currBlogLocation.setText(blogLocation);
        currBlogDesc.setText(blogDesc);
        if(blogLikes == null) {
            String likes = "0 likes";
            currBlogLikes.setText(likes);
        }
        else{
            String likes = blogLikes + " likes";
            currBlogLikes.setText(likes);
        }
        if(blogReviews.size() != 0 || blogReviews != null){
            setComments(blogReviews);
        }
        Picasso.with(this).load(blogPicUrl).into(currBlogImg);
    }

    private void toggleHeart() {
         heartRed.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 return gestureDetector.onTouchEvent(motionEvent);
             }
         });

         heartWhite.setOnTouchListener(new View.OnTouchListener() {
             @Override
             public boolean onTouch(View view, MotionEvent motionEvent) {
                 return gestureDetector.onTouchEvent(motionEvent);
             }
         });
    }

    public class GestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            //call db from here to insert likes

            heart.toggleLike();

            DocumentReference docRef = db.collection("blog_table").document(blogId);

            docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<DocumentSnapshot> task) {

                    if(task.isSuccessful()) {
                        DocumentSnapshot documentSnapshot = task.getResult();
                        if(documentSnapshot != null) {
                            Log.i(TAG, "DB like value ---> " + documentSnapshot.get("blogLikes"));
                            final long currLikes = (long) documentSnapshot.get("blogLikes");

                            DocumentReference docRef = db.collection("blog_table").document(blogId);
                            docRef.update("blogLikes", currLikes + 1).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    String likes = (currLikes + 1) + " likes";
                                    currBlogLikes.setText(likes);
                                }
                            });
                        }
                    }

                }
            });

            return true;
        }
    }

    private void checkIfBookmarkedByCurrUser() {
        Log.i(TAG, "checkIfBookmarkedByCurrUser : called");
        DocumentReference docRef = db.collection("user_table").document(userId);

        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if(documentSnapshot != null) {
                        if(documentSnapshot.get("bookmarkIds") == null || ((ArrayList<String>) documentSnapshot.get("bookmarkIds")).isEmpty()) {
                            bookmarkedByCurrUser = false;
                        } else {
                            Log.i(TAG, "DB bookmark value ---> " + documentSnapshot.get("bookmarkIds"));
                            bookmarkedByCurrUser = false;
                            bookmarkedBlogIds = (ArrayList<String>) documentSnapshot.get("bookmarkIds");
                            for (String id : bookmarkedBlogIds) {
                                if (id.equals(blogId)) {
                                    bookmarkedByCurrUser = true;
                                }
                            }
                        }
                    }
                    setupWidgets();
                }
                else {
                    bookmarkedByCurrUser = false;
                    setupWidgets();
                }
                Log.i(TAG, "bookmarkedByCurrUser ===> " + bookmarkedByCurrUser);
            }
        });
    }

    private void setupWidgets(){
        if(bookmarkedByCurrUser){
            bookMarkWhite.setVisibility(View.GONE);
            bookMarkBlack.setVisibility(View.VISIBLE);
            bookMarkBlack.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: black bookmark touch detected.");
                    return bookMarkGestureDetector.onTouchEvent(event);
                }
            });
        }
        else{
            bookMarkWhite.setVisibility(View.VISIBLE);
            bookMarkBlack.setVisibility(View.GONE);
            bookMarkWhite.setOnTouchListener(new View.OnTouchListener() {
                @Override
                public boolean onTouch(View v, MotionEvent event) {
                    Log.d(TAG, "onTouch: white bookmark touch detected.");
                    return bookMarkGestureDetector.onTouchEvent(event);
                }
            });
        }
    }

    public class bookMarkGestureListener extends GestureDetector.SimpleOnGestureListener {
        @Override
        public boolean onDown(MotionEvent e) {
            return true;
        }

        @Override
        public boolean onDoubleTap(MotionEvent e) {
            Log.i(TAG, "onDoubleTap : called");

            // call db from here

            if(bookmarkedByCurrUser) {
                Log.i(TAG, "onDoubleTap : current user has bookmarked the blog");
                //delete the blogId from the user_table
                bookmarkedBlogIds.remove(blogId);
                DocumentReference docRef = db.collection("user_table").document(userId);
                docRef.update("bookmarkIds", bookmarkedBlogIds).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bookMark.toggleBookMark();
                            checkIfBookmarkedByCurrUser();
                            Toast.makeText(getBaseContext(), "Blog removed from Bookmark Successfully!", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getBaseContext(), "Failure to remove Blog as Bookmark!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
            else {
                Log.i(TAG, "onDoubleTap : current user has not bookmarked the blog");
                //add the blogId as being bookmarked to the user_table

                bookmarkedBlogIds.add(blogId);
                DocumentReference docRef = db.collection("user_table").document(userId);
                docRef.update("bookmarkIds", bookmarkedBlogIds).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful()){
                            bookMark.toggleBookMark();
                            checkIfBookmarkedByCurrUser();
                            Toast.makeText(getBaseContext(), "Blog saved as Bookmark Successfully!", Toast.LENGTH_LONG).show();
                        } else{
                            Toast.makeText(getBaseContext(), "Failure to save Blog as Bookmark!", Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }

            return true;
        }
    }
}
