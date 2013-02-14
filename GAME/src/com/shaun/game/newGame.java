package com.shaun.game;

import utilites.SafePoints;
import utilites.Survivor;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

public class newGame extends Activity{

	protected Button newGame;
	protected Button gameOne;
	protected Button gameTwo;
	protected Button gameThree;
	protected Button gameFour;
	protected Button gameFive;
	protected Button delete;
	
	String newName = "newGame";
	String empty = "empty";
	
	boolean didItWork = true;
	
	DatabaseSave database = new DatabaseSave(newGame.this);
	DatabaseSurvivor surData = new DatabaseSurvivor(newGame.this);
	
	String [] names = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
	
	private static final String TAG = "MyActivity";
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
        gameOne = (Button)findViewById(R.id.save1);
        gameTwo = (Button)findViewById(R.id.save2);
        gameThree = (Button)findViewById(R.id.save3);
        gameFour = (Button)findViewById(R.id.save4);
        gameFive = (Button)findViewById(R.id.save5);
        
        //get all save games from database and put names on the save buttons
        database.writeOpen();
        String [] tempNames = database.getSaveNames();
        int j=0;
        for(int i=0; i<tempNames.length; i++)
        {
        	if(!tempNames[i].equals(newName))
        	{
        		names[j] = tempNames[i];
        		j++;
        	}
        }
        database.writeClose();
        
        for(int i=0; i<names.length; i++)
        {
        	switch(i)
    		{
    		case 0:
    			gameOne.setText(names[i]);
    			break;
    		case 1:
    			gameTwo.setText(names[i]);
    			break;
    		case 2:
    			gameThree.setText(names[i]);
    			break;
    		case 3:
    			gameFour.setText(names[i]);
    			break;
    		case 4:
    			gameFive.setText(names[i]);
    			break;
    		}
        }
        
        
        newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//add new entry to the database.
				SafePoints safePoints = new SafePoints();
				Point point = new Point(5,5);
				safePoints.addPoint(point);
				try{
					database.writeOpen();
					if(database.containsName("newGame")){
					
						//do nothing new game already on the database
					
					}else{
						database.createEntry("newGame",50, 0, 25);
						
						surData.writeOpen();
						int i=0;
						for(Survivor survivorTemp: OpeningScreenActivity.survivorsCurrent.getSurvivors())
						{
							if(!survivorTemp.getName().equals(empty))
							{
								Log.d(TAG, "i= " + i);
								Log.d(TAG, "sur name: "+survivorTemp.getName());
								Log.d(TAG, "sur build: "+survivorTemp.getbuilding());
								Log.d(TAG, "sur met: "+survivorTemp.getMet());
								Log.d(TAG, "sur mob: "+survivorTemp.getMob());
								i++;
								if(surData.containsName("newGame")){
									surData.updateEntry("newGame", OpeningScreenActivity.survivorsCurrent);
								}else{
									surData.createSurEntry("newGame", survivorTemp.getbuilding(), survivorTemp.getMet(), survivorTemp.getMob(), survivorTemp.getScav(), survivorTemp.getName(), survivorTemp.getX(), survivorTemp.getY());
								}
							}
						}
						surData.writeClose();
						Log.d(TAG, "out of loop ");
					}
					database.writeClose();
					
					//set the activity to GameScreenActivity
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", "newGame");
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}catch (Exception e){
					didItWork=false;
				}finally{
					if(didItWork){
						
					}
				}
			}
		});
        
        
        gameOne.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				String saveName = (String) gameOne.getText();
				if(!saveName.equals("empty slot"))
				{
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", saveName);
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}
			}
		});
        
        gameTwo.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				String saveName = (String) gameTwo.getText();
				if(!saveName.equals("empty slot"))
				{
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", saveName);
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}
			}
		});
        
        gameThree.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				String saveName = (String) gameThree.getText();
				if(!saveName.equals("empty slot"))
				{
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", saveName);
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}
			}
		});
        
        gameFour.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				String saveName = (String) gameFour.getText();
				if(!saveName.equals("empty slot"))
				{
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", saveName);
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}
			}
		});
        
        gameFive.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				// Auto-generated method stub
				String saveName = (String) gameFive.getText();
				if(!saveName.equals("empty slot"))
				{
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", saveName);
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
				}
			}
		});
	}
	
	
}
