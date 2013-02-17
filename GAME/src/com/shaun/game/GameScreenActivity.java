package com.shaun.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map.Entry;

import utilites.Survivor;
import utilites.Survivors;
import utilites.probHandler;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

public class GameScreenActivity extends Activity {
	
	
	private String name;
	private String newSave = "newGame";
	String empty = "empty";
	
	private Button tFood;
	private Button tResource;
	private Button tTurn;
	private Button tSurvivorCount;
	
	private Button bEndTurn;
	private Button bHome;
	private Button bQuit;
	private Button bSave;
	
	private DrawView drawView;
	
	private probHandler prob = new probHandler();
	
	private int dogRed = 1;
  	private int banditRed = 2;
  	private int fireRed = 4;
  	private int desertRed = 5;
	
	private int food;
	private int resource;
	private int turn;
	private int survivorCount;
	
	private Context context;
	
	private static final String TAG = "MyActivity";
	private Survivors survivors;
	
	private ArrayList<Point> safePoints = new ArrayList<Point>();	
	private ArrayList<Point> farmPoints = new ArrayList<Point>();
	
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
         
         context = getApplicationContext();
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
         tFood.setTextSize(10);
         
         tResource = (Button) findViewById(R.id.tResourcesCount);
         tResource.setText("resource:" + resource);
         tResource.setTextSize(10);;
         
         tTurn = (Button) findViewById(R.id.tTurnsCount);
         tTurn.setText("turn:" + turn);
         tTurn.setTextSize(10);
         
         //take the database survivor info and store it
         surData.writeOpen();
         survivors = surData.getSurvivors(name);
         surData.writeClose();
         int surCount = 0;
         for(Survivor survivor:survivors.getSurvivors())
         {
        	 if(!survivor.getName().equals(empty))
        	 {
        		 surCount++;
        	 }
         }
         
         survivorCount = surCount;
         
         tSurvivorCount = (Button) findViewById(R.id.tSurCount);
         tSurvivorCount.setText("surv Count:" + survivorCount);
         tSurvivorCount.setTextSize(10);
         
         Log.d(TAG, "about to set survivors in draw view");
         drawView.setSurvivors(survivors);
         Survivor [] sur = survivors.getSurvivors();
         Log.d(TAG, "survivors = " + sur[0].getName());
         
         
         bEndTurn = (Button) findViewById(R.id.bEndTurn);
         bEndTurn.setOnClickListener(new View.OnClickListener(){

			@Override
			public void onClick(View v) {
				//TODO Auto-generated method stub
				turn++;
				betweenTurnEvents(turn);
				update();
				drawView.emptyUsedSur();
				
				
				
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

					Toast toast = Toast.makeText(context, "game saved successfully", Toast.LENGTH_SHORT);
					toast.show();
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
	  * runs though all the probabilities for the game and determines if an event has occured
	  * 
	  */
	 public void betweenTurnEvents(int turn )
	 {
	 	String dogInfo = "";
	 	String banditInfo = "";
	  	String fireInfo = "";
	  	String desertInfo = "";
	  		
	  	int dogCount=0;
	  	int bandCount =0;
	  	int fireCount =0;
	  	int desertCount = 0;

	  	safePoints = drawView.getSafe();
	  	farmPoints = drawView.getFarm();
	  	
	  	survivors = drawView.getSurvivors();
	  	
	  	List<String> toRemove =new ArrayList<String>();
	  	Log.d(TAG, "the number of survivors is "  + OpeningScreenActivity.survivors.size());
	  		
	  		
	  	//run through all survivors and determine if events have occurred
	  		
	  	if(turn > 2)
	  	{
	  		for(Survivor survivor : survivors.getSurvivors() )
	  		{
	  			boolean isSafe = false;
	  			Point surPoint = new Point(survivor.getX(), survivor.getY());
	  			isSafe = safePoints.contains(surPoint);
	  			for(Point point: safePoints)
	  			{
	  				Point testPointUp = new Point(surPoint.x, surPoint.y - 1);
	  				Point testPointDown = new Point(surPoint.x, surPoint.y + 1);
	  				Point testPointLeft = new Point(surPoint.x-1, surPoint.y);
	  				Point testPointRight = new Point(surPoint.x+1, surPoint.y);
	  				
	  				if(surPoint.equals(testPointUp) ||surPoint.equals(testPointDown) ||surPoint.equals(testPointLeft) ||surPoint.equals(testPointRight) )
	  				{
	  					isSafe = true;
	  				}
	  				
	  			}
	  			
	  			if(!isSafe)
	  			{
	  				boolean dog = prob.eventDog(survivor);
	  				boolean bandit = prob.eventBandit(survivor);
	  				boolean fire = prob.eventFire(survivor);
	  				boolean desert = prob.eventDesert(survivor);
	  			
	  				//use the values returned from the methods to find the values for the food lost and display the list of events to the player etc
	  				if(dog)
	  				{
	  					food -= dogRed;
	  					dogCount++;
	  				}
	  			
	  				if(bandit)
	  				{
	  					food -= banditRed;
	  					bandCount++;
	  				}
	  			
	  				if(fire)
	  				{
	  					food -= fireRed;
	  					fireCount++;
	  				}
	  			
	  				if(desert)
	  				{
	  					food -= desertRed;
	  					desertCount++;
	  					//remove the said survivor from the list
	  					toRemove.add(name);
	  					Log.d(TAG, "desert event need to remove " + name);
	  				}
	  			}
	  			dogInfo = "there was " + dogCount +" dog attack, you lost "  + dogCount * dogRed + "  food.\n";
	  			banditInfo = "there was " + bandCount +" bandit raid you lost " + bandCount * banditRed + " food.\n";
	  			fireInfo = "there was " + fireCount + " fire you lost " + fireCount * fireRed + " food.\n";
	  		}
	  	}
	  	
	  	for(String x : toRemove)
	  	{
	  		if(!x.equals(""))
	  		{
	  			desertInfo.concat(x + " has deserted you! they stole " + desertRed + "food and left.\n");
	  			survivors.removeSurvivors(x);
	  			Log.d(TAG, "removed" + x);
	  		}
	  	}
  		
	  	for(Survivor survivor: survivors.getSurvivors())
	  	{
	  		int metab = survivor.getMet();
	  		food -= (int) (metab/2);
	  	}
	  		
	  	int manedFarms = drawView.getMaddedFarms();
	  	food += manedFarms * 4;
	  	String farmInfo = "you recieved " + (manedFarms * 4) + "food from farms.\n";
  		manedFarms  = 0;
  		//feedBack = dogInfo + banditInfo + fireInfo + desertInfo + farmInfo;
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
	 
	 private void update() {
		 tFood = (Button) findViewById(R.id.tFoodCount);
         tFood.setText("food:" + food);
         
         tResource = (Button) findViewById(R.id.tResourcesCount);
         tResource.setText("res:" + resource);
         
         tTurn = (Button) findViewById(R.id.tTurnsCount);
         tTurn.setText("turn:" + turn);
         
         tSurvivorCount = (Button) findViewById(R.id.tSurCount);
         tSurvivorCount.setText("NO surv:" + survivorCount);
         
         drawView.setSurvivors(survivors);
         
         int surCount = 0;
         for(Survivor survivor:survivors.getSurvivors())
         {
        	 if(!survivor.getName().equals(empty))
        	 {
        		 surCount++;
        	 }
         }
         
         survivorCount = surCount;
         tSurvivorCount.setText("NO surv:" + survivorCount);
	     
		}
	 
	 
}
