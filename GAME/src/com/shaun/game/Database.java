package com.shaun.game;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import utilites.SafePoints;
import utilites.Survivor;
import utilites.Survivors;

/*
 * this is a database that stores all the info for playing the game,
 *  stores survivours, there location, safe points,
 *  the database is ided by save game name, the name holds list of survivors, list of safe locations 
 */
public class Database extends Activity {

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
	
	public static final String KEY_SURVIVORID = "_id";
	private static final String KEY_SURVIVORSNAME = "survivor_name";
	private static final String KEY_SURVIVORMOB = "survivor_mob";
	private static final String KEY_SURVIVORBUILDING = "survivor_build";
	private static final String KEY_SURVIVORMETAB = "survivor_metab";
	private static final String KEY_SURVIVORSCAV = "survivor_scav";
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SaveDataBase";
	private static final String DATABASE_SAVE = "save_table";
	private static final String DATABASE_SURVIVORS = "survivors_table";
	
	//this gives us a database helper that handles the opening and closing of the data base
	private DbHelper theHelper;
	private final Context theContext;
	private SQLiteDatabase theDataBase;
	
	
	public Database(Context c){
		theContext = c;
	}
	
	
	//this is the constructor for the database helper
	public class DbHelper extends SQLiteOpenHelper{

		


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
			
			db.execSQL("CREATE TABLE " + DATABASE_SURVIVORS + " (" +
					KEY_SURVIVORID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_SAVENAME + " TEXT NOT NULL, " +
					KEY_SURVIVORSNAME + " TEXT NOT NULL, " +
					KEY_SURVIVORMOB + " INTEGER, " +
					KEY_SURVIVORBUILDING + " INTEGER, " + 
					KEY_SURVIVORMETAB + "INTEGER);"
				);
		}
			
			
		//this method handles the upgrading of the database table
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXISTS " + DATABASE_SAVE);
			db.execSQL("DROP TABLE IF EXSITS " + DATABASE_SURVIVORS);
			onCreate(db);
		}		
	}
	
	
	//this class handles adding a new entry to the database
	public void createEntry(String saveName, int foodCount, int turnCount, int resourceCount)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_SAVENAME, saveName);
		cv.put(KEY_FOODCOUNT, foodCount);
		cv.put(KEY_TURNCOUNT, turnCount);
		cv.put(KEY_RESOURCECOUNT, resourceCount);
		theDataBase.insert(DATABASE_SAVE, null, cv);
	}
	
	public void createSurEntry(String saveName, int build, int metab, int mob, int scav, String name)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_SAVENAME, saveName);
		cv.put(KEY_SURVIVORBUILDING, build);
		cv.put(KEY_SURVIVORMETAB, metab);
		cv.put(KEY_SURVIVORMOB, mob);
		cv.put(KEY_SURVIVORSCAV, scav);
		cv.put(KEY_SURVIVORSNAME, name);
		theDataBase.insert(DATABASE_SURVIVORS, null, cv);
	}
	
	
	public Database writeOpen() throws SQLException{
		theHelper = new DbHelper(theContext);
		theDataBase = theHelper.getWritableDatabase();
		return this;
	}
	
	
	//opens it but readable
	public Database readOpen() throws SQLException{
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
	public String [] getSaveNames(String saveName){
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
	
	
	public Survivors getSurvivors(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SURVIVORS + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
		
		int buildIndex = c.getColumnIndex(KEY_SURVIVORBUILDING);
		int scavIndex = c.getColumnIndex(KEY_SURVIVORSCAV);
		int mobIndex = c.getColumnIndex(KEY_SURVIVORMOB);
		int metabIndex = c.getColumnIndex(KEY_SURVIVORMETAB);
		int survNameIndex = c.getColumnIndex(KEY_SURVIVORSNAME);
		
		Survivors survivors = new Survivors();
		int index  = 0;
		//iterate over the size of the cursor c and add all details to a list of survivors
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			int build = c.getInt(buildIndex);
			int scav = c.getInt(scavIndex);
			int mob = c.getInt(mobIndex);
			int metab = c.getInt(metabIndex);
			String survName = c.getString(survNameIndex);
			Survivor survivor = new Survivor(mob, scav, build, metab,  survName);
			survivors.setSurvivor(survivor, index);
			index++;
		}
		
		
		return survivors;
	}
	
	
	/*public SafePoints getSafePoints(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
				
		int index = c.getColumnIndex(KEY_SAFEPOINTS);
		SafePoints safePoints = (SafePoints) SafePoints.deserializeObject(c.getBlob(index));
				
		return safePoints;
	}
	*/
	
	public int getResourceCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
						
		int resource = c.getColumnIndex(KEY_RESOURCECOUNT);
						
		return resource;
	}
	
	
	public int getFoodCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table
		String [] data = {KEY_FOODCOUNT};
		Cursor c = theDataBase.query(DATABASE_SAVE, data , null, null, null, null, null);
		
		int foodIndex = c.getColumnIndex(KEY_FOODCOUNT);
		int food = 0;
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			food = c.getInt(foodIndex);
		}
		return food;
	}
	
	
	public int getTurnCount(String saveName)
	{
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SAVE + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
								
		int turn = c.getColumnIndex(KEY_TURNCOUNT);
								
		return turn;
	}
	
	public void updateEntry(String name, int foodCount, int turnCount, int resourceCount) {

		//this will set up content values that use the keys to correctly address the passed arguments to the data base when update is called on it
		ContentValues cvUpdate = new ContentValues();
		cvUpdate.put(KEY_FOODCOUNT, foodCount);
		cvUpdate.put(KEY_TURNCOUNT, turnCount);
		cvUpdate.put(KEY_RESOURCECOUNT, resourceCount);
		theDataBase.update(DATABASE_SAVE, cvUpdate, KEY_SAVENAME + "=" + name, null);
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
	
	
	public void removeEntry(String saveName, int type) {
		theDataBase.delete(DATABASE_SAVE, KEY_SAVENAME + "=" + saveName, null);
	}
	
}
