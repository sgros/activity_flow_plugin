package android.support.v4.app;

import android.annotation.TargetApi;
import android.app.AppOpsManager;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.support.annotation.RequiresApi;
import java.lang.reflect.InvocationTargetException;

@TargetApi(19)
@RequiresApi(19)
class NotificationManagerCompatKitKat {
   private static final String CHECK_OP_NO_THROW = "checkOpNoThrow";
   private static final String OP_POST_NOTIFICATION = "OP_POST_NOTIFICATION";

   public static boolean areNotificationsEnabled(Context var0) {
      AppOpsManager var1 = (AppOpsManager)var0.getSystemService("appops");
      ApplicationInfo var2 = var0.getApplicationInfo();
      String var11 = var0.getApplicationContext().getPackageName();
      int var3 = var2.uid;

      boolean var4;
      label37: {
         try {
            Class var12 = Class.forName(AppOpsManager.class.getName());
            var3 = (Integer)var12.getMethod("checkOpNoThrow", Integer.TYPE, Integer.TYPE, String.class).invoke(var1, (Integer)var12.getDeclaredField("OP_POST_NOTIFICATION").get(Integer.class), var3, var11);
            break label37;
         } catch (ClassNotFoundException var5) {
         } catch (NoSuchMethodException var6) {
         } catch (NoSuchFieldException var7) {
         } catch (InvocationTargetException var8) {
         } catch (IllegalAccessException var9) {
         } catch (RuntimeException var10) {
         }

         var4 = true;
         return var4;
      }

      if (var3 == 0) {
         var4 = true;
      } else {
         var4 = false;
      }

      return var4;
   }
}
