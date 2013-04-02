package com.shaun.game;

import java.util.ArrayList;
import java.util.Random;

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
import android.graphics.PorterDuff.Mode;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


public class DrawView extends View {
	private static final String TAG = "MyActivity";
	
	private Context gameContext;
	
	private Bitmap house1;
	private Bitmap reEnforced;
	private Bitmap buildFarm;
	@SuppressWarnings("unused")
	private Bitmap tempIcon;
	private Bitmap singleMan;
	private Bitmap redMan;
	private Bitmap partGroup;
	private Bitmap usedGroup;
	private Bitmap group;
	private Bitmap moveImage;
	
	private Databaseknown knownSur = new Databaseknown(this.getContext());
	
	private Survivors survivors;
	private ArrayList<String> knownSurvivors = new ArrayList<String>();
	private ArrayList<Point> surPoints = new ArrayList<Point>();
	private ArrayList<Point> multiPoint = new ArrayList<Point>();
	
	private ArrayList<Point> safePoints = new ArrayList<Point>();	
	private ArrayList<Point> farmPoints = new ArrayList<Point>();
	
	@SuppressWarnings("unused")
	private Boolean moving = false;
	private Boolean knownEmpt = false;
	private float mPosX;
	private float mPosY;
	private int scavengedFood;
	private int scavangedRes;
	
	private String name;
	
	boolean firstRun;
		
	Matrix matrix;
	Matrix oMatrix;
	float [] m = new float [9];
	
	private String empty =  "empty";
	
	private float mLastTouchX;
	private float mLastTouchY;
	private static final int INVALID_POINTER_ID = -1;

	//The ‘active pointer’ is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	
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

	private boolean moveMode = false;
	private Survivor movingSurvivor = new Survivor(0, 0, 0, 0, "empty",0,0);
	private ArrayList<Point> movePlace= new ArrayList<Point>();
	
	private int mannedFarms =0;
	
	private ArrayList<Survivor> usedSurvivor = new ArrayList<Survivor>();
	private ArrayList<Point> usedPoint = new ArrayList<Point>();
	
	private Point multiOne = new Point(-1,-1);
	private ArrayList<Survivor> multiOneSur = new ArrayList<Survivor>();
	private Point multiTwo = new Point(-1,-1);
	private ArrayList<Survivor> multiTwoSur = new ArrayList<Survivor>();
	
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
		singleMan = BitmapFactory.decodeResource(getResources(), R.drawable.survivor);
		redMan = BitmapFactory.decodeResource(getResources(), R.drawable.redsur);
		partGroup = BitmapFactory.decodeResource(getResources(), R.drawable.partgroup);
		usedGroup = BitmapFactory.decodeResource(getResources(), R.drawable.survivorsred);
		group = BitmapFactory.decodeResource(getResources(), R.drawable.survivors);
		
		
		// Create our ScaleGestureDetector
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
		house1 = BitmapFactory.decodeResource(getResources(), R.drawable.house1);
		reEnforced = BitmapFactory.decodeResource(getResources(), R.drawable.reenforced);
		buildFarm = BitmapFactory.decodeResource(getResources(), R.drawable.farmhouse);
		
		moveImage = BitmapFactory.decodeResource(getResources(), R.drawable.bluesquare);
		
		
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		
		firstRun = true;
		
