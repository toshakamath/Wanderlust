package com.example.wanderlust.ui.auth;

import com.example.wanderlust.Doa.UserObject;
import com.example.wanderlust.MainActivity;
import com.example.wanderlust.R;;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
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

import java.util.UUID;

public class Login extends AppCompatActivity {

    private static final String TAG = "Login";

    Button cancel, register, login;
    EditText email, password;
    Context parentThis = this;
    SignInButton signInButton;
    public static final String EXTRA_NAME = "com.example.wanderlust.NAME";
    public static final String EXTRA_EMAIL = "com.example.wanderlust.EMAIL";
    public static final String EXTRA_ID = "com.example.wanderlust.ID";
    public static final String EXTRA_MESSAGE = "com.example.wanderlust.MESSAGE";
    static final int RC_SIGN_IN = 0;

    private FirebaseFirestore db;
    private GoogleSignInClient mGoogleSignInClient;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().requestFeature(Window.FEATURE_ACTION_BAR);
        getSupportActionBar().hide();
        setContentView(R.layout.activity_login);

        // Configure sign-in to request the user's ID, email address, and basic
// profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        // Build a GoogleSignInClient with the options specified by gso.
        mGoogleSignInClient = GoogleSignIn.getClient(this, gso);

        // Set the dimensions of the sign-in button.
        signInButton = findViewById(R.id.sign_in_button);
        signInButton.setSize(SignInButton.SIZE_STANDARD);

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
                                if(task.getResult().size()>0) {
                                    for (QueryDocumentSnapshot document : task.getResult()) {
                                        if (document.exists()) {
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
                                        }
                                    }
                                }
                                else {
                                    Toast.makeText(parentThis, "User " + email.getText() + " does not exist, please register.", Toast.LENGTH_LONG).show();
                                }

                            } else {
                                Log.d(TAG, "Error getting documents: ", task.getException());

                            }
                        }
                    });
                }
            }
        });

        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                signIn();
            }
            private void signIn() {
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, RC_SIGN_IN);
            }
        });
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInClient.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            // The Task returned from this call is always completed, no need to attach
            // a listener.
            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);
        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        Log.i("TAG??", "111handleSignInResult=handleSignInResult");
        try {
            Log.i("TAG??", "handleSignInResult=handleSignInResult");
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);
            // Signed in successfully, show authenticated UI.
            updateUI(account, "handleSignInResult");
        } catch (ApiException e) {
            // The ApiException status code indicates the detailed failure reason.
            // Please refer to the GoogleSignInStatusCodes class reference for more information.
            Log.w("tag", "signInResult:failed code=" + e.getStatusCode());
            e.printStackTrace();
            updateUI(null, "checkitout");
        }
    }

//
//    @Override
//    protected void onStart()
//    {
//        super.onStart();
//        Toast.makeText(getApplicationContext(),"Now onStart() calls", Toast.LENGTH_LONG).show(); //onStart Called
//        // Check for existing Google Sign In account, if the user is already signed in
//// the GoogleSignInAccount will be non-null.
//        GoogleSignInAccount account = GoogleSignIn.getLastSignedInAccount(this);
//        updateUI(account, "onstart");
////        if(account){
//////
//////        }
//    }

    protected void updateUI(final GoogleSignInAccount account, String methodname){
            Log.i("TAG: method name: ",methodname);
            Log.i("TAG: display name", account.getDisplayName());
            Log.i("TAG: display email", account.getEmail());
            Log.i("TAG: display id", account.getId());


        db.collection("user_table").whereEqualTo("email", account.getEmail()).
                get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if (task.isSuccessful()) {
                    Log.i("TAG: check1", account.getId());
                    if(task.getResult().size()>0){
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            Log.i("TAG: check??", account.getId());
                            if (document.exists()) {
                                Log.i("TAG: check2", account.getId());
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
                            }
                        }
                    }
                    else {
                        Log.i("TAG: check3", account.getId());
                        Toast.makeText(parentThis, "User " + email.getText() + " does not exist, please register.", Toast.LENGTH_LONG).show();
                        final UUID userId = UUID.randomUUID();
                        UserObject userObject = new UserObject(userId.toString(), account.getDisplayName(), account.getEmail(), "");
                        db.collection("user_table").document(userId.toString()).set(userObject).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void aVoid) {

                                Toast.makeText(parentThis, "Welcome, " + account.getDisplayName(), Toast.LENGTH_LONG).show();
                                Intent i = new Intent(getApplicationContext(), MainActivity.class);
                                String message = account.getDisplayName();
                                i.putExtra(EXTRA_ID, userId.toString());
                                i.putExtra(EXTRA_NAME, account.getDisplayName());
                                i.putExtra(EXTRA_EMAIL, account.getEmail());
                                startActivity(i);
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(parentThis, "User " + account.getEmail() + " already exists, please login.", Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                } else {
                    Log.i("TAG: check4", account.getId());
                    Log.d(TAG, "Error getting documents: ", task.getException());

                }
            }
        });
    }
}
