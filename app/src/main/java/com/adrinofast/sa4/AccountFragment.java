package com.adrinofast.sa4;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import org.w3c.dom.Text;

public class AccountFragment extends Fragment {

    Button btn_signout;
    private FirebaseAuth mAuth;
    EditText text_loginChecker;
    TextView accountFirstName;
    TextView accountLastName;
    TextView accountEmail;
    TextView accountPhone;
    Button loginButton_account_fragment;
    TextView  userMessage;
    MaterialCardView matcardAccount;
    FloatingActionButton fab;


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

        matcardAccount = view.findViewById(R.id.con_lay_eachitemAccountFrag);

        accountFirstName = view.findViewById(R.id.account_FirstName);
        accountLastName = view.findViewById(R.id.account_LastName);
        accountEmail=view.findViewById(R.id.account_Email);
        accountPhone=view.findViewById(R.id.account_Phone);
        userMessage= view.findViewById(R.id.TVUserMessageFragment_Account);
        userMessage.setVisibility(View.GONE);
        loginButton_account_fragment= view.findViewById(R.id.but_login_Fragment_Account);
        loginButton_account_fragment.setVisibility(View.GONE);
        fab = view.findViewById(R.id.floatingActionButton_acnt_frag);



        btn_signout = view.findViewById(R.id.but_signout);

            FirebaseUser user = mAuth.getCurrentUser();
            if (user != null) {
                accountFirstName.setText(user.getDisplayName());
                accountPhone.setText(user.getPhoneNumber());
                accountEmail.setText(user.getEmail());
                accountLastName.setText(user.getProviderId());

            } else {

            userMessage.setText("Please Sign In");
            userMessage.setVisibility(View.VISIBLE);
            loginButton_account_fragment.setVisibility(View.VISIBLE);
            matcardAccount.setVisibility(View.GONE);
            btn_signout.setVisibility(View.GONE);

        }





        btn_signout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                signOutuser();
            }
        });

        loginButton_account_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LoginScreen();
            }
        });

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToRedirectActivity();

            }
        });
    }

    private void goToRedirectActivity() {
        Intent intent = new Intent(getActivity(), acount_updateActivity.class);

        startActivity(intent);
    }

    private void LoginScreen() {
        Intent intent = new Intent(getActivity(), LoginActivity.class);
        startActivity(intent);

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

