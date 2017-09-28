package com.beerdiary;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.beerdiary.activities.App;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

public class DBHelper {
	private static final String DATABASE_NAME = "beerdiary";
	private static final Integer DATABASE_VERSION = 17;

	private static final String B_ID = "_id";
	private static final String B_NAME = "NAME";
	private static final String B_MAKER = "MAKER";
	private static final String B_MAKERLOCATION = "MAKERLOCATION";
	private static final String B_DESCRIPTION = "DESCRIPTION";
	private static final String B_TAGS = "TAGS";
	private static final String B_TAGSDELIMETER = ";";
	private static final String B_R_OVERALL = "R_OVERALL";
	private static final String B_PIC = "IMAGEURI";
	private static final String D_ID = "_id";
	private static final String D_BID = "BID";
	private static final String D_DATE = "DDATE";
	
	private static final String TBL_BEVERAGE = "BEVERAGE";
	private static final String TBL_DRINK = "DRINK";
	private static final String BEVERAGE_CREATE = "create table BEVERAGE ("
												+ "_id integer primary key autoincrement,"
												+ " NAME text not null,"
												+ " MAKER text,"
												+ " MAKERLOCATION text,"
												+ " DESCRIPTION text,"
												+ " TAGS text,"
												+ " R_OVERALL INTEGER DEFAULT 0,"
												+ " IMAGEURI text"
												+ ");";
	private static final String DRINK_CREATE = "create table DRINK ("
												+ "_id integer primary key autoincrement,"
												+ " BID integer not null," 
												+ " DDATE timestamp not null default current_timestamp"
												+ ");";
	
	private static final String BEVERAGE_INIT1 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Bad Penny\', \'Big Boss\', \'Raleigh, NC\', \'Brown Ale\', \'Caramel\');";
	private static final String BEVERAGE_INIT2 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Hell''s Belle\', \'Big Boss\', \'Raleigh, NC\', \'Belgian Blond\', \'Citrus\');";
	private static final String BEVERAGE_INIT3 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Angry Angel\', \'Big Boss\', \'Raleigh, NC\', \'Kolsch Style Ale\', \'Floral\');";
	private static final String BEVERAGE_INIT4 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Blanco Diablo\', \'Big Boss\', \'Raleigh, NC\', \'Wit Ale\', \'Orange,Spice\');";
	private static final String BEVERAGE_INIT5 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'High Roller\', \'Big Boss\', \'Raleigh, NC\', \'American Style IPA\', \'\');";
	private static final String BEVERAGE_INIT6 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Aces & Ates\', \'Big Boss\', \'Raleigh, NC\', \'Coffee Stout\', \'\');";
	private static final String BEVERAGE_INIT7 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Harvest Time\', \'Big Boss\', \'Raleigh, NC\', \'Pumpkin Ale\', \'\');";
	private static final String BEVERAGE_INIT8 = "INSERT INTO BEVERAGE (NAME,MAKER,MAKERLOCATION,DESCRIPTION,TAGS) VALUES (\'Big Operator\', \'Big Boss\', \'Raleigh, NC\', \'Belgian Black Raspberry\', \'Fruit\');";
	//TODO update database to support multivalue hop flavor, color, aroma, mouthfeel, and taste.  maintain existing tags field
	public static final Uri CONTENT_URI = Uri.parse("content://com.beerdiary.beverageprovider");
	
    private static SQLiteDatabase db;
	private static DBHelper dbHelper;

	private OpenHelper openHelper;
	private static final String lock = "";
	//private static Context cx;

	public class Beverage {
		// TODO This class needs unit tests
		private String Name;
		//private Integer Id;
		private String Maker;
		private String Description;
		private String[] Tags;
		private String MakerLocation;

		public String getName() {
			return Name;
		}
		public String getMaker() {
			return Maker;
		}
		public String getDescription() {
			return Description;
		}
		public String[] getTags() {
			return Tags;
		}
		public String getMakerLocation() {
			return MakerLocation;
		}
		public void setName(String _name) {
			Name = _name;
		}
		public void setMaker(String _maker) {
			Maker = _maker;
		}
		public void setDescription(String _description) {
			Description = _description;
		}
		public void setTags(String[] _tags) {
			Tags = _tags;
		}
		public void setMakerLocation(String _location) {
			MakerLocation = _location;
		}
	}

