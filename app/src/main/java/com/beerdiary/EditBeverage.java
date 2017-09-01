package com.beerdiary;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import com.beerdiary.DBHelper.BeverageAdapter;
import com.beerdiary.DBHelper.BeverageRow;
import com.beerdiary.DBHelper.DrinkRow;

public class EditBeverage extends Activity {

	public static final int PICK_BEVERAGE=1;
	private static final int DIALOG_DRINK_DELETE=0;
	private static final int CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE = 100;
	public static final int MEDIA_TYPE_IMAGE = 1;
	
	DBHelper db = null;
	ArrayList<BeverageRow> allBeverages = null;
	BeverageAdapter bevAdapter=null;
	boolean editMode=false;
	Long editBid=null;
	boolean viewMode=false;
	BeverageRow editBev=null;
	ArrayList<String> allMakers = null;
	// protected Preview preview;
	
	EditText tvBeverageName = null;
	AutoCompleteTextView tvBeverageMaker = null;
	EditText tvBeverageLocation = null;
	EditText tvBeverageDescription = null;
	EditText tvBeverageTags = null;
	ImageButton btnEditBeverage = null;
	ImageButton btnDrinkBeverage = null;
	ImageButton btnDeleteBeverage = null;
	ImageButton btnPictureBeverage = null;
	TextView tvBeverageID = null;
	TextView tvBeverageimageUri = null;
	Boolean addDrinkFlag = false;
	ImageView imgBeverageImg = null;
	
	Uri cameraUri;
	
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
		setContentView(R.layout.screenbeverage);
		
        db = DBHelper.getInstance(this);
        
        tvBeverageName = (EditText) findViewById(R.id.beverage_name);
		tvBeverageMaker = (AutoCompleteTextView) findViewById(R.id.beverage_maker);
		tvBeverageLocation = (EditText) findViewById(R.id.beverage_maker_location);
		tvBeverageDescription = (EditText) findViewById(R.id.beverage_description);
		tvBeverageTags = (EditText) findViewById(R.id.beverage_tags);
		btnEditBeverage = (ImageButton) findViewById(R.id.button_modify_beverage);
		btnPictureBeverage = (ImageButton) findViewById(R.id.button_camera);
//		final Button btnCancelAddBeverage = (Button) findViewById(R.id.button_cancel_beverage);
		btnDrinkBeverage = (ImageButton) findViewById(R.id.button_drink_beverage);
		btnDeleteBeverage = (ImageButton) findViewById(R.id.button_delete_beverage);
		tvBeverageID = (TextView) findViewById(R.id.beverage_id);
		tvBeverageimageUri = (TextView) findViewById(R.id.beverage_imageUri);
		imgBeverageImg = (ImageView) findViewById(R.id.beverage_image);
		if ( ! checkCameraHardware(this) ) {
			imgBeverageImg.setVisibility(View.GONE);
		}
		
