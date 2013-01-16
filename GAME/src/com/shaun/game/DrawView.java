package com.shaun.game;

import android.app.Activity;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.PointF;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class DrawView extends View {
	private static final String TAG = "MyActivity";
	
	private Bitmap mIcon;
	private Bitmap overlay;
	
	private Bitmap tempIcon;
	private Bitmap tempOverlay;
	
	private float mPosX;
	private float mPosY;
	boolean firstRun;
	
	float mHeight;
	float mWidth;
	
	Matrix matrix;
	float [] m = new float [9];
	    
	private float mLastTouchX;
	private float mLastTouchY;
	private static final int INVALID_POINTER_ID = -1;

	//The ‘active pointer’ is the one currently moving our object.
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
    
    int viewWidth, viewHeight;
    protected float origWidth, origHeight;
    int oldMeasuredWidth, oldMeasuredHeight;

	private float yIndent = 0;
	private float xIndent = 0;
	
	private float displayHeight;
	private float displayWidth;

	
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
		overlay = BitmapFactory.decodeResource(getResources(), R.drawable.man);
		DisplayMetrics displayMetrics = new DisplayMetrics();
		((Activity) getContext()).getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
		displayHeight = displayMetrics.heightPixels;
		displayWidth = displayMetrics.widthPixels;
		
		mHeight = tempIcon.getHeight();
		mWidth = tempIcon.getWidth();
		
		firstRun = true;
		
		// Create our ScaleGestureDetector
		mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    clipBounds_canvas = canvas.getClipBounds();
	    canvas.save();
	    matrix = canvas.getMatrix();
	    canvas.translate(mPosX, mPosY);
	    canvas.scale(mScaleFactor, mScaleFactor);
	    canvas.drawBitmap(mIcon, 0, 0, null);
	    canvas.drawBitmap(overlay, 100, 100, null);
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
	    //check the value x and y and if they are greater than bound values then dont draw.
	    canvas.restore();
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
		float relx = ev.getX() / mScaleFactor + clipBounds_canvas.left;
		float rely = ev.getY() / mScaleFactor + clipBounds_canvas.top;
		
		mScaleDetector.onTouchEvent(ev);
	    
	    final int action = ev.getAction();
	    switch (action & MotionEvent.ACTION_MASK) {
	    case MotionEvent.ACTION_DOWN: {
	        final float x = ev.getX();
	        final float y = ev.getY();
	        
	        mLastTouchX = x;
	        mLastTouchY = y;
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
	        			Log.d(TAG ,"max X = " + maxX);
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
	        int xChange = (int) Math.abs(mLastTouchX - ev.getX());
            int yChange = (int) Math.abs(mLastTouchY - ev.getY());
            
            if(( (xChange) < 5 ) && ((yChange) < 5 ))
            {
            	clickNow(ev);
    		}
            
	        
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
	
	public void clickNow(MotionEvent event)
    {
    	// Get the values of the matrix
    	float[] values = new float[9];
    	matrix.getValues(values);

    	// values[2] and values[5] are the x,y coordinates of the top left corner of the drawable image, regardless of the zoom factor.
    	// values[0] and values[4] are the zoom factors for the image's width and height respectively. If you zoom at the same factor, these should both be the same value.

    	// event is the touch event for MotionEvent.ACTION_UP
    	float relativeX = (event.getX() - values[2]) / values[0];
    	float relativeY = (event.getY() - values[5]) / values[4];
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