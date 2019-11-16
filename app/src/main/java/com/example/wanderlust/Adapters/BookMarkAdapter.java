package com.example.wanderlust.Adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class BookMarkAdapter extends RecyclerView.Adapter<BookMarkAdapter.ViewHolder>{

    private static final String TAG = "BookMarkAdapter";
    private Context context;
    private List<BlogObject> bookMarkedBlogs;

    public BookMarkAdapter(List<BlogObject> bookMarkedBlogs) {
        this.bookMarkedBlogs = bookMarkedBlogs;
        Log.i(TAG, bookMarkedBlogs.toString());
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.listview_card, parent, false);

        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        // Get the data model based on position
        final BlogObject blogObject = bookMarkedBlogs.get(position);

        // Set item views based on your views and data model
        TextView blgTitle = holder.viewBlogTitle;
        blgTitle.setText(blogObject.getBlogTitle());
        TextView blgLocation = holder.viewBlogLocation;
        blgLocation.setText(blogObject.getBlogLocation());
        ImageView blgImg= holder.viewBlogImg;
        if(blogObject.getblogPicUrl() != null) {
            Picasso.with(context).load(blogObject.getblogPicUrl()).into(blgImg);
        }
    }

    @Override
    public int getItemCount() {
        return bookMarkedBlogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder{

        public TextView viewBlogTitle;
        public TextView viewBlogLocation;
        public ImageView viewBlogImg;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            viewBlogTitle = (TextView) itemView.findViewById(R.id.listview_title);
            viewBlogLocation = (TextView) itemView.findViewById(R.id.listview_loaction);
            viewBlogImg = (ImageView) itemView.findViewById(R.id.listview_img);
        }
    }
}
