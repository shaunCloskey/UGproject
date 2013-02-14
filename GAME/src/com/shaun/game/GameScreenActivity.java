package com.shaun.game;

import utilites.Survivor;
import utilites.Survivors;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GameScreenActivity extends Activity {
	
	
	private String name;
	private String newSave = "newGame";
	String empty = "empty";
	
	private Button tFood;
	private Button tResource;
	private Button tTurn;
	private Button tSurvivorCount;
	
	private Button bPause;
	private Button bHome;
	private Button bQuit;
	private Button bSave;
	
	private DrawView drawView;
	
	private int food;
	private int resource;
	private int turn;
	private int survivorCount;
	
	private static final String TAG = "MyActivity";
	private Survivors survivors;
	
	private DatabaseSave database = new DatabaseSave(GameScreenActivity.this);
	private DatabaseSurvivor surData = new DatabaseSurvivor(GameScreenActivity.this); 
	
	 public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         // requesting to turn the title OFF
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         // making it full screen
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // set our MainGamePanel as the View
         setContentView(R.layout.game_screen);
         
         
         drawView = (DrawView) findViewById(R.id.map);
         drawView.giveContext(this);
         Bundle extras = getIntent().getExtras();
         name  = extras.getString("saveName");
         
         database.writeOpen();
         food = database.getFoodCount(name);
         resource = database.getResourceCount(name);
         turn = database.getTurnCount(name);
         database.writeClose();
         
         tFood = (Button) findViewById(R.id.tFoodCount);
         tFood.setText("food:" + food);
         
         tResource = (Button) findViewById(R.id.tResourcesCount);
         tResource.setText("res:" + resource);
         
         tTurn = (Button) findViewById(R.id.tTurnsCount);
         tTurn.setText("turn:" + turn);
         
         tSurvivorCount = (Button) findViewById(R.id.tSurCount);
         tSurvivorCount.setText("NO surv:" + survivorCount);
         
         //take the database survivor info and store it
         surData.writeOpen();
         survivors = surData.getSurvivors(name);
         surData.writeClose();
         
         Log.d(TAG, "about to set survivors in draw view");
         drawView.setSurvivors(survivors);
         Survivor [] sur = survivors.getSurvivors();
         Log.d(TAG, "survivors = " + sur[0].getName());
         
         
         bPause = (Button) findViewById(R.id.bPause);
         bPause.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				// TODO Auto-generated method stub
				
			}
        	 
         });
         
         
         bQuit = (Button) findViewById(R.id.bQuit);
         
         bHome = (Button) findViewById(R.id.bHome);
         
         bSave = (Button) findViewById(R.id.bSave);
         bSave.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//Auto-generated method stub
				/*
				 * check if save name == newGame
				 * if it does prompt user for new name to save
				 * check this name with the list of names in the database
				 * if the name exists ask if the user wants to overwite the save file
				 * if the number of saves =5 ask them which game name they want to remove to make space
				 * 
				 */
				
				if(name.equals(newSave))
				{
					// name the save
					promptUser();
				}else{
					// replace the database info with current progress
					updateDataBase(name);
				}
				
			}
		});
	 }
	 
	 private void promptUser()
	 {
		 
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("Alert");
			alert.setMessage("what would you like to name the save file");

			// Set an EditText view to get user input 
			final EditText input = new EditText(this);
			alert.setView(input);

			
			alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				String value = input.getText().toString();
				// check if value is in database
				database.writeOpen();
				if(database.containsName(value))
				{
					// prompt if user wants to rewrite save file or cancel
					database.writeClose();
					promptOverwrite(value);
					
				}else{
					String [] saveTemp = database.getSaveNames();
					database.writeClose();
					if((saveTemp.length - 1)==5)
					{
						//TODO tell user that there are two many saves ask if they wish to delete one
						
					}else{
						//save new info to the database
						saveNewGame(value);
					}
				}
			
			}
			});

			alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			     // Canceled.
			}
			});

			 alert.show();
	 }
	 
	 /**
	  * replace info on tables with new info
	  * @param name save name
	  */
	 private void updateDataBase(String name) {
		database.writeOpen();
		database.updateEntry(name, food, turn, resource);
		database.writeClose();
		
		survivors = drawView.getSurvivors();
		
		surData.writeOpen();
		//remove all entries where savename = name
		surData.removeEntry(name);
		
		for(Survivor survivor: survivors.getSurvivors())
		{
			if(!survivor.getName().equals(empty))
			{
				Log.d(TAG, "sur name: "+survivor.getName());
				Log.d(TAG, "sur build: "+survivor.getbuilding());
				Log.d(TAG, "sur met: "+survivor.getMet());
				Log.d(TAG, "sur mob: "+survivor.getMob());
				
				surData.createSurEntry(name, survivor.getbuilding(), survivor.getMet(), survivor.getMob(), survivor.getScav(), survivor.getName(), survivor.getX(), survivor.getY());
			}
		}
		surData.writeClose();
	 }	
	 
	 /**
	  * create new entry to the databases
	  * @param name save name 
	  */
	 private void saveNewGame(String name) {
		 database.writeOpen();
		 database.createEntry(name, food, turn, resource);
		 database.writeClose();
		 
		 surData.writeOpen();
		 for(Survivor survivor: survivors.getSurvivors())
			{
				if(!survivor.getName().equals(empty))
				{
					Log.d(TAG, "sur name: "+survivor.getName());
					Log.d(TAG, "sur build: "+survivor.getbuilding());
					Log.d(TAG, "sur met: "+survivor.getMet());
					Log.d(TAG, "sur mob: "+survivor.getMob());
					
					surData.createSurEntry(name, survivor.getbuilding(), survivor.getMet(), survivor.getMob(), survivor.getScav(), survivor.getName(), survivor.getX(), survivor.getY());
				}
			}
		 surData.writeClose();
		 
	}
	 
	 private void promptOverwrite(String value) {
		 final String name = value;
		 AlertDialog.Builder alert = new AlertDialog.Builder(this);

			alert.setTitle("save name already exists!");
			alert.setMessage("do you want to overite the save file?");

			alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int whichButton) {
				updateDataBase(name);
			}
			});

			alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
			 public void onClick(DialogInterface dialog, int whichButton) {
			     // Canceled.
			}
			});

			 alert.show();
	 }
	 
	 
}
