package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class SignUpActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button but_signup_cognito;
    Context context = this;

    EditText text_signupPass;
    EditText text_signupEmail;
    EditText text_siggnupConfirmPassword;

    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);
        FirebaseApp.initializeApp(this);

        mAuth = FirebaseAuth.getInstance();

        but_signup_cognito = findViewById(R.id.but_signup);


        text_signupPass = findViewById(R.id.signup_password);
        text_signupEmail = findViewById(R.id.signup_email);
        text_siggnupConfirmPassword = findViewById(R.id.signup_confirm_password);

        but_signup_cognito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email = String.valueOf(text_signupEmail.getText());
                String password = String.valueOf(text_signupPass.getText());
                String confirmPassword = String.valueOf(text_siggnupConfirmPassword.getText());

                signUp(email,password,confirmPassword);
            }
        });


    }

    public void signUp(String email,String password,String confirmPassword)
    {


        if(password.contentEquals(confirmPassword))
        {
            if(email.matches(emailPattern))
            {
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    //Log.i(TAG, "createUserWithEmail:success");
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    user.sendEmailVerification()
                                            .addOnCompleteListener(new OnCompleteListener<Void>() {
                                                @Override
                                                public void onComplete(@NonNull Task<Void> task) {
                                                    if (task.isSuccessful()) {
                                                        Log.i("SignUp Activity", "Email sent.");
                                                    }
                                                    else
                                                    {
                                                        Log.i("SignUp Activity", task.getException().toString());
                                                    }
                                                }
                                            });

                                    Intent intent = new Intent(context,UserIntrestActivity.class);
                                    startActivity(intent);

                                    //  updateUI(user);
                                } else {
                                    // If sign in fails, display a message to the user.
                                    //Log.i(TAG, "createUserWithEmail:failure", task.getException());
                                    Toast.makeText(SignUpActivity.this, "Authentication failed.",
                                            Toast.LENGTH_SHORT).show();
                                    //updateUI(null);
                                }

                                // ...
                            }
                        });
            }

            else
            {
                Toast toast = Toast.makeText(getApplicationContext(), "Invalid Email", Toast.LENGTH_SHORT);
                toast.show();
            }
      }

        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Password Mismatch", Toast.LENGTH_SHORT);
            toast.show();

        }

    }

    public void sendverificationMail(FirebaseUser user) {

        user.sendEmailVerification()
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.i("SignUp Activity", "Email sent.");
                        }
                        else
                        {
                            Log.i("SignUp Activity", task.getException().toString());
                        }
                    }
                });
    }
}