	public static class BeverageRow {
		// TODO Change BEverage row to the Beverage Class
        private Long _id;
        private String Name;
        private String Maker;
        private String MakerLocation;
        private String Description;
        private String[] Tags;
        private String imageUri;

        BeverageRow() {
        	//
        }
        public String toString() {
        	return Name;
        }
        BeverageRow(String _name, String _maker, String _makerLocation, String _description, String _tags) {
        	Name = _name;
        	Maker = _maker;
        	MakerLocation = _makerLocation;
        	Description = _description;
        	Tags = _tags.split(B_TAGSDELIMETER);
        	imageUri = "";
        }
        BeverageRow(String _name, String _maker, String _makerLocation, String _description, String _tags, Integer _rating, String _imageUri) {
        	Name = _name;
        	Maker = _maker;
        	MakerLocation = _makerLocation;
        	Description = _description;
        	Tags = _tags.split(B_TAGSDELIMETER);
        	imageUri = _imageUri;
        }
        BeverageRow(Long new_id, String _name, String _maker, String _makerLocation, String _description, String _tags, Integer _rating, String _imageUri) {
        	_id = new_id;
        	Name = _name;
        	Maker = _maker;
        	MakerLocation = _makerLocation;
        	Description = _description;
        	Tags = _tags.split(B_TAGSDELIMETER);
        	imageUri = _imageUri;
        }
        BeverageRow(Long new_id) {
        	_id= new_id;
        }

		public String getName() {
    		return Name;
    	}
    	public String getMaker() {
    		return Maker;
    	}
		public String[] getTagsArray() {
			return Tags;
		}
		public void setName(String _name) {
			Name = _name;
		}
		public void set_id(Long new_id) {
			_id = new_id;
		}

		String getMakerLocation() {
    		return MakerLocation;
    	}
    	String getDescription() {
    		return Description;
    	}
    	String getTags() {
    		return combine(Tags);
    	}
    	public Long get_id() {
    		return _id;
    	}
    	String getimageUri() {
    		return imageUri;
    	}
    	public BeverageRow getRow() { return this; }

    	void setMaker(String _maker) {
    		Maker = _maker;
    	}
    	void setMakerLocation(String _makerLocation) {
    		MakerLocation = _makerLocation;
    	}
    	void setDescription(String _description) {
    		Description = _description;
    	}
    	void setTags(String _tags) {
    		Tags = _tags.split(B_TAGSDELIMETER);
    	}
    	void setimageUri(String _imageUri) {
    		imageUri = _imageUri;
    	}
    	private static String combine(String[] s)
    	{
    	  int k=s.length;
    	  if (k==0)
    	    return null;
    	  StringBuilder out=new StringBuilder();
    	  out.append(s[0]);
    	  for (int x=1;x<k;++x)
    	    out.append(B_TAGSDELIMETER).append(s[x]);
    	  return out.toString();
    	}
	}

	class BeverageAdapter extends ArrayAdapter<BeverageRow> {
		private List<BeverageRow> myObjects;
		private final Object myLock = new Object();
		private ArrayList<BeverageRow> myOriginalValues;
		private BeverageFilter myFilter;
		private LayoutInflater myInflater;

