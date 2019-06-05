package android.support.v4.view;

import android.os.Build.VERSION;
import android.view.ViewConfiguration;

public final class ViewConfigurationCompat {
   static final ViewConfigurationCompat.ViewConfigurationVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 14) {
         IMPL = new ViewConfigurationCompat.IcsViewConfigurationVersionImpl();
      } else if (VERSION.SDK_INT >= 11) {
         IMPL = new ViewConfigurationCompat.HoneycombViewConfigurationVersionImpl();
      } else {
         IMPL = new ViewConfigurationCompat.BaseViewConfigurationVersionImpl();
      }

   }

   private ViewConfigurationCompat() {
   }

   @Deprecated
   public static int getScaledPagingTouchSlop(ViewConfiguration var0) {
      return var0.getScaledPagingTouchSlop();
   }

   public static boolean hasPermanentMenuKey(ViewConfiguration var0) {
      return IMPL.hasPermanentMenuKey(var0);
   }

   static class BaseViewConfigurationVersionImpl implements ViewConfigurationCompat.ViewConfigurationVersionImpl {
      public boolean hasPermanentMenuKey(ViewConfiguration var1) {
         return true;
      }
   }

   static class HoneycombViewConfigurationVersionImpl extends ViewConfigurationCompat.BaseViewConfigurationVersionImpl {
      public boolean hasPermanentMenuKey(ViewConfiguration var1) {
         return false;
      }
   }

   static class IcsViewConfigurationVersionImpl extends ViewConfigurationCompat.HoneycombViewConfigurationVersionImpl {
      public boolean hasPermanentMenuKey(ViewConfiguration var1) {
         return ViewConfigurationCompatICS.hasPermanentMenuKey(var1);
      }
   }

   interface ViewConfigurationVersionImpl {
      boolean hasPermanentMenuKey(ViewConfiguration var1);
   }
}
