package com.adrinofast.sa4;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;



import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class HomeActivity extends AppCompatActivity implements SearchView.OnQueryTextListener{
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        //setSupportActionBar(findViewById(R.id.topAppBar));

        mAuth = FirebaseAuth.getInstance();
              Toolbar actionBar = (Toolbar)findViewById(R.id.topAppBar);
        setSupportActionBar(actionBar);
        getSupportActionBar().setTitle("Home");

        BottomNavigationView bottomNav = findViewById(R.id.bottom_navigation);
        bottomNav.setOnNavigationItemSelectedListener(navListener);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                    new HomeFragment()).commit();
        }

    }

    //Bottom navigation bar handler
    private BottomNavigationView.OnNavigationItemSelectedListener navListener =
            new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    Fragment selectedFragment = null;
                    switch (item.getItemId()) {
                        case R.id.bot_nav_home:
                            selectedFragment = new HomeFragment();
                            break;
                        case R.id.bot_nav_fav:
                            Log.i("i am","item cliekced");
                            selectedFragment = new FavouriteFragment();
                            break;
                        case R.id.bot_nav_prof:
                            selectedFragment = new AccountFragment();
                            break;
                    }
                    getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                            selectedFragment).commit();
                    return true;
                }
            };


    public boolean onCreateOptionsMenu(Menu menu)
    {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.top_app_bar,menu);

        return  true;
    }

    @Override
    public boolean onQueryTextSubmit(String query) {
        Log.i("phani", query);
        return true;
    }

    @Override
    public boolean onQueryTextChange(String newText) {
        Log.i("phani", newText);
        return true;
    }

}