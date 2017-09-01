package com.beerdiary;

import java.io.IOException;
import java.util.ArrayList;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.ContextThemeWrapper;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import com.beerdiary.DBHelper.BeverageRow;
import com.beerdiary.DBHelper.DrinkAdapter;
import com.beerdiary.DBHelper.DrinkRow;

public class BeerDiary extends ListActivity {
	public static final int EDIT_BEVERAGE=10;
	public static final int PICK_BEVERAGE=1;
	public static int THEME=0;
	public static final int DIALOG_DRINK_DELETE=0;
	
	private DrinkRow delDrink = null;
	private int delDrinkPositionID = 0;
	
	DBHelper db = null;
	ArrayList<DrinkRow> allDrinks = null;
	DrinkAdapter TopDrinkAdapter=null;
	ListView viewDrinkList = null;
	Button btnAddDrink = null;
	LinearLayout drink_top_control_bar =null;
	LinearLayout drink_bottom_control_bar = null;
	
	@Override
    public void onCreate(Bundle savedInstanceState) {
        SharedPreferences settings = getSharedPreferences("theme",0);
        int theme = settings.getInt("theme", 0);
        
        switch (theme) {
        // normal colors
        case 0: super.setTheme(R.style.BrownSkin); break;
        // light
        case 1: super.setTheme(R.style.Theme); break;
        // dark
        case 2: super.setTheme(R.style.DarkSkin); break;
        // none
        default: super.setTheme(R.style.BrownSkin); break;
        }
        super.onCreate(savedInstanceState);
        
        
        setContentView(R.layout.screendrinklist);
        
        db = DBHelper.getInstance(this);
        allDrinks = db.getAllDrinks();
        TopDrinkAdapter=db.new DrinkAdapter(this, R.layout.listdrink, (ArrayList<DrinkRow>) allDrinks);
        
        drink_top_control_bar =(LinearLayout) this.findViewById(R.id.top_control_bar);
    	drink_bottom_control_bar =(LinearLayout) this.findViewById(R.id.bottom_control_bar);
    	
    	btnAddDrink = (Button)this.findViewById(R.id.btnAddDrink);
        
    	viewDrinkList = (ListView)getListView();
        setListAdapter(TopDrinkAdapter);
        viewDrinkList.setOnItemClickListener(new OnItemClickListener() {
				@Override
				public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
					// Dealing with Drinks -- create a beverage from this row's drink
					DrinkRow dr = TopDrinkAdapter.getItem(position);
					BeverageRow br = new BeverageRow(dr.getBeverage().get_id());
		
					// show the beverage
					Intent intent = new Intent(BeerDiary.this, EditBeverage.class);
					intent.putExtra("VIEW_MODE", true); // Viewing
					intent.putExtra("BID", br.get_id());
					startActivityForResult(intent, EDIT_BEVERAGE);	
				}
            });
        
        btnAddDrink.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		Intent intent = new Intent(BeerDiary.this, SelectBeverage.class);
        		intent.putExtra(SelectBeverage.FILTER_TYPE, 0);
        		startActivityForResult(intent, PICK_BEVERAGE);
        	}
        });
    }
	@Override
	public void onActivityResult(int reqCode,int resCode, Intent data){
		super.onActivityResult(reqCode, resCode, data);

		switch(reqCode) {
		case (PICK_BEVERAGE) : {
				allDrinks = db.getAllDrinks();
		      TopDrinkAdapter=db.new DrinkAdapter(this, R.layout.listdrink, (ArrayList<DrinkRow>) allDrinks);
		      setListAdapter(TopDrinkAdapter);
		      break;
			}
		}
	}
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		
		MenuInflater inflater = getMenuInflater();
		inflater.inflate(R.menu.main, menu);
		
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		Intent intent = null;
		
		 switch (item.getItemId()) {
		 case (R.id.menuCsvExport) : 
			 try {
				 db.toCSV();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
			 break;
		 
		 case (R.id.menuCsvImport) : 
			 try {
				 db.fromCSV();
			 } catch (IOException e) {
				 e.printStackTrace();
			 }
			 break;
			 
		  case (R.id.menuAddDrink) : 
			  intent = new Intent(BeerDiary.this, SearchAll.class);
			  startActivity(intent);
			  break;
		  
		  case (R.id.menuTheme) :
			  	intent = new Intent(BeerDiary.this, SetPreferences.class);
		  		startActivity(intent);
		  		break;
		 }
		return false;
		
	}

	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	
    
	    switch(id) {
	    case DIALOG_DRINK_DELETE:
	    	AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Dialog));

	    	builder.setMessage(R.string.confirm_drink_delete)
	    	       .setCancelable(false)
	    	       .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() { 
	    	    	   public void onClick(DialogInterface dialog, int id) {
	    	    			Toast.makeText(getApplicationContext(), R.string.delete_drink, Toast.LENGTH_SHORT).show();
	    	    			removeADrink();
	    	    			}
	    	       		})
	    	       .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    	                dialog.cancel();
	    	           }
	    	       });
	    	dialog = builder.create();
	    	break;
	    default:
	        dialog = null;
	    }
	    return dialog;
	}

	public void removeADrink() {
	    // Create some local variables that we can use in the builder onClick
	    // Get the position in the adapter of the drink to be deleted
	    final int localpos = this.delDrinkPositionID;
	    // Get the database ID of this drink
	    final long localDid = this.delDrink.get_id();
		TopDrinkAdapter.remove(localpos);
		db.delDrink(localDid);
	}
	@Override
	public void onResume() {
			super.onResume();
			allDrinks = db.getAllDrinks();
			TopDrinkAdapter=db.new DrinkAdapter(this, R.layout.listdrink, (ArrayList<DrinkRow>) allDrinks);
			setListAdapter(TopDrinkAdapter);
	}
	
	public void  EditDrinkClick(View view) {
		int row = getListView().getPositionForView(view);
    
        BeverageRow br = (BeverageRow) TopDrinkAdapter.getItem(row).getBeverage();
	
        Intent intent = new Intent(BeerDiary.this, EditBeverage.class);
		intent.putExtra("EDITING_MODE", true); // EDITING
		intent.putExtra("BID", br.get_id());
		startActivityForResult(intent, EDIT_BEVERAGE);
	}
	
	public void  AddDrinkClick(View view) {
		int row = getListView().getPositionForView(view);
	    
        BeverageRow br = (BeverageRow) TopDrinkAdapter.getItem(row).getBeverage();
	
        DrinkRow dr = new DrinkRow(br);
        
        //TopDrinkAdapter.add(dr);
        db.setDrink(dr);
        //Add to the database, then requery to get the right drinkId in the adapter 
		allDrinks = db.getAllDrinks();
		TopDrinkAdapter=db.new DrinkAdapter(this, R.layout.listdrink, (ArrayList<DrinkRow>) allDrinks);
		setListAdapter(TopDrinkAdapter);
		}
	
	public void  RemoveDrinkClick(View view) {
		// Populate some class variables so we can delete the right drink in the delDrink dialog
		
		// Get the position that was just clicked
		this.delDrinkPositionID = getListView().getPositionForView(view);
		// Get the DrinkRow from the database through the adapter from this view
		this.delDrink = (DrinkRow) getListView().getAdapter().getItem(delDrinkPositionID);
		
		showDialog(DIALOG_DRINK_DELETE);
		}
	
}