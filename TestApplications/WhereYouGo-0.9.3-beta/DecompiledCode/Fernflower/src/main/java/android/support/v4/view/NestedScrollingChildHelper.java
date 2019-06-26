package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
   private boolean mIsNestedScrollingEnabled;
   private ViewParent mNestedScrollingParent;
   private int[] mTempNestedScrollConsumed;
   private final View mView;

   public NestedScrollingChildHelper(View var1) {
      this.mView = var1;
   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
         var3 = ViewParentCompat.onNestedFling(this.mNestedScrollingParent, this.mView, var1, var2, var3);
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      boolean var3;
      if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
         var3 = ViewParentCompat.onNestedPreFling(this.mNestedScrollingParent, this.mView, var1, var2);
      } else {
         var3 = false;
      }

      return var3;
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      boolean var5 = false;
      boolean var6 = var5;
      if (this.isNestedScrollingEnabled()) {
         var6 = var5;
         if (this.mNestedScrollingParent != null) {
            if (var1 == 0 && var2 == 0) {
               var6 = var5;
               if (var4 != null) {
                  var4[0] = 0;
                  var4[1] = 0;
                  var6 = var5;
               }
            } else {
               int var7 = 0;
               int var8 = 0;
               if (var4 != null) {
                  this.mView.getLocationInWindow(var4);
                  var7 = var4[0];
                  var8 = var4[1];
               }

               int[] var9 = var3;
               if (var3 == null) {
                  if (this.mTempNestedScrollConsumed == null) {
                     this.mTempNestedScrollConsumed = new int[2];
                  }

                  var9 = this.mTempNestedScrollConsumed;
               }

               var9[0] = 0;
               var9[1] = 0;
               ViewParentCompat.onNestedPreScroll(this.mNestedScrollingParent, this.mView, var1, var2, var9);
               if (var4 != null) {
                  this.mView.getLocationInWindow(var4);
                  var4[0] -= var7;
                  var4[1] -= var8;
               }

               if (var9[0] == 0) {
                  var6 = var5;
                  if (var9[1] == 0) {
                     return var6;
                  }
               }

               var6 = true;
            }
         }
      }

      return var6;
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      boolean var8;
      if (this.isNestedScrollingEnabled() && this.mNestedScrollingParent != null) {
         if (var1 != 0 || var2 != 0 || var3 != 0 || var4 != 0) {
            int var6 = 0;
            int var7 = 0;
            if (var5 != null) {
               this.mView.getLocationInWindow(var5);
               var6 = var5[0];
               var7 = var5[1];
            }

            ViewParentCompat.onNestedScroll(this.mNestedScrollingParent, this.mView, var1, var2, var3, var4);
            if (var5 != null) {
               this.mView.getLocationInWindow(var5);
               var5[0] -= var6;
               var5[1] -= var7;
            }

            var8 = true;
            return var8;
         }

         if (var5 != null) {
            var5[0] = 0;
            var5[1] = 0;
         }
      }

      var8 = false;
      return var8;
   }

   public boolean hasNestedScrollingParent() {
      boolean var1;
      if (this.mNestedScrollingParent != null) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public boolean isNestedScrollingEnabled() {
      return this.mIsNestedScrollingEnabled;
   }

   public void onDetachedFromWindow() {
      ViewCompat.stopNestedScroll(this.mView);
   }

   public void onStopNestedScroll(View var1) {
      ViewCompat.stopNestedScroll(this.mView);
   }

   public void setNestedScrollingEnabled(boolean var1) {
      if (this.mIsNestedScrollingEnabled) {
         ViewCompat.stopNestedScroll(this.mView);
      }

      this.mIsNestedScrollingEnabled = var1;
   }

   public boolean startNestedScroll(int var1) {
      boolean var2 = true;
      if (!this.hasNestedScrollingParent()) {
         if (this.isNestedScrollingEnabled()) {
            ViewParent var3 = this.mView.getParent();

            for(View var4 = this.mView; var3 != null; var3 = var3.getParent()) {
               if (ViewParentCompat.onStartNestedScroll(var3, var4, this.mView, var1)) {
                  this.mNestedScrollingParent = var3;
                  ViewParentCompat.onNestedScrollAccepted(var3, var4, this.mView, var1);
                  return var2;
               }

               if (var3 instanceof View) {
                  var4 = (View)var3;
               }
            }
         }

         var2 = false;
      }

      return var2;
   }

   public void stopNestedScroll() {
      if (this.mNestedScrollingParent != null) {
         ViewParentCompat.onStopNestedScroll(this.mNestedScrollingParent, this.mView);
         this.mNestedScrollingParent = null;
      }

   }
}
