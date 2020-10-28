package com.adrinofast.sa4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;


import com.google.firebase.auth.FirebaseAuth;

public class MainActivity extends AppCompatActivity {
    private FirebaseAuth mAuth;
    Context context = this;
    Button but_signup_cognito;

    Button but_login;
    Button but_skip;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mAuth = FirebaseAuth.getInstance();

        but_signup_cognito = findViewById(R.id.but_signup);
        but_login = findViewById(R.id.but_login_main);
        but_skip = findViewById(R.id.but_skip);

        but_signup_cognito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToSignUp();
            }
        });

        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                redirectToLogin();
            }
        });

        but_skip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                skipLogin();
            }
        });
    }

   private void  redirectToSignUp()
    {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void  redirectToLogin()
    {
        Intent intent = new Intent(context, LoginActivity.class);
        startActivity(intent);
    }

    private void skipLogin()
    {
        Intent intent = new Intent(context, HomeActivity.class);
        startActivity(intent);
    }
}