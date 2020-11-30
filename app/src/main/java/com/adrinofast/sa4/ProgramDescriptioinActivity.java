package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
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

    Context context= this;
    ProgressLoader proload;

    public static final String TAG = "ProgramDescriptioinActivity";


    //Firebase declarations
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    FirebaseStorage storage = FirebaseStorage.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser user;


    Program pro;
    List<WishlistModel> wishModel;
    ArrayList<String> userWishList;
    String userUniID;
    public ImageView college_image;
    public Button fav_button;
    public Button fav_remove_button;
    public TextView programName;
    public TextView collegeName;
    public TextView programLevel;
    public TextView departmentName;
    public TextView facultyName;
    public TextView degreeName;
    public TextView durationTime;
    public TextView startEndTermTime;
    public TextView moreInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_program_descriptioin);

        proload = new ProgressLoader(ProgramDescriptioinActivity.this);

        //initiations
        mAuth = FirebaseAuth.getInstance();
        college_image = findViewById(R.id.produDescriMainImage);
        fav_button = findViewById(R.id.add2WishListButton);
        fav_remove_button=findViewById(R.id.removeFromWishListButton);
        programName = findViewById(R.id.program_des_ProgramName);
        collegeName = findViewById(R.id.program_des_CollegeName);
        programLevel= findViewById(R.id.program_des_level);
        departmentName = findViewById(R.id.pro_des_departmentName);
        facultyName= findViewById(R.id.pro_des_facultyName);
        degreeName = findViewById(R.id.pro_des_degreeName);
        durationTime = findViewById(R.id.pro_des_durationTime);
        startEndTermTime= findViewById(R.id.pro_des_startTermDuration);
        moreInfo= findViewById(R.id.pro_des_moreInfoData);



        user = mAuth.getCurrentUser();
        if (user != null) {
            userUniID  = FirebaseAuth.getInstance().getCurrentUser().getUid();


        } else {


        }

        //Getting Buldle data
        getBundleData();

        //Binding the data to views
        bindingData2Views();



        fav_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                proload.StartProgressLoader();
                Log.i(TAG,"Insid ehe wishlist fucntion");
                wishlisthandler();
            }
        });


        fav_remove_button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeWishlisthandler();
            }
        });


    }

    private void getBundleData()
    {
        Intent i = getIntent();
        Bundle data = i.getExtras();
        pro = (Program) data.getSerializable("ProgramData");
        userWishList = (ArrayList<String>) data.getSerializable("wishListItems");

    }

    private void bindingData2Views()
    {
        Log.i(TAG,"Inside the pro des activty");
        Log.i(TAG,pro.toString());
        for(String st:userWishList)
        {
            Log.i(TAG,st);
        }

       if(userWishList.contains(pro.getDocumentid()))
       {
           fav_button.setEnabled(false);
       }
       else
       {
           fav_remove_button.setEnabled(false);
       }
        programName.setText(pro.getEngineering());
        collegeName.setText(pro.getCollegeName() + " "+ pro.getTypeCollegeUni());
        programLevel.setText(pro.getLevel());
        departmentName.setText(pro.getDepartment());
        facultyName.setText(pro.getFaculty());
        degreeName.setText("Bachelor of Engineering (BEng)");
        durationTime.setText("4-5 Years");
        startEndTermTime.setText(pro.getStartTerm());
        if(pro.getDescription()!=null)
        {
            moreInfo.setText(pro.getDescription());
        }

        if(pro.getPossibleCareer()!=null)
        {
            moreInfo.setText(pro.getPossibleCareer());
        }

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
                                    fav_button.setEnabled(false);
                                    fav_remove_button.setEnabled(true);
                                    //fav_button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wishlist_button_red_bg));
                                    Toast toast = Toast.makeText(getApplicationContext(), "Item Moved to WishList", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            });

                        }

                    } else {
                        Log.d(TAG, "No such document");
                        createNewDocument();
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
                proload.stopProgresBar();
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


    //This method created new document, if user is storing his/her first wishlist item
    private void createNewDocument() {
        WishlistModel ww1 = new WishlistModel();
        ww1.setUserId(userUniID);
        ArrayList<String> firstWishListId = new ArrayList<>();
        firstWishListId.add(pro.getDocumentid());
        ww1.setProgramId(firstWishListId);

        db.collection("wishlist").document(userUniID).set(ww1).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                fav_button.setEnabled(false);
                fav_remove_button.setEnabled(true);
                Toast toast = Toast.makeText(getApplicationContext(), "Item Added to WishList", Toast.LENGTH_SHORT);
                toast.show();

            }
        });
    }

    private void removeWishlisthandler()
    {
        proload.StartProgressLoader();
        String userUniqueId = FirebaseAuth.getInstance().getCurrentUser().getUid();
        final DocumentReference docRef = db.collection("wishlist").document(userUniqueId);
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        WishlistModel revWw1 = new WishlistModel();
                        revWw1 = document.toObject(WishlistModel.class);
                        ArrayList<String> currentPrograms = new ArrayList<String>();
                        currentPrograms = (ArrayList<String>) revWw1.getProgramId();
                        if(currentPrograms.contains(pro.getDocumentid()))
                        {
                           currentPrograms.remove(pro.getDocumentid());
                           revWw1.setProgramId(currentPrograms);
                            docRef.update("programId",currentPrograms).addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void aVoid) {
                                    fav_button.setEnabled(true);
                                    fav_remove_button.setEnabled(false);
                                    //fav_button.setBackground(ContextCompat.getDrawable(context, R.drawable.ic_wishlist_button_red_bg));
                                    Toast toast = Toast.makeText(getApplicationContext(), "Item Removed From WishList", Toast.LENGTH_SHORT);
                                    toast.show();

                                }
                            });
                        }
                        proload.stopProgresBar();

                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });
    }

}