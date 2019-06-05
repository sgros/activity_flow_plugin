// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import android.view.GestureDetector$SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.GestureDetector$OnGestureListener;
import android.content.Context;
import android.view.GestureDetector;
import android.view.View$OnTouchListener;

public class SwipeMotionDetector implements View$OnTouchListener
{
    private GestureDetector gestureDetector;
    private OnSwipeListener onSwipeListener;
    
    public SwipeMotionDetector(final Context context, final OnSwipeListener onSwipeListener) {
        this.gestureDetector = new GestureDetector(context, (GestureDetector$OnGestureListener)new GestureListener());
        this.onSwipeListener = onSwipeListener;
    }
    
    public boolean onTouch(final View view, final MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }
    
    private final class GestureListener extends GestureDetector$SimpleOnGestureListener
    {
        public boolean onDoubleTap(final MotionEvent motionEvent) {
            return SwipeMotionDetector.this.onSwipeListener.onDoubleTap();
        }
        
        public boolean onDown(final MotionEvent motionEvent) {
            return true;
        }
        
        public boolean onFling(final MotionEvent motionEvent, final MotionEvent motionEvent2, final float a, final float a2) {
            final float n = motionEvent2.getY() - motionEvent.getY();
            final float n2 = motionEvent2.getX() - motionEvent.getX();
            if (Math.abs(n2) > Math.abs(n)) {
                if (Math.abs(n2) > 100.0f && Math.abs(a) > 100.0f) {
                    if (n2 > 0.0f) {
                        if (SwipeMotionDetector.this.onSwipeListener != null) {
                            SwipeMotionDetector.this.onSwipeListener.onSwipeRight();
                        }
                    }
                    else if (SwipeMotionDetector.this.onSwipeListener != null) {
                        SwipeMotionDetector.this.onSwipeListener.onSwipeLeft();
                    }
                }
            }
            else if (Math.abs(n) > 100.0f && Math.abs(a2) > 100.0f) {
                if (n > 0.0f) {
                    if (SwipeMotionDetector.this.onSwipeListener != null) {
                        SwipeMotionDetector.this.onSwipeListener.onSwipeDown();
                    }
                }
                else if (SwipeMotionDetector.this.onSwipeListener != null) {
                    SwipeMotionDetector.this.onSwipeListener.onSwipeUp();
                }
            }
            return false;
        }
        
        public void onLongPress(final MotionEvent motionEvent) {
            SwipeMotionDetector.this.onSwipeListener.onLongPress();
        }
        
        public boolean onSingleTapConfirmed(final MotionEvent motionEvent) {
            return SwipeMotionDetector.this.onSwipeListener.onSingleTapConfirmed();
        }
    }
}
