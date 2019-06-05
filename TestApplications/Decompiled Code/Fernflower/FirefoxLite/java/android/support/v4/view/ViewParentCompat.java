package android.support.v4.view;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.view.ViewParent;

public final class ViewParentCompat {
   public static boolean onNestedFling(ViewParent var0, View var1, float var2, float var3, boolean var4) {
      if (VERSION.SDK_INT >= 21) {
         try {
            var4 = var0.onNestedFling(var1, var2, var3, var4);
            return var4;
         } catch (AbstractMethodError var6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("ViewParent ");
            var5.append(var0);
            var5.append(" does not implement interface ");
            var5.append("method onNestedFling");
            Log.e("ViewParentCompat", var5.toString(), var6);
         }
      } else if (var0 instanceof NestedScrollingParent) {
         return ((NestedScrollingParent)var0).onNestedFling(var1, var2, var3, var4);
      }

      return false;
   }

   public static boolean onNestedPreFling(ViewParent var0, View var1, float var2, float var3) {
      if (VERSION.SDK_INT >= 21) {
         try {
            boolean var4 = var0.onNestedPreFling(var1, var2, var3);
            return var4;
         } catch (AbstractMethodError var6) {
            StringBuilder var5 = new StringBuilder();
            var5.append("ViewParent ");
            var5.append(var0);
            var5.append(" does not implement interface ");
            var5.append("method onNestedPreFling");
            Log.e("ViewParentCompat", var5.toString(), var6);
         }
      } else if (var0 instanceof NestedScrollingParent) {
         return ((NestedScrollingParent)var0).onNestedPreFling(var1, var2, var3);
      }

      return false;
   }

   public static void onNestedPreScroll(ViewParent var0, View var1, int var2, int var3, int[] var4, int var5) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedPreScroll(var1, var2, var3, var4, var5);
      } else if (var5 == 0) {
         if (VERSION.SDK_INT >= 21) {
            try {
               var0.onNestedPreScroll(var1, var2, var3, var4);
            } catch (AbstractMethodError var6) {
               StringBuilder var7 = new StringBuilder();
               var7.append("ViewParent ");
               var7.append(var0);
               var7.append(" does not implement interface ");
               var7.append("method onNestedPreScroll");
               Log.e("ViewParentCompat", var7.toString(), var6);
            }
         } else if (var0 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var0).onNestedPreScroll(var1, var2, var3, var4);
         }
      }

   }

   public static void onNestedScroll(ViewParent var0, View var1, int var2, int var3, int var4, int var5, int var6) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedScroll(var1, var2, var3, var4, var5, var6);
      } else if (var6 == 0) {
         if (VERSION.SDK_INT >= 21) {
            try {
               var0.onNestedScroll(var1, var2, var3, var4, var5);
            } catch (AbstractMethodError var8) {
               StringBuilder var9 = new StringBuilder();
               var9.append("ViewParent ");
               var9.append(var0);
               var9.append(" does not implement interface ");
               var9.append("method onNestedScroll");
               Log.e("ViewParentCompat", var9.toString(), var8);
            }
         } else if (var0 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var0).onNestedScroll(var1, var2, var3, var4, var5);
         }
      }

   }

   public static void onNestedScrollAccepted(ViewParent var0, View var1, View var2, int var3, int var4) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onNestedScrollAccepted(var1, var2, var3, var4);
      } else if (var4 == 0) {
         if (VERSION.SDK_INT >= 21) {
            try {
               var0.onNestedScrollAccepted(var1, var2, var3);
            } catch (AbstractMethodError var5) {
               StringBuilder var6 = new StringBuilder();
               var6.append("ViewParent ");
               var6.append(var0);
               var6.append(" does not implement interface ");
               var6.append("method onNestedScrollAccepted");
               Log.e("ViewParentCompat", var6.toString(), var5);
            }
         } else if (var0 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var0).onNestedScrollAccepted(var1, var2, var3);
         }
      }

   }

   public static boolean onStartNestedScroll(ViewParent var0, View var1, View var2, int var3, int var4) {
      if (var0 instanceof NestedScrollingParent2) {
         return ((NestedScrollingParent2)var0).onStartNestedScroll(var1, var2, var3, var4);
      } else {
         if (var4 == 0) {
            if (VERSION.SDK_INT >= 21) {
               try {
                  boolean var5 = var0.onStartNestedScroll(var1, var2, var3);
                  return var5;
               } catch (AbstractMethodError var6) {
                  StringBuilder var7 = new StringBuilder();
                  var7.append("ViewParent ");
                  var7.append(var0);
                  var7.append(" does not implement interface ");
                  var7.append("method onStartNestedScroll");
                  Log.e("ViewParentCompat", var7.toString(), var6);
               }
            } else if (var0 instanceof NestedScrollingParent) {
               return ((NestedScrollingParent)var0).onStartNestedScroll(var1, var2, var3);
            }
         }

         return false;
      }
   }

   public static void onStopNestedScroll(ViewParent var0, View var1, int var2) {
      if (var0 instanceof NestedScrollingParent2) {
         ((NestedScrollingParent2)var0).onStopNestedScroll(var1, var2);
      } else if (var2 == 0) {
         if (VERSION.SDK_INT >= 21) {
            try {
               var0.onStopNestedScroll(var1);
            } catch (AbstractMethodError var4) {
               StringBuilder var5 = new StringBuilder();
               var5.append("ViewParent ");
               var5.append(var0);
               var5.append(" does not implement interface ");
               var5.append("method onStopNestedScroll");
               Log.e("ViewParentCompat", var5.toString(), var4);
            }
         } else if (var0 instanceof NestedScrollingParent) {
            ((NestedScrollingParent)var0).onStopNestedScroll(var1);
         }
      }

   }
}
