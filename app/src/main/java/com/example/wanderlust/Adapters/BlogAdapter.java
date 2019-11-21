package com.example.wanderlust.Adapters;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.media.Image;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.BlogView;
import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;

import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static java.security.AccessController.getContext;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    private List<BlogObject> blogObjects;
    private AdapterView.OnClickListener listener;
    ArrayList<Integer> imgs = new ArrayList<>();


    // Pass in the array into the constructor
    public BlogAdapter(List<BlogObject> blogObjects, AdapterView.OnClickListener listener) {
        this.blogObjects = blogObjects;
        this.listener = listener;
        imgs.add(R.drawable.helloindia);
        imgs.add(R.drawable.beijing);
        imgs.add(R.drawable.tajmahal);
        imgs.add(R.drawable.sf);
        imgs.add(R.drawable.timesquare);
        imgs.add(R.drawable.wanderlust_logo);
        imgs.add(R.drawable.wanderlust_logo);
        imgs.add(R.drawable.wanderlust_logo);
        imgs.add(R.drawable.wanderlust_logo);
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView blogTitle;
        public TextView blogContent;
        public ImageView blogImage;
        public Button blogButton;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            blogTitle = (TextView) itemView.findViewById(R.id.blog_title);
            blogContent = (TextView) itemView.findViewById(R.id.blog_content);
            blogImage = (ImageView) itemView.findViewById(R.id.blog_image);
            //blogButton = (Button) itemView.findViewById(R.id.viewBlogButton);
        }
    }

    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View blogCardView = inflater.inflate(R.layout.blog_card_view, parent, false);
        blogCardView.setOnClickListener(this.listener);
        // Return a new holder instance
        ViewHolder viewHolder = new ViewHolder(blogCardView);
        return viewHolder;
    }

    // Involves populating data into the item through holder
    @Override
    public void onBindViewHolder(BlogAdapter.ViewHolder viewHolder, int position) {

        // Get the data model based on position
        BlogObject blogObject = blogObjects.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.blogTitle;
        textView.setText(blogObject.getBlogTitle());
        TextView textView2 = viewHolder.blogContent;
        String str = blogObject.getBlogText().substring(0,30);
        textView2.setText(str+"...");
        //Button b = viewHolder.blogButton;

        textView2.setText(blogObject.getBlogText());
        ImageView img= (ImageView) viewHolder.blogImage;

        img.setImageResource(imgs.get(position));
        try{
            Log.i("TAG", "coming here");
//            byte array, int, length
            Log.i("TAG", blogObject.getBlogPics()+"");
            Log.i("TAG", blogObject.getBlogPics().get(0)+"");
            Bitmap bm = BitmapFactory.decodeByteArray(blogObject.getBlogPics().get(0), 0, blogObject.getBlogPics().get(0).length);
            img.setImageBitmap(bm);

        }catch(Exception e){
            System.out.println("ERROR IMAGE"+e.getMessage());
        }
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return blogObjects.size();
    }
}