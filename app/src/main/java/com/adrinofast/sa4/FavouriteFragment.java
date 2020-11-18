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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;

public class FavouriteFragment extends Fragment {

    public static final String TAG = "FavouriteFragment";

   //Firebase Declarations
    private FirebaseAuth mAuth;
    FirebaseUser user;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    List<Program> arryProgWish;
    RecyclerView rvcollges;
    WishListAdapter rvAdapter;
    String userID;
    ArrayList<String> userWishList;

    Button loginButton_fav_fragment;
    TextView userMessageFF;

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


        userMessageFF= view.findViewById(R.id.TVUserMessageFragment_Favourite);
        userMessageFF.setVisibility(View.GONE);
        loginButton_fav_fragment= view.findViewById(R.id.but_login_Fragment_Favourite);
        loginButton_fav_fragment.setVisibility(View.GONE);


        arryProgWish = new ArrayList<Program>();
        rvcollges = (RecyclerView) view.findViewById(R.id.rvwishList);
        rvcollges.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new WishListAdapter(arryProgWish);
        rvcollges.setAdapter(rvAdapter);


        user = mAuth.getCurrentUser();
        if (user != null) {
            userID = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.i("the is id ", userID);
            getWishListData();

        } else {
            userMessageFF.setText("Please Sign In");
            userMessageFF.setVisibility(View.VISIBLE);
            loginButton_fav_fragment.setVisibility(View.VISIBLE);

        }

        loginButton_fav_fragment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), LoginActivity.class);
                startActivity(intent);
            }
        });


        //Handling click event of each item in the adapter
        rvAdapter.setOnItemClickListe(new WishListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("item", String.valueOf(position));
                if (user != null){
                    Bundle b = new Bundle();
                    b.putSerializable("ProgramData",arryProgWish.get(position));
                    b.putSerializable("wishListItems",userWishList);
                    Intent intent = new Intent(getActivity(), ProgramDescriptioinActivity.class);
                    intent.putExtras(b);
                    startActivity(intent);
                }
                else
                {
                    Toast toast = Toast.makeText(getActivity(), "Please SignIn, to View Program details", Toast.LENGTH_SHORT);
                    toast.show();
                }

            }
        });

    }

    //Getting the user wishlist data from the firebase.
 private void getWishListData()
 {
     String userUniId = FirebaseAuth.getInstance().getCurrentUser().getUid();
     final DocumentReference docRef = db.collection("wishlist").document(userUniId);

     docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
         @Override
         public void onComplete(@NonNull Task<DocumentSnapshot> task) {
             if (task.isSuccessful()) {
                 DocumentSnapshot document = task.getResult();
                 if (document.exists()) {
                     Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                     WishlistModel ww1 = new WishlistModel();
                     ww1 = document.toObject(WishlistModel.class);
                     Log.i(TAG,ww1.toString());
                     getProgramDetails(ww1);

                 } else {
                     Log.d(TAG, "No such document");
                 }
             } else {
                 Log.d(TAG, "get failed with ", task.getException());
             }
         }
     });
 }

//For each item in teh wishlist, getting the complete details of the program
 private void  getProgramDetails(WishlistModel ww1)
 {  userWishList= (ArrayList<String>) ww1.getProgramId();


     final CollectionReference docRefPrograms = db.collection("Programs");

     for(String str: ww1.getProgramId())
     {
         Log.i(TAG,str);

         docRefPrograms.document(str).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
             @Override
             public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                 if(task.isSuccessful())
                 {
                     DocumentSnapshot document = task.getResult();
                     if (document.exists()) {
                         String docid = document.getId();

                         Program p1 = document.toObject(Program.class);
                         p1.setDocumentid(docid);
                         Log.i(TAG,p1.toString());
                         arryProgWish.add(p1);

                     }
                     rvAdapter.notifyDataSetChanged();
                 }
             }
         });
     }
 }

    //Another way to get the wishlist details, but not using as of now.
    private ArrayList<String> getUserWishListFavouriteFragment(String userUniId)
    {
        String asd = null ;
        final ArrayList<String> FFcurrentPrograms = new ArrayList<String>();

        final DocumentReference docRef = db.collection("wishlist").document(userUniId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        WishlistModel ww1 = new WishlistModel();
                        ww1 = document.toObject(WishlistModel.class);
                        ArrayList<String> cps = new ArrayList<String>();
                        for(int i=0;i< ww1.getProgramId().size();i++)
                        {
                            FFcurrentPrograms.add(ww1.getProgramId().get(i));
                        }

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


        Log.i(TAG, String.valueOf(FFcurrentPrograms.size()));
        return FFcurrentPrograms;

    }


}
