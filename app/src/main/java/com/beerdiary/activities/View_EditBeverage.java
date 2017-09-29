package com.beerdiary.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.widget.TextView;

import com.beerdiary.DBHelper;
import com.beerdiary.R;

/**
 * Created by Jim on 9/28/2017.
 */

public class View_EditBeverage extends AppCompatActivity {
    DBHelper db = null;
    private TextView beverage_name;
    private TextView beverage_maker;
    private TextView beverage_maker_location;
    private TextView beverage_description;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setTheme(R.style.Theme_AppCompat_Light);
        setContentView(R.layout.view_edit_beverage);

        beverage_name = findViewById(R.id.beverage_name);
        beverage_maker= findViewById(R.id.beverage_maker);
        beverage_maker_location= findViewById(R.id.beverage_maker_location);
        beverage_description= findViewById(R.id.beverage_maker_location);

        Intent intent = getIntent();
        Long bid = intent.getLongExtra("BID", -1L);

        db = DBHelper.getInstance(this);

        DBHelper.BeverageRow br = db.getBeverage(bid);

        beverage_name.setText(br.getName());
        beverage_maker.setText(br.getMaker());
        beverage_maker_location.setText(br.getMakerLocation());
        beverage_description.setText(br.getDescription());



    }
}
