package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;

public final class AccessibilityManagerCompat {
   public static boolean addTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      if (VERSION.SDK_INT >= 19) {
         return var1 == null ? false : var0.addTouchExplorationStateChangeListener(new AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper(var1));
      } else {
         return false;
      }
   }

   public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      if (VERSION.SDK_INT >= 19) {
         return var1 == null ? false : var0.removeTouchExplorationStateChangeListener(new AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper(var1));
      } else {
         return false;
      }
   }

   public interface TouchExplorationStateChangeListener {
      void onTouchExplorationStateChanged(boolean var1);
   }

   private static class TouchExplorationStateChangeListenerWrapper implements android.view.accessibility.AccessibilityManager.TouchExplorationStateChangeListener {
      final AccessibilityManagerCompat.TouchExplorationStateChangeListener mListener;

      TouchExplorationStateChangeListenerWrapper(AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
         this.mListener = var1;
      }

      public boolean equals(Object var1) {
         if (this == var1) {
            return true;
         } else if (var1 != null && this.getClass() == var1.getClass()) {
            AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper var2 = (AccessibilityManagerCompat.TouchExplorationStateChangeListenerWrapper)var1;
            return this.mListener.equals(var2.mListener);
         } else {
            return false;
         }
      }

      public int hashCode() {
         return this.mListener.hashCode();
      }

      public void onTouchExplorationStateChanged(boolean var1) {
         this.mListener.onTouchExplorationStateChanged(var1);
      }
   }
}
