package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class ProgramDescriptioinActivity extends AppCompatActivity {

    public static final String TAG = "ProgramDescriptioinActivity";


    //Firebase declarations
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser user;


    Program pro;
    List<WishlistModel> wishModel;

    public ImageView college_image;
    public ImageButton fav_button;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_descriptioin);

        //initiations
        mAuth = FirebaseAuth.getInstance();
        college_image = findViewById(R.id.produDescriMainImage);
        fav_button = findViewById(R.id.fav_Icon_imageButton);

        user = mAuth.getCurrentUser();
        if (user != null) {
            String id  = FirebaseAuth.getInstance().getCurrentUser().getUid();


        } else {


        }

        //Getting Buldle data
        getBundleData();

        //Binding the data to views
        bindingData2Views();



        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(TAG,"Insid ehe wishlist fucntion");
                wishlisthandler();
            }
        });


    }

    private void getBundleData()
    {
        Intent i = getIntent();
        Bundle data = i.getExtras();
        pro = (Program) data.getSerializable("ProgramData");
    }

    private void bindingData2Views()
    {
        StorageReference storageRef = storage.getReferenceFromUrl(pro.getImageURL());
        storageRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(college_image);

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception exception) {
                // Handle any errors
            }
        });
    }


    public void wishlisthandler()
    {

        String userUniqueId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        WishlistModel userWishList = getUserWishList(userUniqueId);
       // Log.i(TAG,userWishList.toString());


        WishlistModel wishModel = new WishlistModel();

        ArrayList<String> hash_Set_programids = new ArrayList<>();

        hash_Set_programids.add(pro.getDocumentid());

        wishModel.setUserId(FirebaseAuth.getInstance().getCurrentUser().getUid());
        wishModel.setProgramId(hash_Set_programids);

//        db.collection("wishlist").document(userUniqueId)
//                .set(wishModel)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "DocumentSnapshot successfully written!");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        Log.w(TAG, "Error writing document", e);
//                    }
//                });
    }

    private WishlistModel getUserWishList(String userUniId)
    {
      String asd = null ;


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
                        ArrayList<String> currentPrograms = new ArrayList<String>();
                        currentPrograms = (ArrayList<String>) ww1.getProgramId();
                        if(currentPrograms.contains(pro.getDocumentid()))
                        {
                            Toast toast = Toast.makeText(getApplicationContext(), "Item already in the WishList", Toast.LENGTH_SHORT);
                            toast.show();
                        }
                        else
                        {
                            currentPrograms.add(pro.getDocumentid());
                            ww1.setProgramId(currentPrograms);
                            docRef.update("programId",currentPrograms).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    Toast toast = Toast.makeText(getApplicationContext(), "Item Moved to WishList", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            });

                        }



                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });

       // Log.i(TAG,ww1[0].toString());
      //  Log.i(TAG, String.valueOf(wishModel.size()));

//        DocumentReference docRef = db.collection("wishlist").document(userUniId);
//        docRef.get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
//            @Override
//            public void onSuccess(DocumentSnapshot documentSnapshot) {
//
//                WishlistModel ww1 = new WishlistModel();
//                ww1 = documentSnapshot.toObject(WishlistModel.class);
//                //asd= ww1.getUserId();
//                Log.i(TAG, ww1.toString());
//
//            }
//        });
//
//        Log.i(TAG,asd);


        return null;

    }

}