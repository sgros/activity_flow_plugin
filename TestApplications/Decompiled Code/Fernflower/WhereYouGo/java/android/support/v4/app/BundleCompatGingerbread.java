package android.support.v4.app;

import android.annotation.TargetApi;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.RequiresApi;
import android.util.Log;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

@TargetApi(9)
@RequiresApi(9)
class BundleCompatGingerbread {
   private static final String TAG = "BundleCompatGingerbread";
   private static Method sGetIBinderMethod;
   private static boolean sGetIBinderMethodFetched;
   private static Method sPutIBinderMethod;
   private static boolean sPutIBinderMethodFetched;

   public static IBinder getBinder(Bundle var0, String var1) {
      if (!sGetIBinderMethodFetched) {
         try {
            sGetIBinderMethod = Bundle.class.getMethod("getIBinder", String.class);
            sGetIBinderMethod.setAccessible(true);
         } catch (NoSuchMethodException var3) {
            Log.i("BundleCompatGingerbread", "Failed to retrieve getIBinder method", var3);
         }

         sGetIBinderMethodFetched = true;
      }

      IBinder var8;
      if (sGetIBinderMethod != null) {
         Object var7;
         try {
            var8 = (IBinder)sGetIBinderMethod.invoke(var0, var1);
            return var8;
         } catch (InvocationTargetException var4) {
            var7 = var4;
         } catch (IllegalAccessException var5) {
            var7 = var5;
         } catch (IllegalArgumentException var6) {
            var7 = var6;
         }

         Log.i("BundleCompatGingerbread", "Failed to invoke getIBinder via reflection", (Throwable)var7);
         sGetIBinderMethod = null;
      }

      var8 = null;
      return var8;
   }

   public static void putBinder(Bundle var0, String var1, IBinder var2) {
      if (!sPutIBinderMethodFetched) {
         try {
            sPutIBinderMethod = Bundle.class.getMethod("putIBinder", String.class, IBinder.class);
            sPutIBinderMethod.setAccessible(true);
         } catch (NoSuchMethodException var4) {
            Log.i("BundleCompatGingerbread", "Failed to retrieve putIBinder method", var4);
         }

         sPutIBinderMethodFetched = true;
      }

      if (sPutIBinderMethod != null) {
         Object var8;
         try {
            sPutIBinderMethod.invoke(var0, var1, var2);
            return;
         } catch (InvocationTargetException var5) {
            var8 = var5;
         } catch (IllegalAccessException var6) {
            var8 = var6;
         } catch (IllegalArgumentException var7) {
            var8 = var7;
         }

         Log.i("BundleCompatGingerbread", "Failed to invoke putIBinder via reflection", (Throwable)var8);
         sPutIBinderMethod = null;
      }

   }
}
