package android.support.v4.app;

import android.app.Activity;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import android.os.Build.VERSION;
import android.support.annotation.Nullable;
import android.util.Log;

public final class NavUtils {
   public static final String PARENT_ACTIVITY = "android.support.PARENT_ACTIVITY";
   private static final String TAG = "NavUtils";

   private NavUtils() {
   }

   public static Intent getParentActivityIntent(Activity var0) {
      if (VERSION.SDK_INT >= 16) {
         Intent var1 = var0.getParentActivityIntent();
         if (var1 != null) {
            return var1;
         }
      }

      String var6 = getParentActivityName(var0);
      if (var6 == null) {
         return null;
      } else {
         ComponentName var2 = new ComponentName(var0, var6);

         try {
            Intent var5;
            if (getParentActivityName(var0, var2) == null) {
               var5 = Intent.makeMainActivity(var2);
            } else {
               var5 = new Intent();
               var5 = var5.setComponent(var2);
            }

            return var5;
         } catch (NameNotFoundException var3) {
            StringBuilder var4 = new StringBuilder();
            var4.append("getParentActivityIntent: bad parentActivityName '");
            var4.append(var6);
            var4.append("' in manifest");
            Log.e("NavUtils", var4.toString());
            return null;
         }
      }
   }

   public static Intent getParentActivityIntent(Context var0, ComponentName var1) throws NameNotFoundException {
      String var2 = getParentActivityName(var0, var1);
      if (var2 == null) {
         return null;
      } else {
         var1 = new ComponentName(var1.getPackageName(), var2);
         Intent var3;
         if (getParentActivityName(var0, var1) == null) {
            var3 = Intent.makeMainActivity(var1);
         } else {
            var3 = (new Intent()).setComponent(var1);
         }

         return var3;
      }
   }

   public static Intent getParentActivityIntent(Context var0, Class var1) throws NameNotFoundException {
      String var3 = getParentActivityName(var0, new ComponentName(var0, var1));
      if (var3 == null) {
         return null;
      } else {
         ComponentName var4 = new ComponentName(var0, var3);
         Intent var2;
         if (getParentActivityName(var0, var4) == null) {
            var2 = Intent.makeMainActivity(var4);
         } else {
            var2 = (new Intent()).setComponent(var4);
         }

         return var2;
      }
   }

   @Nullable
   public static String getParentActivityName(Activity var0) {
      try {
         String var2 = getParentActivityName(var0, var0.getComponentName());
         return var2;
      } catch (NameNotFoundException var1) {
         throw new IllegalArgumentException(var1);
      }
   }

   @Nullable
   public static String getParentActivityName(Context var0, ComponentName var1) throws NameNotFoundException {
      ActivityInfo var3 = var0.getPackageManager().getActivityInfo(var1, 128);
      String var2;
      if (VERSION.SDK_INT >= 16) {
         var2 = var3.parentActivityName;
         if (var2 != null) {
            return var2;
         }
      }

      if (var3.metaData == null) {
         return null;
      } else {
         var2 = var3.metaData.getString("android.support.PARENT_ACTIVITY");
         if (var2 == null) {
            return null;
         } else {
            String var4 = var2;
            if (var2.charAt(0) == '.') {
               StringBuilder var5 = new StringBuilder();
               var5.append(var0.getPackageName());
               var5.append(var2);
               var4 = var5.toString();
            }

            return var4;
         }
      }
   }

   public static void navigateUpFromSameTask(Activity var0) {
      Intent var1 = getParentActivityIntent(var0);
      if (var1 == null) {
         StringBuilder var2 = new StringBuilder();
         var2.append("Activity ");
         var2.append(var0.getClass().getSimpleName());
         var2.append(" does not have a parent activity name specified.");
         var2.append(" (Did you forget to add the android.support.PARENT_ACTIVITY <meta-data> ");
         var2.append(" element in your manifest?)");
         throw new IllegalArgumentException(var2.toString());
      } else {
         navigateUpTo(var0, var1);
      }
   }

   public static void navigateUpTo(Activity var0, Intent var1) {
      if (VERSION.SDK_INT >= 16) {
         var0.navigateUpTo(var1);
      } else {
         var1.addFlags(67108864);
         var0.startActivity(var1);
         var0.finish();
      }

   }

   public static boolean shouldUpRecreateTask(Activity var0, Intent var1) {
      if (VERSION.SDK_INT >= 16) {
         return var0.shouldUpRecreateTask(var1);
      } else {
         String var3 = var0.getIntent().getAction();
         boolean var2;
         if (var3 != null && !var3.equals("android.intent.action.MAIN")) {
            var2 = true;
         } else {
            var2 = false;
         }

         return var2;
      }
   }
}
