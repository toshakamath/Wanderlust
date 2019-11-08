package com.example.wanderlust.Adapters;


import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.AsyncTask;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;

import java.io.InputStream;
import java.net.URL;
import java.util.List;

import static java.security.AccessController.getContext;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    private List<BlogObject> blogObjects;

    // Pass in the array into the constructor
    public BlogAdapter(List<BlogObject> blogObjects) {
        this.blogObjects = blogObjects;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView blogTitle;
        public TextView blogContent;
        public ImageView blogImage;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            blogTitle = (TextView) itemView.findViewById(R.id.blog_title);
            blogContent = (TextView) itemView.findViewById(R.id.blog_content);
            blogImage = (ImageView) itemView.findViewById(R.id.blog_image);
        }
    }

    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        Context context = parent.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);

        // Inflate the custom layout
        View blogCardView = inflater.inflate(R.layout.blog_card_view, parent, false);

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
        textView2.setText(blogObject.getBlogText());
        ImageView img= viewHolder.blogImage;
        try{

            Bitmap bm = BitmapFactory.decodeByteArray(blogObject.getBlogPics().get(0), 0, blogObject.getBlogPics().get(0).length);

//            DisplayMetrics dm = new DisplayMetrics();
//            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
//            img.setMinimumHeight(dm.heightPixels);
//            img.setMinimumWidth(dm.widthPixels);

            img.setImageBitmap(bm);

//            new DownloadImageFromInternet(img).execute(cafe.getDescription());
//            URL url = new URL(cafe.getDescription());
//            Bitmap bmp = BitmapFactory.decodeStream(url.openConnection().getInputStream());
//            img.setImageBitmap(bmp);
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