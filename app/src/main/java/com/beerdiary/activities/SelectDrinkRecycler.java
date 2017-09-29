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

        // specify an adapter
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
                // note that putextra is needed since startActivityForResult doens't send the request code,
                // request code is used when the next activity returns
                intent.putExtra("MODE", App.SELECT_BEVERAGE_REQUEST);
                startActivityForResult(intent, App.SELECT_BEVERAGE_REQUEST);
                break;
        }
        // TODO add a case for the options menu that opens an options page (similar to SetPreferences Class and preferences.xml
        // TODO add options page with any other preferences
        return false;

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        Long bid;
        if (requestCode==1 && data != null) {
            switch (requestCode) {
                case (App.SELECT_BEVERAGE_REQUEST): {
                    if (resultCode == Activity.RESULT_OK) {

                        String returnValue = data.getStringExtra("beverage");

                        DBHelper.BeverageRow br = db.getOneBeverage(returnValue);

                        DBHelper.DrinkRow dr = new DBHelper.DrinkRow(br);

                        // insert at row 0, at the top, since this is reverse sorted
                        allDrinks.add(0,dr);
                        //adding to the database
                         db.setDrink(dr);

                        // refresh the adapter and the view
                        mAdapter.notifyItemInserted(0);
                        // scroll to the top
                        mRecyclerView.scrollToPosition(0);
                    }
                    break;
                }
            }
        }
    }
}


