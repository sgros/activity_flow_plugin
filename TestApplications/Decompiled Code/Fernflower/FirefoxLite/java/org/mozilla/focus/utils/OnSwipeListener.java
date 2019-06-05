package org.mozilla.focus.utils;

public interface OnSwipeListener {
   boolean onDoubleTap();

   void onLongPress();

   boolean onSingleTapConfirmed();

   void onSwipeDown();

   void onSwipeLeft();

   void onSwipeRight();

   void onSwipeUp();
}
