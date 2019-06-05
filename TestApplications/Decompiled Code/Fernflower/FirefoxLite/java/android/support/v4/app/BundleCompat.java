package android.support.v4.app;

import android.os.Bundle;
import android.os.IBinder;
import android.os.Build.VERSION;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public final class BundleCompat {
   public static IBinder getBinder(Bundle var0, String var1) {
      return VERSION.SDK_INT >= 18 ? var0.getBinder(var1) : BundleCompat.BundleCompatBaseImpl.getBinder(var0, var1);
   }

   static class BundleCompatBaseImpl {
      private static Method sGetIBinderMethod;
      private static boolean sGetIBinderMethodFetched;

      public static IBinder getBinder(Bundle var0, String var1) {
         if (!sGetIBinderMethodFetched) {
            try {
               sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class);
               sGetIBinderMethod.setAccessible(true);
            } catch (NoSuchMethodException var3) {
               Log.i("BundleCompatBaseImpl", "Failed to retrieve getIBinder method", var3);
            }

            sGetIBinderMethodFetched = true;
         }

         if (sGetIBinderMethod != null) {
            try {
               IBinder var5 = (IBinder)sGetIBinderMethod.invoke(var0, var1);
               return var5;
            } catch (IllegalAccessException | IllegalArgumentException | InvocationTargetException var4) {
               Log.i("BundleCompatBaseImpl", "Failed to invoke getIBinder via reflection", var4);
               sGetIBinderMethod = null;
            }
         }

         return null;
      }
   }
}
