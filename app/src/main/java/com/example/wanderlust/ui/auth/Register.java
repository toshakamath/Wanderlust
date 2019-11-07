package com.example.wanderlust.ui.auth;

import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;

public class Register extends AppCompatActivity {

    Button cancel, register, login;
    EditText name, email, password;
    DbInstance dbHelper = new DbInstance(this);
    Context parentThis = this;
    public static final String EXTRA_MESSAGE = "com.example.lab1.MESSAGE";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_register);

        cancel = (Button) findViewById(R.id.cancelbutton);
        register = (Button) findViewById(R.id.registerbutton);
        login = (Button) findViewById(R.id.redirectToLoginButton);

        name = (EditText) findViewById(R.id.nametext);
        email = (EditText) findViewById(R.id.emailtext);
        password = (EditText) findViewById(R.id.passwordtext);

//        final DbInstance dbHelper = new DbInstance(this);
//        final ArrayList al = dbHelper.getAllBlogData();

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                name.setText("");
                email.setText("");
                password.setText("");
            }
        });

//        register.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                long insertsuccess = dbHelper.insertNewUser(name.getText().toString(), email.getText().toString(), password.getText().toString());
//                if (insertsuccess>0) {
//                    al.clear();
//                    al.addAll(dbHelper.getAllBlogData());
//                    Log.i("checkout4: AL blogtitle",al+"");
//                    Toast.makeText(parentThis, "Welcome, " + name.getText(), Toast.LENGTH_LONG).show();
//                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
//                    String message = name.getText().toString();
//                    i.putExtra(EXTRA_MESSAGE, message);
//                    startActivity(i);
//                }
//                else{
//                    Toast.makeText(parentThis, "User " + email.getText()+ " already exists, please login.", Toast.LENGTH_LONG).show();
//                }
//            }
//        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                long insertsuccess = dbHelper.insertNewUser(name.getText().toString(), email.getText().toString(), password.getText().toString());
                if (insertsuccess>0) {
                    Toast.makeText(parentThis, "Welcome, " + name.getText()+"! Please login!", Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), Login.class);
                    String message = name.getText().toString();
                    i.putExtra(EXTRA_MESSAGE, message);
                    startActivity(i);
                }
                else{
                    Toast.makeText(parentThis, "User " + email.getText()+ " already exists, please login.", Toast.LENGTH_LONG).show();
                }
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Login.class);
                startActivity(i);
            }
        });
    }
}
