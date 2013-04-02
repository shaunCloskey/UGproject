package com.shaun.game;

import java.util.ArrayList;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.graphics.Point;

public class DatabaseSafe {

		public boolean ISNOT = false;
		
		//this sets up the keys for the columns of the tables of the database
		public static final String KEY_ID = "_id";
		public static final String KEY_SAVENAME = "name";
		public static final String KEY_XPOINT = "safe_x";
		public static final String KEY_YPOINT = "safe_y";
	
		private static final int DATABASE_VERSION = 1;
		private static final String DATABASE_NAME = "safe_data";
		private static final String DATABASE_SAFE = "safe_table";
	
		//this gives us a database helper that handles the opening and closing of the data base
		private DbHelper theHelper;
		private final Context theContext;
		private SQLiteDatabase theDataBase;
	
	
		public DatabaseSafe(Context c){
			theContext = c;
		}
	
	
		//this is the constructor for the database helper
		public static class DbHelper extends SQLiteOpenHelper{
			
			public DbHelper(Context context) {
				super(context, DATABASE_NAME, null, DATABASE_VERSION);
			
			}

			
			//this method uses sql to set up the database tables passing the correct keys into the correct tables
			@Override
			public void onCreate(SQLiteDatabase db) {

				db.execSQL("CREATE TABLE " + DATABASE_SAFE + " (" +
						KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
						KEY_SAVENAME + " TEXT NOT NULL, " +
						KEY_XPOINT + " INTEGER, " +
						KEY_YPOINT + " INTEGER);"
						);	
			}
			
			
			//this method handles the upgrading of the database table
			@Override
			public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
				db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SAFE);
				onCreate(db);
			}		
		}
	
		//this class handles adding a new entry to the database
		public long  createEntry(String saveName, Point point)
		{
			ContentValues cv = new ContentValues();
			cv.put(KEY_SAVENAME, saveName);
			cv.put(KEY_XPOINT, point.x);
			cv.put(KEY_YPOINT, point.y);
			return theDataBase.insert(DATABASE_SAFE, null, cv);
		}
		
		public DatabaseSafe writeOpen() throws SQLException{
			theHelper = new DbHelper(theContext);
			theDataBase = theHelper.getWritableDatabase();
			return this;
		}
		
		
		//opens it but readable
		public DatabaseSafe readOpen() throws SQLException{
			theHelper = new DbHelper(theContext);
			theDataBase = theHelper.getReadableDatabase();
			return this;
		}
		
		
		//closes the table for write
		public void writeClose(){
			if(theHelper!=null){
				theHelper.close();
			}
		}
		
		
		//closes the table for read
		public void readClose(){
			if(theHelper!=null){
				theHelper.close();
			}
		}
		
		
		//deletes data base
		public boolean deletedb(){
			theContext.deleteDatabase(DATABASE_NAME);
			return true;
		}
		
		
		public boolean isEmpty(String name){
			// the cursor is used to query the database
			Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAFE + " where " + KEY_SAVENAME + "='" + name + "'" , null);
			
			int size = 0;
			//the for statement will move through the database table and for each entry increment size
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				size = size+1;
			}
			//after the for loop is finished if size is any value but 0 the table is not empty 
			if(size ==0){
				return true;
			}else{
				return false;
			}
		}
		
		
		//this method handles getting the names of locations from the database
		public ArrayList<Point> getSafePoints(String name){
			
			// the for loop is used to set up size which is used to ensure that the return array is of the correct size
			Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAFE + " where " + KEY_SAVENAME + "='" + name + "'" , null);

			
			int xIndex = c.getColumnIndex(KEY_XPOINT);
			int yIndex = c.getColumnIndex(KEY_YPOINT);

			int x = 0;
			int y = 0;
			ArrayList<Point> safePoints = new ArrayList<Point>();

			
			//the for loop will move through the database using c.setString() to set name and then it passes this string into the correct place in the array name
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){

				x = c.getInt(xIndex);
				y = c.getInt(yIndex);
				Point point = new Point(x,y);
				safePoints.add(point);
			}

			return safePoints;
		}
		
		
		public void updateEntry(String name, Point point) {
			//this will set up content values that use the keys to correctly address the passed arguments to the data base when update is called on it
			ContentValues cvUpdate = new ContentValues();
			cvUpdate.put(KEY_XPOINT, point.x);
			cvUpdate.put(KEY_YPOINT, point.y);
			
			theDataBase.update(DATABASE_SAFE, cvUpdate, KEY_SAVENAME + "='" + name +"'", null);
		}
		
		
		public int getSize(int type) {

			String [] data = {KEY_SAVENAME};
			int size = 0;
			Cursor c = theDataBase.query(DATABASE_SAFE, data, null, null, null, null, null);
			size = 0;
			for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
				size = size+1;
			}
			return size;
		}
		
		
		public void removeEntry(String saveName, int type) {
			theDataBase.delete(DATABASE_SAFE, KEY_SAVENAME + "='" + saveName +"'", null);
		}

		/**
		 * check to see if the database contains entry with save name
		 * @param name save name
		 * @return boolean 
		 */
		public boolean containsName(String name) {
			Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAFE + " where " + KEY_SAVENAME + "='" + name + "'" , null);
			if(c.getCount() == 0)
			{
				return false;
			}else{
				return true;
			}
		}
		
		public boolean cotainsPoint(String name, Point point)
		{
			Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAFE + " where " + KEY_SAVENAME + "='" + name + "'" + " AND KEY_XPOINT" + "='" +  point.x + "'" + " AND KEY_YPOINT" + "='" +  point.y + "'" , null);
			if(c.getCount()==0)
			{
				return false;
			}else{
				return true;
			}
		}

		public void removeEntry(String saveName) {
			theDataBase.delete(DATABASE_SAFE, KEY_SAVENAME + "='" + saveName+"'", null);
			
		}
		
		
	}
