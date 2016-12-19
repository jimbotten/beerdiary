package com.beerdiary;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.StringTokenizer;

import com.beerdiary.DBHelper.BeverageRow;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

public class DatabaseMover {
	   private String DATASUBDIRECTORY;
	   private static final String EXPORTER="Exporter";
	   
	   private SQLiteDatabase db;
	   private XmlBuilder xmlBuilder;
	   private CsvBuilder csvBuilder;
		boolean mExternalStorageAvailable = false;
	    boolean mExternalStorageWriteable = false;
	    String state = null;
	    Context context = null;
	    
	   public DatabaseMover(SQLiteDatabase db, Context cx) {
	      this.db = db;
	      this.context = cx;
	      DATASUBDIRECTORY = (String) cx.getText(R.string.app_name);
	        state =Environment.getExternalStorageState();
	        if (Environment.MEDIA_MOUNTED.equals(state)) {
	            // We can read and write the media
	            mExternalStorageAvailable = mExternalStorageWriteable = true;
	        } else if (Environment.MEDIA_MOUNTED_READ_ONLY.equals(state)) {
	            // We can only read the media
	            mExternalStorageAvailable = true;
	            mExternalStorageWriteable = false;
	        } else {
	            // Something else is wrong. It may be one of many other states, but all we need
	            //  to know is we can neither read nor write
	            mExternalStorageAvailable = mExternalStorageWriteable = false;
	        }

	   }

	   public boolean sdAvailable() {
		if (mExternalStorageAvailable && mExternalStorageWriteable) { return true; } else { return false; }
	   }
	   
	   public void export(String dbName, String exportFileNamePrefix) throws IOException {
	      Log.i(EXPORTER, "exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

	      this.xmlBuilder = new XmlBuilder();
	      this.xmlBuilder.start(dbName);

	      // get the tables
	      String sql = "select * from sqlite_master";
	      Cursor c = this.db.rawQuery(sql, new String[0]);
	      Log.d(EXPORTER, "select * from sqlite_master, cur size " + c.getCount());
	      if (c.moveToFirst()) {
	         do {
	            String tableName = c.getString(c.getColumnIndex("name"));
	            Log.d(EXPORTER, "table name " + tableName);

	            // skip metadata, sequence, and uidx (unique indexes)
	            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
	                     && !tableName.startsWith("uidx")) {
	               this.exportTable(tableName);
	            }
	         } while (c.moveToNext());
	      }
	      c.close();
	      String xmlString = this.xmlBuilder.end();
	      this.writeToFile(xmlString, exportFileNamePrefix + ".xml");
	      Log.i(EXPORTER, "exporting database complete");
	   }
	 
	   public void exportCsvTable(String dbName, String exportFileNamePrefix, String exportTableName) throws IOException {
		      Log.i(EXPORTER, "exporting database - " + dbName + " exportFileNamePrefix=" + exportFileNamePrefix);

		      this.csvBuilder = new CsvBuilder();

		      // get the tables
		      String sql = "select * from sqlite_master where tbl_name='" + exportTableName+"'";
		      Cursor c = this.db.rawQuery(sql, new String[0]);
		      // og.d(EXPORTER, "select * from sqlite_master, cur size " + c.getCount());
		      if (c.moveToFirst()) {
		         do {
		            String tableName = c.getString(c.getColumnIndex("name"));
		            Log.d(EXPORTER, "table name " + tableName);

		            // skip metadata, sequence, and uidx (unique indexes)
		            if (!tableName.equals("android_metadata") && !tableName.equals("sqlite_sequence")
		                     && !tableName.startsWith("uidx")) {
		               this.exportCsvTable(tableName);
		            }
		         } while (c.moveToNext());
		      }
		      c.close();
		      String csvString = this.csvBuilder.end();
		      this.writeToFile(csvString, exportFileNamePrefix + ".csv");
		      Log.i(EXPORTER, "exporting database complete");
		   }

