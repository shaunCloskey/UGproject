package com.shaun.game;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

/*
 * this is a database that stores all the info for playing the game,
 *  stores survivours, there location, safe points,
 *  the database is ided by save game name, the name holds list of survivors, list of safe locations 
 */
public class DatabaseSave{

	//this is the boolean value that is used to tell if a table is empty
	public boolean ISNOT = false;
	
	//this sets up the keys for the columns of the tables of the database
	public static final String KEY_ID = "_id";
	public static final String KEY_SAVENAME = "name";
	public static final String KEY_SURVIVORS = "survivors";
	public static final String KEY_SAFEPOINTS = "built_point";
	public static final String KEY_FOODCOUNT = "food_count";
	public static final String KEY_TURNCOUNT = "turn_count";
	public static final String KEY_RESOURCECOUNT = "resource_count";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SaveData";
	private static final String DATABASE_SAVE = "save_table";
	
	//this gives us a database helper that handles the opening and closing of the data base
	private DbHelper theHelper;
	private final Context theContext;
	private SQLiteDatabase theDataBase;
	
	
	public DatabaseSave(Context c){
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

			db.execSQL("CREATE TABLE " + DATABASE_SAVE + " (" +
					KEY_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_SAVENAME + " TEXT NOT NULL, " +
					KEY_FOODCOUNT + " INTEGER, " +
					KEY_TURNCOUNT + " INTEGER, " +
					KEY_RESOURCECOUNT + " INTEGER);"
				);
		}
			
			
		//this method handles the upgrading of the database table
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SAVE);
			onCreate(db);
		}		
	}
	
	
	//this class handles adding a new entry to the database
	public long  createEntry(String saveName, int foodCount, int turnCount, int resourceCount)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_SAVENAME, saveName);
		cv.put(KEY_FOODCOUNT, foodCount);
		cv.put(KEY_TURNCOUNT, turnCount);
		cv.put(KEY_RESOURCECOUNT, resourceCount);
		return theDataBase.insert(DATABASE_SAVE, null, cv);
	}
	
	public DatabaseSave writeOpen() throws SQLException{
		theHelper = new DbHelper(theContext);
		theDataBase = theHelper.getWritableDatabase();
		return this;
	}
	
	
	//opens it but readable
	public DatabaseSave readOpen() throws SQLException{
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
	
	
	public boolean isEmpty(){
		// the cursor is used to query the database
		String [] data = {KEY_SAVENAME};
		Cursor c = theDataBase.query(DATABASE_SAVE, data, null, null, null, null, null);
		int size = 0;
		c = theDataBase.query(DATABASE_SAVE, data, null, null, null, null, null);
		
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
	
	
	//this method handles getting the names of locations from the database
	public String [] getSaveNames(){
		String [] data = {KEY_SAVENAME};
		int size = 0;
			
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = theDataBase.query(DATABASE_SAVE, data, null, null, null, null, null);
			
		// the for loop is used to set up size which is used to ensure that the return array is of the correct size
		c = theDataBase.query(DATABASE_SAVE, data, null, null, null, null, null);
			
		size = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			size = size+1;
		}
			
		//the return array is setup
		String [] Names = new String [size];
		String name = "";
		int nameIndex = c.getColumnIndex(KEY_SAVENAME);
		int index = 0;
	
		//the for loop will move through the database using c.setString() to set name and then it passes this string into the correct place in the array name
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			name = c.getString(nameIndex);
			Names[index] = name;
			index = index+1;
		}		
		return Names;
	}
	
	public int getResourceCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
		
		int resourceIndex = c.getColumnIndex(KEY_RESOURCECOUNT);
		int nameIndex = c.getColumnIndex(KEY_SAVENAME);
		
		int resource = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			if(c.getString(nameIndex).equals(saveName))
			{
				resource = c.getInt(resourceIndex);
			}
		}
		return resource;
	}
	
	
	public int getFoodCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
		
		int foodIndex = c.getColumnIndex(KEY_FOODCOUNT);
		int nameIndex = c.getColumnIndex(KEY_SAVENAME);
		
		int food = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			if(c.getString(nameIndex).equals(saveName))
			{
				food = c.getInt(foodIndex);
			}
		}
		return food;
	}
	
	
	public int getTurnCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
								
		int turnIndex = c.getColumnIndex(KEY_TURNCOUNT);
		int nameIndex = c.getColumnIndex(KEY_SAVENAME);
		
		int turn = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			if(c.getString(nameIndex).equals(saveName))
			{
				turn = c.getInt(turnIndex);
			}
		}
		return turn;
	}
	
	public void updateEntry(String name, int foodCount, int turnCount, int resourceCount) {
		//this will set up content values that use the keys to correctly address the passed arguments to the data base when update is called on it
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_FOODCOUNT, foodCount);
		cvUpdate.put(KEY_TURNCOUNT, turnCount);
		cvUpdate.put(KEY_RESOURCECOUNT, resourceCount);
		
		theDataBase.update(DATABASE_SAVE, cvUpdate, KEY_SAVENAME + "='" + name +"'", null);
	}
	
	
	public int getSize(int type) {

		String [] data = {KEY_SAVENAME};
		int size = 0;
		Cursor c = theDataBase.query(DATABASE_SAVE, data, null, null, null, null, null);
		size = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext()){
			size = size+1;
		}
		return size;
	}
	
	
	public void removeEntry(String saveName) {
		theDataBase.delete(DATABASE_SAVE, KEY_SAVENAME + "='" + saveName +"'", null);
	}

	/**
	 * check to see if the database contains entry with save name
	 * @param name save name
	 * @return boolean 
	 */
	public boolean containsName(String name) {
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + name + "'" , null);
		if(c.getCount() == 0)
		{
			return false;
		}else{
			return true;
		}
	}
	
}
