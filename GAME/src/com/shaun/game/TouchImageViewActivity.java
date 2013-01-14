package com.shaun.game;


import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;


public class TouchImageViewActivity extends Activity {
    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main);
        TouchImageView img = (TouchImageView) findViewById(R.id.snoop);
        img.setImageResource(R.drawable.snoopy);
        img.setMaxZoom(4f);
        
        Button myButton = (Button) findViewById(R.id.btn1);
        myButton.setOnClickListener(new OnClickListener() {
			@Override
			public void onClick(View v) {
				// TODO start activity that shows the DrawView 
		            	switch(v.getId())
		         		{
		         		case R.id.btn1:
		         			
		         			//bundle the survivors, survivor names and known survivors to the main activity
		         	
		         			//need to pass the value of the surivior in question to the class and the value of food resources and turns.
		         			Intent ourIntent = new Intent(TouchImageViewActivity.this, DrawViewActivity.class);
		         			Bundle bundle = new Bundle();
		         			//add the list of survivors to the bundle
		         			startActivity(ourIntent);
		         			break;
		         		}
		             }
        });
    }
}