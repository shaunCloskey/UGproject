package com.shaun.game;

import java.util.ArrayList;
import java.util.Random;

import utilites.Survivor;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;


public class SurvivorActivity extends Activity implements View.OnClickListener{

	private static final String TAG = "MyActivity";
	
	
	Button drop;
	Button scavange;
  	Button buildFarm;
  	Button useFarm;
  	Button buildSafe;
  	Button moveSafe;
  	Button moveUnsafe;
  	
  	
  	TextView survivorName;
  	TextView metabSkill;
  	TextView scavangeSkill;
  	TextView mobilitySkill;
  	TextView buildSkill;
  	
  	Survivor survivor;
	private int turnCount;
  	private int food;
  	private int resource;
  	private String feedBack= "";
  	private ArrayList<String> knownSurvivors = new ArrayList<String>();
	private String [] survivorNames ={"bob", "john", "kate", "morgan", "paul", "mary", "liam" , "mark" , "peter" , "greg" , "andrew" , "ed" , "pong" , "jimmy" , "trent" , "sarah" , "cazz" , "mickeal" , "jerry" , "elly"};
	
	
  	 @Override
     public void onCreate(Bundle savedInstanceState) {
     	 super.onCreate(savedInstanceState);
          // requesting to turn the title OFF
          requestWindowFeature(Window.FEATURE_NO_TITLE);
          // making it full screen
          getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
          // set our MainGamePanel as the View
          setContentView(R.layout.survivor_info);
          Bundle extras = getIntent().getExtras();
          
          drop = (Button) findViewById(R.id.bDropSur);
          scavange = (Button) findViewById(R.id.bScavanged);
          buildFarm = (Button) findViewById(R.id.bBuildFarm);
          useFarm = (Button) findViewById(R.id.bUseFarm);
          buildSafe = (Button) findViewById(R.id.bBuildstructure);
          moveSafe = (Button) findViewById(R.id.bMoveSafe);
          moveUnsafe = (Button) findViewById(R.id.bMoveUnsafe);
          
          survivorName = (TextView) findViewById(R.id.tSurvivorName);
          metabSkill = (TextView) findViewById(R.id.tSkillMet);
          scavangeSkill = (TextView) findViewById(R.id.tSkillScav);
          mobilitySkill = (TextView) findViewById(R.id.tSkillMobility);
          buildSkill = (TextView) findViewById(R.id.tSkillBuilding);
          
          drop.setOnClickListener(this);
          scavange.setOnClickListener(this);
          buildFarm.setOnClickListener(this);
          useFarm.setOnClickListener(this);
          buildSafe.setOnClickListener(this);
          moveSafe.setOnClickListener(this);
          moveUnsafe.setOnClickListener(this);
          
          Log.v(TAG, "about to get the bundle");
          
          survivorName.setText(extras.getString("name"));
          food = extras.getInt("food");
          turnCount = extras.getInt("turnCount");
          resource = extras.getInt("resource");
          feedBack = extras.getString("feedBack");
          
          metabSkill.setText("metab rate: " + extras.getInt("metab") );
          scavangeSkill.setText( "Scavange skill: " +extras.getInt("scavange"));
          mobilitySkill.setText("mobility Skill: " + extras.getInt("mobility"));
          buildSkill.setText("building Skill: " + extras.getInt("build"));
          
          survivor =  new Survivor(extras.getInt("mobility"), extras.getInt("scavange"),extras.getInt("build"), extras.getInt("metab"), extras.getString("name"));
          
          knownSurvivors = extras.getStringArrayList("knownSurvivors");
  	 }

  	 
  	public void onClick(View v)
  	{
  		
  		switch(v.getId())
  		{
  		case R.id.bDropSur:
  			//prompt user are your sure
  			AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setCancelable(true);
			builder.setInverseBackgroundForced(true);
			builder.setPositiveButton("Confirm", new
			DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
					//Yes button clicked
					//remove from list of survivors and safe
					 Log.v(TAG, "about to remove " + survivorName +"from the list of survivors" );
					FrameWorkActivity.survivors.remove(survivorName.getText());
					if( MainScreenActivity.isUnsafe((String) survivorName.getText()) )
					{
						MainScreenActivity.safeSurvivor((String) survivorName.getText());
					}
					startNewActivity();
				}
			});
				
