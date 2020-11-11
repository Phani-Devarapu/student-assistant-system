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
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;
    Button but_login;
    Button but_forpas;
    Context context = this;

    EditText text_loginName;
    EditText text_loginPass;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);

        text_loginName=findViewById(R.id.login_username);
        text_loginPass = findViewById(R.id.login_password);
        but_login = findViewById(R.id.but_login);
        but_forpas =findViewById(R.id.but_forgotPas);

        mAuth = FirebaseAuth.getInstance();


        but_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                login();
            }
        });

        but_forpas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                forgotpassword();
            }
        });

    }


    private void login()
    {
        String  email = String.valueOf(text_loginName.getText());
        String password = String.valueOf(text_loginPass.getText());
        Log.i("The mail is" , email);
        Log.i("the pass is ", password);

        if(email.length()>5 && password.length()>5)
        {
            mAuth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if (task.isSuccessful()) {

                                FirebaseUser user = mAuth.getCurrentUser();
                                Intent intent = new Intent(context,HomeActivity.class);
                                startActivity(intent);

                            } else {
                                Toast toast = Toast.makeText(getApplicationContext(), "Login Failed", Toast.LENGTH_SHORT);
                                toast.show();
                            }


                        }
                    });
        }
        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Invalid Credentials", Toast.LENGTH_SHORT);
            toast.show();
        }


    }



    private  void forgotpassword()
    {

        Intent intent = new Intent(context, ForgotpasswordActivity.class);
        startActivity(intent);

    }
}