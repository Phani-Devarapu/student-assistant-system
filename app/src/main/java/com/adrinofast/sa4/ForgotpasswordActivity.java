package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotpasswordActivity extends AppCompatActivity {

    private FirebaseAuth mAuth;

    Button but_sendmail;
    EditText text_forgotmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        mAuth = FirebaseAuth.getInstance();
        but_sendmail = findViewById(R.id.but_forgotpassword_sendmail);
        text_forgotmail = findViewById(R.id.forgot_Password_email_input);

        Toast toast = Toast.makeText(getApplicationContext(), "Type Your Email - We will send you a link for Password reset", Toast.LENGTH_LONG);
        toast.show();

        but_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }


    private void sendMail()
    {
       String emailAddress = String.valueOf(text_forgotmail.getText());

        if(emailAddress.length()>8)
        {
            mAuth.sendPasswordResetEmail(emailAddress)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.i("mesg", "Email sent.");
                                Toast toast = Toast.makeText(getApplicationContext(), "Email Send - Please check your Inbox", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                            else
                            {
                                Toast toast = Toast.makeText(getApplicationContext(), "Email not send : Error Occurred", Toast.LENGTH_SHORT);
                                toast.show();
                            }
                        }
                    });

        }

        else
        {
            Toast toast = Toast.makeText(getApplicationContext(), "Email Not Valid", Toast.LENGTH_SHORT);
            toast.show();

        }


    }
}