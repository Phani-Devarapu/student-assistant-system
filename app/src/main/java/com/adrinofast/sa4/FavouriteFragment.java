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

         user = mAuth.getCurrentUser();
        if (user != null) {
            String id  = FirebaseAuth.getInstance().getCurrentUser().getUid();
            Log.i("the is id ", id);

        } else {


        }

        arryProgWish = new ArrayList<Program>();
        rvcollges = (RecyclerView) view.findViewById(R.id.rvwishList);
        rvcollges.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new WishListAdapter(arryProgWish);
        rvcollges.setAdapter(rvAdapter);

        getWishListData();
 }

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


 private void  getProgramDetails(WishlistModel ww1)
 {
     Log.i("inside the program getter", ww1.toString());

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
                         Program p1 = document.toObject(Program.class);
                         Log.i(TAG,p1.toString());
                         arryProgWish.add(p1);

                     }
                     rvAdapter.notifyDataSetChanged();
                 }
             }
         });
     }
 }
}
