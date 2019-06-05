package android.support.v4.accessibilityservice;

import android.accessibilityservice.AccessibilityServiceInfo;
import android.content.pm.PackageManager;
import android.content.pm.ResolveInfo;
import android.os.Build.VERSION;

public final class AccessibilityServiceInfoCompat {
   public static final int CAPABILITY_CAN_FILTER_KEY_EVENTS = 8;
   public static final int CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 4;
   public static final int CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION = 2;
   public static final int CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT = 1;
   public static final int DEFAULT = 1;
   public static final int FEEDBACK_ALL_MASK = -1;
   public static final int FEEDBACK_BRAILLE = 32;
   public static final int FLAG_INCLUDE_NOT_IMPORTANT_VIEWS = 2;
   public static final int FLAG_REPORT_VIEW_IDS = 16;
   public static final int FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY = 8;
   public static final int FLAG_REQUEST_FILTER_KEY_EVENTS = 32;
   public static final int FLAG_REQUEST_TOUCH_EXPLORATION_MODE = 4;
   private static final AccessibilityServiceInfoCompat.AccessibilityServiceInfoVersionImpl IMPL;

   static {
      if (VERSION.SDK_INT >= 18) {
         IMPL = new AccessibilityServiceInfoCompat.AccessibilityServiceInfoJellyBeanMr2Impl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new AccessibilityServiceInfoCompat.AccessibilityServiceInfoJellyBeanImpl();
      } else if (VERSION.SDK_INT >= 14) {
         IMPL = new AccessibilityServiceInfoCompat.AccessibilityServiceInfoIcsImpl();
      } else {
         IMPL = new AccessibilityServiceInfoCompat.AccessibilityServiceInfoStubImpl();
      }

   }

   private AccessibilityServiceInfoCompat() {
   }

   public static String capabilityToString(int var0) {
      String var1;
      switch(var0) {
      case 1:
         var1 = "CAPABILITY_CAN_RETRIEVE_WINDOW_CONTENT";
         break;
      case 2:
         var1 = "CAPABILITY_CAN_REQUEST_TOUCH_EXPLORATION";
         break;
      case 3:
      case 5:
      case 6:
      case 7:
      default:
         var1 = "UNKNOWN";
         break;
      case 4:
         var1 = "CAPABILITY_CAN_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
         break;
      case 8:
         var1 = "CAPABILITY_CAN_FILTER_KEY_EVENTS";
      }

      return var1;
   }

   public static String feedbackTypeToString(int var0) {
      StringBuilder var1 = new StringBuilder();
      var1.append("[");

      while(var0 > 0) {
         int var2 = 1 << Integer.numberOfTrailingZeros(var0);
         var0 &= ~var2;
         if (var1.length() > 1) {
            var1.append(", ");
         }

         switch(var2) {
         case 1:
            var1.append("FEEDBACK_SPOKEN");
            break;
         case 2:
            var1.append("FEEDBACK_HAPTIC");
            break;
         case 4:
            var1.append("FEEDBACK_AUDIBLE");
            break;
         case 8:
            var1.append("FEEDBACK_VISUAL");
            break;
         case 16:
            var1.append("FEEDBACK_GENERIC");
         }
      }

      var1.append("]");
      return var1.toString();
   }

   public static String flagToString(int var0) {
      String var1;
      switch(var0) {
      case 1:
         var1 = "DEFAULT";
         break;
      case 2:
         var1 = "FLAG_INCLUDE_NOT_IMPORTANT_VIEWS";
         break;
      case 4:
         var1 = "FLAG_REQUEST_TOUCH_EXPLORATION_MODE";
         break;
      case 8:
         var1 = "FLAG_REQUEST_ENHANCED_WEB_ACCESSIBILITY";
         break;
      case 16:
         var1 = "FLAG_REPORT_VIEW_IDS";
         break;
      case 32:
         var1 = "FLAG_REQUEST_FILTER_KEY_EVENTS";
         break;
      default:
         var1 = null;
      }

      return var1;
   }

