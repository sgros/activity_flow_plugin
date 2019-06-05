package androidx.work.impl;

import android.content.Context;
import android.content.BroadcastReceiver.PendingResult;
import android.os.Build.VERSION;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.Operation;
import androidx.work.R;
import androidx.work.WorkManager;
import androidx.work.WorkerParameters;
import androidx.work.impl.background.greedy.GreedyScheduler;
import androidx.work.impl.background.systemjob.SystemJobScheduler;
import androidx.work.impl.utils.CancelWorkRunnable;
import androidx.work.impl.utils.ForceStopRunnable;
import androidx.work.impl.utils.Preferences;
import androidx.work.impl.utils.StartWorkRunnable;
import androidx.work.impl.utils.StatusRunnable;
import androidx.work.impl.utils.StopWorkRunnable;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import androidx.work.impl.utils.taskexecutor.WorkManagerTaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Arrays;
import java.util.List;

public class WorkManagerImpl extends WorkManager {
   private static WorkManagerImpl sDefaultInstance;
   private static WorkManagerImpl sDelegatedInstance;
   private static final Object sLock = new Object();
   private Configuration mConfiguration;
   private Context mContext;
   private boolean mForceStopRunnableCompleted;
   private final WorkManagerLiveDataTracker mLiveDataTracker;
   private Preferences mPreferences;
   private Processor mProcessor;
   private PendingResult mRescheduleReceiverResult;
   private List mSchedulers;
   private WorkDatabase mWorkDatabase;
   private TaskExecutor mWorkTaskExecutor;

   public WorkManagerImpl(Context var1, Configuration var2, TaskExecutor var3) {
      this(var1, var2, var3, var1.getResources().getBoolean(R.bool.workmanager_test_configuration));
   }

   public WorkManagerImpl(Context var1, Configuration var2, TaskExecutor var3, boolean var4) {
      this.mLiveDataTracker = new WorkManagerLiveDataTracker();
      Context var5 = var1.getApplicationContext();
      WorkDatabase var6 = WorkDatabase.create(var5, var4);
      Logger.setLogger(new Logger.LogcatLogger(var2.getMinimumLoggingLevel()));
      List var7 = this.createSchedulers(var5);
      this.internalInit(var1, var2, var3, var6, var7, new Processor(var1, var2, var3, var6, var7));
   }

