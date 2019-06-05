package android.support.v4.widget;

import android.os.Build.VERSION;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewCompat;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
   private static Field sOverlapAnchorField;
   private static boolean sOverlapAnchorFieldAttempted;
   private static Method sSetWindowLayoutTypeMethod;
   private static boolean sSetWindowLayoutTypeMethodAttempted;

   public static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      if (VERSION.SDK_INT >= 23) {
         var0.setOverlapAnchor(var1);
      } else if (VERSION.SDK_INT >= 21) {
         if (!sOverlapAnchorFieldAttempted) {
            try {
               sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
               sOverlapAnchorField.setAccessible(true);
            } catch (NoSuchFieldException var4) {
               Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", var4);
            }

            sOverlapAnchorFieldAttempted = true;
         }

         if (sOverlapAnchorField != null) {
            try {
               sOverlapAnchorField.set(var0, var1);
            } catch (IllegalAccessException var3) {
               Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", var3);
            }
         }
      }

   }

   public static void setWindowLayoutType(PopupWindow var0, int var1) {
      if (VERSION.SDK_INT >= 23) {
         var0.setWindowLayoutType(var1);
      } else {
         if (!sSetWindowLayoutTypeMethodAttempted) {
            try {
               sSetWindowLayoutTypeMethod = PopupWindow.class.getDeclaredMethod("setWindowLayoutType", Integer.TYPE);
               sSetWindowLayoutTypeMethod.setAccessible(true);
            } catch (Exception var4) {
            }

            sSetWindowLayoutTypeMethodAttempted = true;
         }

         if (sSetWindowLayoutTypeMethod != null) {
            try {
               sSetWindowLayoutTypeMethod.invoke(var0, var1);
            } catch (Exception var3) {
            }
         }

      }
   }

   public static void showAsDropDown(PopupWindow var0, View var1, int var2, int var3, int var4) {
      if (VERSION.SDK_INT >= 19) {
         var0.showAsDropDown(var1, var2, var3, var4);
      } else {
         int var5 = var2;
         if ((GravityCompat.getAbsoluteGravity(var4, ViewCompat.getLayoutDirection(var1)) & 7) == 5) {
            var5 = var2 - (var0.getWidth() - var1.getWidth());
         }

         var0.showAsDropDown(var1, var5, var3);
      }

   }
}
