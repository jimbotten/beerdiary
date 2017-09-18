package com.beerdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

import java.util.ArrayList;

/**
 * Created by Jim on 9/2/2017.
 */

public class SelectBeverageRecycler extends AppCompatActivity {
    DBHelper db = null;
    ArrayList<DBHelper.BeverageRow> allBeverages = null;

    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.Theme_AppCompat_Light);
        setContentView(R.layout.select_beverage_recycler_view);
        mRecyclerView = findViewById(R.id.select_beverage_recycler_view_recyclerview);

        // use this setting to improve performance if you know that changes
        // in content do not change the layout size of the RecyclerView
        mRecyclerView.setHasFixedSize(true);

        // use a linear layout manager
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        // specify an adapter
        db = DBHelper.getInstance(this);
        allBeverages = db.getAllBeverages();
        mAdapter = new BeverageRecyclerAdapter(allBeverages);
        mRecyclerView.setAdapter(mAdapter);

        // TODO make this view respond to intents - if its opened from an intent, display a toast message
        // TODO if this view is responding to an intent, update the title bar somehow to show edit mode or select mode
        // TODO make the camera feature more seamless on the edit page
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.beverage_menu, menu);
        return true;
    }
    public boolean onOptionsItemSelected(MenuItem item) {
        Intent intent = null;

//        switch (item.getItemId()) {
//            case (R.id.menuAddDrink) :
//                intent = new Intent(this, SearchAll.class);
//                startActivity(intent);
//                break;
//        }
        return false;

    }
}