package androidx.work.impl.background.systemjob;

import android.app.Application;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.WorkManagerImpl;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class SystemJobService extends JobService implements ExecutionListener {
   private static final String TAG = Logger.tagWithPrefix("SystemJobService");
   private final Map mJobParameters = new HashMap();
   private WorkManagerImpl mWorkManagerImpl;

   public void onCreate() {
      super.onCreate();
      this.mWorkManagerImpl = WorkManagerImpl.getInstance();
      if (this.mWorkManagerImpl == null) {
         if (!Application.class.equals(this.getApplication().getClass())) {
            throw new IllegalStateException("WorkManager needs to be initialized via a ContentProvider#onCreate() or an Application#onCreate().");
         }

         Logger.get().warning(TAG, "Could not find WorkManager instance; this may be because an auto-backup is in progress. Ignoring JobScheduler commands for now. Please make sure that you are initializing WorkManager if you have manually disabled WorkManagerInitializer.");
      } else {
         this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
      }

   }

   public void onDestroy() {
      super.onDestroy();
      if (this.mWorkManagerImpl != null) {
         this.mWorkManagerImpl.getProcessor().removeExecutionListener(this);
      }

   }

   public void onExecuted(String param1, boolean param2) {
      // $FF: Couldn't be decompiled
   }

   public boolean onStartJob(JobParameters var1) {
      if (this.mWorkManagerImpl == null) {
         Logger.get().debug(TAG, "WorkManager is not initialized; requesting retry.");
         this.jobFinished(var1, true);
         return false;
      } else {
         String var2 = var1.getExtras().getString("EXTRA_WORK_SPEC_ID");
         if (TextUtils.isEmpty(var2)) {
            Logger.get().error(TAG, "WorkSpec id not found!");
            return false;
         } else {
            Map var3 = this.mJobParameters;
            synchronized(var3){}

            Throwable var10000;
            boolean var10001;
            label228: {
               try {
                  if (this.mJobParameters.containsKey(var2)) {
                     Logger.get().debug(TAG, String.format("Job is already being executed by SystemJobService: %s", var2));
                     return false;
                  }
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label228;
               }

               try {
                  Logger.get().debug(TAG, String.format("onStartJob for %s", var2));
                  this.mJobParameters.put(var2, var1);
               } catch (Throwable var15) {
                  var10000 = var15;
                  var10001 = false;
                  break label228;
               }

               WorkerParameters.RuntimeExtras var18 = null;
               if (VERSION.SDK_INT >= 24) {
                  WorkerParameters.RuntimeExtras var4 = new WorkerParameters.RuntimeExtras();
                  if (var1.getTriggeredContentUris() != null) {
                     var4.triggeredContentUris = Arrays.asList(var1.getTriggeredContentUris());
                  }

                  if (var1.getTriggeredContentAuthorities() != null) {
                     var4.triggeredContentAuthorities = Arrays.asList(var1.getTriggeredContentAuthorities());
                  }

                  var18 = var4;
                  if (VERSION.SDK_INT >= 28) {
                     var4.network = var1.getNetwork();
                     var18 = var4;
                  }
               }

               this.mWorkManagerImpl.startWork(var2, var18);
               return true;
            }

            while(true) {
               Throwable var17 = var10000;

               try {
                  throw var17;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  continue;
               }
            }
         }
      }
   }

   public boolean onStopJob(JobParameters param1) {
      // $FF: Couldn't be decompiled
   }
}
