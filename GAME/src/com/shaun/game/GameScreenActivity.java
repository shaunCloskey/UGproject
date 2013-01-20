package com.shaun.game;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.Window;
import android.view.WindowManager;
import android.widget.TextView;

public class GameScreenActivity extends Activity {
	
	
	private String name;
	private TextView tFood;
	private TextView tResource;
	private TextView tTurn;
	private TextView tSurvivorCount;
	
	
	private int food;
	private int resource;
	private int turn;
	private int survivorCount;
	
	private Database database = new Database(this);
	
	 public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         // requesting to turn the title OFF
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         // making it full screen
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // set our MainGamePanel as the View
         setContentView(R.layout.game_screen);
         
         
         Bundle extras = getIntent().getExtras();
         name  = extras.getString("saveName");
         
         database.writeOpen();
         food = database.getFoodCount(name);
         resource = database.getResourceCount(name);
         turn = database.getTurnCount(name);
         database.writeClose();
         
         tFood = (TextView) findViewById(R.id.tFoodCount);
         tFood.setText("food:" + food);
         
         tResource = (TextView) findViewById(R.id.tResourcesCount);
         tResource.setText("resource:" + resource);
         
         tTurn = (TextView) findViewById(R.id.tTurnsCount);
         tTurn.setText("turn:" + turn);
         
         tSurvivorCount = (TextView) findViewById(R.id.tSurCount);
         tSurvivorCount.setText("NO surv:" + survivorCount);
         
         //take the values from the Database and present them.
         
         
         
	 }
}