		static final int FILTER_TYPE_BEVERAGE=1;
		static final int FILTER_TYPE_MAKER=2;
		static final int FILTER_TYPE_TAGS=3;
		int filterType = FILTER_TYPE_BEVERAGE;
		
		
		private final Comparator<BeverageRow> mySorter = new Comparator<BeverageRow>() {
			public int compare(BeverageRow dr1, BeverageRow dr2) {
				String first = dr1.getName();
				String second = dr2.getName();
				return first.compareTo(second);
			}
		};
		void sort() {
		    Collections.sort(myObjects, mySorter);
		    notifyDataSetChanged();
		}
		BeverageAdapter(Context context, int textViewResourceId, ArrayList<BeverageRow> objects) {
		    super(context, textViewResourceId, objects);
		    myInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
		    myObjects = objects;
		}
		void setFilterType(int _filterType) {
			filterType = _filterType;
		}
		@NonNull
		public View getView (int position, View v, @NonNull ViewGroup parent) {
			if (v == null) {
				v = myInflater.inflate(R.layout.listbeverage, null);
		    	}
			BeverageRow b = getItem(position);
			if (b != null) {
				TextView tt = v.findViewById(R.id.toptext);
				TextView bt = v.findViewById(R.id.bottomtext);
				if (tt != null) {
				tt.setText(b.getName());                            }
					//tt.setText("Name: "+b.getName());                            }
			    			if (bt != null) {
			    				bt.setText(b.getMaker());                            }
								}	
			    		return v;
			    	}
		public void remove(BeverageRow object) {
		    if (myOriginalValues != null) {
		        synchronized (myLock) {
		            myOriginalValues.remove(object);
		        }
		    } else {
		        myObjects.remove(object);
		    }
		    notifyDataSetChanged();
		}
		public void add(BeverageRow object) {
		    if (myOriginalValues != null) {
		        synchronized (myLock) {
		            myOriginalValues.add(object);
		            notifyDataSetChanged();
		        }
		    } else {
		        myObjects.add(object);
		        sort();
		        notifyDataSetChanged();
		    }
		}
		void setContents(List<BeverageRow> strs) {
			myObjects.clear();
			myObjects.addAll(strs);
		}
		@NonNull
		public Filter getFilter() {
		    if (myFilter == null) {
		        myFilter = new BeverageFilter();
		    }
		    return myFilter;
		}
		private class BeverageFilter extends Filter {
			@Override
		            protected Filter.FilterResults performFiltering(CharSequence prefix) {
		                FilterResults results = new FilterResults();
		                if (myOriginalValues == null) {
		                    synchronized (myLock) {
		                        myOriginalValues = new ArrayList<BeverageRow>(myObjects);
		                    }
		                }
		                if (prefix == null || prefix.length() == 0) {
		                    synchronized (myLock) {
		                        ArrayList<BeverageRow> list = new ArrayList<BeverageRow>(myOriginalValues);
		                        results.values = list;
		                        results.count = list.size();
		                    }
		                } else {
		                    String prefixString = prefix.toString().toLowerCase();
	   
		                	final ArrayList<BeverageRow> values = myOriginalValues;
		                    final int count = values.size();
		                    final ArrayList<BeverageRow> newValues = new ArrayList<BeverageRow>(count);
		                    BeverageRow br = null;
		                    String valueText =null;
		                    int index=0;
		                    
		                    if (filterType==FILTER_TYPE_MAKER) {
		                    	for (index = 0; index < count ; index++) {
		                    
			                    	br = values.get(index);
			                        valueText = br.getMaker().toLowerCase();
			                    	if(valueText.contains(prefixString)){
			                    		newValues.add(br);  
				    	            }
		                    	}
		                    } else 
			    	         if (filterType==FILTER_TYPE_TAGS) {
		    	            	for (index = 0; index < count ; index++) {
		    	                    
		    	            		br = values.get(index);
			                    	valueText = br.getTags().toLowerCase();
			                    	if(valueText.contains(prefixString)){
			                    		newValues.add(br);  
			                    	}
		    	            	}
			    	         } else {  //else FILTER_TYPE_BEVERAGES
		    	            	for (index = 0; index < count ; index++) {
		    	                    
			                    	br = values.get(index);
			                        valueText = br.getName().toLowerCase();
			                    	if(valueText.contains(prefixString)){
			                    		newValues.add(br);  
			                    	}
			    	            }
			                }	
		                    
			    	        results.values = newValues;
		                    results.count = newValues.size();
		                }
		                return results;
		            }
	            @SuppressWarnings("unchecked")
				@Override
			    protected void publishResults(CharSequence constraint, FilterResults results) {
			        //noinspection unchecked
	            	setContents((List<BeverageRow>) results.values);
			        if (results.count > 0) {
			            notifyDataSetChanged();
			        } else {
			            notifyDataSetInvalidated();
			        }
			    }
			}  	
	}
	