   public static WorkManagerImpl getInstance() {
      Object var0 = sLock;
      synchronized(var0){}

      Throwable var10000;
      boolean var10001;
      label123: {
         WorkManagerImpl var14;
         try {
            if (sDelegatedInstance != null) {
               var14 = sDelegatedInstance;
               return var14;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label123;
         }

         label117:
         try {
            var14 = sDefaultInstance;
            return var14;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label117;
         }
      }

      while(true) {
         Throwable var1 = var10000;

         try {
            throw var1;
         } catch (Throwable var11) {
            var10000 = var11;
            var10001 = false;
            continue;
         }
      }
   }

   public static void initialize(Context var0, Configuration var1) {
      Object var2 = sLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label409: {
         label415: {
            try {
               if (sDelegatedInstance != null && sDefaultInstance != null) {
                  break label415;
               }
            } catch (Throwable var46) {
               var10000 = var46;
               var10001 = false;
               break label409;
            }

            label398: {
               try {
                  if (sDelegatedInstance != null) {
                     break label398;
                  }

                  Context var3 = var0.getApplicationContext();
                  if (sDefaultInstance == null) {
                     WorkManagerTaskExecutor var4 = new WorkManagerTaskExecutor();
                     WorkManagerImpl var47 = new WorkManagerImpl(var3, var1, var4);
                     sDefaultInstance = var47;
                  }
               } catch (Throwable var45) {
                  var10000 = var45;
                  var10001 = false;
                  break label409;
               }

               try {
                  sDelegatedInstance = sDefaultInstance;
               } catch (Throwable var44) {
                  var10000 = var44;
                  var10001 = false;
                  break label409;
               }
            }

            try {
               return;
            } catch (Throwable var43) {
               var10000 = var43;
               var10001 = false;
               break label409;
            }
         }

         label389:
         try {
            IllegalStateException var49 = new IllegalStateException("WorkManager is already initialized.  Did you try to initialize it manually without disabling WorkManagerInitializer? See WorkManager#initialize(Context, Configuration) or the class levelJavadoc for more information.");
            throw var49;
         } catch (Throwable var42) {
            var10000 = var42;
            var10001 = false;
            break label389;
         }
      }

      while(true) {
         Throwable var48 = var10000;

         try {
            throw var48;
         } catch (Throwable var41) {
            var10000 = var41;
            var10001 = false;
            continue;
         }
      }
   }

   private void internalInit(Context var1, Configuration var2, TaskExecutor var3, WorkDatabase var4, List var5, Processor var6) {
      var1 = var1.getApplicationContext();
      this.mContext = var1;
      this.mConfiguration = var2;
      this.mWorkTaskExecutor = var3;
      this.mWorkDatabase = var4;
      this.mSchedulers = var5;
      this.mProcessor = var6;
      this.mPreferences = new Preferences(this.mContext);
      this.mForceStopRunnableCompleted = false;
      this.mWorkTaskExecutor.executeOnBackgroundThread(new ForceStopRunnable(var1, this));
   }

   public Operation cancelAllWorkByTag(String var1) {
      CancelWorkRunnable var2 = CancelWorkRunnable.forTag(var1, this);
      this.mWorkTaskExecutor.executeOnBackgroundThread(var2);
      return var2.getOperation();
   }

   public List createSchedulers(Context var1) {
      return Arrays.asList(Schedulers.createBestAvailableBackgroundScheduler(var1, this), new GreedyScheduler(var1, this));
   }

   public Operation enqueue(List var1) {
      if (!var1.isEmpty()) {
         return (new WorkContinuationImpl(this, var1)).enqueue();
      } else {
         throw new IllegalArgumentException("enqueue needs at least one WorkRequest.");
      }
   }

   public Context getApplicationContext() {
      return this.mContext;
   }

   public Configuration getConfiguration() {
      return this.mConfiguration;
   }

   public Preferences getPreferences() {
      return this.mPreferences;
   }

   public Processor getProcessor() {
      return this.mProcessor;
   }

   public List getSchedulers() {
      return this.mSchedulers;
   }

   public WorkDatabase getWorkDatabase() {
      return this.mWorkDatabase;
   }

   public ListenableFuture getWorkInfosByTag(String var1) {
      StatusRunnable var2 = StatusRunnable.forTag(this, var1);
      this.mWorkTaskExecutor.getBackgroundExecutor().execute(var2);
      return var2.getFuture();
   }

   public TaskExecutor getWorkTaskExecutor() {
      return this.mWorkTaskExecutor;
   }

   public void onForceStopRunnableCompleted() {
      Object var1 = sLock;
      synchronized(var1){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.mForceStopRunnableCompleted = true;
            if (this.mRescheduleReceiverResult != null) {
               this.mRescheduleReceiverResult.finish();
               this.mRescheduleReceiverResult = null;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var2 = var10000;

         try {
            throw var2;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void rescheduleEligibleWork() {
      if (VERSION.SDK_INT >= 23) {
         SystemJobScheduler.jobSchedulerCancelAll(this.getApplicationContext());
      }

      this.getWorkDatabase().workSpecDao().resetScheduledState();
      Schedulers.schedule(this.getConfiguration(), this.getWorkDatabase(), this.getSchedulers());
   }

   public void setReschedulePendingResult(PendingResult var1) {
      Object var2 = sLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label122: {
         try {
            this.mRescheduleReceiverResult = var1;
            if (this.mForceStopRunnableCompleted) {
               this.mRescheduleReceiverResult.finish();
               this.mRescheduleReceiverResult = null;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label122;
         }

         label119:
         try {
            return;
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label119;
         }
      }

      while(true) {
         Throwable var15 = var10000;

         try {
            throw var15;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            continue;
         }
      }
   }

   public void startWork(String var1) {
      this.startWork(var1, (WorkerParameters.RuntimeExtras)null);
   }

   public void startWork(String var1, WorkerParameters.RuntimeExtras var2) {
      this.mWorkTaskExecutor.executeOnBackgroundThread(new StartWorkRunnable(this, var1, var2));
   }

   public void stopWork(String var1) {
      this.mWorkTaskExecutor.executeOnBackgroundThread(new StopWorkRunnable(this, var1));
   }
}
