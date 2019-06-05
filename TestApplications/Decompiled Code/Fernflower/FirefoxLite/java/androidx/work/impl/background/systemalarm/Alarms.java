package androidx.work.impl.background.systemalarm;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.Logger;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.SystemIdInfoDao;
import androidx.work.impl.utils.IdGenerator;

class Alarms {
   private static final String TAG = Logger.tagWithPrefix("Alarms");

   public static void cancelAlarm(Context var0, WorkManagerImpl var1, String var2) {
      SystemIdInfoDao var4 = var1.getWorkDatabase().systemIdInfoDao();
      SystemIdInfo var3 = var4.getSystemIdInfo(var2);
      if (var3 != null) {
         cancelExactAlarm(var0, var2, var3.systemId);
         Logger.get().debug(TAG, String.format("Removing SystemIdInfo for workSpecId (%s)", var2));
         var4.removeSystemIdInfo(var2);
      }

   }

   private static void cancelExactAlarm(Context var0, String var1, int var2) {
      AlarmManager var3 = (AlarmManager)var0.getSystemService("alarm");
      PendingIntent var4 = PendingIntent.getService(var0, var2, CommandHandler.createDelayMetIntent(var0, var1), 536870912);
      if (var4 != null && var3 != null) {
         Logger.get().debug(TAG, String.format("Cancelling existing alarm with (workSpecId, systemId) (%s, %s)", var1, var2));
         var3.cancel(var4);
      }

   }

   public static void setAlarm(Context var0, WorkManagerImpl var1, String var2, long var3) {
      SystemIdInfoDao var7 = var1.getWorkDatabase().systemIdInfoDao();
      SystemIdInfo var5 = var7.getSystemIdInfo(var2);
      if (var5 != null) {
         cancelExactAlarm(var0, var2, var5.systemId);
         setExactAlarm(var0, var2, var5.systemId, var3);
      } else {
         int var6 = (new IdGenerator(var0)).nextAlarmManagerId();
         var7.insertSystemIdInfo(new SystemIdInfo(var2, var6));
         setExactAlarm(var0, var2, var6, var3);
      }

   }

   private static void setExactAlarm(Context var0, String var1, int var2, long var3) {
      AlarmManager var5 = (AlarmManager)var0.getSystemService("alarm");
      PendingIntent var6 = PendingIntent.getService(var0, var2, CommandHandler.createDelayMetIntent(var0, var1), 1073741824);
      if (var5 != null) {
         if (VERSION.SDK_INT >= 19) {
            var5.setExact(0, var3, var6);
         } else {
            var5.set(0, var3, var6);
         }
      }

   }
}
