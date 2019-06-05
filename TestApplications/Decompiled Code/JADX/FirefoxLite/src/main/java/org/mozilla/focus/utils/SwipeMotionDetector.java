package org.mozilla.focus.utils;

import android.content.Context;
import android.view.GestureDetector;
import android.view.GestureDetector.SimpleOnGestureListener;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;

public class SwipeMotionDetector implements OnTouchListener {
    private GestureDetector gestureDetector;
    private OnSwipeListener onSwipeListener;

    private final class GestureListener extends SimpleOnGestureListener {
        public boolean onDown(MotionEvent motionEvent) {
            return true;
        }

        private GestureListener() {
        }

        public void onLongPress(MotionEvent motionEvent) {
            SwipeMotionDetector.this.onSwipeListener.onLongPress();
        }

        public boolean onDoubleTap(MotionEvent motionEvent) {
            return SwipeMotionDetector.this.onSwipeListener.onDoubleTap();
        }

        public boolean onSingleTapConfirmed(MotionEvent motionEvent) {
            return SwipeMotionDetector.this.onSwipeListener.onSingleTapConfirmed();
        }

        public boolean onFling(MotionEvent motionEvent, MotionEvent motionEvent2, float f, float f2) {
            float y = motionEvent2.getY() - motionEvent.getY();
            float x = motionEvent2.getX() - motionEvent.getX();
            if (Math.abs(x) > Math.abs(y)) {
                if (Math.abs(x) > 100.0f && Math.abs(f) > 100.0f) {
                    if (x > 0.0f) {
                        if (SwipeMotionDetector.this.onSwipeListener != null) {
                            SwipeMotionDetector.this.onSwipeListener.onSwipeRight();
                        }
                    } else if (SwipeMotionDetector.this.onSwipeListener != null) {
                        SwipeMotionDetector.this.onSwipeListener.onSwipeLeft();
                    }
                }
            } else if (Math.abs(y) > 100.0f && Math.abs(f2) > 100.0f) {
                if (y > 0.0f) {
                    if (SwipeMotionDetector.this.onSwipeListener != null) {
                        SwipeMotionDetector.this.onSwipeListener.onSwipeDown();
                    }
                } else if (SwipeMotionDetector.this.onSwipeListener != null) {
                    SwipeMotionDetector.this.onSwipeListener.onSwipeUp();
                }
            }
            return false;
        }
    }

    public SwipeMotionDetector(Context context, OnSwipeListener onSwipeListener) {
        this.gestureDetector = new GestureDetector(context, new GestureListener());
        this.onSwipeListener = onSwipeListener;
    }

    public boolean onTouch(View view, MotionEvent motionEvent) {
        return this.gestureDetector.onTouchEvent(motionEvent);
    }
}
