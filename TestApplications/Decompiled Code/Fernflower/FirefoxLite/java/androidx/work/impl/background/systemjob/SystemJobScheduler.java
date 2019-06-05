package androidx.work.impl.background.systemjob;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.Context;
import android.os.PersistableBundle;
import android.os.Build.VERSION;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.Scheduler;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.SystemIdInfo;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.IdGenerator;
import java.util.Iterator;
import java.util.List;

public class SystemJobScheduler implements Scheduler {
   private static final String TAG = Logger.tagWithPrefix("SystemJobScheduler");
   private final IdGenerator mIdGenerator;
   private final JobScheduler mJobScheduler;
   private final SystemJobInfoConverter mSystemJobInfoConverter;
   private final WorkManagerImpl mWorkManager;

   public SystemJobScheduler(Context var1, WorkManagerImpl var2) {
      this(var1, var2, (JobScheduler)var1.getSystemService("jobscheduler"), new SystemJobInfoConverter(var1));
   }

   public SystemJobScheduler(Context var1, WorkManagerImpl var2, JobScheduler var3, SystemJobInfoConverter var4) {
      this.mWorkManager = var2;
      this.mJobScheduler = var3;
      this.mIdGenerator = new IdGenerator(var1);
      this.mSystemJobInfoConverter = var4;
   }

   private static JobInfo getPendingJobInfo(JobScheduler var0, String var1) {
      List var4 = var0.getAllPendingJobs();
      if (var4 != null) {
         Iterator var2 = var4.iterator();

         while(var2.hasNext()) {
            JobInfo var3 = (JobInfo)var2.next();
            PersistableBundle var5 = var3.getExtras();
            if (var5 != null && var5.containsKey("EXTRA_WORK_SPEC_ID") && var1.equals(var5.getString("EXTRA_WORK_SPEC_ID"))) {
               return var3;
            }
         }
      }

      return null;
   }

   public static void jobSchedulerCancelAll(Context var0) {
      JobScheduler var3 = (JobScheduler)var0.getSystemService("jobscheduler");
      if (var3 != null) {
         List var1 = var3.getAllPendingJobs();
         if (var1 != null) {
            Iterator var2 = var1.iterator();

            while(var2.hasNext()) {
               JobInfo var4 = (JobInfo)var2.next();
               if (var4.getExtras().containsKey("EXTRA_WORK_SPEC_ID")) {
                  var3.cancel(var4.getId());
               }
            }
         }
      }

   }

   public void cancel(String var1) {
      List var2 = this.mJobScheduler.getAllPendingJobs();
      if (var2 != null) {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            JobInfo var4 = (JobInfo)var3.next();
            if (var1.equals(var4.getExtras().getString("EXTRA_WORK_SPEC_ID"))) {
               this.mWorkManager.getWorkDatabase().systemIdInfoDao().removeSystemIdInfo(var1);
               this.mJobScheduler.cancel(var4.getId());
               if (VERSION.SDK_INT != 23) {
                  return;
               }
            }
         }
      }

   }

   public void schedule(WorkSpec... var1) {
      WorkDatabase var2 = this.mWorkManager.getWorkDatabase();
      int var3 = var1.length;

      for(int var4 = 0; var4 < var3; ++var4) {
         WorkSpec var5 = var1[var4];
         var2.beginTransaction();

         label965: {
            Throwable var10000;
            label973: {
               WorkSpec var6;
               boolean var10001;
               try {
                  var6 = var2.workSpecDao().getWorkSpec(var5.id);
               } catch (Throwable var118) {
                  var10000 = var118;
                  var10001 = false;
                  break label973;
               }

               Logger var7;
               if (var6 == null) {
                  label932:
                  try {
                     var7 = Logger.get();
                     String var123 = TAG;
                     StringBuilder var124 = new StringBuilder();
                     var124.append("Skipping scheduling ");
                     var124.append(var5.id);
                     var124.append(" because it's no longer in the DB");
                     var7.warning(var123, var124.toString());
                  } catch (Throwable var110) {
                     var10000 = var110;
                     var10001 = false;
                     break label932;
                  }
               } else {
                  label974: {
                     try {
                        if (var6.state != WorkInfo.State.ENQUEUED) {
                           var7 = Logger.get();
                           String var8 = TAG;
                           StringBuilder var122 = new StringBuilder();
                           var122.append("Skipping scheduling ");
                           var122.append(var5.id);
                           var122.append(" because it is no longer enqueued");
                           var7.warning(var8, var122.toString());
                           break label965;
                        }
                     } catch (Throwable var119) {
                        var10000 = var119;
                        var10001 = false;
                        break label974;
                     }

                     SystemIdInfo var121;
                     try {
                        var121 = var2.systemIdInfoDao().getSystemIdInfo(var5.id);
                     } catch (Throwable var117) {
                        var10000 = var117;
                        var10001 = false;
                        break label974;
                     }

                     if (var121 != null) {
                        try {
                           if (getPendingJobInfo(this.mJobScheduler, var5.id) != null) {
                              Logger.get().debug(TAG, String.format("Skipping scheduling %s because JobScheduler is aware of it already.", var5.id));
                              break label965;
                           }
                        } catch (Throwable var116) {
                           var10000 = var116;
                           var10001 = false;
                           break label974;
                        }
                     }

                     int var9;
                     if (var121 != null) {
                        try {
                           var9 = var121.systemId;
                        } catch (Throwable var115) {
                           var10000 = var115;
                           var10001 = false;
                           break label974;
                        }
                     } else {
                        try {
                           var9 = this.mIdGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId());
                        } catch (Throwable var114) {
                           var10000 = var114;
                           var10001 = false;
                           break label974;
                        }
                     }

                     if (var121 == null) {
                        try {
                           var121 = new SystemIdInfo(var5.id, var9);
                           this.mWorkManager.getWorkDatabase().systemIdInfoDao().insertSystemIdInfo(var121);
                        } catch (Throwable var113) {
                           var10000 = var113;
                           var10001 = false;
                           break label974;
                        }
                     }

                     try {
                        this.scheduleInternal(var5, var9);
                        if (VERSION.SDK_INT == 23) {
                           this.scheduleInternal(var5, this.mIdGenerator.nextJobSchedulerIdWithRange(this.mWorkManager.getConfiguration().getMinJobSchedulerId(), this.mWorkManager.getConfiguration().getMaxJobSchedulerId()));
                        }
                     } catch (Throwable var112) {
                        var10000 = var112;
                        var10001 = false;
                        break label974;
                     }

                     label934:
                     try {
                        var2.setTransactionSuccessful();
                     } catch (Throwable var111) {
                        var10000 = var111;
                        var10001 = false;
                        break label934;
                     }
                  }
               }
               break label965;
            }

            Throwable var120 = var10000;
            var2.endTransaction();
            throw var120;
         }

         var2.endTransaction();
      }

   }

   public void scheduleInternal(WorkSpec var1, int var2) {
      JobInfo var3 = this.mSystemJobInfoConverter.convert(var1, var2);
      Logger.get().debug(TAG, String.format("Scheduling work ID %s Job ID %s", var1.id, var2));
      this.mJobScheduler.schedule(var3);
   }
}
