package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Build.VERSION;

public final class BundleCompat {
   private BundleCompat() {
   }

   public static IBinder getBinder(Bundle var0, String var1) {
      IBinder var2;
      if (VERSION.SDK_INT >= 18) {
         var2 = BundleCompatJellybeanMR2.getBinder(var0, var1);
      } else {
         var2 = BundleCompatGingerbread.getBinder(var0, var1);
      }

      return var2;
   }

   public static void putBinder(Bundle var0, String var1, IBinder var2) {
      if (VERSION.SDK_INT >= 18) {
         BundleCompatJellybeanMR2.putBinder(var0, var1, var2);
      } else {
         BundleCompatGingerbread.putBinder(var0, var1, var2);
      }

   }
}
