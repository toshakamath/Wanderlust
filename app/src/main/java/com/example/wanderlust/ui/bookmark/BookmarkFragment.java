package com.example.wanderlust.ui.bookmark;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProviders;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.Adapters.BookMarkAdapter;
import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.Doa.UserObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BookmarkFragment extends Fragment {

    private static final String TAG = "BookmarkFragment";

    private BookmarkViewModel bookmarkViewModel;
    private FirebaseFirestore db;
    private String userId;
    private ArrayList<String> bookMarkedBlogIds;
    private ArrayList<BlogObject> bookMarkedBlogs;
    private RecyclerView recyclerView;
    private BookMarkAdapter bookMarkAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        Log.i(TAG, "onCreateView : started");
        bookmarkViewModel =
                ViewModelProviders.of(this).get(BookmarkViewModel.class);
        View root = inflater.inflate(R.layout.fragment_bookmark, container, false);
//        final TextView textView = root.findViewById(R.id.text_bookmark);
//        bookmarkViewModel.getText().observe(this, new Observer<String>() {
//            @Override
//            public void onChanged(@Nullable String s) {
//                textView.setText(s);
//            }
//        });

        db = FirebaseFirestore.getInstance();

        bookMarkedBlogIds = new ArrayList<>();
        bookMarkedBlogs = new ArrayList<>();

        UserObject userObject = ((MainActivity)getActivity()).getUserData();
        userId = userObject.getUserId();

        recyclerView = (RecyclerView) root.findViewById(R.id.bookmark_recyclerview);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        getUserBookmarkedBlogs();

        return root;
    }

    public void getUserBookmarkedBlogs() {
        Log.i(TAG, "getUserBookmarkedBlogs : called");

        DocumentReference docRef = db.collection("user_table").document(userId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if(task.isSuccessful()) {
                    DocumentSnapshot documentSnapshot = task.getResult();
                    if (documentSnapshot != null) {
                        if (documentSnapshot.get("bookmarkIds") == null || ((ArrayList<String>) documentSnapshot.get("bookmarkIds")).isEmpty()) {
                            Toast.makeText(getActivity(), "No Bookmarked Blogs!", Toast.LENGTH_SHORT).show();
                        } else {
                            bookMarkedBlogIds = (ArrayList<String>) documentSnapshot.get("bookmarkIds");
                            List<DocumentReference> listDocRef = new ArrayList<>();
                            CollectionReference collectionReference = db.collection("blog_table");
                            for (String id : bookMarkedBlogIds) {
                                DocumentReference docRef = collectionReference.document(id);
                                listDocRef.add(docRef);
                            }

                            List<Task<DocumentSnapshot>> tasks = new ArrayList<>();
                            for (DocumentReference documentReference : listDocRef) {
                                Task<DocumentSnapshot> documentSnapshotTask = documentReference.get();
                                tasks.add(documentSnapshotTask);
                            }
                            Tasks.whenAllSuccess(tasks).addOnSuccessListener(new OnSuccessListener<List<Object>>() {
                                @Override
                                public void onSuccess(List<Object> objects) {
                                    Log.i(TAG, "onSuccess : inside");
                                    for (Object object : objects) {
                                        DocumentSnapshot blogDoc = (DocumentSnapshot)object;
                                        long blogLikes = (long) blogDoc.get("blogLikes");
                                        BlogObject blog = new BlogObject((String) blogDoc.get("blogId"), (String) blogDoc.get("userId"), (String) blogDoc.get("blogTitle"),
                                                (String)blogDoc.get("blogLocation"), (String)blogDoc.get("blogText"), (Double) blogDoc.get("blogLat"),
                                                (Double) blogDoc.get("blogLong"), (int) blogLikes, (ArrayList<String>) blogDoc.get("blogReviews"),
                                                (String) blogDoc.get("blogPicUrl"));
                                        Log.i("TAG", "blogObject : " + blog.toString());
                                        bookMarkedBlogs.add(blog);
                                    }
                                    bookMarkAdapter = new BookMarkAdapter(bookMarkedBlogs);
                                    recyclerView.setAdapter(bookMarkAdapter);
                                }
                            });
                        }
                    }
                }
            }
        });
    }
}