   public static boolean getCanRetrieveWindowContent(AccessibilityServiceInfo var0) {
      return IMPL.getCanRetrieveWindowContent(var0);
   }

   public static int getCapabilities(AccessibilityServiceInfo var0) {
      return IMPL.getCapabilities(var0);
   }

   public static String getDescription(AccessibilityServiceInfo var0) {
      return IMPL.getDescription(var0);
   }

   public static String getId(AccessibilityServiceInfo var0) {
      return IMPL.getId(var0);
   }

   public static ResolveInfo getResolveInfo(AccessibilityServiceInfo var0) {
      return IMPL.getResolveInfo(var0);
   }

   public static String getSettingsActivityName(AccessibilityServiceInfo var0) {
      return IMPL.getSettingsActivityName(var0);
   }

   public static String loadDescription(AccessibilityServiceInfo var0, PackageManager var1) {
      return IMPL.loadDescription(var0, var1);
   }

   static class AccessibilityServiceInfoIcsImpl extends AccessibilityServiceInfoCompat.AccessibilityServiceInfoStubImpl {
      public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatIcs.getCanRetrieveWindowContent(var1);
      }

      public int getCapabilities(AccessibilityServiceInfo var1) {
         byte var2;
         if (this.getCanRetrieveWindowContent(var1)) {
            var2 = 1;
         } else {
            var2 = 0;
         }

         return var2;
      }

      public String getDescription(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatIcs.getDescription(var1);
      }

      public String getId(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatIcs.getId(var1);
      }

      public ResolveInfo getResolveInfo(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatIcs.getResolveInfo(var1);
      }

      public String getSettingsActivityName(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatIcs.getSettingsActivityName(var1);
      }
   }

   static class AccessibilityServiceInfoJellyBeanImpl extends AccessibilityServiceInfoCompat.AccessibilityServiceInfoIcsImpl {
      public String loadDescription(AccessibilityServiceInfo var1, PackageManager var2) {
         return AccessibilityServiceInfoCompatJellyBean.loadDescription(var1, var2);
      }
   }

   static class AccessibilityServiceInfoJellyBeanMr2Impl extends AccessibilityServiceInfoCompat.AccessibilityServiceInfoJellyBeanImpl {
      public int getCapabilities(AccessibilityServiceInfo var1) {
         return AccessibilityServiceInfoCompatJellyBeanMr2.getCapabilities(var1);
      }
   }

   static class AccessibilityServiceInfoStubImpl implements AccessibilityServiceInfoCompat.AccessibilityServiceInfoVersionImpl {
      public boolean getCanRetrieveWindowContent(AccessibilityServiceInfo var1) {
         return false;
      }

      public int getCapabilities(AccessibilityServiceInfo var1) {
         return 0;
      }

      public String getDescription(AccessibilityServiceInfo var1) {
         return null;
      }

      public String getId(AccessibilityServiceInfo var1) {
         return null;
      }

      public ResolveInfo getResolveInfo(AccessibilityServiceInfo var1) {
         return null;
      }

      public String getSettingsActivityName(AccessibilityServiceInfo var1) {
         return null;
      }

      public String loadDescription(AccessibilityServiceInfo var1, PackageManager var2) {
         return null;
      }
   }

   interface AccessibilityServiceInfoVersionImpl {
      boolean getCanRetrieveWindowContent(AccessibilityServiceInfo var1);

      int getCapabilities(AccessibilityServiceInfo var1);

      String getDescription(AccessibilityServiceInfo var1);

      String getId(AccessibilityServiceInfo var1);

      ResolveInfo getResolveInfo(AccessibilityServiceInfo var1);

      String getSettingsActivityName(AccessibilityServiceInfo var1);

      String loadDescription(AccessibilityServiceInfo var1, PackageManager var2);
   }
}
