package org.mozilla.focus.utils;

import android.app.Activity;
import android.graphics.Rect;
import android.os.Build.VERSION;
import android.view.View;
import android.view.Window;
import android.view.inputmethod.InputMethodManager;

public class ViewUtils {
   public static void exitImmersiveMode(int var0, Activity var1) {
      if (var1 != null) {
         Window var2 = var1.getWindow();
         var2.clearFlags(128);
         var2.getDecorView().setSystemUiVisibility(var0);
         var1.setRequestedOrientation(1);
      }
   }

   public static int getStatusBarHeight(Activity var0) {
      Rect var1 = new Rect();
      var0.getWindow().getDecorView().getWindowVisibleDisplayFrame(var1);
      return var1.top;
   }

   public static void hideKeyboard(View var0) {
      ((InputMethodManager)var0.getContext().getSystemService("input_method")).hideSoftInputFromWindow(var0.getWindowToken(), 1);
   }

   public static void showKeyboard(View var0) {
      ((InputMethodManager)var0.getContext().getSystemService("input_method")).showSoftInput(var0, 1);
   }

   public static int switchToImmersiveMode(Activity var0) {
      if (var0 == null) {
         return -1;
      } else {
         Window var1 = var0.getWindow();
         int var2 = var1.getDecorView().getSystemUiVisibility();
         var1.addFlags(128);
         var1.getDecorView().setSystemUiVisibility(5894);
         var0.setRequestedOrientation(4);
         return var2;
      }
   }

   public static void updateStatusBarStyle(boolean var0, Window var1) {
      if (VERSION.SDK_INT >= 23) {
         int var2 = var1.getDecorView().getSystemUiVisibility();
         if (!var0) {
            var2 &= -8193;
         } else {
            var2 |= 8192;
         }

         var1.getDecorView().setSystemUiVisibility(var2);
      }
   }
}
