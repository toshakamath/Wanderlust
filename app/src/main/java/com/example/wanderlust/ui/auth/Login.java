package com.example.wanderlust.ui.auth;

import com.example.wanderlust.DatabaseHelper.DbInstance;
import com.example.wanderlust.Doa.BlogObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;
import com.example.wanderlust.ui.home.HomeFragment;

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

public class Login extends AppCompatActivity {

    Button cancel, register, login;
    EditText email, password;
    DbInstance dbHelper = new DbInstance(this);
    Context parentThis = this;
    public static final String EXTRA_NAME = "com.example.wanderlust.NAME";
    public static final String EXTRA_EMAIL = "com.example.wanderlust.EMAIL";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        cancel = (Button) findViewById(R.id.cancelbuttonlogin);
        register = (Button) findViewById(R.id.redirectToRegisterButton);
        login = (Button) findViewById(R.id.loginbutton);

        email = (EditText) findViewById(R.id.emailtextlogin);
        password = (EditText) findViewById(R.id.passwordtextlogin);

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                email.setText("");
                password.setText("");
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(getApplicationContext(), Register.class);
                    startActivity(i);
            }
        });

        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.i("HEYYYY", "I came here");
                String username = dbHelper.verifyUser(email.getText().toString(), password.getText().toString());
                Log.i("HEYYYY", username);

                if (!username.equals("")) {
                    Toast.makeText(parentThis, "Welcome, " + username, Toast.LENGTH_LONG).show();
                    Intent i = new Intent(getApplicationContext(), MainActivity.class);
                    i.putExtra(EXTRA_NAME, username);
                    i.putExtra(EXTRA_EMAIL, email.getText().toString());
                    startActivity(i);
                }
                else{
                    Toast.makeText(parentThis, "User " + email.getText()+ " does not exist, please register.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }
}
