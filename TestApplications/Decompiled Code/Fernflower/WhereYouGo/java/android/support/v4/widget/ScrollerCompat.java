package android.support.v4.widget;

import android.content.Context;
import android.os.Build.VERSION;
import android.view.animation.Interpolator;
import android.widget.OverScroller;

public final class ScrollerCompat {
   private final boolean mIsIcsOrNewer;
   OverScroller mScroller;

   ScrollerCompat(boolean var1, Context var2, Interpolator var3) {
      this.mIsIcsOrNewer = var1;
      OverScroller var4;
      if (var3 != null) {
         var4 = new OverScroller(var2, var3);
      } else {
         var4 = new OverScroller(var2);
      }

      this.mScroller = var4;
   }

   public static ScrollerCompat create(Context var0) {
      return create(var0, (Interpolator)null);
   }

   public static ScrollerCompat create(Context var0, Interpolator var1) {
      boolean var2;
      if (VERSION.SDK_INT >= 14) {
         var2 = true;
      } else {
         var2 = false;
      }

      return new ScrollerCompat(var2, var0, var1);
   }

   public void abortAnimation() {
      this.mScroller.abortAnimation();
   }

   public boolean computeScrollOffset() {
      return this.mScroller.computeScrollOffset();
   }

   public void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8) {
      this.mScroller.fling(var1, var2, var3, var4, var5, var6, var7, var8);
   }

   public void fling(int var1, int var2, int var3, int var4, int var5, int var6, int var7, int var8, int var9, int var10) {
      this.mScroller.fling(var1, var2, var3, var4, var5, var6, var7, var8, var9, var10);
   }

   public float getCurrVelocity() {
      float var1;
      if (this.mIsIcsOrNewer) {
         var1 = ScrollerCompatIcs.getCurrVelocity(this.mScroller);
      } else {
         var1 = 0.0F;
      }

      return var1;
   }

   public int getCurrX() {
      return this.mScroller.getCurrX();
   }

   public int getCurrY() {
      return this.mScroller.getCurrY();
   }

   public int getFinalX() {
      return this.mScroller.getFinalX();
   }

   public int getFinalY() {
      return this.mScroller.getFinalY();
   }

   public boolean isFinished() {
      return this.mScroller.isFinished();
   }

   public boolean isOverScrolled() {
      return this.mScroller.isOverScrolled();
   }

   public void notifyHorizontalEdgeReached(int var1, int var2, int var3) {
      this.mScroller.notifyHorizontalEdgeReached(var1, var2, var3);
   }

   public void notifyVerticalEdgeReached(int var1, int var2, int var3) {
      this.mScroller.notifyVerticalEdgeReached(var1, var2, var3);
   }

   public boolean springBack(int var1, int var2, int var3, int var4, int var5, int var6) {
      return this.mScroller.springBack(var1, var2, var3, var4, var5, var6);
   }

   public void startScroll(int var1, int var2, int var3, int var4) {
      this.mScroller.startScroll(var1, var2, var3, var4);
   }

   public void startScroll(int var1, int var2, int var3, int var4, int var5) {
      this.mScroller.startScroll(var1, var2, var3, var4, var5);
   }
}
