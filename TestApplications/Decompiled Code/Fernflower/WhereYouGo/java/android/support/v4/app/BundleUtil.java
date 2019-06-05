package android.support.v4.app;

import android.os.Bundle;
import android.os.Parcelable;
import java.util.Arrays;

class BundleUtil {
   public static Bundle[] getBundleArrayFromBundle(Bundle var0, String var1) {
      Parcelable[] var2 = var0.getParcelableArray(var1);
      Bundle[] var3;
      if (!(var2 instanceof Bundle[]) && var2 != null) {
         Bundle[] var4 = (Bundle[])Arrays.copyOf(var2, var2.length, Bundle[].class);
         var0.putParcelableArray(var1, var4);
         var3 = var4;
      } else {
         var3 = (Bundle[])var2;
      }

      return var3;
   }
}
