package com.beerdiary;

import java.util.ArrayList;

import android.app.Activity;
import android.app.ListActivity;
import android.app.SearchManager;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.beerdiary.DBHelper.BeverageAdapter;
import com.beerdiary.DBHelper.BeverageRow;
import com.beerdiary.DBHelper.DrinkRow;

 public class SelectBeverage extends ListActivity {
// public class SelectBeverage extends AppCompatActivity {
	public static final int EDIT_BEVERAGE=10;
    DBHelper db = null;
    ArrayList<BeverageRow> allBeverages = null;
    BeverageAdapter bevAdapter=null; 
    Button btnAddBeverage = null;
	ListView viewBeverageList = null;
	EditText filterText = null;
	TextView topControlBar=null;
	Button bottomControlBar = null;
	int filterType = BeverageAdapter.FILTER_TYPE_BEVERAGE;
	public static final String FILTER_TYPE="FilterType";
	
	String filterField = null;
	
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
		setContentView(R.layout.selectbeveragelist);
		
		btnAddBeverage = findViewById(R.id.select_add_new_beverage);
    	viewBeverageList = getListView();
    	filterText = findViewById(R.id.filterText);
    	topControlBar = findViewById(R.id.top_control_text);
        bottomControlBar = findViewById(R.id.select_add_new_beverage);
		Bundle ex = getIntent().getExtras();
		
		switch (ex.getInt(FILTER_TYPE)) {
		case BeverageAdapter.FILTER_TYPE_MAKER:
			filterType = ex.getInt(FILTER_TYPE);
			topControlBar.setVisibility(android.view.View.GONE);
			bottomControlBar.setVisibility(android.view.View.GONE);
			filterText.setHint(getString(R.string.filter_label_maker));
			break;
		case BeverageAdapter.FILTER_TYPE_TAGS:
			filterType = ex.getInt(FILTER_TYPE);
			topControlBar.setVisibility(android.view.View.GONE);
			bottomControlBar.setVisibility(android.view.View.GONE);
			filterText.setHint(getString(R.string.filter_label_tags));
			break;
		default:
			filterType = 1;
		}
		
		if ( ex.getInt(FILTER_TYPE) >0 ) {
			filterType = ex.getInt(FILTER_TYPE);
			topControlBar.setVisibility(android.view.View.GONE);
			bottomControlBar.setVisibility(android.view.View.GONE);	
		}
		
		db = DBHelper.getInstance(this);
        allBeverages = db.getAllBeverages();
        bevAdapter=db.new BeverageAdapter(this, R.layout.listbeverage, allBeverages);
        bevAdapter.setFilterType(filterType);
        
        viewBeverageList.setTextFilterEnabled(true);
		setListAdapter(bevAdapter);
        	
    	TextWatcher filterTextWatcher = new TextWatcher() {
        	@Override
    	    public void onTextChanged(CharSequence s, int start, int before, int count) {
        		bevAdapter.getFilter().filter(s);
        		bevAdapter.notifyDataSetChanged();
        	}
			@Override 	public void afterTextChanged(Editable s) {  }
			@Override	public void beforeTextChanged(CharSequence s, int start, int count, int after) {  }
    	};
    	
    	filterText.addTextChangedListener(filterTextWatcher);
    	
    	btnAddBeverage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
        		Intent intent = new Intent(SelectBeverage.this, EditBeverage.class);
        		intent.putExtra("EDITING_MODE", false); // ADDING
        		startActivityForResult(intent, EDIT_BEVERAGE);				
			}
        });
    	
    	viewBeverageList.setOnItemClickListener(new OnItemClickListener() {
		// Add Bev to Drink List
    		@Override
			public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
				BeverageRow br = bevAdapter.getItem(position);
				DrinkRow dr = new DrinkRow(br);
				
				db.setDrink(dr);
				Intent result = new Intent();
				setResult(RESULT_OK, result);
				finish();
        		}});

    	performSearch(getIntent());
	}



	@Override
	protected void onNewIntent(Intent intent) {
	    setIntent(intent);
	    performSearch(intent);
	}
	
	private void performSearch(Intent intent) {
    	
    	if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
    	      	String query = intent.getStringExtra(SearchManager.QUERY);
    	      	bevAdapter.getFilter().filter(query);
      			bevAdapter.notifyDataSetChanged();
      			filterText.setText(query);
    	    } else {
    	    	String existingFilter = filterText.getText().toString();
    	    	bevAdapter.getFilter().filter(existingFilter);
      			bevAdapter.notifyDataSetChanged();
    	    }	
	}
	
	@Override
	public void onActivityResult(int reqCode,int resCode, Intent data){
		super.onActivityResult(reqCode, resCode, data);
		switch(reqCode) {
		case (EDIT_BEVERAGE) : {
			if (resCode == Activity.RESULT_OK) {
				allBeverages = db.getAllBeverages();
		        bevAdapter=db.new BeverageAdapter(this, R.layout.listbeverage, allBeverages);
				setListAdapter(bevAdapter);
				// if it was result ok, and this was returned from adding and they chose to drink			
				if (data.hasExtra("Drank")) {
					finish();
				}
			}
			break;
		}
		}
	}

	@Override
	public void onResume() {
			super.onResume();
			db = DBHelper.getInstance(this);
	        allBeverages = db.getAllBeverages();
	        bevAdapter=db.new BeverageAdapter(this, R.layout.listbeverage, allBeverages);
	        bevAdapter.setFilterType(filterType);
	        
	        viewBeverageList.setTextFilterEnabled(true);
			setListAdapter(bevAdapter);	
	        		
	}

	public void  EditDrinkClick(View view) {
		int position = getListView().getPositionForView(view);
	    
		BeverageRow br = bevAdapter.getItem(position);
		Intent intent = new Intent(SelectBeverage.this, EditBeverage.class);
		intent.putExtra("EDITING_MODE", true); // EDITING
		intent.putExtra("BID", br.get_id());
		startActivityForResult(intent, EDIT_BEVERAGE);		
	}

	public void AddDrinkClick(View view) {
		int position = getListView().getPositionForView(view);

		BeverageRow br = bevAdapter.getItem(position);
		DrinkRow dr = new DrinkRow(br);
		
		db.setDrink(dr);
		Intent result = new Intent();
		setResult(RESULT_OK, result);
		finish();
	}
}

