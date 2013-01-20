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
	
	Database database = new Database(this);
	
	String [] names = {"empty slot", "empty slot", "empty slot", "empty slot", "empty slot" };
	
	public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.new_game);
        
        
        /*
        gameOne.findViewById(R.id.save1);
        gameTwo.findViewById(R.id.save2);
        gameThree.findViewById(R.id.save3);
        gameFour.findViewById(R.id.save4);
        gameFive.findViewById(R.id.save5);
        */
        newGame = (Button) findViewById(R.id.newGame);
        newGame.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View v) {
				
				//add new entry to the database.
				SafePoints safePoints = new SafePoints();
				Point point = new Point(5,5);
				safePoints.addPoint(point);
				database.writeOpen();
				database.createEntry("newGame", 50, 0, 25);
				//TODO create new entry for all the survivors
				for(Survivor survivorTemp: OpeningScreenActivity.survivorsCurrent.getSurvivors())
				{
					//database.createSurEntry("new Game", survivorTemp.getbuilding(), survivorTemp.getMet(), survivorTemp.getMob(), survivorTemp.getScav(), survivorTemp.getName());
				}
				database.writeClose();
				//set the activity to GameScreenActivity
				Intent ourIntent = new Intent(newGame.this, GameScreenActivity.class);
				Bundle bundle = new Bundle();
				bundle.putString("saveName", "newGame");
				ourIntent.putExtras(bundle);
				startActivity(ourIntent);
			}
		});

        
        
        
        
	}
	
	
}
