package com.shaun.game;

import java.util.Random;

import utilites.SafePoints;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
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
	@SuppressWarnings("unused")
	private Context context;
	String newName = "newGame";
	String empty = "empty";
	
	boolean didItWork = true;
	
	DatabaseSave database = new DatabaseSave(newGame.this);
	DatabaseSurvivor surData = new DatabaseSurvivor(newGame.this);
	DatabaseSafe safeData = new DatabaseSafe(newGame.this);
	DatabaseFarms farmData = new DatabaseFarms(newGame.this);
	
	private String [] names = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
	private String [] orignalNames = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
        gameOne = (Button)findViewById(R.id.save1);
        gameTwo = (Button)findViewById(R.id.save2);
        gameThree = (Button)findViewById(R.id.save3);
        gameFour = (Button)findViewById(R.id.save4);
        gameFive = (Button)findViewById(R.id.save5);
        delete = (Button)findViewById(R.id.deleteb);
        
        context = getApplicationContext();
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
			        if(database.containsName("newGame"))
			        {
			        	database.removeEntry("newGame");
			        }
			        database.writeClose();
			        
			        surData.writeOpen();
			        if(surData.containsName("newGame"))
			        {
			        	surData.removeEntry("newGame");
			        }
			        surData.writeClose();
					
			        safeData.writeOpen();
			        if(safeData.containsName("newGame"))
			        {
			        	safeData.removeEntry("newGame");
			        }
			        safeData.writeClose();
			        
			        
			        farmData.writeOpen();
			        if(farmData.containsName("newGame"))
			        {
			        	farmData.removeEntry("newGame");
			        }
			        farmData.writeClose();
			        
			        
					database.writeOpen();
					database.createEntry("newGame",50, 0, 25);
					database.writeClose();
					
					safeData.writeOpen();			        
					safeData.createEntry("newGame", new Point(4,4));
			        safeData.writeClose();
					
					String [] set = {"bob", "john" , "kate"};
					Random generator = new Random();
					for(String surName: set)
					{	
						int mob = generator.nextInt(5) + 1;
						int scav = generator.nextInt(5) + 1;
						int build = generator.nextInt(5) + 1;
						int metab = generator.nextInt(5) + 1;
						int x = 4;
						int y = 4;
						surData.writeOpen();
						surData.createSurEntry("newGame", build, metab, mob, scav, surName, x, y);
						surData.writeClose();
					}
				
					//set the activity to GameScreenActivity
					Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
					Bundle bundle = new Bundle();
					bundle.putString("saveName", "newGame");
					ourIntent.putExtras(bundle);
					startActivity(ourIntent);
					finish();
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
					finish();
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
					finish();
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
					finish();
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
					finish();
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
					finish();
				}
			}
		});
        
        
        delete.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				//present a list of the save files to choose one to remove
				displayDelete();
			}
		});
	}
	
	public void displayDelete()
	{
		AlertDialog.Builder b = new Builder(this);
			b.setTitle("select a save file to delete");
			final String [] saveList = names;
			b.setSingleChoiceItems(saveList, 0, new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {  
					//item is the index of the chosen item
				}
			});
				
			b.setPositiveButton("delete", new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int whichButton) {
						int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
		                //the selectedPosition can tell what save file to remove
		                
		                String saveName = saveList[selectedPosition];
		                //remove from saves
		                database.writeOpen();
		                database.removeEntry(saveName);
		                database.writeClose();
		                
		                //remove from survivors
		                surData.writeOpen();
		                surData.removeEntry(saveName);
		                surData.writeClose();
		                
		                //remove from safe
		                safeData.writeOpen();
		                safeData.removeEntry(saveName);
		                safeData.writeClose();
		                
		                
		                //remove from farms
		                farmData.writeOpen();
		                farmData.removeEntry(saveName);
		                farmData.writeClose();
		                
		                names = orignalNames;
		                
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
		                dialog.dismiss();
		            }
			});
				
			b.setNegativeButton("cancel",  new DialogInterface.OnClickListener() {
				public void onClick(DialogInterface dialog, int item) {  
					
				}
			});
				
			AlertDialog alertInner = b.create();
			alertInner.show();
	}
}
