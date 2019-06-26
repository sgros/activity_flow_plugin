package android.support.v4.net;

import android.annotation.TargetApi;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.support.annotation.RequiresApi;

@TargetApi(13)
@RequiresApi(13)
class ConnectivityManagerCompatHoneycombMR2 {
   public static boolean isActiveNetworkMetered(ConnectivityManager var0) {
      boolean var1 = true;
      NetworkInfo var3 = var0.getActiveNetworkInfo();
      boolean var2;
      if (var3 == null) {
         var2 = var1;
      } else {
         var2 = var1;
         switch(var3.getType()) {
         case 0:
         case 2:
         case 3:
         case 4:
         case 5:
         case 6:
            break;
         case 1:
         case 7:
         case 9:
            var2 = false;
            break;
         case 8:
         default:
            var2 = var1;
         }
      }

      return var2;
   }
}
