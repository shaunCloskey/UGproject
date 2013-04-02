package com.shaun.game;

import java.util.ArrayList;

import utilites.Survivor;
import utilites.Survivors;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Databaseknown {

	public static final String KEY_SURVIVORID = "_id";
	public static final String KEY_SAVENAME = "name";
	private static final String KEY_SURVIVORSNAME = "survivor_name";
	private static final String KEY_USED = "used";
	
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "usedData";
	private static final String DATABASE_KNOWN = "use_table";
	
	
	private DbHelper theHelper;
	private final Context theContext;
	private SQLiteDatabase theDataBase;
	
	
	
	public Databaseknown(Context c){
		theContext = c;
	}
	
	
	public static class DbHelper extends SQLiteOpenHelper{

		public DbHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
			
		//this method uses sql to set up the database tables passing the correct keys into the correct tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL("CREATE TABLE " + DATABASE_KNOWN + " (" +
					KEY_SURVIVORID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_SAVENAME + " TEXT NOT NULL, " +
					KEY_SURVIVORSNAME + " TEXT NOT NULL, " +
					KEY_USED + " INTEGER);"
				);
		}
			
			
		//this method handles the upgrading of the database table
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXSITS " + DATABASE_KNOWN);
			onCreate(db);
		}		
	}
	
	public long createSurEntry(String saveName, String name, int used)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_SAVENAME, saveName);
		cv.put(KEY_SURVIVORSNAME, name);
		cv.put(KEY_USED, used);

		
		return theDataBase.insert(DATABASE_KNOWN, null, cv);
	}
	
	public Databaseknown writeOpen() throws SQLException{
		theHelper = new DbHelper(theContext);
		theDataBase = theHelper.getWritableDatabase();
		return this;
	}
	
	
	//opens it but readable
	public Databaseknown readOpen() throws SQLException{
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
	
	public ArrayList<String> getUsedSur(String saveName, Survivors survivors)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_KNOWN + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
		int survNameIndex = c.getColumnIndex(KEY_SURVIVORSNAME);
		int usedIndex = c.getColumnIndex(KEY_USED);
		
		ArrayList<String> usedSur = new ArrayList<String>();
		
		//iterate over the size of the cursor c and add all details to a list of survivors
		
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			int isUsed = c.getInt(usedIndex);
			String survName = c.getString(survNameIndex);
			if(isUsed == 1)
			{
				usedSur.add(survName);
			}
		}
		return usedSur;
	}
	
	public boolean isEmpty(){
		// the cursor is used to query the database
		String [] data = {KEY_SAVENAME};
		Cursor c = theDataBase.query(DATABASE_KNOWN, data, null, null, null, null, null);
		int size = 0;
		c = theDataBase.query(DATABASE_KNOWN, data, null, null, null, null, null);
		
		size = 0;
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
	
	
	public void updateEntry(String name, Survivors survivors, ArrayList<String> usedSur ) {

		//this will set up content values that use the keys to correctly address the passed arguments to the data base when update is called on it
		for(Survivor survivor: survivors.getSurvivors())
		{	
			ContentValues cvSurupdate = new ContentValues();
			cvSurupdate.put(KEY_SURVIVORSNAME, survivor.getName());
			boolean isUsed = false;
			for(String surName: usedSur)
			{
				if(surName.equals(survivor.getName()))
				{
					isUsed = true;
					break;
				}
			}
			if(isUsed)
			{
				cvSurupdate.put(KEY_USED, 1);
			}else{
				cvSurupdate.put(KEY_USED, 0);
			}
			
			theDataBase.update(DATABASE_KNOWN, cvSurupdate, KEY_SAVENAME + "='" + name +"'" + " AND KEY_SURVIVORNAME" + "='" +  survivor.getName() + "'", null);
		}
	}
	
	public void removeEntry(String saveName) {
		theDataBase.delete(DATABASE_KNOWN, KEY_SAVENAME + "='" + saveName+"'", null);
	}

	/**
	 * check to see if the database contains entry with save name
	 * @param name save name
	 * @return boolean 
	 */
	public boolean containsName(String name) {
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_KNOWN + " where " + KEY_SAVENAME + "='" + name + "'" , null);
		if(c.getCount() == 0)
		{
			return false;
		}else{
			return true;
		}
	}

	public ArrayList<String> getKnownSurvivor(String saveName) {
		
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_KNOWN + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
				
		int survNameIndex = c.getColumnIndex(KEY_SURVIVORSNAME);
				
		ArrayList<String> survivors = new ArrayList<String>();
		//iterate over the size of the cursor c and add all details to a list of survivors
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			String surName = c.getString(survNameIndex);
			survivors.add(surName);
		}
		return survivors;
	}	
}