		// Create our ScaleGestureDetector
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	
		knownSur.writeOpen();
		if(knownSur.isEmpty()){
			knownEmpt = true;
		}else{
			knownSurvivors = knownSur.getKnownSurvivor(name);
		}
		knownSur.writeClose();
	}
	
	@SuppressWarnings("deprecation")
	@SuppressLint({ "NewApi", "DrawAllocation" })
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    if(moveMode){
	    	clipBounds_canvas = canvas.getClipBounds();
	    	canvas.save();
	    	canvas.translate(mPosX, mPosY);
	    	canvas.scale(mScaleFactor, mScaleFactor);
	    	matrix = canvas.getMatrix();
	    	drawMoveGrid(canvas);
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
	    	
	    }else{
	    	clipBounds_canvas = canvas.getClipBounds();
	    	canvas.drawColor(0, Mode.CLEAR);
	    	canvas.save();
	    	canvas.translate(mPosX, mPosY);
	    	canvas.scale(mScaleFactor, mScaleFactor);
	    	matrix = canvas.getMatrix();
	    	drawGrid(canvas);
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
	    
	    	canvas.restore();
	    }
	}
	
	/**
	 * takes the size fo the view and divides it by 81 each grid point takes up viewheight/81 x veiwWidth/81
	 */
	public void drawGrid(Canvas canvas)
	{
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
		
		Bitmap grid1 = Bitmap.createScaledBitmap(house1, (int) oneGridWidth, (int) oneGridHeight, true);
		
		for(int y=0; y<9;y++)
		{
			//do the first 9 across draws for the groups
			for(int i=0; i<9; i++)
			{	
					canvas.drawBitmap(grid1, i*oneGridWidth, y*oneGridHeight,null);
				
			}
		}
		
		if(safePoints != null)
		{
			Bitmap safe = Bitmap.createScaledBitmap(reEnforced, (int) oneGridWidth, (int) oneGridHeight, true);
			
			for(Point point: safePoints)
			{
				canvas.drawBitmap(safe,  oneGridWidth*point.x, oneGridHeight*point.y, null);
			}
		}
		
		if(farmPoints != null)
		{
			Bitmap farm = Bitmap.createScaledBitmap(buildFarm, (int) oneGridWidth, (int) oneGridHeight, true);
			
			for(Point point: farmPoints)
			{
				canvas.drawBitmap(farm, oneGridWidth*point.x, oneGridHeight*point.y, null);
			}
		}
	}

	public void drawMoveGrid(Canvas canvas)
	{
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
		
		Bitmap grid1 = Bitmap.createScaledBitmap(house1, (int) oneGridWidth, (int) oneGridHeight, true);
		
		for(int y=0; y<9;y++)
		{
			//do the first 9 across draws for the groups
			for(int i=0; i<9; i++)
			{	
					canvas.drawBitmap(grid1, i*oneGridWidth, y*oneGridHeight,null);
			}
		}
		
		if(safePoints != null)
		{
			Bitmap safe = Bitmap.createScaledBitmap(reEnforced, (int) oneGridWidth, (int) oneGridHeight, true);
			
			for(Point point: safePoints)
			{
				canvas.drawBitmap(safe,  oneGridWidth*point.x, oneGridHeight*point.y, null);
			}
		}
		
		if(farmPoints != null)
		{
			Bitmap farm = Bitmap.createScaledBitmap(buildFarm, (int) oneGridWidth, (int) oneGridHeight, true);
			
			for(Point point: farmPoints)
			{
				canvas.drawBitmap(farm, oneGridWidth*point.x, oneGridHeight*point.y, null);
			}
		}
		
		//after drawing the grid place blue squares where the player can move to
		drawMove(canvas);
	}
	
	private void drawMove(Canvas canvas) {
		/*
		 * take the moveingSurvivor and get coordinates x, y,
		 * get his mobility skill
		 * now grow out a list of all the points that the survivor can reach based on his mobility
		 * go thorugh the list and draw the colour of the movable space  
		 */
		int x = movingSurvivor.getX();
		int y = movingSurvivor.getY();
		
		int mobility = movingSurvivor.getMob();
		
		//the plus add
		for(int i=1; i<=mobility; i++)
		{
			int newX = x+ i;
			if(newX<=8)
			{
				Point point = new Point(newX, y);
				movePlace.add(point);
			}
			int newY = y+i;
			if(newY<=8)
			{
				Point point = new Point(x, newY);
				movePlace.add(point);
			}
			
			
			newX = x-i;
			if(newX >=0)
			{
				Point point = new Point(newX, y);
				movePlace.add(point);
			}
			
			newY = y-i;
			if(newY>=0)
			{
				Point point = new Point(x, newY);
				movePlace.add(point);
			}
			
			Point point = new Point(0, 0);
			int z = mobility - i;
			for(int j =1; j<=z; j++)
			{
				newX = x+i;
				newY = y+j;
				if(newX<=8 && newY<=8)
				{	
					point = new Point(newX, newY);
					movePlace.add(point);
				}
				
				newX = x+i;
				newY = y-j;
				if(newX<=8 && newY>=0)
				{
					point = new Point(newX, newY);
					movePlace.add(point);
				}
				
				newX = x-i;
				newY = y+j;
				if(newX>=0 && newY<=8)
				{
					point = new Point(newX, newY);
					movePlace.add(point);
				}
				
				newX = x-i;
				newY = y-j;
				if(newX>=0 && newY>=0)
				{
					point = new Point(newX, newY);
					movePlace.add(point);
				}
			}
		}
	
		//now use movePlace to draw the points
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
		
		Bitmap moveGrid = Bitmap.createScaledBitmap(moveImage, (int) oneGridWidth, (int) oneGridHeight, true);
		for(Point point: movePlace)
		{
			canvas.drawBitmap(moveGrid, point.x*oneGridWidth, point.y*oneGridHeight,null);
		}
		
	}

	public void drawSur(Canvas canvas)
	{
		multiPoint = new ArrayList<Point>();
		surPoints = new ArrayList<Point>();
		
		multiOne = new Point(-1,-1);
		multiOneSur = new ArrayList<Survivor>();
		multiTwo = new Point(-1,-1);
		multiTwoSur = new ArrayList<Survivor>();
		
		//iterate through all the survivors and place them on the map
		for(Survivor tempSurvivor: survivors.getSurvivors())
		{
			String empty = "empty";
			if(! (tempSurvivor.getName().equals(empty)) )
			{
				Point point = new Point(tempSurvivor.getX(), tempSurvivor.getY());
				if(surPoints.isEmpty())
				{
				}
				
				
				if(!(surPoints.contains(point)))
				{
					surPoints.add(point);
				}else{
					if(multiPoint.contains(point))
					{
						//its either in multione or multiTwo
						if(multiOne.x==point.x && multiOne.y==point.y)
						{
							multiOne = point;
							multiOneSur.add(tempSurvivor);
						}else{
							multiTwo = point;
							multiTwoSur.add(tempSurvivor);
						}
					}else{
						//new multipoint
						multiPoint.add(point);
						if(multiOne.x==-1 && multiOne.y==-1)
						{
							multiOne = point;
							multiOneSur.add(tempSurvivor);
						}else{
							multiTwo = point;
							multiTwoSur.add(tempSurvivor);
						}
					}
				}
				for(Point tPoint: multiPoint)
				{
					
						while(surPoints.contains(tPoint)) {surPoints.remove(tPoint); }
				}
			}
		}
		
		float oneGridHeight = viewHeight / 9;
		float oneGridWidth = viewWidth / 9;
		
		Bitmap singleSur = Bitmap.createScaledBitmap(singleMan, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap redSur = Bitmap.createScaledBitmap(redMan, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap oneUsed = Bitmap.createScaledBitmap(partGroup, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap used = Bitmap.createScaledBitmap(usedGroup, (int) oneGridWidth, (int) oneGridHeight, true);
		Bitmap groupSur = Bitmap.createScaledBitmap(group, (int) oneGridWidth, (int) oneGridHeight, true);
		
		for(Point point:surPoints)
		{
			//drawOnePerson
			if(!multiPoint.contains(point))
			{
					
				if(usedPoint.contains(point))
				{
					canvas.drawBitmap(redSur, point.x*oneGridWidth, point.y*oneGridHeight,null);
				}else{
					canvas.drawBitmap(singleSur, point.x*oneGridWidth, point.y*oneGridHeight,null);
				}
			}else{
			}
		}
			
		int multiOneSurSize = multiOneSur.size();
		int countOne =0;
			
		int multiTwoSurSize = multiTwoSur.size();
		int countTwo =0;
			
			
		for(Point point:multiPoint)
		{
			//drawMultiPeople
			//check if any of the people in point that belongs two are used 
			
			if(multiOne.x == point.x && multiOne.y == point.y)
			{
				//check number of sur in multiOneSur that are used
				for(Survivor survivor: multiOneSur)
				{
					if(usedSurvivor.contains(survivor))
					{
						countOne++;
					}
				}
			}else{
				//check the number of sur in multiTwoSur that are used
				for(Survivor survivor: multiTwoSur)
				{
					if(usedSurvivor.contains(survivor))
					{
						countTwo++;
					}
				}
			}
		}
			
		if(multiOne.x == -1 || multiOne.y == -1)
		{
			//multiOne not set so dont draw
		}else{
			//check the size of count to determine what to draw
			if(countOne==0)
			{
				canvas.drawBitmap(groupSur, multiOne.x*oneGridWidth, multiOne.y*oneGridHeight, null);
			}else{
				//its greater than 0 so check if it == the size of multiSur
				if(countOne==multiOneSurSize)
				{
					canvas.drawBitmap(used, multiOne.x*oneGridWidth, multiOne.y*oneGridHeight, null);
				}else{
					canvas.drawBitmap(oneUsed, multiOne.x*oneGridWidth, multiOne.y*oneGridHeight, null);
				}
				
			}
		}
			
		if(multiTwo.x == -1 || multiTwo.y == -1)
		{
			//multiTwo not set so dont draw
		}else{
			//check the size of count to determine what to draw
			if(countTwo==0)
			{
				canvas.drawBitmap(groupSur, multiTwo.x*oneGridWidth, multiTwo.y*oneGridHeight, null);
			}else{
				//its greater than 0 so check if it == the size of multiSur
				if(countTwo==multiTwoSurSize)
				{
					canvas.drawBitmap(used, multiTwo.x*oneGridWidth, multiTwo.y*oneGridHeight, null);
				}else{
					canvas.drawBitmap(oneUsed, multiTwo.x*oneGridWidth, multiTwo.y*oneGridHeight, null);
				}
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
    	
    	// values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
    	// values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

    	// event is the touch event for MotionEvent.ACTION_UP
    	float relativeX = (event.getX() - values[2]) / values[0];
    	float relativeY = (event.getY() - values[5]) / values[4];
    	
    	int squareX = ((int) relativeX / (int)oneGridWidth);
    	int squareY = ((int) relativeY / (int) oneGridHeight);
    	
    	if(moveMode)
    	{
    		Point point =  new Point(squareX, squareY);
    		if(movePlace.contains(point))
    		{
    			
    			//move the movingSurvivor to the new location
    			movingSurvivor.setX(squareX);
    			movingSurvivor.setY(squareY);
    			
    			survivors.removeSurvivors(movingSurvivor.getName());
    			int i =0;
    			for(Survivor survivor: survivors.getSurvivors())
    			{
    				if(survivor.getName().equals(empty))
    				{
    					survivors.setSurvivor(movingSurvivor, i);
    					characterUsed(movingSurvivor);
    					moveMode = false;
    	    			movingSurvivor = new Survivor(0, 0, 0, 0, "empty",0,0);
    	    			movePlace= new ArrayList<Point>();
    	    			invalidate();
    					break;
    				}
    				i++;
    			}
    		}
    		
    	}else{
    		ArrayList<Survivor> clickedSur = new ArrayList<Survivor>();
    		//get a list of all survivors in the clicked square
    		for(Survivor survivor:survivors.getSurvivors())
    		{
    			if(survivor.getX() == squareX && survivor.getY() == squareY )
    			{
    			
    				if(!clickedSur.contains(survivor))
    				{
    					clickedSur.add(survivor);
    				}
    			}
    		}
    		ArrayList <String> survivorNames = new ArrayList<String>();
    		for(Survivor survivor:clickedSur )
    		{
    			if(!usedSurvivor.contains(survivor))
    			{
    				survivorNames.add(survivor.getName());
    			}
    		}
    		if(survivorNames.size()>0)
    		{
    			listSur(survivorNames);
    		}
    	}
    }
	
	private void listSur(ArrayList<String> clickedSur) {
		
		AlertDialog.Builder b = new Builder(gameContext);
		b.setTitle("select a survivor");
		int value =0;
		for(@SuppressWarnings("unused") String sur: clickedSur)
		{
			value++;
		}
		String [] list = new String[value];
		for(int i=0; i<list.length; i++)
		{
			list[i] = clickedSur.get(i);
		}
		final String [] surList = list;
		b.setSingleChoiceItems(surList, 0, new DialogInterface.OnClickListener() {
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
		
		b.setNegativeButton("cancel",  new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int item) {  
				
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
		
		int type = 0;
		Survivor selectedSur = survivors.getSurvivor(name);
        for(Point point: safePoints)
        {
        	if(selectedSur.getX() == point.x && selectedSur.getY() == point.y)
            {
            	type = 1;
            }
        }
        
        for(Point point: farmPoints)
        {
        	if(selectedSur.getX() == point.x && selectedSur.getY() == point.y)
        	{
        		type = 2;
        	}
        }
        
        final Dialog dialog = new Dialog(gameContext);
        Button drop;
		Button scavange;
		Button useFarm;
		Button buildSafe;
		Button buildFarm;
		Button move;
  	
  	
		TextView survivorName;
		TextView metabSkill;
		TextView scavangeSkill;
		TextView mobilitySkill;
		TextView buildSkill;
		
		final Survivor survivor;
        switch(type)
        {
        
        case(0):
			dialog.setContentView(R.layout.survivor_pop);
			dialog.setTitle("select action for survivor");
		
			//set up the text and info for the survivor info and the buttons
	  	
			survivor = selectedSur;
	  	
			drop = (Button) dialog.findViewById(R.id.bDropSurvivor);
			scavange = (Button) dialog.findViewById(R.id.bScavange);
			buildSafe = (Button) dialog.findViewById(R.id.bBuildstruc);
			move = (Button) dialog.findViewById(R.id.bMove);
        
			survivorName = (TextView) dialog.findViewById(R.id.tSurvivorsName);
			metabSkill = (TextView) dialog.findViewById(R.id.tSkillMetab);
			scavangeSkill = (TextView) dialog.findViewById(R.id.tSkillScavange);
			mobilitySkill = (TextView) dialog.findViewById(R.id.tSkillmobility);
			buildSkill = (TextView) dialog.findViewById(R.id.tSkillbuilding);
        
			survivorName.setText(survivor.getName());
			metabSkill.setText("metab: " + survivor.getMet());
			scavangeSkill.setText("Scavange: " + survivor.getScav());
			mobilitySkill.setText("mobility: " + survivor.getMob());
			buildSkill.setText("build: " + survivor.getbuilding());
			
			drop.setOnClickListener(new OnClickListener() {
				//remove the survivor
				@Override
				public void onClick(View v) {
					survivors.removeSurvivors(survivor.getName());
					invalidate();
					dialog.dismiss();
				}
			});
        
			scavange.setOnClickListener(new OnClickListener() {
				//do the scavange method
				@Override
				public void onClick(View v) {
					scavanged(survivor);
					characterUsed(survivor);
					invalidate();
					dialog.dismiss();
				}	
			});
        
			buildSafe.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					scavangedRes -= survivor.getbuilding();
					addSafe(survivor);
					characterUsed(survivor);
					invalidate();
					dialog.dismiss();
				}	
			});
        
			move.setOnClickListener(new OnClickListener() {
				//this requires change to on draw method set a bool value to make the method act slightly different and draw a translutent area around the parts of the grid the person can move to
				@Override
				public void onClick(View v) {
					setMove(survivor);
					dialog.dismiss();
				}	
			});
			dialog.show();
			break;
        case 1:
			dialog.setContentView(R.layout.safe_pop);
			dialog.setTitle("select action for survivor");
		
			//set up the text and info for the survivor info and the buttons
	  	
			survivor = selectedSur;
	  	
			drop = (Button) dialog.findViewById(R.id.bsDropSurvivor);
			buildFarm = (Button) dialog.findViewById(R.id.bsBuildFarm);
			move = (Button) dialog.findViewById(R.id.bsMove);
        
			survivorName = (TextView) dialog.findViewById(R.id.tsSurvivorsName);
			metabSkill = (TextView) dialog.findViewById(R.id.tsSkillMetab);
			scavangeSkill = (TextView) dialog.findViewById(R.id.tsSkillScavange);
			mobilitySkill = (TextView) dialog.findViewById(R.id.tsSkillmobility);
			buildSkill = (TextView) dialog.findViewById(R.id.tsSkillbuilding);
        
			survivorName.setText(survivor.getName());
			metabSkill.setText("metab: " + survivor.getMet());
			scavangeSkill.setText("Scavange: " + survivor.getScav());
			mobilitySkill.setText("mobility: " + survivor.getMob());
			buildSkill.setText("build: " + survivor.getbuilding());
			
			drop.setOnClickListener(new OnClickListener() {
				//remove the survivor
				@Override
				public void onClick(View v) {
					survivors.removeSurvivors(survivor.getName());
					invalidate();
					dialog.dismiss();
				}
			});
        
			buildFarm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					scavangedRes -= survivor.getbuilding();
					addFarm(survivor);
					characterUsed(survivor);
					invalidate();
					dialog.dismiss();
				}	
			});
        
			move.setOnClickListener(new OnClickListener() {
				//this requires change to on draw method set a bool value to make the method act slightly different and draw a translutent area around the parts of the grid the person can move to
				@Override
				public void onClick(View v) {
					setMove(survivor);
					dialog.dismiss();
				}	
			});
			dialog.show();
        	
        	break;
        case 2:
        	dialog.setContentView(R.layout.farm_pop);
			dialog.setTitle("select action for survivor");
		
			//set up the text and info for the survivor info and the buttons
	  	
			survivor = selectedSur;
	  	
			drop = (Button) dialog.findViewById(R.id.bfDropSurvivor);
			move = (Button) dialog.findViewById(R.id.bfMove);
			useFarm = (Button) dialog.findViewById(R.id.bfManFarm);
        
			survivorName = (TextView) dialog.findViewById(R.id.tfSurvivorsName);
			metabSkill = (TextView) dialog.findViewById(R.id.tfSkillMetab);
			scavangeSkill = (TextView) dialog.findViewById(R.id.tfSkillScavange);
			mobilitySkill = (TextView) dialog.findViewById(R.id.tfSkillmobility);
			buildSkill = (TextView) dialog.findViewById(R.id.tfSkillbuilding);
        
			survivorName.setText(survivor.getName());
			metabSkill.setText("metab: " + survivor.getMet());
			scavangeSkill.setText("Scavange: " + survivor.getScav());
			mobilitySkill.setText("mobility: " + survivor.getMob());
			buildSkill.setText("build: " + survivor.getbuilding());
			
			drop.setOnClickListener(new OnClickListener() {
				//remove the survivor
				@Override
				public void onClick(View v) {
					survivors.removeSurvivors(survivor.getName());
					invalidate();
					dialog.dismiss();
				}
			});
        
			useFarm.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					mannedFarms++;
					characterUsed(survivor);
					invalidate();
					dialog.dismiss();
				}	
			});
        
			move.setOnClickListener(new OnClickListener() {
				//this requires change to on draw method set a bool value to make the method act slightly different and draw a translutent area around the parts of the grid the person can move to
				@Override
				public void onClick(View v) {
					setMove(survivor);
					dialog.dismiss();
				}	
			});
			dialog.show();
        	
        	break;
        }
        	
	}
	
	private void addSafe(Survivor survivor)
	{
		Point point = new Point(survivor.getX(), survivor.getY());
		safePoints.add(point);
	}
	
	public void setSafePoints(ArrayList<Point> newSafe)
	{
		this.safePoints = newSafe;
	}
	
	public ArrayList<Point> getSafe()
	{
		return this.safePoints;
	}

	
	private void addFarm(Survivor survivor)
	{
		Point point = new Point(survivor.getX(), survivor.getY());
		farmPoints.add(point);
	}
	
	public ArrayList<Point> getFarms()
	{
		return this.farmPoints;
	}
	
	public void setFarms(ArrayList<Point> oldPoints)
	{
		this.farmPoints = oldPoints;
	}
	
	
	private void characterUsed(Survivor survivor) {
		Point point = new Point(survivor.getX(),survivor.getY());
		usedPoint.add(point);
		usedSurvivor.add(survivor);
	}
	
	public void emptyUsedSur()
	{
		usedPoint.clear();
		usedSurvivor.clear();
		invalidate();
	}
	
	public void setMove(Survivor survivor)
	{
		moveMode = true;
		movingSurvivor = survivor;
		invalidate();
	}
	
	public void giveContext(Context context)
	{
		gameContext = context;
	}
	
	public void setSurvivors(Survivors survivorsNew)
	{
		this.survivors = survivorsNew;
		if(knownEmpt)
		{
			for(Survivor survivor : survivors.getSurvivors())
			{
				knownSurvivors.add(survivor.getName());
			}
			knownEmpt = false;
		}
		invalidate();
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
	        
	        float[] values = new float[9];
	        matrix.getValues(values);
	    	
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
	        return true;
	    }
	}
	
	@Override 
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec){
	   viewWidth = MeasureSpec.getSize(widthMeasureSpec);
	   viewHeight = MeasureSpec.getSize(heightMeasureSpec);
	   
	   super.onMeasure(widthMeasureSpec, heightMeasureSpec);
	}

	
	private void scavanged(Survivor s)
	{
		
		// take the point and create the maximum food for the square using the distance of the point from Home point
		
   		Random generator = new Random();
   		int getSur = generator.nextInt(10);
   		if(getSur>=7)
   		{
   			int surInd = generator.nextInt(OpeningScreenActivity.survivorNames.length);
   			Survivor newSurvivor = new Survivor();
   			boolean found = false;
   			while(!found)
   			{
   				//get random new survivor 
   				String newName = OpeningScreenActivity.survivorNames[surInd];
   				if((!knownSurvivors.contains(newName)))
   				{
   					int mob = generator.nextInt(5) + 1;
   					int scav = generator.nextInt(5) + 1;
   					int build = generator.nextInt(5) + 1;
   					int metab = generator.nextInt(5) + 1;
   					newSurvivor = new Survivor(mob, scav, build, metab, newName,s.getX(),s.getY());
   					found=true;
   				}else{
   					surInd = generator.nextInt(OpeningScreenActivity.survivorNames.length);
   				}
   				if(knownSurvivors.size() == 20)
   				{
   					found=true;
   				}
   			}

   			if(survivors.isFull())
   			{
   				// prompt the user to drop a survivor if they want to keep the current one
   				AlertDialog.Builder alert = new AlertDialog.Builder(gameContext);
   				alert.setTitle("YOU FOUND A NEW SURVIVOR");
   				alert.setMessage("you already have the maxium space for survivors, would you like to drop one of your current survivors for " + newSurvivor.getName() + "?");
   				final Survivor tempNewSurvivor = newSurvivor;
   				
   				alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int whichButton) {
   					AlertDialog.Builder b = new Builder(gameContext);
   	   				b.setTitle("select a survivor to drop for new survivor " + tempNewSurvivor.getName());
   	   				Survivor[] survivorList = survivors.getSurvivors();
   	   				String [] list = new String[survivorList.length];
   	   				for(int i=0; i<survivorList.length; i++)
   	   				{
   	   					 list[i] = survivorList[i].getName();
   	   				}
   	   				final String [] surList = list;
   	   				b.setSingleChoiceItems(surList, 0, new DialogInterface.OnClickListener() {
   	   					public void onClick(DialogInterface dialog, int item) {  
   	   						//item is the index of the chosen item
   	   					}
   	   				});
   	   				
   	   				b.setPositiveButton("OK", new DialogInterface.OnClickListener() {
   	   		            public void onClick(DialogInterface dialog, int whichButton) {
   	   		                dialog.dismiss();
   	   		                int selectedPosition = ((AlertDialog)dialog).getListView().getCheckedItemPosition();
   	   		                //the selectedPosition can tell what sur has been chosen
   	   		                
   	   		                survivors.removeSurvivors(surList[selectedPosition]);
   	   		                knownSurvivors.add(tempNewSurvivor.getName());
   	    	   				survivors.add(tempNewSurvivor);
   	   		            }
   	   				});
   	   				
   	   				b.setNegativeButton("cancel",  new DialogInterface.OnClickListener() {
   	   					public void onClick(DialogInterface dialog, int item) {  
   	   						knownSurvivors.add(tempNewSurvivor.getName());
   	   					}
   	   				});
   	   				
   	   				AlertDialog alertInner = b.create();
   	   				alertInner.show();
   				}
   				});
   				
   				alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
   	   				public void onClick(DialogInterface dialog, int whichButton) {
   	   					//dont keep newSurvivor
   	   				}
   				});
   				alert.show();
   			}else{
   				//ask if the user wants to keep the new survivor
   				
   				AlertDialog.Builder alert = new AlertDialog.Builder(gameContext);
   				
   				final Survivor tempNewSurvivor = newSurvivor;
   				
   				alert.setTitle("while scavenging " + s.getName() + " found a survivor");
   				alert.setMessage("keep survivor " + newSurvivor.getName());

   				alert.setPositiveButton("YES", new DialogInterface.OnClickListener() {
   				public void onClick(DialogInterface dialog, int whichButton) {
   					knownSurvivors.add(tempNewSurvivor.getName());
   	   				survivors.add(tempNewSurvivor);
   				}
   				});

   				alert.setNegativeButton("NO", new DialogInterface.OnClickListener() {
   				 public void onClick(DialogInterface dialog, int whichButton) {
   				     // Canceled.
   					knownSurvivors.add(tempNewSurvivor.getName());
   				}
   				});
   				
   				alert.show();
   			}
   		}
   		
   		int maxFood = generator.nextInt(3);
   		
   		// take the skill of the survivor and work out how much food is lost
   		int foodSca = s.getScav();
   		int addedFood = generator.nextInt(foodSca);
   		
   		int foodVal = maxFood + addedFood;
   		scavengedFood += foodVal;
   		
   		//do above for resources
   		int maxResources = generator.nextInt(3);
   		
   		int resSca = s.getScav();
   		int addedRes = generator.nextInt(resSca);
   		
   		int resVal = maxResources + addedRes;
   		scavangedRes += resVal;
   		
   		
   	}
	
	public int getScavFood()
	{
		return this.scavengedFood;
	}
	
	public int getScavRes()
	{
		return this.scavangedRes;
	}
	
	public int getMaddedFarms() {
		return mannedFarms;
	}
	
	public void setMannedFarms()
	{
		this.mannedFarms = 0;
	}

	public void setSaveName(String name) {
		this.name = name;
	}

	public void resetBoard() {
		mScaleFactor = (float) 1.0;
		
		
		mPosX = 0;
		mPosY = 0;
		invalidate();
	}
}