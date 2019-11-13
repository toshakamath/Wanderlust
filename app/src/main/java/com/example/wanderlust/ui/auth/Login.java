package com.example.wanderlust.ui.auth;

import com.example.wanderlust.Doa.UserObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import androidx.annotation.NonNull;
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

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    Button cancel, register, login;
    EditText email, password;
    Context parentThis = this;
    public static final String EXTRA_NAME = "com.example.wanderlust.NAME";
    public static final String EXTRA_EMAIL = "com.example.wanderlust.EMAIL";
    public static final String EXTRA_ID = "com.example.wanderlust.ID";

    private FirebaseFirestore db;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        db = FirebaseFirestore.getInstance();

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
                if (email.getText().toString().equals("") || password.getText().toString().equals("")) {
                    Toast.makeText(parentThis, "Please Enter Email and Password", Toast.LENGTH_LONG).show();

                } else {
                    db.collection("user_table").whereEqualTo("email", email.getText().toString()).
                            whereEqualTo("password", password.getText().toString()).
                            get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    if(document.exists()) {
                                        Log.d(TAG, document.getId() + " => " + document.getData());
                                        UserObject user = new UserObject(document.getString("userId"), document.getString("name"),
                                                document.getString("email"), document.getString("password"));

                                        Log.i(TAG, user.toString());

                                        Toast.makeText(parentThis, "Welcome, " + user.getName(), Toast.LENGTH_LONG).show();
                                        Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                        i.putExtra(EXTRA_ID, user.getUserId());
                                        i.putExtra(EXTRA_NAME, user.getName());
                                        i.putExtra(EXTRA_EMAIL, user.getEmail());
                                        startActivity(i);
                                    } else {
                                        Toast.makeText(parentThis, "User " + email.getText() + " does not exist, please register.", Toast.LENGTH_LONG).show();
                                    }
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());

                            }
                        }
                    });
                }
            }
        });
    }

}
