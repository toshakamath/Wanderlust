package com.example.wanderlust.Adapters;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;
import com.example.wanderlust.ui.blogdetails.BlogDetail;
import com.squareup.picasso.Picasso;

import java.util.List;

// Create the basic adapter extending from RecyclerView.Adapter
// Note that we specify the custom ViewHolder which gives us access to our views

public class BlogAdapter extends RecyclerView.Adapter<BlogAdapter.ViewHolder> {
    // Provide a direct reference to each of the views within a data item
    // Used to cache the views within the item layout for fast access

    private List<BlogObject> blogObjects;
    private String userId;
    private Context context;
//    private OnItemClickListener onItemClickListener;

    // Pass in the array into the constructor
    public BlogAdapter(List<BlogObject> blogObjects, String userId) {
        this.userId = userId;
        this.blogObjects = blogObjects;
    }

//    public interface OnItemClickListener {
//        void onButtonCLick(int position);
//    }
//
//    public void setOnItemClickListener(OnItemClickListener listener) {
//        onItemClickListener = listener;
//    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        // Your holder should contain a member variable
        // for any view that will be set as you render a row
        public TextView blogTitle;
        public TextView blogContent;
        public ImageView blogImage;
        public Button blogDetail;

        // We also create a constructor that accepts the entire item row
        // and does the view lookups to find each subview
        public ViewHolder(View itemView) {
            // Stores the itemView in a public final member variable that can be used
            // to access the context from any ViewHolder instance.
            super(itemView);

            blogTitle = (TextView) itemView.findViewById(R.id.blog_title);
            blogContent = (TextView) itemView.findViewById(R.id.blog_content);
            blogImage = (ImageView) itemView.findViewById(R.id.blog_image);
            blogDetail = (Button) itemView.findViewById(R.id.viewBlogButton);
        }
    }

    @Override
    public BlogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        context = parent.getContext();
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
        final BlogObject blogObject = blogObjects.get(position);

        // Set item views based on your views and data model
        TextView textView = viewHolder.blogTitle;
        textView.setText(blogObject.getBlogTitle());
        TextView textView2 = viewHolder.blogContent;
        textView2.setText(blogObject.getBlogText());
        ImageView img= viewHolder.blogImage;
        if(blogObject.getblogPicUrl() != null) {
            Picasso.with(context).load(blogObject.getblogPicUrl()).into(img);
        }

        Button detailButton = viewHolder.blogDetail;
        detailButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Toast.makeText(context, "Button Clicked", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(context, BlogDetail.class);
                intent.putExtra("blogId", blogObject.getBlogId());
                intent.putExtra("blogTitle", blogObject.getBlogTitle());
                intent.putExtra("blogLocation", blogObject.getBlogLocation());
                intent.putExtra("blogPicUrl", blogObject.getblogPicUrl());
                intent.putExtra("blogLikes", String.valueOf(blogObject.getBlogLikes()));
                intent.putExtra("blogDesc", blogObject.getBlogText());
                intent.putExtra("userId", userId);
                intent.putExtra("blogReviews", blogObject.getBlogReviews());
                context.startActivity(intent);
            }
        });
    }

    // Returns the total count of items in the list
    @Override
    public int getItemCount() {
        return blogObjects.size();
    }
}