package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.KeyEvent;
import android.view.View;
import android.view.KeyEvent.Callback;
import android.view.KeyEvent.DispatcherState;

public final class KeyEventCompat {
   static final KeyEventCompat.KeyEventVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 11) {
         IMPL = new KeyEventCompat.HoneycombKeyEventVersionImpl();
      } else {
         IMPL = new KeyEventCompat.BaseKeyEventVersionImpl();
      }

   }

   private KeyEventCompat() {
   }

   @Deprecated
   public static boolean dispatch(KeyEvent var0, Callback var1, Object var2, Object var3) {
      return var0.dispatch(var1, (DispatcherState)var2, var3);
   }

   @Deprecated
   public static Object getKeyDispatcherState(View var0) {
      return var0.getKeyDispatcherState();
   }

   public static boolean hasModifiers(KeyEvent var0, int var1) {
      return IMPL.metaStateHasModifiers(var0.getMetaState(), var1);
   }

   public static boolean hasNoModifiers(KeyEvent var0) {
      return IMPL.metaStateHasNoModifiers(var0.getMetaState());
   }

   public static boolean isCtrlPressed(KeyEvent var0) {
      return IMPL.isCtrlPressed(var0);
   }

   @Deprecated
   public static boolean isTracking(KeyEvent var0) {
      return var0.isTracking();
   }

   public static boolean metaStateHasModifiers(int var0, int var1) {
      return IMPL.metaStateHasModifiers(var0, var1);
   }

   public static boolean metaStateHasNoModifiers(int var0) {
      return IMPL.metaStateHasNoModifiers(var0);
   }

   public static int normalizeMetaState(int var0) {
      return IMPL.normalizeMetaState(var0);
   }

   @Deprecated
   public static void startTracking(KeyEvent var0) {
      var0.startTracking();
   }

   static class BaseKeyEventVersionImpl implements KeyEventCompat.KeyEventVersionImpl {
      private static final int META_ALL_MASK = 247;
      private static final int META_MODIFIER_MASK = 247;

      private static int metaStateFilterDirectionalModifiers(int var0, int var1, int var2, int var3, int var4) {
         boolean var5 = true;
         boolean var6;
         if ((var1 & var2) != 0) {
            var6 = true;
         } else {
            var6 = false;
         }

         var4 |= var3;
         boolean var7;
         if ((var1 & var4) != 0) {
            var7 = var5;
         } else {
            var7 = false;
         }

         if (var6) {
            if (var7) {
               throw new IllegalArgumentException("bad arguments");
            }

            var1 = var0 & ~var4;
         } else {
            var1 = var0;
            if (var7) {
               var1 = var0 & ~var2;
            }
         }

         return var1;
      }

      public boolean isCtrlPressed(KeyEvent var1) {
         return false;
      }

      public boolean metaStateHasModifiers(int var1, int var2) {
         boolean var3 = true;
         if (metaStateFilterDirectionalModifiers(metaStateFilterDirectionalModifiers(this.normalizeMetaState(var1) & 247, var2, 1, 64, 128), var2, 2, 16, 32) != var2) {
            var3 = false;
         }

         return var3;
      }

      public boolean metaStateHasNoModifiers(int var1) {
         boolean var2;
         if ((this.normalizeMetaState(var1) & 247) == 0) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }

      public int normalizeMetaState(int var1) {
         int var2 = var1;
         if ((var1 & 192) != 0) {
            var2 = var1 | 1;
         }

         var1 = var2;
         if ((var2 & 48) != 0) {
            var1 = var2 | 2;
         }

         return var1 & 247;
      }
   }

   static class HoneycombKeyEventVersionImpl extends KeyEventCompat.BaseKeyEventVersionImpl {
      public boolean isCtrlPressed(KeyEvent var1) {
         return KeyEventCompatHoneycomb.isCtrlPressed(var1);
      }

      public boolean metaStateHasModifiers(int var1, int var2) {
         return KeyEventCompatHoneycomb.metaStateHasModifiers(var1, var2);
      }

      public boolean metaStateHasNoModifiers(int var1) {
         return KeyEventCompatHoneycomb.metaStateHasNoModifiers(var1);
      }

      public int normalizeMetaState(int var1) {
         return KeyEventCompatHoneycomb.normalizeMetaState(var1);
      }
   }

   interface KeyEventVersionImpl {
      boolean isCtrlPressed(KeyEvent var1);

      boolean metaStateHasModifiers(int var1, int var2);

      boolean metaStateHasNoModifiers(int var1);

      int normalizeMetaState(int var1);
   }
}
