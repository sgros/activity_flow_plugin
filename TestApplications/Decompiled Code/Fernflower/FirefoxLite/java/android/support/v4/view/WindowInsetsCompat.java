package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.WindowInsets;

public class WindowInsetsCompat {
   private final Object mInsets;

   private WindowInsetsCompat(Object var1) {
      this.mInsets = var1;
   }

   static Object unwrap(WindowInsetsCompat var0) {
      Object var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = var0.mInsets;
      }

      return var1;
   }

   static WindowInsetsCompat wrap(Object var0) {
      WindowInsetsCompat var1;
      if (var0 == null) {
         var1 = null;
      } else {
         var1 = new WindowInsetsCompat(var0);
      }

      return var1;
   }

   public WindowInsetsCompat consumeSystemWindowInsets() {
      return VERSION.SDK_INT >= 20 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).consumeSystemWindowInsets()) : null;
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this == var1) {
         return true;
      } else if (var1 != null && this.getClass() == var1.getClass()) {
         WindowInsetsCompat var3 = (WindowInsetsCompat)var1;
         if (this.mInsets == null) {
            if (var3.mInsets != null) {
               var2 = false;
            }
         } else {
            var2 = this.mInsets.equals(var3.mInsets);
         }

         return var2;
      } else {
         return false;
      }
   }

   public int getSystemWindowInsetBottom() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetBottom() : 0;
   }

   public int getSystemWindowInsetLeft() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetLeft() : 0;
   }

   public int getSystemWindowInsetRight() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetRight() : 0;
   }

   public int getSystemWindowInsetTop() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).getSystemWindowInsetTop() : 0;
   }

   public boolean hasSystemWindowInsets() {
      return VERSION.SDK_INT >= 20 ? ((WindowInsets)this.mInsets).hasSystemWindowInsets() : false;
   }

   public int hashCode() {
      int var1;
      if (this.mInsets == null) {
         var1 = 0;
      } else {
         var1 = this.mInsets.hashCode();
      }

      return var1;
   }

   public boolean isConsumed() {
      return VERSION.SDK_INT >= 21 ? ((WindowInsets)this.mInsets).isConsumed() : false;
   }

   public WindowInsetsCompat replaceSystemWindowInsets(int var1, int var2, int var3, int var4) {
      return VERSION.SDK_INT >= 20 ? new WindowInsetsCompat(((WindowInsets)this.mInsets).replaceSystemWindowInsets(var1, var2, var3, var4)) : null;
   }
}
