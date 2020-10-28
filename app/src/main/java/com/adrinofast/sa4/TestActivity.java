package com.adrinofast.sa4;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.core.view.MenuItemCompat;

import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;

public class TestActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }


//    public boolean onCreateOptionsMenu(Menu menu)
//    {
//
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.top_app_bar,menu);
//
//        MenuItem search_menuitem =menu.findItem(R.id.app_bar_search);
//
//        SearchView searchview = (SearchView) search_menuitem.getActionView();
//
//
//        searchview.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
//            @Override
//            public boolean onQueryTextSubmit(String query) {
//                Log.i("phani", query);
//
//                return false;
//            }
//
//            @Override
//            public boolean onQueryTextChange(String newText) {
//                Log.i("phani", newText);
//                return false;
//            }
//        });
//
//
//
//        return super.onCreateOptionsMenu(menu);
//    }
}