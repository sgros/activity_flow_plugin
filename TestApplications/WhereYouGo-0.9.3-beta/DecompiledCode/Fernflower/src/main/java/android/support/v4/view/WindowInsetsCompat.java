package android.support.v4.view;

import android.graphics.Rect;
import android.os.Build.VERSION;

public class WindowInsetsCompat {
   private static final WindowInsetsCompat.WindowInsetsCompatImpl IMPL;
   private final Object mInsets;

   static {
      int var0 = VERSION.SDK_INT;
      if (var0 >= 21) {
         IMPL = new WindowInsetsCompat.WindowInsetsCompatApi21Impl();
      } else if (var0 >= 20) {
         IMPL = new WindowInsetsCompat.WindowInsetsCompatApi20Impl();
      } else {
         IMPL = new WindowInsetsCompat.WindowInsetsCompatBaseImpl();
      }

   }

   public WindowInsetsCompat(WindowInsetsCompat var1) {
      Object var2;
      if (var1 == null) {
         var2 = null;
      } else {
         var2 = IMPL.getSourceWindowInsets(var1.mInsets);
      }

      this.mInsets = var2;
   }

   WindowInsetsCompat(Object var1) {
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

   public WindowInsetsCompat consumeStableInsets() {
      return IMPL.consumeStableInsets(this.mInsets);
   }

   public WindowInsetsCompat consumeSystemWindowInsets() {
      return IMPL.consumeSystemWindowInsets(this.mInsets);
   }

   public boolean equals(Object var1) {
      boolean var2 = true;
      if (this != var1) {
         if (var1 != null && this.getClass() == var1.getClass()) {
            WindowInsetsCompat var3 = (WindowInsetsCompat)var1;
            if (this.mInsets == null) {
               if (var3.mInsets != null) {
                  var2 = false;
               }
            } else {
               var2 = this.mInsets.equals(var3.mInsets);
            }
         } else {
            var2 = false;
         }
      }

      return var2;
   }

   public int getStableInsetBottom() {
      return IMPL.getStableInsetBottom(this.mInsets);
   }

   public int getStableInsetLeft() {
      return IMPL.getStableInsetLeft(this.mInsets);
   }

   public int getStableInsetRight() {
      return IMPL.getStableInsetRight(this.mInsets);
   }

   public int getStableInsetTop() {
      return IMPL.getStableInsetTop(this.mInsets);
   }

   public int getSystemWindowInsetBottom() {
      return IMPL.getSystemWindowInsetBottom(this.mInsets);
   }

   public int getSystemWindowInsetLeft() {
      return IMPL.getSystemWindowInsetLeft(this.mInsets);
   }

   public int getSystemWindowInsetRight() {
      return IMPL.getSystemWindowInsetRight(this.mInsets);
   }

   public int getSystemWindowInsetTop() {
      return IMPL.getSystemWindowInsetTop(this.mInsets);
   }

   public boolean hasInsets() {
      return IMPL.hasInsets(this.mInsets);
   }

   public boolean hasStableInsets() {
      return IMPL.hasStableInsets(this.mInsets);
   }

   public boolean hasSystemWindowInsets() {
      return IMPL.hasSystemWindowInsets(this.mInsets);
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
      return IMPL.isConsumed(this.mInsets);
   }

   public boolean isRound() {
      return IMPL.isRound(this.mInsets);
   }

   public WindowInsetsCompat replaceSystemWindowInsets(int var1, int var2, int var3, int var4) {
      return IMPL.replaceSystemWindowInsets(this.mInsets, var1, var2, var3, var4);
   }

   public WindowInsetsCompat replaceSystemWindowInsets(Rect var1) {
      return IMPL.replaceSystemWindowInsets(this.mInsets, var1);
   }

   private static class WindowInsetsCompatApi20Impl extends WindowInsetsCompat.WindowInsetsCompatBaseImpl {
      WindowInsetsCompatApi20Impl() {
      }

      public WindowInsetsCompat consumeSystemWindowInsets(Object var1) {
         return new WindowInsetsCompat(WindowInsetsCompatApi20.consumeSystemWindowInsets(var1));
      }

      public Object getSourceWindowInsets(Object var1) {
         return WindowInsetsCompatApi20.getSourceWindowInsets(var1);
      }

      public int getSystemWindowInsetBottom(Object var1) {
         return WindowInsetsCompatApi20.getSystemWindowInsetBottom(var1);
      }

      public int getSystemWindowInsetLeft(Object var1) {
         return WindowInsetsCompatApi20.getSystemWindowInsetLeft(var1);
      }

      public int getSystemWindowInsetRight(Object var1) {
         return WindowInsetsCompatApi20.getSystemWindowInsetRight(var1);
      }

      public int getSystemWindowInsetTop(Object var1) {
         return WindowInsetsCompatApi20.getSystemWindowInsetTop(var1);
      }

      public boolean hasInsets(Object var1) {
         return WindowInsetsCompatApi20.hasInsets(var1);
      }

      public boolean hasSystemWindowInsets(Object var1) {
         return WindowInsetsCompatApi20.hasSystemWindowInsets(var1);
      }

      public boolean isRound(Object var1) {
         return WindowInsetsCompatApi20.isRound(var1);
      }

      public WindowInsetsCompat replaceSystemWindowInsets(Object var1, int var2, int var3, int var4, int var5) {
         return new WindowInsetsCompat(WindowInsetsCompatApi20.replaceSystemWindowInsets(var1, var2, var3, var4, var5));
      }
   }

   private static class WindowInsetsCompatApi21Impl extends WindowInsetsCompat.WindowInsetsCompatApi20Impl {
      WindowInsetsCompatApi21Impl() {
      }

      public WindowInsetsCompat consumeStableInsets(Object var1) {
         return new WindowInsetsCompat(WindowInsetsCompatApi21.consumeStableInsets(var1));
      }

      public int getStableInsetBottom(Object var1) {
         return WindowInsetsCompatApi21.getStableInsetBottom(var1);
      }

      public int getStableInsetLeft(Object var1) {
         return WindowInsetsCompatApi21.getStableInsetLeft(var1);
      }

      public int getStableInsetRight(Object var1) {
         return WindowInsetsCompatApi21.getStableInsetRight(var1);
      }

      public int getStableInsetTop(Object var1) {
         return WindowInsetsCompatApi21.getStableInsetTop(var1);
      }

      public boolean hasStableInsets(Object var1) {
         return WindowInsetsCompatApi21.hasStableInsets(var1);
      }

      public boolean isConsumed(Object var1) {
         return WindowInsetsCompatApi21.isConsumed(var1);
      }

      public WindowInsetsCompat replaceSystemWindowInsets(Object var1, Rect var2) {
         return new WindowInsetsCompat(WindowInsetsCompatApi21.replaceSystemWindowInsets(var1, var2));
      }
   }

   private static class WindowInsetsCompatBaseImpl implements WindowInsetsCompat.WindowInsetsCompatImpl {
      WindowInsetsCompatBaseImpl() {
      }

      public WindowInsetsCompat consumeStableInsets(Object var1) {
         return null;
      }

      public WindowInsetsCompat consumeSystemWindowInsets(Object var1) {
         return null;
      }

      public Object getSourceWindowInsets(Object var1) {
         return null;
      }

      public int getStableInsetBottom(Object var1) {
         return 0;
      }

      public int getStableInsetLeft(Object var1) {
         return 0;
      }

      public int getStableInsetRight(Object var1) {
         return 0;
      }

      public int getStableInsetTop(Object var1) {
         return 0;
      }

      public int getSystemWindowInsetBottom(Object var1) {
         return 0;
      }

      public int getSystemWindowInsetLeft(Object var1) {
         return 0;
      }

      public int getSystemWindowInsetRight(Object var1) {
         return 0;
      }

      public int getSystemWindowInsetTop(Object var1) {
         return 0;
      }

      public boolean hasInsets(Object var1) {
         return false;
      }

      public boolean hasStableInsets(Object var1) {
         return false;
      }

      public boolean hasSystemWindowInsets(Object var1) {
         return false;
      }

      public boolean isConsumed(Object var1) {
         return false;
      }

      public boolean isRound(Object var1) {
         return false;
      }

      public WindowInsetsCompat replaceSystemWindowInsets(Object var1, int var2, int var3, int var4, int var5) {
         return null;
      }

      public WindowInsetsCompat replaceSystemWindowInsets(Object var1, Rect var2) {
         return null;
      }
   }

   private interface WindowInsetsCompatImpl {
      WindowInsetsCompat consumeStableInsets(Object var1);

      WindowInsetsCompat consumeSystemWindowInsets(Object var1);

      Object getSourceWindowInsets(Object var1);

      int getStableInsetBottom(Object var1);

      int getStableInsetLeft(Object var1);

      int getStableInsetRight(Object var1);

      int getStableInsetTop(Object var1);

      int getSystemWindowInsetBottom(Object var1);

      int getSystemWindowInsetLeft(Object var1);

      int getSystemWindowInsetRight(Object var1);

      int getSystemWindowInsetTop(Object var1);

      boolean hasInsets(Object var1);

      boolean hasStableInsets(Object var1);

      boolean hasSystemWindowInsets(Object var1);

      boolean isConsumed(Object var1);

      boolean isRound(Object var1);

      WindowInsetsCompat replaceSystemWindowInsets(Object var1, int var2, int var3, int var4, int var5);

      WindowInsetsCompat replaceSystemWindowInsets(Object var1, Rect var2);
   }
}
