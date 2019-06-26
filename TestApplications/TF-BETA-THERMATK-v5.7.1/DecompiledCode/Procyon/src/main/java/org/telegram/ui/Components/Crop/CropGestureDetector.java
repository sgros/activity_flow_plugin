// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.ui.Components.Crop;

import android.view.MotionEvent;
import android.view.ScaleGestureDetector$OnScaleGestureListener;
import org.telegram.messenger.AndroidUtilities;
import android.view.ViewConfiguration;
import android.content.Context;
import android.view.VelocityTracker;
import android.view.ScaleGestureDetector;

public class CropGestureDetector
{
    private static final int INVALID_POINTER_ID = -1;
    private int mActivePointerId;
    private int mActivePointerIndex;
    private ScaleGestureDetector mDetector;
    private boolean mIsDragging;
    float mLastTouchX;
    float mLastTouchY;
    private CropGestureListener mListener;
    final float mMinimumVelocity;
    final float mTouchSlop;
    private VelocityTracker mVelocityTracker;
    private boolean started;
    
    public CropGestureDetector(final Context context) {
        this.mMinimumVelocity = (float)ViewConfiguration.get(context).getScaledMinimumFlingVelocity();
        this.mTouchSlop = (float)AndroidUtilities.dp(1.0f);
        this.mActivePointerId = -1;
        this.mActivePointerIndex = 0;
        this.mDetector = new ScaleGestureDetector(context, (ScaleGestureDetector$OnScaleGestureListener)new ScaleGestureDetector$OnScaleGestureListener() {
            public boolean onScale(final ScaleGestureDetector scaleGestureDetector) {
                final float scaleFactor = scaleGestureDetector.getScaleFactor();
                if (!Float.isNaN(scaleFactor) && !Float.isInfinite(scaleFactor)) {
                    CropGestureDetector.this.mListener.onScale(scaleFactor, scaleGestureDetector.getFocusX(), scaleGestureDetector.getFocusY());
                    return true;
                }
                return false;
            }
            
            public boolean onScaleBegin(final ScaleGestureDetector scaleGestureDetector) {
                return true;
            }
            
            public void onScaleEnd(final ScaleGestureDetector scaleGestureDetector) {
            }
        });
    }
    
    float getActiveX(final MotionEvent motionEvent) {
        try {
            return motionEvent.getX(this.mActivePointerIndex);
        }
        catch (Exception ex) {
            return motionEvent.getX();
        }
    }
    
    float getActiveY(final MotionEvent motionEvent) {
        try {
            return motionEvent.getY(this.mActivePointerIndex);
        }
        catch (Exception ex) {
            return motionEvent.getY();
        }
    }
    
    public boolean isDragging() {
        return this.mIsDragging;
    }
    
    public boolean isScaling() {
        return this.mDetector.isInProgress();
    }
    
    public boolean onTouchEvent(final MotionEvent motionEvent) {
        this.mDetector.onTouchEvent(motionEvent);
        final int n = motionEvent.getAction() & 0xFF;
        boolean mIsDragging = false;
        if (n != 0) {
            if (n != 1 && n != 3) {
                if (n == 6) {
                    final int n2 = (0xFF00 & motionEvent.getAction()) >> 8;
                    if (motionEvent.getPointerId(n2) == this.mActivePointerId) {
                        int n3;
                        if (n2 == 0) {
                            n3 = 1;
                        }
                        else {
                            n3 = 0;
                        }
                        this.mActivePointerId = motionEvent.getPointerId(n3);
                        this.mLastTouchX = motionEvent.getX(n3);
                        this.mLastTouchY = motionEvent.getY(n3);
                    }
                }
            }
            else {
                this.mActivePointerId = -1;
            }
        }
        else {
            this.mActivePointerId = motionEvent.getPointerId(0);
        }
        int mActivePointerId = this.mActivePointerId;
        if (mActivePointerId == -1) {
            mActivePointerId = 0;
        }
        this.mActivePointerIndex = motionEvent.findPointerIndex(mActivePointerId);
        final int action = motionEvent.getAction();
        if (action != 0) {
            if (action == 1) {
                if (this.mIsDragging) {
                    if (this.mVelocityTracker != null) {
                        this.mLastTouchX = this.getActiveX(motionEvent);
                        this.mLastTouchY = this.getActiveY(motionEvent);
                        this.mVelocityTracker.addMovement(motionEvent);
                        this.mVelocityTracker.computeCurrentVelocity(1000);
                        final float xVelocity = this.mVelocityTracker.getXVelocity();
                        final float yVelocity = this.mVelocityTracker.getYVelocity();
                        if (Math.max(Math.abs(xVelocity), Math.abs(yVelocity)) >= this.mMinimumVelocity) {
                            this.mListener.onFling(this.mLastTouchX, this.mLastTouchY, -xVelocity, -yVelocity);
                        }
                    }
                    this.mIsDragging = false;
                }
                final VelocityTracker mVelocityTracker = this.mVelocityTracker;
                if (mVelocityTracker != null) {
                    mVelocityTracker.recycle();
                    this.mVelocityTracker = null;
                }
                this.started = false;
                return true;
            }
            if (action != 2) {
                if (action != 3) {
                    return true;
                }
                final VelocityTracker mVelocityTracker2 = this.mVelocityTracker;
                if (mVelocityTracker2 != null) {
                    mVelocityTracker2.recycle();
                    this.mVelocityTracker = null;
                }
                this.started = false;
                this.mIsDragging = false;
                return true;
            }
        }
        if (!this.started) {
            this.mVelocityTracker = VelocityTracker.obtain();
            final VelocityTracker mVelocityTracker3 = this.mVelocityTracker;
            if (mVelocityTracker3 != null) {
                mVelocityTracker3.addMovement(motionEvent);
            }
            this.mLastTouchX = this.getActiveX(motionEvent);
            this.mLastTouchY = this.getActiveY(motionEvent);
            this.mIsDragging = false;
            return this.started = true;
        }
        final float activeX = this.getActiveX(motionEvent);
        final float activeY = this.getActiveY(motionEvent);
        final float n4 = activeX - this.mLastTouchX;
        final float n5 = activeY - this.mLastTouchY;
        if (!this.mIsDragging) {
            if ((float)Math.sqrt(n4 * n4 + n5 * n5) >= this.mTouchSlop) {
                mIsDragging = true;
            }
            this.mIsDragging = mIsDragging;
        }
        if (this.mIsDragging) {
            this.mListener.onDrag(n4, n5);
            this.mLastTouchX = activeX;
            this.mLastTouchY = activeY;
            final VelocityTracker mVelocityTracker4 = this.mVelocityTracker;
            if (mVelocityTracker4 != null) {
                mVelocityTracker4.addMovement(motionEvent);
            }
        }
        return true;
    }
    
    public void setOnGestureListener(final CropGestureListener mListener) {
        this.mListener = mListener;
    }
    
    public interface CropGestureListener
    {
        void onDrag(final float p0, final float p1);
        
        void onFling(final float p0, final float p1, final float p2, final float p3);
        
        void onScale(final float p0, final float p1, final float p2);
    }
}