	   private void exportTable(final String tableName) throws IOException {
	      Log.d(EXPORTER, "exporting table - " + tableName);
	      this.xmlBuilder.openTable(tableName);
	      String sql = "select * from " + tableName;
	      Cursor c = this.db.rawQuery(sql, new String[0]);
	      if (c.moveToFirst()) {
	         int cols = c.getColumnCount();
	         do {
	            this.xmlBuilder.openRow();
	            for (int i = 0; i < cols; i++) {
	               this.xmlBuilder.addColumn(c.getColumnName(i), c.getString(i));
	            }
	            this.xmlBuilder.closeRow();
	         } while (c.moveToNext());
	      }
	      c.close();
	      this.xmlBuilder.closeTable();
	   }
	   private void exportCsvTable(final String tableName) throws IOException {
		      Log.d(EXPORTER, "exporting csv table - " + tableName);
		      
		      String sql = "select * from " + tableName;
		      Cursor c = this.db.rawQuery(sql, new String[0]);
		      if (c.moveToFirst()) {
		         int cols = c.getColumnCount();
		         do {
		            for (int i = 0; i < cols; i++) {
		               if (i==0) {
		            	   this.csvBuilder.addColumn(c.getString(i));
		               } else {
		            	   this.csvBuilder.addAdditionalColumn(c.getString(i));
		               }
		            }
		            this.csvBuilder.closeRow();
		         } while (c.moveToNext());
		      }
		      c.close();
		      
		   }
	   
	   private String readFromFile(String importFileName) throws IOException {
		   String Directory = Environment.getExternalStorageDirectory() + "/" + DATASUBDIRECTORY;
		   String output = null;
		   
		   try { 
			   File importFile = new File(Directory, importFileName+".csv");
			   
			   FileInputStream fis = new FileInputStream(importFile);
			   DataInputStream dis = new DataInputStream(fis);
			
			   while (dis.available() != 0) {
			        output = output + dis.readLine();
			      }
			   if (output.substring(0,4).contains("null")) {
				   output = output.substring(4, output.length());
			   }
			      fis.close();
			      dis.close();

			    } catch (FileNotFoundException e) {
					Toast.makeText(context,
	      					R.string.import_error_no_file, Toast.LENGTH_LONG).show();

			    } catch (IOException e) {
			      e.printStackTrace();
			    }
			   
		return output;
	   }

	   private void writeToFile(String outString, String exportFileName) throws IOException {
	      File dir = new File(Environment.getExternalStorageDirectory(), DATASUBDIRECTORY);
	      if (!dir.exists()) {
	         dir.mkdirs();
	      }
	      File file = new File(dir, exportFileName);
	      file.createNewFile();

	      ByteBuffer buff = ByteBuffer.wrap(outString.getBytes());
	      FileChannel channel = new FileOutputStream(file).getChannel();
	      try {
	         channel.write(buff);
	      } finally {
	         if (channel != null)
	            channel.close();
	      }
	   }