			builder.setNegativeButton("cancel", new
			DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int which) {
				}
			});
			
			
			builder.setMessage("are you sure you want to remove this survivor?");
			AlertDialog alert = builder.create();
			alert.show();
  			break;
  		case R.id.bScavanged:
  			tellUsed(survivor.getName());
  			scavangeEvent(survivor);
  			Random generator = new Random();
   			int randNu  = generator.nextInt(3);
  			if(!(knownSurvivors.size() == survivorNames.length) && (randNu == 2))
  			{
  				
  				final Survivor newSurvivor = getNewSurvivor();
  				knownSurvivors.add(newSurvivor.getName());
  				if(FrameWorkActivity.survivors.size() == 5)
  				{
  					//tell the users that they need cant keep the new survivor
  					 Toast.makeText(getApplicationContext(), 
                             "you already have 5 survivors so a new survivor has been left", Toast.LENGTH_LONG).show();
  					 
  					 startNewActivity();
  				}else{
  					AlertDialog.Builder builder2 = new AlertDialog.Builder(this);
  					builder2.setCancelable(true);
  					builder2.setInverseBackgroundForced(true);
  					builder2.setPositiveButton("Yes", new
  					DialogInterface.OnClickListener() {
  						public void onClick(DialogInterface dialog, int which) {
  							//Yes button clicked
  							FrameWorkActivity.survivors.put(newSurvivor.getName(), newSurvivor);
  							MainScreenActivity.unsafeSurvivor(newSurvivor.getName());
  							startNewActivity();
  						}
  					});
  						
  					builder2.setNegativeButton("no", new
  					DialogInterface.OnClickListener() {
  						public void onClick(DialogInterface dialog, int which) {
  							startNewActivity();
  						}
  					});
  					
  					
  					builder2.setMessage("do you want to take in the new survivor?");
  					AlertDialog ask = builder2.create();
  					ask.show();
  				}
  				break;
  			}else{
  				startNewActivity();
  				break;
  			}
  		case R.id.bBuildFarm:
  			resource -= (5 - survivor.getbuilding());
  			tellUsed(survivor.getName());
  			startNewActivity();
  			break;
  		case R.id.bUseFarm:
  			MainScreenActivity.manFarm();
  			tellUsed(survivor.getName());
  			startNewActivity();
  			break;
  		case R.id.bBuildstructure:
  			resource -= (5 - survivor.getbuilding());
  			tellUsed(survivor.getName());
  			break;
  		case R.id.bMoveSafe:
  			if( MainScreenActivity.isUnsafe((String) survivorName.getText()) )
			{
				MainScreenActivity.safeSurvivor((String) survivorName.getText());
			}
  			tellUsed(survivor.getName());
  			startNewActivity();
  			break;
  		case R.id.bMoveUnsafe:
  			//nothing yet
  			if( !MainScreenActivity.isUnsafe((String) survivorName.getText()) )
			{
				MainScreenActivity.unsafeSurvivor((String) survivorName.getText());
			}
  			tellUsed(survivor.getName());
  			startNewActivity();
  			break;
  		}
  		
  		
	}
  	 
  	private void tellUsed(String name) {
		String emp = "";
  		for(int i = 0; i< MainScreenActivity.usedTurn.length; i++)
		{
			if(MainScreenActivity.usedTurn[i].equals(emp))
			{
				MainScreenActivity.usedTurn[i] = name;
				return;
			}
		}
	}


	private void startNewActivity() {
		//start the Activity to pass back to the main game screen
		//need to pass the value of the surivior in question to the class and the value of food resources and turns.
		Intent ourIntent = new Intent(SurvivorActivity.this, MainScreenActivity.class);
		Bundle bundle = new Bundle();
		bundle.putInt("food", food);
		bundle.putInt("resource", resource);
		bundle.putInt("turnCount",turnCount);
		bundle.putString("feedBack", feedBack);
		//add the list of survivors to the bundle
		bundle.putStringArrayList("knownSurvivors", knownSurvivors);
		ourIntent.putExtras(bundle);
		
        startActivity(ourIntent);
	}


	/**
   	 * method to decide what the player has found on the square they have scavenged on
   	 * need to consider the place on the grid, survivor skill level to determine the food amount
   	 */
   	public void scavangeEvent(Survivor s)
   	{
   		// take the point and create the maximum food for the square using the distance of the point from Home point 
   		Random generator = new Random();
   		int maxFood = generator.nextInt(3);
   		
   		// take the skill of the survivor and work out how much food is lost
   		int foodSca = s.getScav();
   		int addedFood = generator.nextInt(foodSca);
   		
   		int foodVal = maxFood + addedFood;
   		food += foodVal;
   		
   		//do above for resources
   		int maxResources = generator.nextInt(3);
   		
   		int resSca = s.getScav();
   		int addedRes = generator.nextInt(resSca);
   		
   		int resVal = maxResources + addedRes;
   		resource += resVal;
   	}

   	
   	/**
   	 * gets a new Survivor for the player
   	 */
   	private Survivor getNewSurvivor()
   	{
   		Survivor newSurvivor;
   		while(true)
   		{
   			//get random new survivor 
   			Random generator = new Random();
   			int surInt  = generator.nextInt(20);
   			String newName = survivorNames[surInt];
   			
   			if(!knownSurvivors.contains(newName))
   			{
   				int mob = generator.nextInt(5) + 1;
   				int scav = generator.nextInt(5) + 1;
   				int build = generator.nextInt(5) + 1;
   				int metab = generator.nextInt(5) + 1;
   				newSurvivor = new Survivor(mob, scav, build, metab, newName);
   				break;
   			}
   		}
   		return newSurvivor;
   	}
}