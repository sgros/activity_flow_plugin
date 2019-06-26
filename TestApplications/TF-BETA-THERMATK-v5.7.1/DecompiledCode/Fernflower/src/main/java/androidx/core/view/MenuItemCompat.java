package androidx.core.view;

import android.content.res.ColorStateList;
import android.graphics.PorterDuff.Mode;
import android.os.Build.VERSION;
import android.util.Log;
import android.view.MenuItem;
import androidx.core.internal.view.SupportMenuItem;

public final class MenuItemCompat {
   public static MenuItem setActionProvider(MenuItem var0, ActionProvider var1) {
      if (var0 instanceof SupportMenuItem) {
         return ((SupportMenuItem)var0).setSupportActionProvider(var1);
      } else {
         Log.w("MenuItemCompat", "setActionProvider: item does not implement SupportMenuItem; ignoring");
         return var0;
      }
   }

   public static void setAlphabeticShortcut(MenuItem var0, char var1, int var2) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setAlphabeticShortcut(var1, var2);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setAlphabeticShortcut(var1, var2);
      }

   }

   public static void setContentDescription(MenuItem var0, CharSequence var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setContentDescription(var1);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setContentDescription(var1);
      }

   }

   public static void setIconTintList(MenuItem var0, ColorStateList var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintList(var1);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setIconTintList(var1);
      }

   }

   public static void setIconTintMode(MenuItem var0, Mode var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setIconTintMode(var1);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setIconTintMode(var1);
      }

   }

   public static void setNumericShortcut(MenuItem var0, char var1, int var2) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setNumericShortcut(var1, var2);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setNumericShortcut(var1, var2);
      }

   }

   public static void setTooltipText(MenuItem var0, CharSequence var1) {
      if (var0 instanceof SupportMenuItem) {
         ((SupportMenuItem)var0).setTooltipText(var1);
      } else if (VERSION.SDK_INT >= 26) {
         var0.setTooltipText(var1);
      }

   }
}
