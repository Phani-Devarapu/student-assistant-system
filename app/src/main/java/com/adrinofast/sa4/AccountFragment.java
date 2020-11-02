package com.adrinofast.sa4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class AccountFragment extends Fragment {

    Button btn_signout;
    private FirebaseAuth mAuth;
    EditText text_loginChecker;



    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_account, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Account");

        text_loginChecker = view.findViewById(R.id.login_checker);
        btn_signout = view.findViewById(R.id.but_signout);

        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String id  = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.i("the is id ", id);
            text_loginChecker.setText("user signed in ");
        } else {

            text_loginChecker.setText("User not signed in Please Sign In ");
            btn_signout.setVisibility(View.GONE);

        }





        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOutuser();
            }
        });
    }

   private void signOutuser()
    {
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null){
            mAuth.signOut();
            Toast toast = Toast.makeText(getActivity(), "User Signed Out", Toast.LENGTH_SHORT);
            toast.show();
            Intent intent = new Intent(getActivity(), MainActivity.class);
            startActivity(intent);

        }else{

        }
    }
}
