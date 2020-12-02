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

import com.algolia.search.saas.AlgoliaException;
import com.algolia.search.saas.Client;
import com.algolia.search.saas.CompletionHandler;
import com.algolia.search.saas.Index;
import com.algolia.search.saas.Query;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.StorageReference;
import com.google.gson.Gson;

import java.io.DataInput;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.function.LongFunction;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;


public class HomeFragment extends Fragment    {

    public static final String TAG = "HomeFragment";

    //firebase auth, db and user declarations
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private FirebaseAuth mAuth;
    FirebaseUser user;

    //variable declarations
    private SearchView searchView = null;
    private SearchView.OnQueryTextListener queryTextListener;
    List<Program> arryProg;
    SearchView mySearch;
    RecyclerView rvcollges;
    ArrayList<String> userWishListIds;
    ProgramAdapter rvAdapter;
    ProgressLoader proload;

    //AlgoliaKeys declarations
    private String clientId;
    private String apikey;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        return inflater.inflate(R.layout.fragment_home, container, false); //inflating the view

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //firebase auth intializations
        mAuth = FirebaseAuth.getInstance();
        setHasOptionsMenu(true);

        proload = new ProgressLoader(getActivity()); //progress loader
        proload.StartProgressLoader();

        //Checking the user signedin or not
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

    //getting the reference for the programs and getting the results
        CollectionReference citiesRef = db.collection("Programs");

        Program p1 = new Program();
        arryProg = new ArrayList<Program>();

       //Intializing the adapter of programs
        rvcollges = (RecyclerView) view.findViewById(R.id.rvcollegeView);
        rvcollges.setLayoutManager(new LinearLayoutManager(view.getContext()));
        rvAdapter = new ProgramAdapter(arryProg);
        rvcollges.setAdapter(rvAdapter);

        if(user!=null)
        {
            userWishListIds =getUserWishListHomeFragment(user.getUid());   //getting the user wishlist items
        }

        //Handling on click of each program from HomeFragment
        rvAdapter.setOnItemClickListe(new ProgramAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Log.i("item", String.valueOf(position));  //Up on clicking the each program, the program data is bundled and send into the next activity.
                if (user != null){
                    Bundle b = new Bundle();
                    b.putSerializable("ProgramData",arryProg.get(position));
                    b.putSerializable("wishListItems",userWishListIds);
                    Intent intent = new Intent(getActivity(), ProgramDescriptioinActivity.class);  //navigating to next activity
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



       getData();  //Method which retrives all the programs from the Firebase
       getAlgoliaConfig();  //Getting keys of Algolia to implement the search functionlity.

    }


    //the algolia keys are stored in the db with collection name algoliasyek.
    private void getAlgoliaConfig() {

        //getting the document reference and extracting details
        final DocumentReference docRef = db.collection("AlgoliaSYEK").document("M5wmDvN0nfQlXVJ7YENS");
        docRef.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        Log.d(TAG, "DocumentSnapshot data: " + document.getData());
                        clientId= (String) document.getData().get("clientID");  //client id and keys of algolia api.
                        apikey= (String) document.getData().get("apikey");
                    } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


    }

    private void getData()
    {
        //This function will get the all the deatils in the program collection and inflate the recycle adapter.
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
                            proload.stopProgresBar();
                            rvAdapter.notifyDataSetChanged();  // updating the recyler adapter with the new data.

                        } else {
                           // Log.w(TAG, "Error getting documents.", task.getException());
                        }
                    }
                });
    }



    //This method will send search request to algolia with keys and update the adapter with the response.
    private void queryDB(String qpram )
    {
        Client client = new Client(clientId, apikey);  //building the algolia client with clientid and key
        Index index = client.getIndex("programs");  //getting refernce of index in algolia
        Query query = new Query(qpram)    //preparing the query configurations, setting limit of 50.
                .setHitsPerPage(50);
        index.searchAsync(query, new CompletionHandler() {
            @Override
            public void requestCompleted(JSONObject content, AlgoliaException error) {
                try {
                    JSONArray asd = content.getJSONArray("hits"); //parsing the response and assigning to local values.
                   // arryProg.clear();
                    List<String> li = new ArrayList<>();
                    Program p1 = new Program();
                    arryProg.clear();
                    for(int i=0;i<asd.length();i++)
                    {
                        JSONObject obj = asd.getJSONObject(i);
                        Gson gson= new Gson();
                        p1.setCollegeName(obj.getString("collegeName"));
                        p1.setTypeCollegeUni(obj.getString("typeCollegeUni"));
                        p1.setDocumentid(obj.getString("objectID"));
                        p1.setDepartment(obj.getString("department"));
                       // p1.setDuration(obj.getString("duration"));
                        p1.setEngineering(obj.getString("engineering"));
                        //p1.setFaculty(obj.getString("faculty"));
                        p1.setLevel(obj.getString("level"));
                        //p1.setPossibleCareer(obj.getString("possibleCareer"));
                        p1.setStartTerm(obj.getString("startTerm"));
                        //p1.setPrimaryCampus(obj.getString("primaryCampus"));
                        p1.setImageURL(obj.getString("imageURL"));
                        arryProg.add(p1);

                    }
                    rvAdapter.notifyDataSetChanged();
                } catch (JSONException e) {


                    e.printStackTrace();
                }


            }
        });
//
 //       this will search in the firebase firestore database
//        db.collection("Programs")
//                .whereEqualTo("collegeName", qpram)
//                .get()
//                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
//                    @Override
//                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
//                        if (task.isSuccessful()) {
//
//                            arryProg.clear();
//                            for (QueryDocumentSnapshot document : task.getResult()) {
//
//                                String proId = document.getId().toString();
//                                // Log.i("the data us " , proId);
//
//                                Program p1 = document.toObject(Program.class);
//
//                                p1.setId(proId);
//
//                                arryProg.add(p1);
//
//                            }
//                            rvAdapter.notifyDataSetChanged();
//                        }
//
//                        else {
//                            //Log.d(TAG, "Error getting documents: ", task.getException());
//                        }
//                    }
//                });
    }


    //Method which wil render the top app bar with menu
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
                    Log.i("onQueryTextSubmit", query);   //if new query is submitted, again the data will load from algolia.
                    queryDB(query);

                    return true;
                }
            };
            searchView.setOnQueryTextListener(queryTextListener);
        }
        super.onCreateOptionsMenu(menu, inflater);


        searchView.setOnCloseListener(new SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                onBackPressed();
                return false;
            }
        });
    }


    public void onBackPressed() {
        if (!searchView.isIconified()) {
            getData();
        } else {

        }
    }

//This metbod will retrive the user wishlist, if the user is logged in.
    private ArrayList<String> getUserWishListHomeFragment(String userUniId)
    {
        String asd = null ;
         final ArrayList<String> currentPrograms = new ArrayList<String>();

         //getting document referecne for wishlist, based on the userid the wishlist will be retrived
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
                            currentPrograms.add(ww1.getProgramId().get(i));
                        }
                 } else {
                        Log.d(TAG, "No such document");
                    }
                } else {
                    Log.d(TAG, "get failed with ", task.getException());
                }
            }
        });


       Log.i(TAG, String.valueOf(currentPrograms.size()));
        return currentPrograms;

    }

}
