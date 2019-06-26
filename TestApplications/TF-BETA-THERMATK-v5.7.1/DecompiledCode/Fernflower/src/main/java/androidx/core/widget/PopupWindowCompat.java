package androidx.core.widget;

import android.os.Build.VERSION;
import android.util.Log;
import android.view.View;
import android.widget.PopupWindow;
import androidx.core.view.GravityCompat;
import androidx.core.view.ViewCompat;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public final class PopupWindowCompat {
   private static Field sOverlapAnchorField;
   private static boolean sOverlapAnchorFieldAttempted;
   private static Method sSetWindowLayoutTypeMethod;
   private static boolean sSetWindowLayoutTypeMethodAttempted;

   public static void setOverlapAnchor(PopupWindow var0, boolean var1) {
      int var2 = VERSION.SDK_INT;
      if (var2 >= 23) {
         var0.setOverlapAnchor(var1);
      } else if (var2 >= 21) {
         if (!sOverlapAnchorFieldAttempted) {
            try {
               sOverlapAnchorField = PopupWindow.class.getDeclaredField("mOverlapAnchor");
               sOverlapAnchorField.setAccessible(true);
            } catch (NoSuchFieldException var5) {
               Log.i("PopupWindowCompatApi21", "Could not fetch mOverlapAnchor field from PopupWindow", var5);
            }

            sOverlapAnchorFieldAttempted = true;
         }

         Field var3 = sOverlapAnchorField;
         if (var3 != null) {
            try {
               var3.set(var0, var1);
            } catch (IllegalAccessException var4) {
               Log.i("PopupWindowCompatApi21", "Could not set overlap anchor field in PopupWindow", var4);
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

         Method var2 = sSetWindowLayoutTypeMethod;
         if (var2 != null) {
            try {
               var2.invoke(var0, var1);
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
