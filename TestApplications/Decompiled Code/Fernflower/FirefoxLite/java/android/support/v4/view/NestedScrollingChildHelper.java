package android.support.v4.view;

import android.view.View;
import android.view.ViewParent;

public class NestedScrollingChildHelper {
   private boolean mIsNestedScrollingEnabled;
   private ViewParent mNestedScrollingParentNonTouch;
   private ViewParent mNestedScrollingParentTouch;
   private int[] mTempNestedScrollConsumed;
   private final View mView;

   public NestedScrollingChildHelper(View var1) {
      this.mView = var1;
   }

   private ViewParent getNestedScrollingParentForType(int var1) {
      switch(var1) {
      case 0:
         return this.mNestedScrollingParentTouch;
      case 1:
         return this.mNestedScrollingParentNonTouch;
      default:
         return null;
      }
   }

   private void setNestedScrollingParentForType(int var1, ViewParent var2) {
      switch(var1) {
      case 0:
         this.mNestedScrollingParentTouch = var2;
         break;
      case 1:
         this.mNestedScrollingParentNonTouch = var2;
      }

   }

   public boolean dispatchNestedFling(float var1, float var2, boolean var3) {
      if (this.isNestedScrollingEnabled()) {
         ViewParent var4 = this.getNestedScrollingParentForType(0);
         if (var4 != null) {
            return ViewParentCompat.onNestedFling(var4, this.mView, var1, var2, var3);
         }
      }

      return false;
   }

   public boolean dispatchNestedPreFling(float var1, float var2) {
      if (this.isNestedScrollingEnabled()) {
         ViewParent var3 = this.getNestedScrollingParentForType(0);
         if (var3 != null) {
            return ViewParentCompat.onNestedPreFling(var3, this.mView, var1, var2);
         }
      }

      return false;
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4) {
      return this.dispatchNestedPreScroll(var1, var2, var3, var4, 0);
   }

   public boolean dispatchNestedPreScroll(int var1, int var2, int[] var3, int[] var4, int var5) {
      if (this.isNestedScrollingEnabled()) {
         ViewParent var6 = this.getNestedScrollingParentForType(var5);
         if (var6 == null) {
            return false;
         }

         boolean var7 = true;
         if (var1 != 0 || var2 != 0) {
            int var8;
            int var9;
            if (var4 != null) {
               this.mView.getLocationInWindow(var4);
               var8 = var4[0];
               var9 = var4[1];
            } else {
               var8 = 0;
               var9 = 0;
            }

            int[] var10 = var3;
            if (var3 == null) {
               if (this.mTempNestedScrollConsumed == null) {
                  this.mTempNestedScrollConsumed = new int[2];
               }

               var10 = this.mTempNestedScrollConsumed;
            }

            var10[0] = 0;
            var10[1] = 0;
            ViewParentCompat.onNestedPreScroll(var6, this.mView, var1, var2, var10, var5);
            if (var4 != null) {
               this.mView.getLocationInWindow(var4);
               var4[0] -= var8;
               var4[1] -= var9;
            }

            boolean var11 = var7;
            if (var10[0] == 0) {
               if (var10[1] != 0) {
                  var11 = var7;
               } else {
                  var11 = false;
               }
            }

            return var11;
         }

         if (var4 != null) {
            var4[0] = 0;
            var4[1] = 0;
         }
      }

      return false;
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5) {
      return this.dispatchNestedScroll(var1, var2, var3, var4, var5, 0);
   }

   public boolean dispatchNestedScroll(int var1, int var2, int var3, int var4, int[] var5, int var6) {
      if (this.isNestedScrollingEnabled()) {
         ViewParent var7 = this.getNestedScrollingParentForType(var6);
         if (var7 == null) {
            return false;
         }

         if (var1 != 0 || var2 != 0 || var3 != 0 || var4 != 0) {
            int var8;
            int var9;
            if (var5 != null) {
               this.mView.getLocationInWindow(var5);
               var8 = var5[0];
               var9 = var5[1];
            } else {
               var8 = 0;
               var9 = 0;
            }

            ViewParentCompat.onNestedScroll(var7, this.mView, var1, var2, var3, var4, var6);
            if (var5 != null) {
               this.mView.getLocationInWindow(var5);
               var5[0] -= var8;
               var5[1] -= var9;
            }

            return true;
         }

         if (var5 != null) {
            var5[0] = 0;
            var5[1] = 0;
         }
      }

      return false;
   }

   public boolean hasNestedScrollingParent() {
      return this.hasNestedScrollingParent(0);
   }

   public boolean hasNestedScrollingParent(int var1) {
      boolean var2;
      if (this.getNestedScrollingParentForType(var1) != null) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   public boolean isNestedScrollingEnabled() {
      return this.mIsNestedScrollingEnabled;
   }

   public void setNestedScrollingEnabled(boolean var1) {
      if (this.mIsNestedScrollingEnabled) {
         ViewCompat.stopNestedScroll(this.mView);
      }

      this.mIsNestedScrollingEnabled = var1;
   }

   public boolean startNestedScroll(int var1) {
      return this.startNestedScroll(var1, 0);
   }

   public boolean startNestedScroll(int var1, int var2) {
      if (this.hasNestedScrollingParent(var2)) {
         return true;
      } else {
         if (this.isNestedScrollingEnabled()) {
            ViewParent var3 = this.mView.getParent();

            for(View var4 = this.mView; var3 != null; var3 = var3.getParent()) {
               if (ViewParentCompat.onStartNestedScroll(var3, var4, this.mView, var1, var2)) {
                  this.setNestedScrollingParentForType(var2, var3);
                  ViewParentCompat.onNestedScrollAccepted(var3, var4, this.mView, var1, var2);
                  return true;
               }

               if (var3 instanceof View) {
                  var4 = (View)var3;
               }
            }
         }

         return false;
      }
   }

   public void stopNestedScroll() {
      this.stopNestedScroll(0);
   }

   public void stopNestedScroll(int var1) {
      ViewParent var2 = this.getNestedScrollingParentForType(var1);
      if (var2 != null) {
         ViewParentCompat.onStopNestedScroll(var2, this.mView, var1);
         this.setNestedScrollingParentForType(var1, (ViewParent)null);
      }

   }
}
