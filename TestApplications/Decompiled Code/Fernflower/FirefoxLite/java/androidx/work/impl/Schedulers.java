package androidx.work.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.impl.background.systemalarm.SystemAlarmScheduler;
import androidx.work.impl.background.systemalarm.SystemAlarmService;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.background.systemjob.SystemJobService;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.utils.PackageManagerHelper;
import java.util.Iterator;
import java.util.List;

public class Schedulers {
   private static final String TAG = Logger.tagWithPrefix("Schedulers");

   @SuppressLint({"NewApi"})
   static Scheduler createBestAvailableBackgroundScheduler(Context var0, WorkManagerImpl var1) {
      int var2 = VERSION.SDK_INT;
      boolean var3 = true;
      Object var4;
      if (var2 >= 23) {
         var4 = new SystemJobScheduler(var0, var1);
         PackageManagerHelper.setComponentEnabled(var0, SystemJobService.class, true);
         Logger.get().debug(TAG, "Created SystemJobScheduler and enabled SystemJobService");
         var3 = false;
      } else {
         var4 = new SystemAlarmScheduler(var0);
         Logger.get().debug(TAG, "Created SystemAlarmScheduler");
      }

      PackageManagerHelper.setComponentEnabled(var0, SystemAlarmService.class, var3);
      return (Scheduler)var4;
   }

   public static void schedule(Configuration var0, WorkDatabase var1, List var2) {
      if (var2 != null && var2.size() != 0) {
         WorkSpecDao var3 = var1.workSpecDao();
         var1.beginTransaction();

         List var4;
         label304: {
            Throwable var10000;
            label311: {
               boolean var10001;
               try {
                  var4 = var3.getEligibleWorkForScheduling(var0.getMaxSchedulerLimit());
               } catch (Throwable var26) {
                  var10000 = var26;
                  var10001 = false;
                  break label311;
               }

               if (var4 != null) {
                  label298: {
                     long var5;
                     Iterator var27;
                     try {
                        if (var4.size() <= 0) {
                           break label298;
                        }

                        var5 = System.currentTimeMillis();
                        var27 = var4.iterator();
                     } catch (Throwable var25) {
                        var10000 = var25;
                        var10001 = false;
                        break label311;
                     }

                     while(true) {
                        try {
                           if (!var27.hasNext()) {
                              break;
                           }

                           var3.markWorkSpecScheduled(((WorkSpec)var27.next()).id, var5);
                        } catch (Throwable var24) {
                           var10000 = var24;
                           var10001 = false;
                           break label311;
                        }
                     }
                  }
               }

               label287:
               try {
                  var1.setTransactionSuccessful();
                  break label304;
               } catch (Throwable var23) {
                  var10000 = var23;
                  var10001 = false;
                  break label287;
               }
            }

            Throwable var28 = var10000;
            var1.endTransaction();
            throw var28;
         }

         var1.endTransaction();
         if (var4 != null && var4.size() > 0) {
            WorkSpec[] var29 = (WorkSpec[])var4.toArray(new WorkSpec[0]);
            Iterator var30 = var2.iterator();

            while(var30.hasNext()) {
               ((Scheduler)var30.next()).schedule(var29);
            }
         }

      }
   }
}
