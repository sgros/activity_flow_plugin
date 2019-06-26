package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.view.View;
import android.widget.PopupWindow;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
   static final PopupWindowCompat.PopupWindowImpl IMPL;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 23) {
         IMPL = new PopupWindowCompat.Api23PopupWindowImpl();
      } else if (var0 >= 21) {
         IMPL = new PopupWindowCompat.Api21PopupWindowImpl();
      } else if (var0 >= 19) {
         IMPL = new PopupWindowCompat.KitKatPopupWindowImpl();
      } else {
         IMPL = new PopupWindowCompat.BasePopupWindowImpl();
      }

   }

   private PopupWindowCompat() {
   }

   public static boolean getOverlapAnchor(PopupWindow var0) {
      return IMPL.getOverlapAnchor(var0);
   }

   public static int getWindowLayoutType(PopupWindow var0) {
      return IMPL.getWindowLayoutType(var0);
   }

   public static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      IMPL.setOverlapAnchor(var0, var1);
   }

   public static void setWindowLayoutType(PopupWindow var0, int var1) {
      IMPL.setWindowLayoutType(var0, var1);
   }

   public static void showAsDropDown(PopupWindow var0, View var1, int var2, int var3, int var4) {
      IMPL.showAsDropDown(var0, var1, var2, var3, var4);
   }

   static class Api21PopupWindowImpl extends PopupWindowCompat.KitKatPopupWindowImpl {
      public boolean getOverlapAnchor(PopupWindow var1) {
         return PopupWindowCompatApi21.getOverlapAnchor(var1);
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
         PopupWindowCompatApi21.setOverlapAnchor(var1, var2);
      }
   }

   static class Api23PopupWindowImpl extends PopupWindowCompat.Api21PopupWindowImpl {
      public boolean getOverlapAnchor(PopupWindow var1) {
         return PopupWindowCompatApi23.getOverlapAnchor(var1);
      }

      public int getWindowLayoutType(PopupWindow var1) {
         return PopupWindowCompatApi23.getWindowLayoutType(var1);
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
         PopupWindowCompatApi23.setOverlapAnchor(var1, var2);
      }

      public void setWindowLayoutType(PopupWindow var1, int var2) {
         PopupWindowCompatApi23.setWindowLayoutType(var1, var2);
      }
   }

   static class BasePopupWindowImpl implements PopupWindowCompat.PopupWindowImpl {
      private static Method sGetWindowLayoutTypeMethod;
      private static boolean sGetWindowLayoutTypeMethodAttempted;
      private static Method sSetWindowLayoutTypeMethod;
      private static boolean sSetWindowLayoutTypeMethodAttempted;

      public boolean getOverlapAnchor(PopupWindow var1) {
         return false;
      }

      public int getWindowLayoutType(PopupWindow var1) {
         if (!sGetWindowLayoutTypeMethodAttempted) {
            try {
               sGetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("getWindowLayoutType");
               sGetWindowLayoutTypeMethod.setAccessible(true);
            } catch (Exception var4) {
            }

            sGetWindowLayoutTypeMethodAttempted = true;
         }

         int var2;
         if (sGetWindowLayoutTypeMethod != null) {
            try {
               var2 = (Integer)sGetWindowLayoutTypeMethod.invoke(var1);
               return var2;
            } catch (Exception var5) {
            }
         }

         var2 = 0;
         return var2;
      }

      public void setOverlapAnchor(PopupWindow var1, boolean var2) {
      }

      public void setWindowLayoutType(PopupWindow var1, int var2) {
         if (!sSetWindowLayoutTypeMethodAttempted) {
            try {
               sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE);
               sSetWindowLayoutTypeMethod.setAccessible(true);
            } catch (Exception var5) {
            }

            sSetWindowLayoutTypeMethodAttempted = true;
         }

         if (sSetWindowLayoutTypeMethod != null) {
            try {
               sSetWindowLayoutTypeMethod.invoke(var1, var2);
            } catch (Exception var4) {
            }
         }

      }

      public void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5) {
         int var6 = var3;
         if ((GravityCompat.getAbsoluteGravity(var5, ViewCompat.getLayoutDirection(var2)) & 7) == 5) {
            var6 = var3 - (var1.getWidth() - var2.getWidth());
         }

         var1.showAsDropDown(var2, var6, var4);
      }
   }

   static class KitKatPopupWindowImpl extends PopupWindowCompat.BasePopupWindowImpl {
      public void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5) {
         PopupWindowCompatKitKat.showAsDropDown(var1, var2, var3, var4, var5);
      }
   }

   interface PopupWindowImpl {
      boolean getOverlapAnchor(PopupWindow var1);

      int getWindowLayoutType(PopupWindow var1);

      void setOverlapAnchor(PopupWindow var1, boolean var2);

      void setWindowLayoutType(PopupWindow var1, int var2);

      void showAsDropDown(PopupWindow var1, View var2, int var3, int var4, int var5);
   }
}
