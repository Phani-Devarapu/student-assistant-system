package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.messaging.FirebaseMessaging;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = "MainActivity";

    //firebase auth instance
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //declaring variables
    Context context = this;
    Button but_signup_cognito;
    ProgressLoader proload;
    Button but_login;
    Button but_skip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();
        proload = new ProgressLoader(MainActivity.this);

        user = mAuth.getCurrentUser();
        if (user != null) {
            proload.StartProgressLoader();
            skipLogin();
            Log.i(TAG,"User Signed In");
        } else {
            Log.i(TAG,"User Not Signed In");
        }

        but_signup_cognito = findViewById(R.id.but_signup);
        but_login = findViewById(R.id.but_login_main);
        but_skip = findViewById(R.id.but_skip);

        but_signup_cognito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proload.StartProgressLoader();
                redirectToSignUp();
            }
        });

        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                proload.StartProgressLoader();
                redirectToLogin();
            }
        });

        but_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                proload.StartProgressLoader();
//                Handler handler = new Handler();
//                handler.postDelayed(new Runnable() {
//                    @Override
//                    public void run() {
//                        proload.stopProgresBar();
//
//                    }
//                },3000);
                skipLogin();
            }
        });

    }

//this function will redirect the user to SignUpActivity
    private void  redirectToSignUp()
    {
        Intent intent = new Intent(context, SignUpActivity.class);
        proload.stopProgresBar();
        startActivity(intent);
    }

    //this function will redirect the usre to LoginActivity
    private void  redirectToLogin()
    {
        Intent intent = new Intent(context, LoginActivity.class);
        proload.stopProgresBar();
        startActivity(intent);
    }

    //this function will redirect the usre to HomeActivity, skipping the login

    private void skipLogin()
    {
        proload.StartProgressLoader();  //progress loader start
        Intent intent = new Intent(context, HomeActivity.class);
        proload.stopProgresBar();  //progress loader stop
        startActivity(intent);
    }
}