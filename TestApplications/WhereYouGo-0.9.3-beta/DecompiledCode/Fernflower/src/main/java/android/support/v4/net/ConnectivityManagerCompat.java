package android.support.v4.net;

import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.support.annotation.RestrictTo;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public final class ConnectivityManagerCompat {
   private static final ConnectivityManagerCompat.ConnectivityManagerCompatImpl IMPL;
   public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
   public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
   public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;

   static {
      if (VERSION.SDK_INT >= 24) {
         IMPL = new ConnectivityManagerCompat.Api24ConnectivityManagerCompatImpl();
      } else if (VERSION.SDK_INT >= 16) {
         IMPL = new ConnectivityManagerCompat.JellyBeanConnectivityManagerCompatImpl();
      } else if (VERSION.SDK_INT >= 13) {
         IMPL = new ConnectivityManagerCompat.HoneycombMR2ConnectivityManagerCompatImpl();
      } else {
         IMPL = new ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl();
      }

   }

   private ConnectivityManagerCompat() {
   }

   public static NetworkInfo getNetworkInfoFromBroadcast(ConnectivityManager var0, Intent var1) {
      NetworkInfo var3 = (NetworkInfo)var1.getParcelableExtra("networkInfo");
      NetworkInfo var2;
      if (var3 != null) {
         var2 = var0.getNetworkInfo(var3.getType());
      } else {
         var2 = null;
      }

      return var2;
   }

   public static int getRestrictBackgroundStatus(ConnectivityManager var0) {
      return IMPL.getRestrictBackgroundStatus(var0);
   }

   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      return IMPL.isActiveNetworkMetered(var0);
   }

   static class Api24ConnectivityManagerCompatImpl extends ConnectivityManagerCompat.JellyBeanConnectivityManagerCompatImpl {
      public int getRestrictBackgroundStatus(ConnectivityManager var1) {
         return ConnectivityManagerCompatApi24.getRestrictBackgroundStatus(var1);
      }
   }

   static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompat.ConnectivityManagerCompatImpl {
      public int getRestrictBackgroundStatus(ConnectivityManager var1) {
         return 3;
      }

      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         boolean var2 = true;
         NetworkInfo var4 = var1.getActiveNetworkInfo();
         boolean var3;
         if (var4 == null) {
            var3 = var2;
         } else {
            var3 = var2;
            switch(var4.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               break;
            case 1:
               var3 = false;
               break;
            default:
               var3 = var2;
            }
         }

         return var3;
      }
   }

   interface ConnectivityManagerCompatImpl {
      int getRestrictBackgroundStatus(ConnectivityManager var1);

      boolean isActiveNetworkMetered(ConnectivityManager var1);
   }

   static class HoneycombMR2ConnectivityManagerCompatImpl extends ConnectivityManagerCompat.BaseConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(var1);
      }
   }

   static class JellyBeanConnectivityManagerCompatImpl extends ConnectivityManagerCompat.HoneycombMR2ConnectivityManagerCompatImpl {
      public boolean isActiveNetworkMetered(ConnectivityManager var1) {
         return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(var1);
      }
   }

   @Retention(RetentionPolicy.SOURCE)
   @RestrictTo({RestrictTo.Scope.LIBRARY_GROUP})
   public @interface RestrictBackgroundStatus {
   }
}
