package com.example.wanderlust.Adapters;

import android.content.Context;
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

public class PersonalBlogAdapter extends RecyclerView.Adapter<PersonalBlogAdapter.ViewHolder> {

    private List<BlogObject> allPersonalBlogs;
    private Context context;

    public PersonalBlogAdapter(List<BlogObject> allPersonalBlogs, Context context) {
        this.allPersonalBlogs = allPersonalBlogs;
        this.context = context;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.personal_blog_card_view, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {

        // Get the data model based on position
        BlogObject blogObject = allPersonalBlogs.get(position);

        // Set item views based on your views and data model
        TextView textView = holder.personalBlogTitle;
        textView.setText(blogObject.getBlogTitle());
        TextView textView2 = holder. personalBlogLocation;
        textView2.setText(blogObject.getBlogLocation());
        ImageView img= holder.personalBlogImage;
        if(blogObject.getblogPicUrl() != null) {
            Picasso.with(context).load(blogObject.getblogPicUrl()).into(img);
        }

    }

    @Override
    public int getItemCount() {
        return allPersonalBlogs.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {

        private TextView personalBlogTitle;
        private TextView personalBlogLocation;
        private ImageView personalBlogImage;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            personalBlogTitle = (TextView) itemView.findViewById(R.id.personal_blog_title);
            personalBlogLocation = (TextView) itemView.findViewById(R.id.personal_blog_location);
            personalBlogImage = (ImageView) itemView.findViewById(R.id.personal_blog_image);
        }
    }

}