	public class BeverageProvider extends ContentProvider {

		
		DBHelper db = null;
		ArrayList<BeverageRow> allBeverages=null;

	    
		@Override
		public boolean onCreate() {
			Context context = getContext();
			db = DBHelper.getInstance(context);
	        allBeverages = db.getAllBeverages();
	        return false;
		}

		@Override
		public Cursor query(@NonNull Uri uri, String[] projection, String selection,
							String[] selectionArgs, String sortOrder) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getType(@NonNull Uri uri) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Uri insert(@NonNull Uri uri, ContentValues values) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int delete(@NonNull Uri uri, String selection, String[] selectionArgs) {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public int update(@NonNull Uri uri, ContentValues values, String selection,
				String[] selectionArgs) {
			// TODO Auto-generated method stub
			return 0;
		}

	}

	public static class DrinkRow  {
		private Long _id;
		private BeverageRow Beverage;
		private Date Ddate;

		DrinkRow() { 
			// 
		}
		DrinkRow(Long new_id) { 
			// used for error state
			_id = new_id;
			Beverage = null;
			Ddate = null;
		}
		DrinkRow (BeverageRow _beverage, Date _ddate) {
			Beverage = _beverage;
			Ddate = _ddate;
		}
		public DrinkRow (BeverageRow _beverage) {
			Beverage = _beverage;
			Ddate = new Date();
		}
		DrinkRow (Long new_id, BeverageRow _beverage, Date d) {
			_id = new_id;
			Beverage = _beverage;
			Ddate = d;
		}
		
		public BeverageRow getBeverage() {
			return Beverage;
		}
		Date getDate() {
			return Ddate;
		}
		public String getName() {
			return Beverage.getName();
		}
		public String getFormattedDate() {
			SimpleDateFormat formatter = new SimpleDateFormat("h:mm a dd MMM");
			//hh:mm a dd MMM // Example: 10:17 AM 12 Feb
			//dd MMM yyyy HH:mm:ss	// Example:	12 Feb 2011 12:18:27
			
			return formatter.format(Ddate);
		}
		Long get_id() {
			return _id;
		}
		public void setBeverage(BeverageRow newbev) {
			Beverage = newbev;
		}
		public void setDate(Date newdate) {
			Ddate = newdate;
		}
		public void set_id(Long new_id) {
			_id = new_id;
		}
	}

	private class ViewHolder {
		TextView toptext;
		TextView bottomtext;
		Long hidingID;
	}

	class DrinkAdapter extends ArrayAdapter<DrinkRow>  {
		private List<DrinkRow> mObjects;
	  	private final Object mLock = new Object();
	    private ArrayList<DrinkRow> mOriginalValues;
	    private BeverageFilter mFilter;
	    private LayoutInflater mInflater;

		private final Comparator<DrinkRow> dSorter = new Comparator<DrinkRow>() {
			public int compare(DrinkRow dr1, DrinkRow dr2) {
				return dr2.getDate().compareTo(dr1.getDate());
			}
	    };
	   
	    void sort() {
	        Collections.sort(mObjects, dSorter);
	        notifyDataSetChanged();
	    }
	       	
	   	DrinkAdapter(Context context, int textViewResourceId, ArrayList<DrinkRow> objects) {
	        super(context, textViewResourceId, objects);
	        mInflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
	        mObjects = objects;
	    }
		
		@NonNull
		public View getView (final int position, final View convertView, final ViewGroup parent) {

			View v = convertView;
			       if (v == null) {
			          v = mInflater.inflate(R.layout.listdrink, null);
			          ViewHolder viewHolder = new ViewHolder();
			          viewHolder.toptext = v.findViewById(R.id.toptext);
			          viewHolder.bottomtext = v.findViewById(R.id.bottomtext);
			          v.setTag(viewHolder);
			       }
				DrinkRow d = getItem(position);
					 if (d != null) {
			          ViewHolder viewHolder = (ViewHolder) v.getTag();
			          viewHolder.toptext.setText(d.getBeverage().getName());
			          viewHolder.bottomtext.setText(d.getFormattedDate());
			          viewHolder.hidingID = d.get_id();
			       }
			       return v;
		}

