package com.adrinofast.sa4;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.auth.User;

import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArrayList;

public class UserIntrestActivity extends AppCompatActivity {

    Context context= this;

    private FirebaseAuth mAuth;
    FirebaseFirestore db;
    FirebaseUser user;

    CheckBox int_1;
    CheckBox int_2;
    CheckBox int_3;
    CheckBox int_4;
    CheckBox int_5;
    CheckBox int_6;

    Button submit_button;
    Button skip_button;
    ArrayList<String>  userIntrest = new ArrayList<>();
    ProgressLoader proload;

    public static final String TAG = "UserIntrestActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_intrest);
        mAuth = FirebaseAuth.getInstance();
         db = FirebaseFirestore.getInstance();

        proload = new ProgressLoader(UserIntrestActivity.this);

        user = mAuth.getCurrentUser();
        if (user != null) {
            Log.i(TAG,"User Signed In");
        } else {
            Log.i(TAG,"User Not Signed In");
        }

        int_1 = findViewById(R.id.user_intrest_one);
        int_2= findViewById(R.id.user_intrest_two);
        int_3= findViewById(R.id.user_intrest_three);
        int_4= findViewById(R.id.user_intrest_four);
        int_5=findViewById(R.id.user_intrest_five);
        int_6=findViewById(R.id.user_intrest_six);
        submit_button=findViewById(R.id.Update_intrest_button);
        skip_button=findViewById(R.id.unser_int_skip_button);

        int_1.setText("Computer Engineering");
        int_2.setText("Electrical Engineering");
        int_3.setText("Mechanical Engineering");
        int_4.setText("BioEngineering");
        int_5.setText("Management");
        int_6.setText("Others");

        submit_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proload.StartProgressLoader();
                submitIntrestes();
            }
        });

        skip_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proload.stopProgresBar();
                gotoHomeActivity();
            }
        });

    }

    private void gotoHomeActivity() {
        Intent intent = new Intent(context, HomeActivity.class);
        proload.stopProgresBar();
        startActivity(intent);
    }

    //add the user to intrset group, which is planned to used for notifications.
    private void submitIntrestes() {

        if(int_1.isChecked())
        {
            userIntrest.add("Computer Engineering");
            addIntrests("Nchrra0Ly0BD0T52jvfQ");
            
        }

        if(int_2.isChecked())
        {
            userIntrest.add("Electrical Engineering");
            addIntrests("p0dRkr3xbwcqVpfP27Eb");
        }
        if(int_3.isChecked())
        {
            userIntrest.add("Mechanical Engineering");
            addIntrests("nUPYIVmZ5EKiARrSy0T8");
        }
        if(int_4.isChecked())
        {
            userIntrest.add("BioEngineering");
            addIntrests("793kpwMLzDRA2fcilUTd");
        }
        if(int_5.isChecked())
        {
            userIntrest.add("Management");
            addIntrests("cmZiKoWyrLABlEshAbRJ");
        }
        if(int_6.isChecked())
        {
            userIntrest.add("Others");
            addIntrests("G0ads4aQZvMxfpss5ZM2");
        }
        gotoHomeActivity();

    }

    private void addIntrests(String docid) {
        String userId = user.getUid();
        DocumentReference washingtonRef = db.collection("userInrests").document(docid);
        washingtonRef.update("userID", FieldValue.arrayUnion(userId));
    }
}


