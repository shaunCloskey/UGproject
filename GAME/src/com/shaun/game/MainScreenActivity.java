package com.shaun.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;
import java.util.Set;
import java.util.Vector;

import utilites.Survivor;
import utilites.probHandler;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

public class MainScreenActivity extends Activity {
	
	TextView foodView;
	TextView resourceView;
	TextView turnView;
	TextView eventView;
	
	Button survivorOne;
	Button survivorTwo;
	Button survivorThree;
	Button survivorFour;
	Button survivorFive;
	
  	Button finishTurn;

  	private probHandler prob = new probHandler();
  	private static int manedFarms = 0;
  	private int turnCount;
  	private int food;
  	private int resource;
  	private int dogRed = 1;
  	private int banditRed = 2;
  	private int fireRed = 4;
  	private int desertRed = 5;
  	private String feedBack = "";
  	final String empty = "empty slot";
  	
  	private static final String TAG = "MyActivity";
  	public static String [] usedTurn= {"","","","",""};
  	
  	private Iterator it = FrameWorkActivity.survivors.entrySet().iterator();
  	
  	private String [] survivorNames ={"bob", "john", "kate", "morgan", "paul", "mary", "liam" , "mark" , "peter" , "greg" , "andrew" , "ed" , "pong" , "jimmy" , "trent" , "sarah" , "cazz" , "mickeal" , "jerry" , "elly"}; 
  	private ArrayList<String> knownSurvivors = new ArrayList<String>();
  	private static ArrayList<String> unsafeSurvivors = new ArrayList<String>();
  	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         // requesting to turn the title OFF
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         // making it full screen
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // set our MainGamePanel as the View
         setContentView(R.layout.main_screen);
         Bundle extras = getIntent().getExtras();
         
         foodView = (TextView) findViewById(R.id.tFood);
         food = extras.getInt("food");
         
         foodView.setText("food: " + food);
         
         feedBack = extras.getString("feedback");
         
         resourceView = (TextView) findViewById(R.id.tResources);
         resource = extras.getInt("resource");
         resourceView.setText("resource: " + resource);
         
         turnView = (TextView) findViewById(R.id.tTurns);
         turnCount = extras.getInt("turnCount");
         turnView.setText("Turns: " + turnCount);
         
         eventView = (TextView) findViewById(R.id.tFeedbackInfo);
         eventView.setText(feedBack);
         
         finishTurn = (Button) findViewById(R.id.bOutTurn);
         finishTurn.setOnClickListener(new View.OnClickListener() {
 		 	
 		 	public void onClick(View v) {
 		 		switch(v.getId())
 		 		{	
 		 			case R.id.bOutTurn:
 		 				turnCount++;
 		 				betweenTurnEvents(turnCount);
 		 				update();
 		 				if(food<0)
 		 				{
 		 					Intent ourIntent = new Intent(MainScreenActivity.this, FrameWorkActivity.class);
 		 					startActivity(ourIntent);
 		 				}
 		 				break;
 		 		}
 		 	}
 		 });

         knownSurvivors = extras.getStringArrayList("knownSurvivors");
         
         String [] names = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
         int i=0;
         
         for(Entry<String, Survivor> cursor : FrameWorkActivity.survivors.entrySet())
         {
        	 names[i] = cursor.getKey();
        	 Log.v(TAG, "survivor " + i +" = " + names[i]);
        	 i++;
         }
         
         Log.d(TAG, "the number of survivors is "  + FrameWorkActivity.survivors.size());
         
         
         final String empty = "empty slot";
         