		void remove(int index) {
	        if (mOriginalValues != null) {
	            synchronized (mLock) {
	                mOriginalValues.remove(index);
	                notifyDataSetChanged();
	            }
	        } else {
	            mObjects.remove(index);
	            notifyDataSetChanged();
	        }
	    }
		
	    public void remove(DrinkRow object) {
	        if (mOriginalValues != null) {
	            synchronized (mLock) {
	                mOriginalValues.remove(object);
	                notifyDataSetChanged();
	            }
	        } else {
	            mObjects.remove(object);
	            notifyDataSetChanged();
	        }
	    }
	    
	    public void add(DrinkRow object) {
	        if (mOriginalValues != null) {
	            synchronized (mLock) {
	                mOriginalValues.add(object);
	                notifyDataSetChanged();
	            }
	        } else {
	            mObjects.add(object);
	            sort();
	            notifyDataSetChanged();
	        }
	    }
	    
	    public int getCount() {
	    	if (mOriginalValues != null) {
	            synchronized (mLock) {
	                
	                return mOriginalValues.size();
	            }
	        } else {
	            return mObjects.size();
	        }    	
	    }
	    
	    @NonNull
		public Filter getFilter() {
	        if (mFilter == null) {
	            mFilter = new BeverageFilter();
	        }
	        return mFilter;
	    }
		
	    private class BeverageFilter extends Filter {
	        @Override
	        protected FilterResults performFiltering(CharSequence prefix) {
	            FilterResults results = new FilterResults();

	            if (mOriginalValues == null) {
	                synchronized (mLock) {
	                    mOriginalValues = new ArrayList<DrinkRow>(mObjects);
	                }
	            }

	            if (prefix == null || prefix.length() == 0) {
	                synchronized (mLock) {
	                    ArrayList<DrinkRow> list = new ArrayList<DrinkRow>(mOriginalValues);
	                    results.values = list;
	                    results.count = list.size();
	                }
	            } else {
	                String prefixString = prefix.toString().toLowerCase();

	            	final ArrayList<DrinkRow> values = mOriginalValues;
	                final int count = values.size();
	                final ArrayList<DrinkRow> newValues = new ArrayList<DrinkRow>(count);

	                
	                for (int index = 0; index < count ; index++) {
	                	final DrinkRow br = values.get(index);
	                    final String valueText = br.toString().toLowerCase();
	                	if(valueText.contains(prefixString)){
	                		newValues.add(br);  
	    	            }
	                }
	    	        results.values = newValues;
	                results.count = newValues.size();
	            }
	            return results;
	        }

	        @SuppressWarnings("unchecked")
			@Override
	        protected void publishResults(CharSequence constraint, FilterResults results) {
	            //noinspection unchecked
	            mObjects = (List<DrinkRow>) results.values;
	            if (results.count > 0) {
	                notifyDataSetChanged();
	            } else {
	                notifyDataSetInvalidated();
	            }
	        }
	    }

	}

