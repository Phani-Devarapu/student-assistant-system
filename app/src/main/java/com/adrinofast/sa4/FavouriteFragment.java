package com.adrinofast.sa4;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FavouriteFragment extends Fragment {
    private FirebaseAuth mAuth;




    EditText text_loginNameedit;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i("i am","here inside the fav fragemnt");
        mAuth = FirebaseAuth.getInstance();

        View vv = inflater.inflate(R.layout.fragment_favourite, container, false);


        return inflater.inflate(R.layout.fragment_favourite, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Wishlist");

        text_loginNameedit = view.findViewById(R.id.login_edit);
        FirebaseUser user = mAuth.getCurrentUser();
        if (user != null) {
            String id  = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.i("the is id ", id);
            text_loginNameedit.setText("user signed in ");
        } else {
            text_loginNameedit.setText("User not signed in Please Sign In ");

        }
 }
}
