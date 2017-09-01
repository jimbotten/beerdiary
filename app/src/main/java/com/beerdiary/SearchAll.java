package com.beerdiary;

import com.beerdiary.DBHelper.BeverageAdapter;

import android.app.TabActivity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.os.Bundle;
import android.widget.TabHost;

public class SearchAll extends TabActivity {
	public void onCreate(Bundle savedInstanceState) {
		SharedPreferences settings = getSharedPreferences("theme",0);
        int theme = settings.getInt("theme", 0);
        
        switch (theme) {
        // normal colors
        case 0: super.setTheme(R.style.BlueSkin); break;
        // light
        case 1: super.setTheme(R.style.Theme); break;
        // dark
        case 2: super.setTheme(R.style.DarkSkin); break;
        // none
        default: super.setTheme(R.style.BlueSkin); break;
        }
        
        super.onCreate(savedInstanceState);
	
     setContentView(R.layout.searchbeverages);

    Resources res = getResources(); // Resource object to get Drawables
    TabHost tabHost = getTabHost();  // The activity TabHost
    TabHost.TabSpec spec;  // Resusable TabSpec for each tab
    Intent intent;  // Reusable Intent for each tab

    intent = new Intent().setClass(this, SelectBeverage.class);
    intent.putExtra(SelectBeverage.FILTER_TYPE, BeverageAdapter.FILTER_TYPE_BEVERAGE);
    spec = tabHost.newTabSpec(getString(R.string.beverage_Type)).setIndicator(getString(R.string.beverage_Type),
                      res.getDrawable(R.drawable.ic_tab_beverages))
                  .setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this, SelectBeverage.class);
    intent.putExtra(SelectBeverage.FILTER_TYPE, BeverageAdapter.FILTER_TYPE_MAKER);
    spec = tabHost.newTabSpec(getString(R.string.beverage_Maker)).setIndicator(getString(R.string.beverage_maker),
                      res.getDrawable(R.drawable.ic_tab_makers))
                  .setContent(intent);
    tabHost.addTab(spec);

    intent = new Intent().setClass(this, SelectBeverage.class);
    intent.putExtra(SelectBeverage.FILTER_TYPE, BeverageAdapter.FILTER_TYPE_TAGS);
    spec = tabHost.newTabSpec(getString(R.string.beverage_tags)).setIndicator(getString(R.string.beverage_tags),
                      res.getDrawable(R.drawable.ic_tab_tags))
                  .setContent(intent);
    tabHost.addTab(spec);

    tabHost.setCurrentTab(0);
	}

}