	private class OpenHelper extends SQLiteOpenHelper {
    	OpenHelper(Context context) {
    		super(context, DATABASE_NAME, null, DATABASE_VERSION);
    	}
        @Override
        public void onCreate(SQLiteDatabase db) {
        	db.execSQL(BEVERAGE_CREATE);
        	db.execSQL(DRINK_CREATE);
        	db.execSQL(BEVERAGE_INIT1);
        	db.execSQL(BEVERAGE_INIT2);
        	db.execSQL(BEVERAGE_INIT3);
        	db.execSQL(BEVERAGE_INIT4);
        	db.execSQL(BEVERAGE_INIT5);
        	db.execSQL(BEVERAGE_INIT6);
        	db.execSQL(BEVERAGE_INIT7);
        	db.execSQL(BEVERAGE_INIT8);
        }
        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        	if (oldVersion <= 16) {
        		db.beginTransaction();
        		try{
        		// table create with if not exists

        		//get all columns
        		List<String> columns = GetColumns(db, TBL_BEVERAGE);
        		//backup table
        		db.execSQL("ALTER table " + TBL_BEVERAGE + " RENAME TO 'temp_" + TBL_BEVERAGE + "'");
        		// create new table
   	        	db.execSQL(BEVERAGE_CREATE);
        		columns.retainAll(GetColumns(db, TBL_BEVERAGE));
        		String cols = join(columns, ","); 
        		db.execSQL(String.format( "INSERT INTO %s (%s) SELECT %s from temp_%s", TBL_BEVERAGE, cols, cols, TBL_BEVERAGE));
        		
        		db.execSQL("DROP table 'temp_" + TBL_BEVERAGE + "'");
        		
        		db.setTransactionSuccessful();
        	} catch(SQLException e) {
        		db.endTransaction();
        		throw e;
        	}
        	db.endTransaction();
        }
//        	else if (oldVersion == 15) {
//        		  
//        		db.execSQL(BEVERAGE_UPDATE_RANKING);
//            	db.execSQL(BEVERAGE_UPDATE_IMAGE);
//                    
//        	} else if (oldVersion == 14) {
//        		db.execSQL(BEVERAGE_UPDATE_RANKING);
//            	db.execSQL(BEVERAGE_UPDATE_IMAGE);
//            	
//        		db.execSQL(BEVERAGE_INIT2);
//            	db.execSQL(BEVERAGE_INIT3);
//            	db.execSQL(BEVERAGE_INIT4);
//            	db.execSQL(BEVERAGE_INIT5);
//            	db.execSQL(BEVERAGE_INIT6);
//            	db.execSQL(BEVERAGE_INIT7);
//            	db.execSQL(BEVERAGE_INIT8);
//        	} else {
//            	db.execSQL(DBDESTROY_BEVERAGE);
//            	db.execSQL(DBDESTROY_DRINK);
//            	db.execSQL(BEVERAGE_CREATE);
//            	db.execSQL(DRINK_CREATE);
//            	db.execSQL(BEVERAGE_UPDATE_RANKING);
//            	db.execSQL(BEVERAGE_UPDATE_IMAGE);
//            	
//            	db.execSQL(BEVERAGE_INIT1);
//            	db.execSQL(BEVERAGE_INIT2);
//            	db.execSQL(BEVERAGE_INIT3);
//            	db.execSQL(BEVERAGE_INIT4);
//            	db.execSQL(BEVERAGE_INIT5);
//            	db.execSQL(BEVERAGE_INIT6);
//            	db.execSQL(BEVERAGE_INIT7);
//            	db.execSQL(BEVERAGE_INIT8);
//        	}
       	}
	}
    	
    private DBHelper(Context context) {
            openHelper = new OpenHelper(context);
    	}
    
    public static synchronized DBHelper getInstance(Context context) {
		if (dbHelper == null) {
			dbHelper = new DBHelper(context);
		}
		db = dbHelper.openHelper.getWritableDatabase();

		//cx = context;
		return dbHelper;
    }
    
