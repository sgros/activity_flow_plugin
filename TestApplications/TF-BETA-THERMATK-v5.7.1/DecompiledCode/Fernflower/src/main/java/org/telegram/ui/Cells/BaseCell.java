package org.telegram.ui.Cells;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.MotionEvent;
import android.view.ViewConfiguration;
import android.view.ViewGroup;

public abstract class BaseCell extends ViewGroup {
   private boolean checkingForLongPress = false;
   private BaseCell.CheckForLongPress pendingCheckForLongPress = null;
   private BaseCell.CheckForTap pendingCheckForTap = null;
   private int pressCount = 0;

   public BaseCell(Context var1) {
      super(var1);
      this.setWillNotDraw(false);
      this.setFocusable(true);
   }

   // $FF: synthetic method
   static int access$104(BaseCell var0) {
      int var1 = var0.pressCount + 1;
      var0.pressCount = var1;
      return var1;
   }

   public static void setDrawableBounds(Drawable var0, float var1, float var2) {
      setDrawableBounds(var0, (int)var1, (int)var2, var0.getIntrinsicWidth(), var0.getIntrinsicHeight());
   }

   public static void setDrawableBounds(Drawable var0, int var1, int var2) {
      setDrawableBounds(var0, var1, var2, var0.getIntrinsicWidth(), var0.getIntrinsicHeight());
   }

   public static void setDrawableBounds(Drawable var0, int var1, int var2, int var3, int var4) {
      if (var0 != null) {
         var0.setBounds(var1, var2, var3 + var1, var4 + var2);
      }

   }

   protected void cancelCheckLongPress() {
      this.checkingForLongPress = false;
      BaseCell.CheckForLongPress var1 = this.pendingCheckForLongPress;
      if (var1 != null) {
         this.removeCallbacks(var1);
      }

      BaseCell.CheckForTap var2 = this.pendingCheckForTap;
      if (var2 != null) {
         this.removeCallbacks(var2);
      }

   }

   public boolean hasOverlappingRendering() {
      return false;
   }

   protected void onLongPress() {
   }

   protected void startCheckLongPress() {
      if (!this.checkingForLongPress) {
         this.checkingForLongPress = true;
         if (this.pendingCheckForTap == null) {
            this.pendingCheckForTap = new BaseCell.CheckForTap();
         }

         this.postDelayed(this.pendingCheckForTap, (long)ViewConfiguration.getTapTimeout());
      }
   }

   class CheckForLongPress implements Runnable {
      public int currentPressCount;

      public void run() {
         if (BaseCell.this.checkingForLongPress && BaseCell.this.getParent() != null && this.currentPressCount == BaseCell.this.pressCount) {
            BaseCell.this.checkingForLongPress = false;
            BaseCell.this.performHapticFeedback(0);
            BaseCell.this.onLongPress();
            MotionEvent var1 = MotionEvent.obtain(0L, 0L, 3, 0.0F, 0.0F, 0);
            BaseCell.this.onTouchEvent(var1);
            var1.recycle();
         }

      }
   }

   private final class CheckForTap implements Runnable {
      private CheckForTap() {
      }

      // $FF: synthetic method
      CheckForTap(Object var2) {
         this();
      }

      public void run() {
         BaseCell var1;
         if (BaseCell.this.pendingCheckForLongPress == null) {
            var1 = BaseCell.this;
            var1.pendingCheckForLongPress = var1.new CheckForLongPress();
         }

         BaseCell.this.pendingCheckForLongPress.currentPressCount = BaseCell.access$104(BaseCell.this);
         var1 = BaseCell.this;
         var1.postDelayed(var1.pendingCheckForLongPress, (long)(ViewConfiguration.getLongPressTimeout() - ViewConfiguration.getTapTimeout()));
      }
   }
}
