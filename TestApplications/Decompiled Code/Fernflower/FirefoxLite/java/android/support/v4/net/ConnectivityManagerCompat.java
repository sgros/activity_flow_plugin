package android.support.v4.net;

import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build.VERSION;

public final class ConnectivityManagerCompat {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      if (VERSION.SDK_INT >= 16) {
         return var0.isActiveNetworkMetered();
      } else {
         NetworkInfo var1 = var0.getActiveNetworkInfo();
         if (var1 == null) {
            return true;
         } else {
            switch(var1.getType()) {
            case 0:
            case 2:
            case 3:
            case 4:
            case 5:
            case 6:
               return true;
            case 1:
            case 7:
            case 9:
               return false;
            case 8:
            default:
               return true;
            }
         }
      }
   }
}