    public BeverageRow getBeverage(String name) {
    	BeverageRow row = null;
        Cursor c =
            db.query(TBL_BEVERAGE, new String[] {
            		B_ID, B_NAME, B_MAKER, B_MAKERLOCATION, B_DESCRIPTION, B_TAGS, B_R_OVERALL, B_PIC}, "_id=?", 
            		new String[] {name},
            		null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            row = new BeverageRow(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6),c.getString(7));
        } else {
        	row = new BeverageRow(Long.decode("-1"));
        }
        c.close();
        return row;
    }
    
    BeverageRow getBeverage(String name, String maker) {
    	BeverageRow row = null;
        Cursor c =
            db.query(TBL_BEVERAGE, new String[] {
            		B_ID, B_NAME, B_MAKER, B_MAKERLOCATION, B_DESCRIPTION, B_TAGS, B_R_OVERALL, B_PIC}, B_NAME+"=? AND "+B_MAKER+"=?", 
            		new String[] {name, maker},
            		null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            row = new BeverageRow(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6),c.getString(7));
        } else {
        	row = new BeverageRow(Long.decode("-1"));
        }
        c.close();
        return row;
    }
        
    public BeverageRow getBeverage(Long rowId) {
    	BeverageRow row = null;
        Cursor c =
            db.query(TBL_BEVERAGE, new String[] {
            		B_ID, B_NAME, B_MAKER, B_MAKERLOCATION, B_DESCRIPTION, B_TAGS, B_R_OVERALL, B_PIC}, "_id=?", 
            		new String[] {rowId.toString()},
            		null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            row = new BeverageRow(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6),c.getString(7));
        } else {
        	row = new BeverageRow(Long.decode("-1"));
        }
        c.close();
        return row;
    }

    BeverageRow setBeverage(BeverageRow br) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(B_NAME, br.getName());
        initialValues.put(B_MAKER, br.getMaker());
        initialValues.put(B_MAKERLOCATION, br.getMakerLocation());
        initialValues.put(B_DESCRIPTION, br.getDescription());
        initialValues.put(B_TAGS, br.getTags());
        initialValues.put(B_PIC, br.getimageUri());
        
        Long newID = db.insert(TBL_BEVERAGE, null, initialValues);
        if (newID > -1) {
        	return getBeverage(newID);
        } else {
        	System.out.print("error inserting Beverage");

			return new BeverageRow(Long.decode("-1"));
        }
    }
    
    void delBeverage(Long rowId) {
        db.delete(TBL_BEVERAGE, B_ID + "=" + rowId.toString(),null);
    }
    
    int updateBeverage(BeverageRow br) {
    	ContentValues updateValues = new ContentValues();
        updateValues.put(B_NAME, br.getName());
        updateValues.put(B_MAKER, br.getMaker());
        updateValues.put(B_MAKERLOCATION, br.getMakerLocation());
        updateValues.put(B_DESCRIPTION, br.getDescription());
        updateValues.put(B_TAGS, br.getTags());
        updateValues.put(B_PIC, br.getimageUri());
        
        
    	return db.update(TBL_BEVERAGE, updateValues, B_ID + "=" + br._id,null);
    }
    
    public ArrayList<BeverageRow> getAllBeverages() {
        ArrayList<BeverageRow> ret = new ArrayList<BeverageRow>();
        try {
            Cursor c =
                db.query(TBL_BEVERAGE, new String[] {
                    B_ID, B_NAME, B_MAKER, B_MAKERLOCATION, B_DESCRIPTION, B_TAGS, B_R_OVERALL, B_PIC}, null, null, null, null, B_NAME);
            int numRows = c.getCount();
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                BeverageRow row = new BeverageRow(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6),c.getString(7));
                ret.add(row);
                c.moveToNext();
            }
            c.close();
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        
        return ret;
    }

    public BeverageRow getOneBeverage(String _name) {
		BeverageRow row = null;
		Cursor c =
				db.query(TBL_BEVERAGE, new String[] {
							B_ID, B_NAME, B_MAKER, B_MAKERLOCATION, B_DESCRIPTION, B_TAGS, B_R_OVERALL, B_PIC},
							B_NAME + "=?",
						new String[] {_name.toString()},
						null, null, null, null);
		if (c.getCount() > 0) {
			c.moveToFirst();
			row = new BeverageRow(c.getLong(0),c.getString(1),c.getString(2),c.getString(3),c.getString(4),c.getString(5),c.getInt(6),c.getString(7));
		} else {
			row = new BeverageRow(Long.decode("-1"));
		}
		c.close();
		return row;
	}

    public ArrayList<DrinkRow> getAllDrinks() {
    	ArrayList<DrinkRow> ret = new ArrayList<DrinkRow>();
        try {
            Cursor c =
                db.query(TBL_DRINK, new String[] {
                    D_ID, D_BID, "(strftime('%s', " + D_DATE + ") * 1000) AS " + D_DATE}, null, null, null, null, D_DATE + " desc", "150");
            int numRows = c.getCount();
            c.moveToFirst();

            Long dateTimeMillis = null;
            Date d = null;
            BeverageRow br = null;
            
            for (int i = 0; i < numRows; ++i) {
                dateTimeMillis = c.getLong(2);
                d = new Date(dateTimeMillis);
                br = getBeverage(c.getLong(1));

                DrinkRow row = new DrinkRow(c.getLong(0),br,d);
                ret.add(row);
                c.moveToNext();
            }
            c.close();
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        return ret;
    }

    ArrayList<String> getAllMakers() {
        ArrayList<String> ret = new ArrayList<String>();
        try {
            Cursor c =
                db.query(TBL_BEVERAGE, new String[] {
                    B_MAKER}, null, null, B_MAKER, null, B_MAKER);
            int numRows = c.getCount();
            c.moveToFirst();
            for (int i = 0; i < numRows; ++i) {
                ret.add(c.getString(0));
                c.moveToNext();
            }
            c.close();
        } catch (SQLException e) {
            Log.e("Exception on query", e.toString());
        }
        
        return ret;
    	
    }

    String getMakersLocation(String maker) {
    	String ret = null;
        Cursor c =
            db.query(TBL_BEVERAGE, 
            		new String[] {B_MAKER, B_MAKERLOCATION},
            		//B_MAKER + "='" + maker + "'", 
            		B_MAKER + "=?",
            		new String[] {maker},
            		null,
            		null, 
            		null, 
            		null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            ret = c.getString(1);
        } else {
        	ret = "";
        }
        c.close();
        return ret;    
    }
    
    void delDrink(Long rowId) {
    	db.delete(TBL_DRINK, D_ID + "=" + rowId.toString(),null);
    }
	void delDrinkFromBeverage(Long beverageId) {
		db.delete(TBL_DRINK, D_BID+"=?", new String[] {beverageId.toString()});
	}

    private DrinkRow getDrink(Long rowId) {
    	DrinkRow row = null;
        Cursor c =
            db.query(TBL_DRINK, new String[] {
            		D_ID, D_BID, "(strftime('%s', " + D_DATE + ") * 1000) AS " + D_DATE}, "_id=?", 
            		new String[] {rowId.toString()},
            		null, null, null, null);
        if (c.getCount() > 0) {
            c.moveToFirst();
            
            Long dateTimeMillis = c.getLong(2);
            Date d = new Date(dateTimeMillis);
            BeverageRow br = getBeverage(c.getLong(1));
            
            row = new DrinkRow(c.getLong(0),br,d);
                
        } else {
        	row = new DrinkRow(Long.decode("-1"));
        }
        c.close();
        return row;
    }
	private static List<String> GetColumns(SQLiteDatabase db, String tableName) {
        List<String> ar = null;
        Cursor c = null;
        try {
            c = db.rawQuery("select * from " + tableName + " limit 1", null);
            if (c != null) {
                ar = new ArrayList<String>(Arrays.asList(c.getColumnNames()));
            }
        } catch (Exception e) {
            Log.v(tableName, e.getMessage(), e);
            e.printStackTrace();
        } finally {
            if (c != null)
                c.close();
        }
        return ar;
    }

    @NonNull
	private static String join(List<String> list, String delim) {
        StringBuilder buf = new StringBuilder();
        int num = list.size();
        for (int i = 0; i < num; i++) {
            if (i != 0)
                buf.append(delim);
            buf.append(list.get(i));
        }
        return buf.toString();
    }    
    public DrinkRow setDrink(DrinkRow dr) {
    	ContentValues initialValues = new ContentValues();
        initialValues.put(D_BID, dr.getBeverage().get_id().toString());
        Long newID = db.insert(TBL_DRINK, null, initialValues);
        if (newID > -1) {
        	return getDrink(newID);
        } else {
        	System.out.print("error inserting Beverage");

			return new DrinkRow(Long.decode("-1"));
        }
    }
    
    public void toXML() throws IOException {
    		DatabaseMover xmlout = new DatabaseMover(db, App.getAppContext());
    		xmlout.export(DATABASE_NAME, (String) App.getAppContext().getText(R.string.app_name));
    }
    public boolean toCSV() throws IOException {
    	DatabaseMover csvout = new DatabaseMover(db, App.getAppContext());
		if (csvout.sdAvailable()) {
			csvout.exportCsvTable(DATABASE_NAME, (String) App.getAppContext().getText(R.string.app_name),TBL_BEVERAGE);
			return true;
		} else {
			return false;
		}
    }
    public boolean fromCSV() throws IOException {
    	DatabaseMover csvin = new DatabaseMover(db, App.getAppContext());
		if (csvin.sdAvailable()) {
			csvin.importCsvTable(DATABASE_NAME, (String) App.getAppContext().getText(R.string.app_name),TBL_BEVERAGE);
			return true;
		} else {
			return false;
		}
    }
}
