package com.adrinofast.sa4;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class acount_updateActivity extends AppCompatActivity {

    public static final String TAG = "acount_updateActivity";

    private FirebaseAuth mAuth;
    Context context = this;
    EditText accountFirstNameUA;
    EditText accountLastNameUA;
    EditText accountEmailUA;
    EditText accountPhoneUA;
    Button updateUserDeatils;
    Button updateUserIntrestsDeatils;
    String emailPattern = "[a-zA-Z0-9._-]+@[a-z]+\\.+[a-z]+";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_acount_update);
        accountFirstNameUA = findViewById(R.id.account_FirstNameUA);

        accountEmailUA=findViewById(R.id.account_EmailUA);
        updateUserIntrestsDeatils=findViewById(R.id.but_update_intrestsUA);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            accountFirstNameUA.setText(user.getDisplayName());
            accountEmailUA.setText(user.getEmail());

        }

        updateUserDeatils= findViewById(R.id.but_updateUA);

        updateUserDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                updateDetails();
            }
        });
        updateUserIntrestsDeatils.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UserIntrestActivity.class);
                startActivity(intent);
            }
        });


    }

    private void updateDetails() {

        String name = accountFirstNameUA.getText().toString();
        String emailUpdated= accountEmailUA.getText().toString();


        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                .setDisplayName(name)
                .build();

        user.updateProfile(profileUpdates)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {
                            Log.d(TAG, "User Display Name updated.");
                        }
                    }
                });

        if(emailUpdated.matches(emailPattern))
        {
            user.updateEmail("user@example.com")
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "User email address updated.");
                            }
                        }
                    });

            user.sendEmailVerification()
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Log.d(TAG, "Email sent.");
                            }
                        }
                    });
        }


    }

    private void sendback()
    {
        Toast toast = Toast.makeText(getApplicationContext(), "Details Updated", Toast.LENGTH_SHORT);
        toast.show();
        Intent intent = new Intent(context  , HomeActivity.class);
        startActivity(intent);
    }
}