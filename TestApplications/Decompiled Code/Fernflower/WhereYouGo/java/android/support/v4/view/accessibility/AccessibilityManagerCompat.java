package android.support.v4.view.accessibility;

import android.os.Build.VERSION;
import android.view.accessibility.AccessibilityManager;
import java.util.Collections;
import java.util.List;

public final class AccessibilityManagerCompat {
   private static final AccessibilityManagerCompat.AccessibilityManagerVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 19) {
         IMPL = new AccessibilityManagerCompat.AccessibilityManagerKitKatImpl();
      } else if (VERSION.SDK_INT >= 14) {
         IMPL = new AccessibilityManagerCompat.AccessibilityManagerIcsImpl();
      } else {
         IMPL = new AccessibilityManagerCompat.AccessibilityManagerStubImpl();
      }

   }

   private AccessibilityManagerCompat() {
   }

   public static boolean addAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
      return IMPL.addAccessibilityStateChangeListener(var0, var1);
   }

   public static boolean addTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      return IMPL.addTouchExplorationStateChangeListener(var0, var1);
   }

   public static List getEnabledAccessibilityServiceList(AccessibilityManager var0, int var1) {
      return IMPL.getEnabledAccessibilityServiceList(var0, var1);
   }

   public static List getInstalledAccessibilityServiceList(AccessibilityManager var0) {
      return IMPL.getInstalledAccessibilityServiceList(var0);
   }

   public static boolean isTouchExplorationEnabled(AccessibilityManager var0) {
      return IMPL.isTouchExplorationEnabled(var0);
   }

   public static boolean removeAccessibilityStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
      return IMPL.removeAccessibilityStateChangeListener(var0, var1);
   }

   public static boolean removeTouchExplorationStateChangeListener(AccessibilityManager var0, AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
      return IMPL.removeTouchExplorationStateChangeListener(var0, var1);
   }

   static class AccessibilityManagerIcsImpl extends AccessibilityManagerCompat.AccessibilityManagerStubImpl {
      public boolean addAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2) {
         return AccessibilityManagerCompatIcs.addAccessibilityStateChangeListener(var1, this.newAccessibilityStateChangeListener(var2));
      }

      public List getEnabledAccessibilityServiceList(AccessibilityManager var1, int var2) {
         return AccessibilityManagerCompatIcs.getEnabledAccessibilityServiceList(var1, var2);
      }

      public List getInstalledAccessibilityServiceList(AccessibilityManager var1) {
         return AccessibilityManagerCompatIcs.getInstalledAccessibilityServiceList(var1);
      }

      public boolean isTouchExplorationEnabled(AccessibilityManager var1) {
         return AccessibilityManagerCompatIcs.isTouchExplorationEnabled(var1);
      }

      public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(final AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
         return new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper(var1, new AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerBridge() {
            public void onAccessibilityStateChanged(boolean var1x) {
               var1.onAccessibilityStateChanged(var1x);
            }
         });
      }

      public boolean removeAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2) {
         return AccessibilityManagerCompatIcs.removeAccessibilityStateChangeListener(var1, this.newAccessibilityStateChangeListener(var2));
      }
   }

   static class AccessibilityManagerKitKatImpl extends AccessibilityManagerCompat.AccessibilityManagerIcsImpl {
      public boolean addTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2) {
         return AccessibilityManagerCompatKitKat.addTouchExplorationStateChangeListener(var1, this.newTouchExplorationStateChangeListener(var2));
      }

      public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(final AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
         return new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper(var1, new AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerBridge() {
            public void onTouchExplorationStateChanged(boolean var1x) {
               var1.onTouchExplorationStateChanged(var1x);
            }
         });
      }

      public boolean removeTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2) {
         return AccessibilityManagerCompatKitKat.removeTouchExplorationStateChangeListener(var1, this.newTouchExplorationStateChangeListener(var2));
      }
   }

   static class AccessibilityManagerStubImpl implements AccessibilityManagerCompat.AccessibilityManagerVersionImpl {
      public boolean addAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2) {
         return false;
      }

      public boolean addTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2) {
         return false;
      }

      public List getEnabledAccessibilityServiceList(AccessibilityManager var1, int var2) {
         return Collections.emptyList();
      }

      public List getInstalledAccessibilityServiceList(AccessibilityManager var1) {
         return Collections.emptyList();
      }

      public boolean isTouchExplorationEnabled(AccessibilityManager var1) {
         return false;
      }

      public AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListener var1) {
         return null;
      }

      public AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(AccessibilityManagerCompat.TouchExplorationStateChangeListener var1) {
         return null;
      }

      public boolean removeAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2) {
         return false;
      }

      public boolean removeTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2) {
         return false;
      }
   }

   interface AccessibilityManagerVersionImpl {
      boolean addAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2);

      boolean addTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2);

      List getEnabledAccessibilityServiceList(AccessibilityManager var1, int var2);

      List getInstalledAccessibilityServiceList(AccessibilityManager var1);

      boolean isTouchExplorationEnabled(AccessibilityManager var1);

      AccessibilityManagerCompatIcs.AccessibilityStateChangeListenerWrapper newAccessibilityStateChangeListener(AccessibilityManagerCompat.AccessibilityStateChangeListener var1);

      AccessibilityManagerCompatKitKat.TouchExplorationStateChangeListenerWrapper newTouchExplorationStateChangeListener(AccessibilityManagerCompat.TouchExplorationStateChangeListener var1);

      boolean removeAccessibilityStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.AccessibilityStateChangeListener var2);

      boolean removeTouchExplorationStateChangeListener(AccessibilityManager var1, AccessibilityManagerCompat.TouchExplorationStateChangeListener var2);
   }

   public interface AccessibilityStateChangeListener {
      void onAccessibilityStateChanged(boolean var1);
   }

   @Deprecated
   public abstract static class AccessibilityStateChangeListenerCompat implements AccessibilityManagerCompat.AccessibilityStateChangeListener {
   }

   public interface TouchExplorationStateChangeListener {
      void onTouchExplorationStateChanged(boolean var1);
   }
}
