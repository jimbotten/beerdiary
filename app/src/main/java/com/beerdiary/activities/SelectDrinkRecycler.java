package com.beerdiary.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

import java.util.ArrayList;

/**
 * Created by Jim on 9/2/2017.
 */

public class SelectDrinkRecycler extends AppCompatActivity {
    DBHelper db = null;
    ArrayList<DBHelper.DrinkRow> allDrinks= null;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.Theme_AppCompat_Light);
        setContentView(R.layout.select_drink_recycler_view);
        mRecyclerView = findViewById(R.id.select_drink_recycler_view_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter (see also next example)
        db = DBHelper.getInstance(this);
        allDrinks = db.getAllDrinks();
        mAdapter = new DrinkRecyclerAdapter(getApplicationContext(), allDrinks);
        mRecyclerView.setAdapter(mAdapter);
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

        switch (item.getItemId()) {
            case (R.id.menuAddDrink) :
                intent = new Intent(this, SelectBeverageRecycler.class);
                startActivity(intent);
                break;
        }
        return false;

    }


}

