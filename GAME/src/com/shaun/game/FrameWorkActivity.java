package com.shaun.game;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Random;

import utilites.Survivor;
import utilites.probHandler;

import android.app.Activity;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;

public class FrameWorkActivity extends Activity{

	private static final String TAG = "MyActivity";
	Button button;
	Button testb;
	private String [] survivorNames ={"bob", "john", "kate", "morgan", "paul", "mary", "liam" , "mark" , "peter" , "greg" , "andrew" , "ed" , "pong" , "jimmy" , "trent" , "sarah" , "cazz" , "mickeal" , "jerry" , "elly"}; 
  	private ArrayList<String> knownSurvivors = new ArrayList<String>();
	public static HashMap<String, Survivor> survivors = new HashMap<String, Survivor>(5);
	public static Drawable defaultButton;
	
	
	private probHandler prob = new probHandler();
	private int food = 50;
	private int resource = 25;
	private int turnCount = 0;
	private String feedBack = "this will give feedback about what will happen in the game, like what events have occured";
  	
	/** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
    	 super.onCreate(savedInstanceState);
         // requesting to turn the title OFF
         requestWindowFeature(Window.FEATURE_NO_TITLE);
         // making it full screen
         getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);
         // set our MainGamePanel as the View
         setContentView(R.layout.main);
         survivors = new HashMap<String, Survivor>(5);
         
         Survivor newSurvivor = new Survivor();
         while(survivors.size() <3)
		{
			Random generator = new Random();
			int randNu  = generator.nextInt(20);
		
			String name = survivorNames[randNu];
			if(!survivors.containsKey(name))
			{
				int mob = generator.nextInt(5) + 1;
				int scav = generator.nextInt(5) + 1;
				int build = generator.nextInt(5) + 1;
				int metab = generator.nextInt(5) + 1;
				newSurvivor = new Survivor(mob, scav, build, metab, name);
				survivors.put(name, newSurvivor);
				knownSurvivors.add(name);
			}
		}
			
		Log.v(TAG, "the amount of known names is = "  + knownSurvivors.size());
		Log.v(TAG, "survivor names are ");
		for(String s : knownSurvivors)
		{
			Log.v(TAG, s);
		}
         
         
        button = (Button) findViewById(R.id.button1);
        defaultButton = button.getBackground();
        button.setOnClickListener(new View.OnClickListener() {
             public void onClick(View v) {
            	switch(v.getId())
         		{
         		case R.id.button1:
         			
         			//bundle the survivors, survivor names and known survivors to the main activity
         	
         			//need to pass the value of the surivior in question to the class and the value of food resources and turns.
         			Intent ourIntent = new Intent(FrameWorkActivity.this, MainScreenActivity.class);
         			Bundle bundle = new Bundle();
         			bundle.putInt("food", food);
         			bundle.putInt("resource", resource);
         			bundle.putInt("turnCount",turnCount);
         			bundle.putString("feedBack", feedBack);
         			//add the list of survivors to the bundle
         			bundle.putStringArrayList("knownSurvivors", knownSurvivors);
         			ourIntent.putExtras(bundle);
         			startActivity(ourIntent);
         			break;
         		}
             }
         });
    }
}