	   public void importCsvTable(String dbName, String importFileNamePrefix, String importTableName) throws IOException {
		      Log.i(EXPORTER, "importing database - " + dbName + " importFileNamePrefix=" + importFileNamePrefix);
		      String strLine =null;
		      StringTokenizer st = null;
		      String strMaker = null;
		      String strName = null;
		      String strLocation = null;
		      String strDescription = null;
		      String strTags = null;
		      String strFormattedLine = null; 
		      BeverageRow br = null;
		      DBHelper dbH = null;
		      
		      // get the file
		      String file = readFromFile(importFileNamePrefix);
		      if (file != null) {
	    		 String[] lines = file.split("\\\\r\\\\n");
	    		 dbH = DBHelper.getInstance(context);

		      // check each line to see if it exists
		      for(int x=0; x< lines.length; x++) {
		    	  strLine = lines[x];
		    	  
		    	  strFormattedLine = BunkCommas(strLine);
		    	  
		    	  st = new StringTokenizer(strFormattedLine , ",");
		    	  st.nextToken();
		    	  // st.nextToken();
		    	  strName = st.nextToken().replace("~",",");
		    	  strName = strName.substring(1, strName.length()-1);
		    	  strMaker = st.nextToken().replace("~",",");
		    	  strMaker = strMaker.substring(1, strMaker.length()-1);
		    	  strLocation = st.nextToken().replace("~",",");
		    	  strLocation = strLocation.substring(1, strLocation.length()-1);
		    	  strDescription = st.nextToken().replace("~",",");
		    	  strDescription = strDescription.substring(1, strDescription.length()-1);
		    	  strTags = st.nextToken().replace("~",",");
		    	  strTags = strTags.substring(1, strTags.length()-1);
		    	  
		    	  //if this exists
		    	  br = dbH.getBeverage(strName, strMaker);
		    	  // String sql = "select * from " + importTableName + " where NAME='" +strName + "' AND MAKER='" + strMaker + "'";
			      // Cursor c = this.db.rawQuery(sql, new String[0]);
			      // if that query is empty
			      // if (! c.moveToFirst()) {
		    	  if (br.get_id() == -1) {
				         // then insert into the db
			    	  br = new BeverageRow(strName,strMaker,strLocation,strDescription,strTags);
			    	  dbH.setBeverage(br);
			    	  }
			      br = null;
		      	}
		      Log.i(EXPORTER, "importing database complete");
		      }
		   }
	   	private String BunkCommas(String in) {
	   		int len = in.length();
	   		int poscounter = 0;
	   		int start = 0;
	   		int end = 0;
	   		
	   		while (poscounter<len) {
	   			// find a quote
	   			start = in.indexOf("\"", poscounter);
		   		if(start>=0) {
		   			//found one
		   			//find the next
		   			end = in.indexOf("\"", start+1);
		   			//replace all the commas in between
		   			while (in.indexOf(",", start)<end && in.indexOf(",", start)> 0) {
	   				String pre = in.substring(0,in.indexOf(",", start));
	   				String post = in.substring(in.indexOf(",", start)+1,in.length());
		   				in =  pre + "~" + post;
		   			}
		   			poscounter = end+1;
		   			
		   		} else { poscounter = len;}	
	   		}
	   		return in;
	   	}
	class XmlBuilder {
	      private static final String OPEN_XML_STANZA = "<?xml version=\"1.0\" encoding=\"utf-8\"?>";
	      private static final String CLOSE_WITH_TICK = "'>";
	      private static final String DB_OPEN = "<database name='";
	      private static final String DB_CLOSE = "</database>";
	      private static final String TABLE_OPEN = "<table name='";
	      private static final String TABLE_CLOSE = "</table>";
	      private static final String ROW_OPEN = "<row>";
	      private static final String ROW_CLOSE = "</row>";
	      private static final String COL_OPEN = "<col name='";
	      private static final String COL_CLOSE = "</col>";

	      private final StringBuilder sb;

	      public XmlBuilder() throws IOException {
	         this.sb = new StringBuilder();
	      }

	      void start(String dbName) {
	         this.sb.append(OPEN_XML_STANZA);
	         this.sb.append(DB_OPEN + dbName + CLOSE_WITH_TICK);
	      }

	      String end() throws IOException {
	         this.sb.append(DB_CLOSE);
	         return this.sb.toString();
	      }

	      void openTable(String tableName) {
	         this.sb.append(TABLE_OPEN + tableName + CLOSE_WITH_TICK);
	      }

	      void closeTable() {
	         this.sb.append(TABLE_CLOSE);
	      }

	      void openRow() {
	         this.sb.append(ROW_OPEN);
	      }

	      void closeRow() {
	         this.sb.append(ROW_CLOSE);
	      }

	      void addColumn(final String name, final String val) throws IOException {
	         this.sb.append(COL_OPEN + name + CLOSE_WITH_TICK + val + COL_CLOSE);
	      }
	   }

	   class CsvBuilder {
		      private static final String DELIMITER= ",";
		      private static final String PROTECTION= "\"";
		      private static final String ROW_CLOSE = "\\r\\n";
		      
		      private final StringBuilder sb;

		      public CsvBuilder() throws IOException {
		         this.sb = new StringBuilder();
		      }
		      String end() throws IOException {
			         return this.sb.toString();
			      }

		      void closeRow() {
		         this.sb.append(ROW_CLOSE);
		      }
		      void addColumn(final String val) throws IOException {
			         this.sb.append(PROTECTION + val + PROTECTION);
			      }
		      void addAdditionalColumn(final String val) throws IOException {
		         this.sb.append(DELIMITER + PROTECTION + val + PROTECTION);
		      }
		   }

}  
