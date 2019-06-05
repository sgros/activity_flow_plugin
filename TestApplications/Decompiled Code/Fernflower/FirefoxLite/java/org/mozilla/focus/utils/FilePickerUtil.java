package org.mozilla.focus.utils;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.ResolveInfo;
import android.os.Parcelable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

public class FilePickerUtil {
   private static void addActivities(Context var0, Intent var1, HashMap var2, HashMap var3) {
      if (var3 != null) {
         Iterator var6 = var0.getPackageManager().queryIntentActivities(var1, 0).iterator();

         while(var6.hasNext()) {
            ResolveInfo var4 = (ResolveInfo)var6.next();
            ComponentName var5 = new ComponentName(var4.activityInfo.applicationInfo.packageName, var4.activityInfo.name);
            if (!var3.containsKey(var5.toString())) {
               Intent var7 = new Intent(var1);
               var7.setComponent(var5);
               var2.put(var5.toString(), var7);
            }
         }

      }
   }

   private static Intent createIntent(String var0) {
      Intent var1 = new Intent("android.intent.action.GET_CONTENT");
      var1.setType(var0);
      var1.addCategory("android.intent.category.OPENABLE");
      return var1;
   }

   public static Intent getFilePickerIntent(Context var0, CharSequence var1, String[] var2) {
      List var3 = getIntentsForFilePicker(var0, var2);
      if (var3.size() == 0) {
         return null;
      } else {
         Intent var5 = (Intent)var3.remove(0);
         if (var3.size() == 0) {
            return var5;
         } else {
            Intent var4 = Intent.createChooser(var5, var1);
            var4.putExtra("android.intent.extra.INITIAL_INTENTS", (Parcelable[])var3.toArray(new Parcelable[var3.size()]));
            return var4;
         }
      }
   }

   public static List getIntentsForFilePicker(Context var0, String[] var1) {
      HashMap var2 = new HashMap();
      HashMap var3 = new HashMap();
      String var5;
      if (var1 == null) {
         var5 = "*/*";
      } else {
         var5 = var1[0];
      }

      String var4 = var5;
      if (!"audio/*".equals(var5)) {
         var4 = var5;
         if (!"image/*".equals(var5)) {
            if ("video/*".equals(var5)) {
               var4 = var5;
            } else {
               var4 = "*/*";
            }
         }
      }

      addActivities(var0, createIntent(var4), var3, var2);
      if (var2.size() == 0 && var3.size() == 0) {
         var3.clear();
         addActivities(var0, createIntent("*/*"), var2, (HashMap)null);
      }

      return new ArrayList(var3.values());
   }
}
