package com.adrinofast.sa4;

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

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import androidx.appcompat.widget.SearchView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;


public class HomeFragment extends Fragment    {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
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
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        CollectionReference citiesRef = db.collection("Programs");

        Program p1 = new Program();
        arryProg = new ArrayList<Program>();
        p1.setCollegeName("asas");
        p1.setFaculty("Asa");
        p1.setDepartment("plplpl");
        arryProg.add(p1);

        rvcollges = (RecyclerView) view.findViewById(R.id.rvcollegeView);
        rvcollges.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ProgramAdapter(arryProg);
        rvcollges.setAdapter(rvAdapter);

        rvAdapter.setOnItemClickListe(new ProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("item", String.valueOf(position));
                Intent intent = new Intent(getActivity(), TestActivity.class);
                startActivity(intent);

            }
        });



        mySearch = (SearchView) view.findViewById(R.id.searchview);
        mySearch.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Log.i("text submitted", query);
                queryDB(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                Log.i("text changed", newText);
                //queryDB(newText);
                return false;
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

                                //arryProg.add(document.toObject(Program.class));
                                Log.i("the id is " , document.getId());
                                Program p1 = document.toObject(Program.class);
                                arryProg.add(p1);

                                Log.i("the data us " , p1.toString());


                            }
                            rvAdapter.notifyDataSetChanged();
                        } else {
                           // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }


    private void queryDB(String qparam)
    {
        Log.i("Insid ethe querymeh", qparam);
        db.collection("Programs")
                .whereEqualTo("collegeName", qparam)
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            Log.i("sucessful","thw data is ");
                            arryProg.clear();
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                //Log.d(TAG, document.getId() + " => " + document.getData());
                                Log.i("the id is " , document.getData().toString());
                                Program p1 = document.toObject(Program.class);
                                Log.i("progrma",p1.toString());
                                arryProg.add(p1);

                                Log.i("the data us " , p1.toString());

                            }
                            rvAdapter.notifyDataSetChanged();
                        }

                        else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });
    }



}
