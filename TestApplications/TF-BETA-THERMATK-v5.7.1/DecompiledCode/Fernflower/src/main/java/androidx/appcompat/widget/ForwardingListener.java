package androidx.appcompat.widget;

import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewParent;
import android.view.View.OnAttachStateChangeListener;
import android.view.View.OnTouchListener;
import androidx.appcompat.view.menu.ShowableListMenu;

public abstract class ForwardingListener implements OnTouchListener, OnAttachStateChangeListener {
   private int mActivePointerId;
   private Runnable mDisallowIntercept;
   private boolean mForwarding;
   private final int mLongPressTimeout;
   private final float mScaledTouchSlop;
   final View mSrc;
   private final int mTapTimeout;
   private final int[] mTmpLocation = new int[2];
   private Runnable mTriggerLongPress;

   public ForwardingListener(View var1) {
      this.mSrc = var1;
      var1.setLongClickable(true);
      var1.addOnAttachStateChangeListener(this);
      this.mScaledTouchSlop = (float)ViewConfiguration.get(var1.getContext()).getScaledTouchSlop();
      this.mTapTimeout = ViewConfiguration.getTapTimeout();
      this.mLongPressTimeout = (this.mTapTimeout + ViewConfiguration.getLongPressTimeout()) / 2;
   }

   private void clearCallbacks() {
      Runnable var1 = this.mTriggerLongPress;
      if (var1 != null) {
         this.mSrc.removeCallbacks(var1);
      }

      var1 = this.mDisallowIntercept;
      if (var1 != null) {
         this.mSrc.removeCallbacks(var1);
      }

   }

   private boolean onTouchForwarded(MotionEvent var1) {
      View var2 = this.mSrc;
      ShowableListMenu var3 = this.getPopup();
      if (var3 != null && var3.isShowing()) {
         DropDownListView var4 = (DropDownListView)var3.getListView();
         if (var4 != null && var4.isShown()) {
            MotionEvent var8 = MotionEvent.obtainNoHistory(var1);
            this.toGlobalMotionEvent(var2, var8);
            this.toLocalMotionEvent(var4, var8);
            boolean var5 = var4.onForwardedEvent(var8, this.mActivePointerId);
            var8.recycle();
            int var6 = var1.getActionMasked();
            boolean var7 = true;
            boolean var9;
            if (var6 != 1 && var6 != 3) {
               var9 = true;
            } else {
               var9 = false;
            }

            if (!var5 || !var9) {
               var7 = false;
            }

            return var7;
         }
      }

      return false;
   }

   private boolean onTouchObserved(MotionEvent var1) {
      View var2 = this.mSrc;
      if (!var2.isEnabled()) {
         return false;
      } else {
         int var3 = var1.getActionMasked();
         if (var3 != 0) {
            if (var3 != 1) {
               if (var3 == 2) {
                  var3 = var1.findPointerIndex(this.mActivePointerId);
                  if (var3 >= 0 && !pointInView(var2, var1.getX(var3), var1.getY(var3), this.mScaledTouchSlop)) {
                     this.clearCallbacks();
                     var2.getParent().requestDisallowInterceptTouchEvent(true);
                     return true;
                  }

                  return false;
               }

               if (var3 != 3) {
                  return false;
               }
            }

            this.clearCallbacks();
         } else {
            this.mActivePointerId = var1.getPointerId(0);
            if (this.mDisallowIntercept == null) {
               this.mDisallowIntercept = new ForwardingListener.DisallowIntercept();
            }

            var2.postDelayed(this.mDisallowIntercept, (long)this.mTapTimeout);
            if (this.mTriggerLongPress == null) {
               this.mTriggerLongPress = new ForwardingListener.TriggerLongPress();
            }

            var2.postDelayed(this.mTriggerLongPress, (long)this.mLongPressTimeout);
         }

         return false;
      }
   }

   private static boolean pointInView(View var0, float var1, float var2, float var3) {
      float var4 = -var3;
      boolean var5;
      if (var1 >= var4 && var2 >= var4 && var1 < (float)(var0.getRight() - var0.getLeft()) + var3 && var2 < (float)(var0.getBottom() - var0.getTop()) + var3) {
         var5 = true;
      } else {
         var5 = false;
      }

      return var5;
   }

   private boolean toGlobalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)var3[0], (float)var3[1]);
      return true;
   }

   private boolean toLocalMotionEvent(View var1, MotionEvent var2) {
      int[] var3 = this.mTmpLocation;
      var1.getLocationOnScreen(var3);
      var2.offsetLocation((float)(-var3[0]), (float)(-var3[1]));
      return true;
   }

   public abstract ShowableListMenu getPopup();

   protected abstract boolean onForwardingStarted();

   protected boolean onForwardingStopped() {
      ShowableListMenu var1 = this.getPopup();
      if (var1 != null && var1.isShowing()) {
         var1.dismiss();
      }

      return true;
   }

   void onLongPress() {
      this.clearCallbacks();
      View var1 = this.mSrc;
      if (var1.isEnabled() && !var1.isLongClickable()) {
         if (!this.onForwardingStarted()) {
            return;
         }

         var1.getParent().requestDisallowInterceptTouchEvent(true);
         long var2 = SystemClock.uptimeMillis();
         MotionEvent var4 = MotionEvent.obtain(var2, var2, 3, 0.0F, 0.0F, 0);
         var1.onTouchEvent(var4);
         var4.recycle();
         this.mForwarding = true;
      }

   }

   public boolean onTouch(View var1, MotionEvent var2) {
      boolean var3 = this.mForwarding;
      boolean var4 = true;
      boolean var5;
      boolean var6;
      if (var3) {
         if (!this.onTouchForwarded(var2) && this.onForwardingStopped()) {
            var5 = false;
         } else {
            var5 = true;
         }
      } else {
         if (this.onTouchObserved(var2) && this.onForwardingStarted()) {
            var6 = true;
         } else {
            var6 = false;
         }

         var5 = var6;
         if (var6) {
            long var7 = SystemClock.uptimeMillis();
            MotionEvent var9 = MotionEvent.obtain(var7, var7, 3, 0.0F, 0.0F, 0);
            this.mSrc.onTouchEvent(var9);
            var9.recycle();
            var5 = var6;
         }
      }

      this.mForwarding = var5;
      var6 = var4;
      if (!var5) {
         if (var3) {
            var6 = var4;
         } else {
            var6 = false;
         }
      }

      return var6;
   }

   public void onViewAttachedToWindow(View var1) {
   }

   public void onViewDetachedFromWindow(View var1) {
      this.mForwarding = false;
      this.mActivePointerId = -1;
      Runnable var2 = this.mDisallowIntercept;
      if (var2 != null) {
         this.mSrc.removeCallbacks(var2);
      }

   }

   private class DisallowIntercept implements Runnable {
      DisallowIntercept() {
      }

      public void run() {
         ViewParent var1 = ForwardingListener.this.mSrc.getParent();
         if (var1 != null) {
            var1.requestDisallowInterceptTouchEvent(true);
         }

      }
   }

   private class TriggerLongPress implements Runnable {
      TriggerLongPress() {
      }

      public void run() {
         ForwardingListener.this.onLongPress();
      }
   }
}
