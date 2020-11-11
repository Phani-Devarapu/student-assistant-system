package com.adrinofast.sa4;

import android.app.ActionBar;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


public class HomeFragment extends Fragment    {

    public static final String TAG = "HomeFragment";

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;

    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    FirebaseUser user;

    List<Program> arryProg;
    SearchView mySearch;
    RecyclerView rvcollges;
  //RecyclerView.Adapter rvAdapter  ;
    ProgramAdapter rvAdapter;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_home, container, false);


    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);

        user = mAuth.getCurrentUser();
        if (user != null) {
                    Log.i(TAG,"User Signed In");
        } else {
            Log.i(TAG,"User Not Signed In");
        }
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ((HomeActivity) getActivity()).getSupportActionBar().setTitle("Home");

        Log.i("inside","The home fragment");

        CollectionReference citiesRef = db.collection("Programs");

        Program p1 = new Program();
        arryProg = new ArrayList<Program>();


        rvcollges = (RecyclerView) view.findViewById(R.id.rvcollegeView);
        rvcollges.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ProgramAdapter(arryProg);
        rvcollges.setAdapter(rvAdapter);

        rvAdapter.setOnItemClickListe(new ProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("item", String.valueOf(position));
                if (user != null){
                    Bundle b = new Bundle();
                    b.putSerializable("ProgramData",arryProg.get(position));
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



       getData();

    }

    private void getData()
    {
        db.collection("Programs")
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                 String proId = document.getId().toString();

                                Program p1 = document.toObject(Program.class);
                                p1.setDocumentid(proId);
                              //  Log.i("the data", p1.toString());
                                arryProg.add(p1);
                          }
                            rvAdapter.notifyDataSetChanged();
                        } else {
                           // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void queryDB(String qpram )
    {

        db.collection("Programs")
                .whereEqualTo("collegeName", qpram)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            arryProg.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String proId = document.getId().toString();
                                // Log.i("the data us " , proId);

                                Program p1 = document.toObject(Program.class);

                                p1.setId(proId);

                                arryProg.add(p1);

                            }
                            rvAdapter.notifyDataSetChanged();
                        }

                        else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        //inflater.inflate(R.menu.top_app_bar, menu);
        MenuItem searchItem = menu.findItem(R.id.searchview);
        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);

        if (searchItem != null) {
            searchView = (SearchView) searchItem.getActionView();
        }
        if (searchView != null) {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            queryTextListener = new SearchView.OnQueryTextListener() {
                @Override
                public boolean onQueryTextChange(String newText) {
                    Log.i("onQueryTextChange", newText);

                    return true;
                }
                @Override
                public boolean onQueryTextSubmit(String query) {
                    Log.i("onQueryTextSubmit", query);
                    queryDB(query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.searchview:
//                // Not implemented here
//                return false;
//            default:
//                break;
//        }
//        searchView.setOnQueryTextListener(queryTextListener);
//        return super.onOptionsItemSelected(item);
//    }



}
