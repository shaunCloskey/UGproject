package com.shaun.game;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.ScaleGestureDetector;
import android.view.View;


public class DrawView extends View {
	private Bitmap mIcon;
	private Bitmap overlay;
	private float mPosX;
	private float mPosY;
	
	Matrix matrix;
	    
	private float mLastTouchX;
	private float mLastTouchY;
	private static final int INVALID_POINTER_ID = -1;

	//The �active pointer� is the one currently moving our object.
	private int mActivePointerId = INVALID_POINTER_ID;
	
	private ScaleGestureDetector mScaleDetector;
	private float mScaleFactor = 1.f;
	private float maxScale = 3f;
	
	public DrawView(Context context) {
		super(context, null, 0);
		super.setClickable(true);
		mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.snoopy);
		overlay = BitmapFactory.decodeResource(getResources(), R.drawable.man);
	    // Create our ScaleGestureDetector
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	    
	public DrawView(Context context, AttributeSet attrs) {
	        super(context, attrs, 0);
	        super.setClickable(true);
	        mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.snoopy);
			overlay = BitmapFactory.decodeResource(getResources(), R.drawable.man);
		    // Create our ScaleGestureDetector
		    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	    
	public DrawView(Context context, AttributeSet attrs, int defStyle) {
	    super(context, attrs, defStyle);
	    super.setClickable(true);
	    mIcon = BitmapFactory.decodeResource(getResources(), R.drawable.snoopy);
		overlay = BitmapFactory.decodeResource(getResources(), R.drawable.man);
	    // Create our ScaleGestureDetector
	    mScaleDetector = new ScaleGestureDetector(context, new ScaleListener());
	}
	
	
	@Override
	public void onDraw(Canvas canvas) {
	    super.onDraw(canvas);
	    
	    canvas.save();
	    canvas.translate(mPosX, mPosY);
	    canvas.scale(mScaleFactor, mScaleFactor);
	    canvas.drawBitmap(mIcon, 0, 0, null);
	    canvas.drawBitmap(overlay, 100, 100, null);
	    canvas.restore();
	}


	@Override
	public boolean onTouchEvent(MotionEvent ev) {
	    // Let the ScaleGestureDetector inspect all events.
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
	            final float dx = x - mLastTouchX;
	            final float dy = y - mLastTouchY;

	            mPosX += dx;
	            mPosY += dy;

	            invalidate();
	        }

	        mLastTouchX = x;
	        mLastTouchY = y;

	        break;
	    }
	        
	    case MotionEvent.ACTION_UP: {
	        mActivePointerId = INVALID_POINTER_ID;
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
	
	private class ScaleListener extends ScaleGestureDetector.SimpleOnScaleGestureListener {
	    @Override
	    public boolean onScale(ScaleGestureDetector detector) {
	        mScaleFactor *= detector.getScaleFactor();
	        
	        // Don't let the object get too small or too large.
	        mScaleFactor = Math.max(0.1f, Math.min(mScaleFactor, 5.0f));

	        invalidate();
	        return true;
	    }
	}
}