         survivorOne = (Button) findViewById(R.id.bsurOne);
         survivorOne.setText(names[0]);
         if(isUsed((String) survivorOne.getText()) )
         {
        	 survivorOne.setBackgroundColor(Color.RED);
         }
         survivorOne.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	switch(v.getId())
         		{
         		case R.id.bsurOne:
         			Log.d(TAG, "name of survivor is "  + survivorOne.getText());
         			Log.d(TAG, "survivor list is = " + FrameWorkActivity.survivors);
         			
        			String key = (String) survivorOne.getText();
        			if((!key.equals(empty)) && (!alreadyGone(key)) )
        			{
        				startActivity(key);
        			}
        			break;
         		}
         		}
         });
         
         survivorTwo = (Button) findViewById(R.id.bsurTwo);
         survivorTwo.setText(names[1]);
         if(isUsed((String) survivorTwo.getText()) )
         {
        	 survivorTwo.setBackgroundColor(Color.RED);
         }
         survivorTwo.setOnClickListener(new View.OnClickListener() {
			
			public void onClick(View v) {
				switch(v.getId())
				{
				case R.id.bsurTwo:
					Log.d(TAG, "survivor list is = " + FrameWorkActivity.survivors);
					String key = (String) survivorTwo.getText();
					if(!key.equals(empty) && (!alreadyGone(key)) )
					{
						startActivity(key);
					}
					break;
				}
				
			}
		});
         
         survivorThree = (Button) findViewById(R.id.bsurThree);
         survivorThree.setText(names[2]);
         if(isUsed((String) survivorThree.getText()) )
         {
        	 survivorThree.setBackgroundColor(Color.RED);
         }
         survivorThree.setOnClickListener(new View.OnClickListener() {
 			
 			public void onClick(View v) {
 				switch(v.getId())
 				{
 				case R.id.bsurThree:
 					Log.d(TAG, "survivor list is = " + FrameWorkActivity.survivors);
 					String key = (String) survivorThree.getText();
 					if(!key.equals(empty) && (!alreadyGone(key)) )
 					{
 						startActivity(key);
 					}
 					break;
 				}
 				
 			}
 		});
         
         survivorFour = (Button) findViewById(R.id.bsurFour);
         survivorFour.setText(names[3]);
         if(isUsed((String) survivorFour.getText()) )
         {
        	 survivorFour.setBackgroundColor(Color.RED);
         }
         survivorFour.setOnClickListener(new View.OnClickListener() {
 			
 			public void onClick(View v) {
 				switch(v.getId())
 				{
 				case R.id.bsurFour:
 					Log.d(TAG, "survivor list is = " + FrameWorkActivity.survivors);
 					String key = (String) survivorFour.getText();
 					if(!key.equals(empty)  && (!alreadyGone(key)) )
 					{
 						startActivity(key);
 					}
 					break;
 				}
 				
 			}
 		});
         
         survivorFive = (Button) findViewById(R.id.bsurFive);
         survivorFive.setText(names[4]);
         if(isUsed((String) survivorFive.getText()) )
         {
        	 survivorFive.setBackgroundColor(Color.RED);
         }
         survivorFive.setOnClickListener(new View.OnClickListener() {
 			
 			public void onClick(View v) {
 				switch(v.getId())
 				{
 				case R.id.bsurFive:
 					Log.d(TAG, "survivor list is = " + FrameWorkActivity.survivors);
 					String key = (String) survivorFive.getText();
 					if(!key.equals(empty) && (!alreadyGone(key)) )
 					{
 						startActivity(key);
 					}else{
 						 Toast.makeText(getApplicationContext(), 
 	                             "already used that survivor", Toast.LENGTH_LONG).show();
 					}
 					break;
 				}
 				
 			}
 		});
    }
    
    
  	

  	private boolean isUsed(String name) {
  		for(String x: usedTurn)
  		{
  			if(x.equals(name))
  			{
  				return true;
  			}
  		}
		return false;
	}




	/**
  	 * method to remove a survivor from the list of unsafe survivors
  	 */
  	public static void safeSurvivor(String s)
  	{
  		if( unsafeSurvivors.contains(s))
  		{
  			unsafeSurvivors.remove(s);
  		}
  	}
  	
  	
  	/**
  	 * method to add a survivor to the list of unsafe survivors
  	 */
  	public static void unsafeSurvivor(String s)
  	{
  		unsafeSurvivors.add(s);
  	}
  	
  	public static boolean isUnsafe(String s)
  	{
  		if(unsafeSurvivors.contains(s))
  		{
  			return true;
  		}
  		else
  		{
  			return false;
  		}

  	}
  	
  	
  	public static void manFarm()
  	{
  		manedFarms++;
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

  		
  		List<String> toRemove =new ArrayList<String>();
  		Log.d(TAG, "the number of survivors is "  + FrameWorkActivity.survivors.size());
  		//run through all survivors and determine if events have occurred
  		if(turn > 2)
  		{
  			for(String name : unsafeSurvivors )
  			{
  				Survivor s = FrameWorkActivity.survivors.get(name);
  				Log.d(TAG, "the name of the survivor is "  + s.getName());
  				
  				boolean dog = prob.eventDog(s);
  				boolean bandit = prob.eventBandit(s);
  				boolean fire = prob.eventFire(s);
  				boolean desert = prob.eventDesert(s);
  				
  				
  				
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
  		
  		for(String x : toRemove)
  		{
  			if(!x.equals(""))
  			{
  				desertInfo.concat(x + " has deserted you! they stole " + desertRed + "food and left.\n");
  				FrameWorkActivity.survivors.remove(x);
  				safeSurvivor(x);
					Log.d(TAG, "removed" + x);

  			}
  		}
  		
  		for(Iterator iter = FrameWorkActivity.survivors.entrySet().iterator(); iter.hasNext(); )
		{
  			HashMap.Entry pairs = (HashMap.Entry)iter.next();
			Survivor s = (Survivor) pairs.getValue();
			
			int metab = s.getMet();
			food -= (int) (metab/2);
  		}
  		
  		food += manedFarms * 4;
  		String farmInfo = "you recieved " + (manedFarms * 4) + "food from farms.\n";
  		
  		manedFarms  = 0;
  		
  		feedBack = dogInfo + banditInfo + fireInfo + desertInfo + farmInfo;
  	}




	private void startActivity(String key) {
		Survivor s = FrameWorkActivity.survivors.get(key);
		//start the new SurvivorActivity
		//need to pass the value of the surivior in question to the class and the value of food resources and turns.
		Intent ourIntent = new Intent(MainScreenActivity.this, SurvivorActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("food", food);
		bundle.putInt("resource", resource);
		bundle.putInt("turnCount",turnCount);
		bundle.putString("name", s.getName());
		bundle.putInt("scavange", s.getScav());
		bundle.putInt("mobility", s.getMob());
		bundle.putInt("metab", s.getMet());
		bundle.putInt("build", s.getbuilding());
		bundle.putString("feedBack", feedBack);
		//add the list of survivors to the bundle
		bundle.putStringArrayList("knownSurvivors", knownSurvivors);
		ourIntent.putExtras(bundle);
		
        startActivity(ourIntent);
	}
	
	//checks if the user has already used this survivor
	public boolean alreadyGone(String name)
	{
		for(String x : usedTurn)
		{
			if(x.equals(name))
			{
				return true;
			}
		}
		return false;
	}


	private void update() {
		String [] usedTurnnew = {"","","","",""};
		usedTurn = usedTurnnew;
        foodView.setText("food:" + food);
        resourceView.setText("resource: " + resource);
        turnView.setText("turnCount: " + turnCount);
        eventView = (TextView) findViewById(R.id.tFeedbackInfo);
        eventView.setText(feedBack);
        //update list of survivors to buttons
        
        survivorOne.setBackgroundDrawable(FrameWorkActivity.defaultButton);
        survivorTwo.setBackgroundDrawable(FrameWorkActivity.defaultButton);
        survivorThree.setBackgroundDrawable(FrameWorkActivity.defaultButton);
        survivorFour.setBackgroundDrawable(FrameWorkActivity.defaultButton);
        survivorFive.setBackgroundDrawable(FrameWorkActivity.defaultButton);
        
        
        String [] names = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
        int i=0;
        
        for(Entry<String, Survivor> cursor : FrameWorkActivity.survivors.entrySet())
        {
       	 names[i] = cursor.getKey();
       	 Log.v(TAG, "survivor " + i +" = " + names[i]);
       	 i++;
        }
        
        
        survivorOne.setText(names[0]);
        survivorTwo.setText(names[1]);
        survivorThree.setText(names[2]);
        survivorFour.setText(names[3]);
        survivorFive.setText(names[4]);
        
	}
	
}