		allMakers = db.getAllMakers();
		final ArrayAdapter<String> makerAdapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_dropdown_item_1line, allMakers);
		tvBeverageMaker.setAdapter(makerAdapter);
		tvBeverageMaker.setOnItemClickListener(new OnItemClickListener() {
		@Override
		public void onItemClick(AdapterView<?> parent, View view, int position,
				long id) {
			String loc = db.getMakersLocation(makerAdapter.getItem(position));
			tvBeverageLocation.setText(loc);
		}
		}); 

		Intent intent = getIntent();
        editMode=intent.getBooleanExtra("EDITING_MODE",false);
        viewMode=intent.getBooleanExtra("VIEW_MODE",false);
        editBid=intent.getLongExtra("BID", (long) -1);
		
		if (viewMode) { // VIEWING
			editBev = db.getBeverage(editBid);
			// btnEditBeverage.setText(R.string.update);
			tvBeverageName.setText(editBev.getName());
			tvBeverageName.setEnabled(false);
			tvBeverageMaker.setText(editBev.getMaker());
			tvBeverageMaker.setEnabled(false);
			tvBeverageLocation.setText(editBev.getMakerLocation());
			tvBeverageLocation.setEnabled(false);
			tvBeverageDescription.setText(editBev.getDescription());
			tvBeverageDescription.setEnabled(false);
			tvBeverageTags.setText(editBev.getTags());
			tvBeverageTags.setEnabled(false);
			tvBeverageID.setText(editBev.get_id().toString());
	  		tvBeverageimageUri.setText(editBev.getimageUri());
	  		File mediaUri = new File((String) tvBeverageimageUri.getText());
	  		
	  		if (mediaUri.exists()) {
	  			// If the file is there
	  			imgBeverageImg.setImageURI(Uri.parse(mediaUri.getPath()));
	  			imgBeverageImg.setEnabled(true);
	  		} else {
	  			// otherwise remove the data we have on this beverage
	  			editBev.setimageUri("");
	  			imgBeverageImg.setEnabled(false);
	  		}
		}else if (editMode && editBid >= 0){ // EDITING
			editBev = db.getBeverage(editBid);
			// btnEditBeverage.setText(R.string.update);
			tvBeverageName.setText(editBev.getName());
			tvBeverageMaker.setText(editBev.getMaker());
			tvBeverageLocation.setText(editBev.getMakerLocation());
			tvBeverageDescription.setText(editBev.getDescription());
			tvBeverageTags.setText(editBev.getTags());
	  		tvBeverageID.setText(editBev.get_id().toString());
	  		tvBeverageimageUri.setText(editBev.getimageUri());
	  		File mediaUri = new File((String) tvBeverageimageUri.getText());
	  		if (mediaUri.exists()) {
	  			// If the file is there
	  			imgBeverageImg.setImageURI(Uri.parse(mediaUri.getPath()));
	  		} else {
	  			// otherwise remove the data we have on this beverage
	  			editBev.setimageUri("");
	  		}
		} else
		{ // ADDING
			// btnEditBeverage.setText(R.string.add_this_beverage);
    		btnDeleteBeverage.setEnabled(false);
    		btnDeleteBeverage.setVisibility(View.GONE);
    		tvBeverageDescription.setLines(2);
    		//imgBeverageImg.setVisibility(View.GONE);
		}

		btnEditBeverage.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
			editOrAddBeverage();	
			}
  		});
		
		btnDeleteBeverage.setOnClickListener(new View.OnClickListener() {
			@Override
  			public void onClick(View view) {
				showDialog(DIALOG_DRINK_DELETE);
  			}
		});
		
		btnDrinkBeverage.setOnClickListener(new View.OnClickListener() {
			// Drink this beer from the Edit page
			@Override
			public void onClick(View v) {
				// Set flag to add this drink to the DB when ready.
				addDrinkFlag = true;
				// Then update database with beverage in case any fields changed.
				editOrAddBeverage();
			}
		});
		btnPictureBeverage.setOnClickListener(new View.OnClickListener() {
			// Go get a picture
			@Override
			public void onClick(View v) {
				Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//			    File photo = new File(Environment.getExternalStorageDirectory(),  "Pic.jpg");
			    cameraUri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
				
				intent.putExtra(MediaStore.EXTRA_OUTPUT, cameraUri);
			    			    
			   // imageUri = Uri.fromFile(photo);
			    startActivityForResult(intent, CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE);
			}
		});
	}
	/** Create a file Uri for saving an image or video */
	private static Uri getOutputMediaFileUri(int type){
	      return Uri.fromFile(getOutputMediaFile(type));
	}
	/** Create a File for saving an image or video */
	private static File getOutputMediaFile(int type){
	    // To be safe, you should check that the SDCard is mounted
	    // using Environment.getExternalStorageState() before doing this.

	    File mediaStorageDir = new File(Environment.getExternalStorageDirectory() + File.separator + "Pictures"  + File.separator + "BeerDiary");
	    
	    // Create the storage directory if it does not exist
	    if (! mediaStorageDir.exists()){
	        if (! mediaStorageDir.mkdirs()){
	            Log.d("BeerDiary", "failed to create directory");
	            return null;
	        }
	    }

	    // Create a media file name
	    String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
	    File mediaFile;
	    if (type == MEDIA_TYPE_IMAGE){
	        mediaFile = new File(mediaStorageDir.getPath() + File.separator +
	        "IMG_"+ timeStamp + ".jpg");
	    } else {
	        return null;
	    }

	    return mediaFile;
	}
	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent intent) {
	    if (requestCode == CAPTURE_IMAGE_ACTIVITY_REQUEST_CODE) {
	        if (resultCode == RESULT_OK) {
	            // Image captured and saved to fileUri specified in the Intent
	            //Toast.makeText(this, "Image saved to:\n" + cameraUri.getPath(), Toast.LENGTH_LONG).show();
	            String promisingPicture = cameraUri.getPath();

		  		File mediaUri = new File(promisingPicture);
	            if (mediaUri.exists()) {
		  			// If the new image file is there
		  			// if the old one is there, we should delete it to save space -- this is a new pic, yes?
	            	
	            	File OldUri = new File((String) tvBeverageimageUri.getText());
	            	if (OldUri.exists()) {
	    	  			// If the file is there then we clean out trash
	            		OldUri.delete();
	    	  		}
	    	  		//if the new file is there we are going to use it
	            	imgBeverageImg.setImageURI(Uri.parse(mediaUri.getPath()));
	            	tvBeverageimageUri.setText(cameraUri.getPath());
		  		} else {
		  			// otherwise we can't find anything --remove the data we have on this beverage
		  			editBev.setimageUri("");
		  		}
	            
	        } else if (resultCode == RESULT_CANCELED) {
	            // User cancelled the image capture
	        } else {
	            // Image capture failed, advise user
	        	Toast.makeText(this, "Camera failed", Toast.LENGTH_LONG).show();
	        }
	    }
	}
	
	protected Dialog onCreateDialog(int id) {
	    Dialog dialog = null;
	    switch(id) {
	    case DIALOG_DRINK_DELETE:	    	
	    	AlertDialog.Builder builder = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.Theme_Dialog));
	    	builder.setMessage(R.string.confirm_beverage_delete)
	    		   .setCancelable(false)
	    		   .setPositiveButton(R.string.remove, new DialogInterface.OnClickListener() {
	    	           public void onClick(DialogInterface dialog, int id) {
	    					Toast.makeText(getApplicationContext(),
	    	      					R.string.delete_beverage, Toast.LENGTH_SHORT).show();
	    	      			
	    						Long beverageID = editBev.get_id();
	    						db.delBeverage(beverageID);
	    						db.delDrinkFromBeverage(beverageID);

	    						Intent result = new Intent();
	    						setResult(RESULT_OK, result);
	    						returnIntent();
	    						
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
	
	protected void returnIntent() {
		Intent result = new Intent();
		setResult(RESULT_OK, result);
		finish();
	}
	/** Check if this device has a camera */
	private boolean checkCameraHardware(Context context) {
	    if (context.getPackageManager().hasSystemFeature(PackageManager.FEATURE_CAMERA)){
	        // this device has a camera
	        return true;
	    } else {
	        // no camera on this device
	        return false;
	    }
	}	
	public void editOrAddBeverage() {
				if (editMode) { // EDITING
	  				Toast.makeText(getApplicationContext(),
	  					getString(R.string.update_beverage) + "\n" + tvBeverageName.getText(), Toast.LENGTH_SHORT).show();		
				
	  				editBev.setName(tvBeverageName.getText().toString());
	  				editBev.setMaker(tvBeverageMaker.getText().toString());
	  				editBev.setMakerLocation(tvBeverageLocation.getText().toString());
	  				editBev.setDescription(tvBeverageDescription.getText().toString());
	  				editBev.setTags(tvBeverageTags.getText().toString());
	  				editBev.setimageUri(tvBeverageimageUri.getText().toString());
	  				// 	has an ID field too
				
					db.updateBeverage(editBev);
					//get Intent ready
					Intent result = new Intent();
				
					if (addDrinkFlag == true) {
						BeverageRow br = db.getBeverage(tvBeverageName.getText().toString(), tvBeverageMaker.getText().toString());
						DrinkRow dr = new DrinkRow(br);
						db.setDrink(dr);
						// add to the result if we drank it
						result.putExtra("Drank", "Drank");
					}
					// return result
					setResult(RESULT_OK, result);
					finish();
						
				} else { //ADDING
					Toast.makeText(getApplicationContext(),
					getString(R.string.adding_beverage) + tvBeverageName.getText(), Toast.LENGTH_SHORT).show();
					
					final BeverageRow addRow= new BeverageRow(); 
					
					addRow.setName(tvBeverageName.getText().toString());
					addRow.setMaker(tvBeverageMaker.getText().toString());
					addRow.setMakerLocation(tvBeverageLocation.getText().toString());
					addRow.setDescription(tvBeverageDescription.getText().toString());
					addRow.setTags(tvBeverageTags.getText().toString());
					addRow.setimageUri(tvBeverageimageUri.getText().toString());
					db.setBeverage(addRow);
					// Get intent ready
					Intent result = new Intent();
					
					if (addDrinkFlag == true) {
						// added it, now get the right bev for it
						BeverageRow addBev = db.getBeverage(tvBeverageName.getText().toString(), tvBeverageMaker.getText().toString());
						DrinkRow dr = new DrinkRow(addBev);
						db.setDrink(dr);
						// add drank if we drank it
						result.putExtra("Drank", "Drank");
					}
					setResult(RESULT_OK, result);
				
					finish();
				}
			}

}
