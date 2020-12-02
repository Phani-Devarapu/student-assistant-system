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

    public static final String TAG = "ForgotpasswordActivity";

    //declaring the firebase auth instance
    private FirebaseAuth mAuth;

    //declaring variables
    Button but_sendmail;
    EditText text_forgotmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgotpassword);


        //intializing the firebase auth instance
        mAuth = FirebaseAuth.getInstance();

        //binding varibles with the view elements
        but_sendmail = findViewById(R.id.but_forgotpassword_sendmail);
        text_forgotmail = findViewById(R.id.forgot_Password_email_input);

        Toast toast = Toast.makeText(getApplicationContext(), "Type Your Email - We will send you a link for Password reset", Toast.LENGTH_LONG);
        toast.show();

        //send mail handler
        but_sendmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                sendMail();
            }
        });

    }

    //Upon providing the user mail, an  e-mail will be sent from firebase to reset the password
    private void sendMail()
    {
       String emailAddress = String.valueOf(text_forgotmail.getText());

        if(emailAddress.length()>8) //checking email validation, with minimum characters length
        {
            mAuth.sendPasswordResetEmail(emailAddress)  //from the auth instance invoking setresetpassword method.
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