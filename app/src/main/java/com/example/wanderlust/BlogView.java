package com.example.wanderlust;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.TextView;

import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.Doa.BlogObject;

import java.util.ArrayList;

public class BlogView extends AppCompatActivity {
    final DbInstance dbHelper = new DbInstance(this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_blog_view);
        ActionBar actionBar = this.getSupportActionBar();
        actionBar.hide();
//        getSupportActionBar().setDisplayShowCustomEnabled(true);
//        getSupportActionBar().setCustomView(R.layout.custom_action_bar);
//
        //BlogObject bo = dbHelper.getOneBlogData(1);
        ArrayList<byte[]> pics = new ArrayList<>();
        BlogObject bo = new BlogObject("123","1", "Magnificent India","India","This is India, with an average population of 2 billion",pics,20.770773,73.7217954,5,"Should Visit");
//
//        final TextView tv = (TextView) findViewById(R.id.textView2);
//        if(bo == null){
//            tv.setText("No Object");
//        }else{
//            tv.setText("Object Found" + bo.getBlogTitle());
//        }


    }
}
