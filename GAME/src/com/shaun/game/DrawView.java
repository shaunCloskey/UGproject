package com.shaun.game;

import java.util.ArrayList;
import utilites.Survivor;
import utilites.Survivors;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Point;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;


public class DrawView extends View {
	private static final String TAG = "MyActivity";
	
	private Context gameContext;
	
	private Bitmap house1;
	private Bitmap house2;
	private Bitmap house3;
	private Bitmap house4;
	private Bitmap tempIcon;
	private Bitmap singleMan;
	private Bitmap group;
	private Bitmap mIcon;
	
	private Survivors survivors;
	private ArrayList<Point> surPoints = new ArrayList<Point>();
	private ArrayList<Point> multiPoint = new ArrayList<Point>();
	
	private Boolean moving = false;
	
	private float mPosX;
	private float mPosY;
	boolean firstRun;
	
	Matrix matrix;
	Matrix oMatrix;
	float [] m = new float [9];
	    
	private float mLastTouchX;
	private float mLastTouchY;
	private static final int INVALID_POINTER_ID = -1;

	//The �active pointer� is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	private float maxScale = 3f;
	
	Rect clipBounds_canvas;
	
	// Remember some things for zooming
    PointF last = new PointF();
    PointF start = new PointF();
    PointF oldXY = new PointF();
    PointF newXY = new PointF();
    float downX;
    float downY;
    
    int viewWidth, viewHeight;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

	private float yIndent = 0;
	private float xIndent = 0;

	
	public DrawView(Context context) {
		super(context, null, 0);
		super.setClickable(true);
		sharedConstructing(context);
	}
	    
