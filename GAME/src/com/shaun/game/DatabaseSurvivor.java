package com.shaun.game;

import utilites.Survivor;
import utilites.Survivors;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class DatabaseSurvivor{
	
	public static final String KEY_SURVIVORID = "_id";
	public static final String KEY_SAVENAME = "name";
	private static final String KEY_SURVIVORSNAME = "survivor_name";
	private static final String KEY_SURVIVORMOB = "survivor_mob";
	private static final String KEY_SURVIVORBUILDING = "survivor_build";
	private static final String KEY_SURVIVORMETAB = "survivor_metab";
	private static final String KEY_SURVIVORSCAV = "survivor_scav";
	private static final String KEY_SURVIVORX = "sur_xpoint";
	private static final String KEY_SURVIVORY = "sur_ypoint";
	
	
	private static final int DATABASE_VERSION = 1;
	private static final String DATABASE_NAME = "SurData";
	private static final String DATABASE_SURVIVORS = "survivors_table";
	
	
	private DbSurHelper theHelper;
	private final Context theContext;
	private SQLiteDatabase theDataBase;
	
	
	
	public DatabaseSurvivor(Context c){
		theContext = c;
	}
	
	
	public static class DbSurHelper extends SQLiteOpenHelper{

		public DbSurHelper(Context context) {
			super(context, DATABASE_NAME, null, DATABASE_VERSION);
		}
			
		//this method uses sql to set up the database tables passing the correct keys into the correct tables
		@Override
		public void onCreate(SQLiteDatabase db) {
			
			db.execSQL("CREATE TABLE " + DATABASE_SURVIVORS + " (" +
					KEY_SURVIVORID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
					KEY_SAVENAME + " TEXT NOT NULL, " +
					KEY_SURVIVORSNAME + " TEXT NOT NULL, " +
					KEY_SURVIVORMOB + " INTEGER, " +
					KEY_SURVIVORSCAV + " INTEGER, " +
					KEY_SURVIVORBUILDING + " INTEGER, " + 
					KEY_SURVIVORMETAB + " INTEGER, " +
					KEY_SURVIVORX + " INTEGER, " +
					KEY_SURVIVORY + " INTEGER);"
				);
		}
			
			
		//this method handles the upgrading of the database table
		@Override
		public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
			db.execSQL("DROP TABLE IF EXSITS " + DATABASE_SURVIVORS);
			onCreate(db);
		}		
	}
	
	public long createSurEntry(String saveName, int build, int metab, int mob, int scav, String name, int x, int y)
	{
		ContentValues cv = new ContentValues();
		cv.put(KEY_SAVENAME, saveName);
		cv.put(KEY_SURVIVORSNAME, name);
		cv.put(KEY_SURVIVORMOB, mob);
		cv.put(KEY_SURVIVORBUILDING, build);
		cv.put(KEY_SURVIVORMETAB, metab);
		cv.put(KEY_SURVIVORSCAV, scav);
		cv.put(KEY_SURVIVORX, x);
		cv.put(KEY_SURVIVORY, y);
		
		return theDataBase.insert(DATABASE_SURVIVORS, null, cv);
	}
	
	public DatabaseSurvivor writeOpen() throws SQLException{
		theHelper = new DbSurHelper(theContext);
		theDataBase = theHelper.getWritableDatabase();
		return this;
	}
	
	
	//opens it but readable
	public DatabaseSurvivor readOpen() throws SQLException{
		theHelper = new DbSurHelper(theContext);
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
	
	public Survivors getSurvivors(String saveName)
	{
		//TODO the database does not recognise the colum for scav X and Y need to fix this.
		//cursor is used in all get methods to query the database, a switch is used to make sure it is checking the correct database table 
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SURVIVORS + " where " + KEY_SAVENAME + "='" + saveName + "'" , null);
		
		int buildIndex = c.getColumnIndex(KEY_SURVIVORBUILDING);
		int scavIndex = c.getColumnIndex(KEY_SURVIVORSCAV);
		int mobIndex = c.getColumnIndex(KEY_SURVIVORMOB);
		int metabIndex = c.getColumnIndex(KEY_SURVIVORMETAB);
		int survNameIndex = c.getColumnIndex(KEY_SURVIVORSNAME);
		int survivorx = c.getColumnIndex(KEY_SURVIVORX);
		int survivory = c.getColumnIndex(KEY_SURVIVORY);
		
		Survivors survivors = new Survivors();
		int index  = 0;
		//iterate over the size of the cursor c and add all details to a list of survivors
		for(c.moveToFirst(); !c.isAfterLast(); c.moveToNext())
		{
			int build = c.getInt(buildIndex);
			int scav =  c.getInt(scavIndex);
			int mob = c.getInt(mobIndex);
			int metab = c.getInt(metabIndex);
			int x = c.getInt(survivorx);
			int y = c.getInt(survivory);
			String survName = c.getString(survNameIndex);
			Survivor survivor = new Survivor(mob, scav, build, metab, survName, x, y);
			if(index<5)
			{
				survivors.setSurvivor(survivor, index);
			}
			index++;
		}
		return survivors;
	}
	
	
	public void updateEntry(String name, Survivors survivors ) {

		//this will set up content values that use the keys to correctly address the passed arguments to the data base when update is called on it
		for(Survivor survivor: survivors.getSurvivors())
		{	
			ContentValues cvSurupdate = new ContentValues();
			cvSurupdate.put(KEY_SURVIVORSNAME, survivor.getName());
			cvSurupdate.put( KEY_SURVIVORMOB, survivor.getMob());
			cvSurupdate.put( KEY_SURVIVORBUILDING, survivor.getbuilding());
			cvSurupdate.put( KEY_SURVIVORMETAB, survivor.getMet());
			cvSurupdate.put(KEY_SURVIVORSCAV, survivor.getScav());
			cvSurupdate.put( KEY_SURVIVORX, survivor.getX());
			cvSurupdate.put( KEY_SURVIVORY, survivor.getY());
			theDataBase.update(DATABASE_SURVIVORS, cvSurupdate, KEY_SAVENAME + "='" + name +"'" + " AND KEY_SURVIVORNAME" + "='" +  survivor.getName() + "'", null);
		}
	}
	
	public void removeEntry(String saveName) {
		theDataBase.delete(DATABASE_SURVIVORS, KEY_SAVENAME + "='" + saveName+"'", null);
	}

	/**
	 * check to see if the database contains entry with save name
	 * @param name save name
	 * @return boolean 
	 */
	public boolean containsName(String name) {
		Cursor c = this.theDataBase.rawQuery("select * from " + DATABASE_SURVIVORS + " where " + KEY_SAVENAME + "='" + name + "'" , null);
		if(c.getCount() == 0)
		{
			return false;
		}else{
			return true;
		}
	}
	
	
}