	public DrawView(Context context, AttributeSet attrs) {
		super(context, attrs, 0);
	    super.setClickable(true);
	    sharedConstructing(context);
	}
	    
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    super.setClickable(true);
	    sharedConstructing(context);
	}
	
	public void sharedConstructing(Context context)
	{
		tempIcon = BitmapFactory.decodeResource(getResources(), R.drawable.snoopy);
		singleMan = BitmapFactory.decodeResource(getResources(), R.drawable.man);
		group = BitmapFactory.decodeResource(getResources(), R.drawable.groupsur);
		
		// Create our ScaleGestureDetector
	
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		house1 = BitmapFactory.decodeResource(getResources(), R.drawable.house1);
		house2 = BitmapFactory.decodeResource(getResources(), R.drawable.house2);
		house3 = BitmapFactory.decodeResource(getResources(), R.drawable.barber);
		house4 = BitmapFactory.decodeResource(getResources(), R.drawable.grocer);
		
		
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		firstRun = true;
		
		// Create our ScaleGestureDetector
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint("NewApi")
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    clipBounds_canvas = canvas.getClipBounds();
	    canvas.save();
	    canvas.translate(mPosX, mPosY);
	    canvas.scale(mScaleFactor, mScaleFactor);
	    matrix = canvas.getMatrix();
	    drawGrid(canvas);
	    //canvas.drawBitmap(mIcon, 0, 0, null);
	    //canvas.drawBitmap(overlay, 100, 100, null);
	    if(survivors != null)
	    {
	    	drawSur(canvas);
	    }
	    matrix.getValues(m);
	    if(firstRun)
	    {
	    	xIndent = m[2];
	    	yIndent = m[5];
	    	firstRun = false;
	    }
	    
	    //m[2] is the x translation
	    //m[5] is the y translation
	    float x = (m[2] / mScaleFactor) - xIndent;
	    float y = (m[5] / mScaleFactor) - yIndent;
	    Log.v(TAG, "matrix = " + matrix + "x trans = " +  x + " y translation "  + y);
	    
	    canvas.restore();
	}
	
	/**
	 * takes the size fo the view and divides it by 81 each grid point takes up viewheight/81 x veiwWidth/81
	 */
	public void drawGrid(Canvas canvas)
	{
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
		
		Bitmap grid1 = Bitmap.createScaledBitmap(house1, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap grid2 = Bitmap.createScaledBitmap(house2, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap grid3 = Bitmap.createScaledBitmap(house3, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap grid4 = Bitmap.createScaledBitmap(house4, (int) oneGridWidth, (int) oneGridHeight, true);
		
		for(int y=0; y<9;y++)
		{
			//do the first 9 across draws for the groups
			for(int i=0; i<9; i++)
			{	
				int gridPlace = ((i+1)*(y+1)) % 4;
				switch(gridPlace)
				{
				case(0):
					canvas.drawBitmap(grid1, i*oneGridWidth, y*oneGridHeight,null);
					break;
				case(1):
					canvas.drawBitmap(grid2, i*oneGridWidth, y*oneGridHeight,null);
					break;
				case(2):
					canvas.drawBitmap(grid3, i*oneGridWidth, y*oneGridHeight,null);
					break;
				case(3):
					canvas.drawBitmap(grid4, i*oneGridWidth, y*oneGridHeight,null);
					break;
				}
			}
		}
		
	}

	
	public void drawSur(Canvas canvas)
	{
		Log.d(TAG, "size of the survivors = " + survivors.getSurvivors().length);
		//iterate through all the survivors and place them on the map
		for(Survivor tempSurvivor: survivors.getSurvivors())
		{
			String empty = "empty";
			if(! (tempSurvivor.getName().equals(empty)) )
			{
				Point point = new Point(tempSurvivor.getX(), tempSurvivor.getY());
				if(surPoints.isEmpty())
				{
					Log.d(TAG, "surpoints is empty");
				}
				
				Log.d(TAG, "points = " + point.x + " " +point.y);
				
				if(!(surPoints.contains(point)))
				{
					surPoints.add(point);
				}else{
					if(multiPoint.contains(point))
					{
					
					}else{
						multiPoint.add(point);
					}
				}
				for(Point tPoint: multiPoint)
				{
					
						while(surPoints.remove(tPoint)) { }
				}
			}
			float oneGridHeight = viewHeight / 9;
			float oneGridWidth = viewWidth / 9;
		
			Bitmap singleSur = Bitmap.createScaledBitmap(singleMan, (int) oneGridWidth, (int) oneGridHeight, true);
			Bitmap groupSur = Bitmap.createScaledBitmap(group, (int) oneGridWidth, (int) oneGridHeight, true);
		
			for(Point point:surPoints)
			{
				//drawOnePerson
				if(!multiPoint.contains(point))
				{
					canvas.drawBitmap(singleSur, point.x*oneGridWidth, point.y*oneGridHeight,null);
				}
			
			}
		
			for(Point point:multiPoint)
			{
				//drawMultiPeople
				canvas.drawBitmap(groupSur, point.x*oneGridWidth, point.y*oneGridHeight, null);
			}
		}
		
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
		//float relx = ev.getX() / mScaleFactor + clipBounds_canvas.left;
		//float rely = ev.getY() / mScaleFactor + clipBounds_canvas.top;
		
		mScaleDetector.onTouchEvent(ev);
	    
	    final int action = ev.getAction();
	    switch (action & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN: {
	        final float x = ev.getX();
	        final float y = ev.getY();
	        
	        mLastTouchX = x;
	        mLastTouchY = y;
	        downX = x;
	        downY = y;
	        mActivePointerId = ev.getPointerId(0);
	        break;
	    }
	        
	    case MotionEvent.ACTION_MOVE: {
	        final int pointerIndex = ev.findPointerIndex(mActivePointerId);
	        final float x = ev.getX(pointerIndex);
	        final float y = ev.getY(pointerIndex);

	        // Only move if the ScaleGestureDetector isn't processing a gesture.
	        if (!mScaleDetector.isInProgress()) {
	        	//if moving make sure that the view is looking at the image and the image only

	        	if( (! (mScaleFactor == 1.0f)) )
	        	{ 			
	        		final float dx = x - mLastTouchX;
	        		final float dy = y - mLastTouchY;

	        		mPosX += dx;
	        		mPosY += dy;
	        		float screenX = viewWidth / mScaleFactor;
	        		float screenY = viewHeight / mScaleFactor;
	        		
	        		float xCheck = Math.abs(mPosX / mScaleFactor) + screenX;
	        		float yCheck = Math.abs(mPosY / mScaleFactor) + screenY;
	        		if(mPosX >=0)
	        		{
	        			mPosX = 0;
	        		}
	        		
	        		if(mPosY >=0)
	        		{
	        			mPosY = 0;
	        		}
	        		
	        		if(xCheck >= viewWidth )
	        		{
	        			//would be outside of boundary so make mPosX = max at this scale.
	        			float maxX = (mScaleFactor * viewWidth) - viewWidth;
	        			mPosX = -maxX;
	        			
	        		}
	        		
	        		if(yCheck >= viewHeight)
	        		{
	        			float maxY = (mScaleFactor * viewHeight) - viewHeight;
	        			mPosY = -maxY;
	        		}
	        		
	        		invalidate();
	        	}
	        }

	        mLastTouchX = x;
	        mLastTouchY = y;

	        break;
	    }
	        
	    case MotionEvent.ACTION_UP: {
	        mActivePointerId = INVALID_POINTER_ID;
	        
	        //check if new x, y is different than old by more than 5
	        int xChange = (int) Math.abs(downX - ev.getX());
            int yChange = (int) Math.abs(downY - ev.getY());
            
            if( (((xChange) < 5 ) && ((yChange) < 5 )))
            {
            	clickNow(ev, mLastTouchX, mLastTouchY, ev.getX(), ev.getY());
    		}
            moving = false;
	        break;
	    }
	        
	    case MotionEvent.ACTION_CANCEL: {
	        mActivePointerId = INVALID_POINTER_ID;
	        break;
	    }
	    
	    case MotionEvent.ACTION_POINTER_UP: {
	        final int pointerIndex = (ev.getAction() & MotionEvent.ACTION_POINTER_INDEX_MASK) 
	                >> MotionEvent.ACTION_POINTER_INDEX_SHIFT;
	        final int pointerId = ev.getPointerId(pointerIndex);
	        if (pointerId == mActivePointerId) {
	            // This was our active pointer going up. Choose a new
	            // active pointer and adjust accordingly.
	            final int newPointerIndex = pointerIndex == 0 ? 1 : 0;
	            mLastTouchX = ev.getX(newPointerIndex);
	            mLastTouchY = ev.getY(newPointerIndex);
	            mActivePointerId = ev.getPointerId(newPointerIndex);
	        }
	        break;
	    }
	    }
	    return true;
	}
	
	public void clickNow(MotionEvent event, float oldX, float oldY, float x, float y )
    {
		
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
    	// Get the values of the matrix
    	float[] values = new float[9];
    	matrix.getValues(values);
    	
    	Log.d(TAG, "values[0] = " + values[0]);
    	Log.d(TAG, "values[4] = " + values[4]);
    	Log.d(TAG, "values[2] = " + values[2]);
    	Log.d(TAG, "values[5] = " + values[5]);
    	// values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
    	// values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

    	// event is the touch event for MotionEvent.ACTION_UP
    	float relativeX = (event.getX() - values[2]) / values[0];
    	float relativeY = (event.getY() - values[5]) / values[4];

    	Log.d(TAG, "relativeX = " + relativeX);
    	Log.d(TAG, "relativeY = " + relativeY);
    	
    	int squareX = ((int) relativeX / (int)oneGridWidth);
    	int squareY = ((int) relativeY / (int) oneGridHeight);
    	
    	Log.d(TAG, "square = " + squareX + " " +squareY );
    	
    	ArrayList<Survivor> clickedSur = new ArrayList<Survivor>();
    	//get a list of all survivors in the clicked square
    	for(Survivor survivor:survivors.getSurvivors())
    	{
    		Log.d(TAG, "surx = " + survivor.getX() + " surY = " + survivor.getY());
    		if(survivor.getX() == squareX && survivor.getY() == squareY )
    		{
    			
    			if(!clickedSur.contains(survivor))
    			{
    				Log.d(TAG, "adding sur to clickedSur");
    				clickedSur.add(survivor);
    			}
    		}
    	}
    	ArrayList <String> survivorNames = new ArrayList<String>();
    	for(Survivor survivor:clickedSur )
    	{
    		Log.d(TAG, "add to surNames");
    		survivorNames.add(survivor.getName());
    	}
    	Log.d(TAG, "survNames.length = " + survivorNames.size());
    	if(survivorNames.size()>0)
    	{
    		listSur(survivorNames);
    	}
    }
	
	private void listSur(ArrayList<String> clickedSur) {
		
		AlertDialog.Builder b = new Builder(gameContext);
		b.setTitle("select a survivor");
		int value =0;
		for(String sur: clickedSur)
		{
			value++;
		}
		String [] list = new String[value];
		for(int i=0; i<list.length; i++)
		{
			list[i] = clickedSur.get(i);
		}
		final String [] surList = list;
		b.setSingleChoiceItems(surList, -1, new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {  
				//item is the index of the chosen item
			}
		});
		
		b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                dialog.dismiss();
                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
                //the selectedPosition can tell what sur has been chosen
                surMenu(surList[selectedPosition]);
            }
		});
		
		AlertDialog alert = b.create();
		alert.show();
		
		
		}

	/**
	 * this will display the survivor list for the selected survivorfrom the click event
	 * 
	 * @param index shows index of the survivor that has been chosen
	 */
	public void surMenu(String name){
		Dialog dialog = new Dialog(gameContext);
		dialog.setContentView(R.layout.survivor_info);
		dialog.setTitle("select action for survivor");
		
		//set up the text and info for the survivor info and the buttons
		Button drop;
		Button scavange;
	  	Button buildFarm;
	  	Button useFarm;
	  	Button buildSafe;
	  	Button move;
	  	
	  	
	  	TextView survivorName;
	  	TextView metabSkill;
	  	TextView scavangeSkill;
	  	TextView mobilitySkill;
	  	TextView buildSkill;
	  	
	  	Survivor survivor = new Survivor();
		int turnCount;
	  	int food;
	  	int resource;

	  	//get the info from sorrect survivor
	  	
	  	for(Survivor sur: survivors.getSurvivors())
	  	{
	  		if(sur.getName().equals(name)){
	  			survivor = sur;
	  		}
	  	}
	  	drop = (Button) dialog.findViewById(R.id.bDropSur);
        scavange = (Button) dialog.findViewById(R.id.bScavanged);
        buildFarm = (Button) dialog.findViewById(R.id.bBuildFarm);
        useFarm = (Button) dialog.findViewById(R.id.bUseFarm);
        buildSafe = (Button) dialog.findViewById(R.id.bBuildstructure);
        move = (Button) dialog.findViewById(R.id.bMoveSafe);
        
        survivorName = (TextView) dialog.findViewById(R.id.tSurvivorName);
        metabSkill = (TextView) dialog.findViewById(R.id.tSkillMet);
        scavangeSkill = (TextView) dialog.findViewById(R.id.tSkillScav);
        mobilitySkill = (TextView) dialog.findViewById(R.id.tSkillMobility);
        buildSkill = (TextView) dialog.findViewById(R.id.tSkillBuilding);
        
        survivorName.setText(survivor.getName());
        metabSkill.setText(survivor.getMet());
        scavangeSkill.setText(survivor.getScav());
        mobilitySkill.setText(survivor.getMob());
        buildSkill.setText(survivor.getbuilding());
        
        drop.setOnClickListener(new OnClickListener() {
        	//remove the survivor
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
        scavange.setOnClickListener(new OnClickListener() {
        	//do the scavange method
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
        buildFarm.setOnClickListener(new OnClickListener() {
        	//do build farm
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
        useFarm.setOnClickListener(new OnClickListener() {
        	//useFarm
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
        buildSafe.setOnClickListener(new OnClickListener() {
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
        move.setOnClickListener(new OnClickListener() {
        	//this requires change to on draw method set a bool value to make the method act slightly different and draw a translutent area around the parts of the grid the person can move to
        	@Override
        	public void onClick(View v) {
        		
        	}
        });
        
	}
	
	
	public void giveContext(Context context)
	{
		gameContext = context;
	}
	
	public void setSurvivors(Survivors survivorsNew)
	{
		Survivor[] sur = survivorsNew.getSurvivors();
		this.survivors = survivorsNew;
	}
	
	public Survivors getSurvivors()
	{
		return survivors;
	}
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(1.0f, Math.min(mScaleFactor, 5.0f));
	        
	        
	        
	        invalidate();
	        return true;
	    }
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	   viewWidth = MeasureSpec.getSize(widthMeasureSpec);
	   viewHeight = MeasureSpec.getSize(heightMeasureSpec);
	   mIcon = Bitmap.createScaledBitmap(tempIcon, viewWidth, viewHeight, true);
	   
	   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}
}