package androidx.work.impl;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.Configuration;
import androidx.work.Data;
import androidx.work.InputMerger;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.WorkerParameters;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTagDao;
import androidx.work.impl.utils.PackageManagerHelper;
import androidx.work.impl.utils.futures.SettableFuture;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

public class WorkerWrapper implements Runnable {
   static final String TAG = Logger.tagWithPrefix("WorkerWrapper");
   private Context mAppContext;
   private Configuration mConfiguration;
   private DependencyDao mDependencyDao;
   private SettableFuture mFuture = SettableFuture.create();
   ListenableFuture mInnerFuture = null;
   private volatile boolean mInterrupted;
   ListenableWorker.Result mResult = ListenableWorker.Result.failure();
   private WorkerParameters.RuntimeExtras mRuntimeExtras;
   private List mSchedulers;
   private List mTags;
   private WorkDatabase mWorkDatabase;
   private String mWorkDescription;
   WorkSpec mWorkSpec;
   private WorkSpecDao mWorkSpecDao;
   private String mWorkSpecId;
   private WorkTagDao mWorkTagDao;
   private TaskExecutor mWorkTaskExecutor;
   ListenableWorker mWorker;

   WorkerWrapper(WorkerWrapper.Builder var1) {
      this.mAppContext = var1.mAppContext;
      this.mWorkTaskExecutor = var1.mWorkTaskExecutor;
      this.mWorkSpecId = var1.mWorkSpecId;
      this.mSchedulers = var1.mSchedulers;
      this.mRuntimeExtras = var1.mRuntimeExtras;
      this.mWorker = var1.mWorker;
      this.mConfiguration = var1.mConfiguration;
      this.mWorkDatabase = var1.mWorkDatabase;
      this.mWorkSpecDao = this.mWorkDatabase.workSpecDao();
      this.mDependencyDao = this.mWorkDatabase.dependencyDao();
      this.mWorkTagDao = this.mWorkDatabase.workTagDao();
   }

   private void assertBackgroundExecutorThread() {
      if (this.mWorkTaskExecutor.getBackgroundExecutorThread() != Thread.currentThread()) {
         throw new IllegalStateException("Needs to be executed on the Background executor thread.");
      }
   }

   private String createWorkDescription(List var1) {
      StringBuilder var2 = new StringBuilder("Work [ id=");
      var2.append(this.mWorkSpecId);
      var2.append(", tags={ ");
      Iterator var3 = var1.iterator();

      String var5;
      for(boolean var4 = true; var3.hasNext(); var2.append(var5)) {
         var5 = (String)var3.next();
         if (var4) {
            var4 = false;
         } else {
            var2.append(", ");
         }
      }

      var2.append(" } ]");
      return var2.toString();
   }

   private void handleResult(ListenableWorker.Result var1) {
      if (var1 instanceof ListenableWorker.Result.Success) {
         Logger.get().info(TAG, String.format("Worker result SUCCESS for %s", this.mWorkDescription));
         if (this.mWorkSpec.isPeriodic()) {
            this.resetPeriodicAndResolve();
         } else {
            this.setSucceededAndResolve();
         }
      } else if (var1 instanceof ListenableWorker.Result.Retry) {
         Logger.get().info(TAG, String.format("Worker result RETRY for %s", this.mWorkDescription));
         this.rescheduleAndResolve();
      } else {
         Logger.get().info(TAG, String.format("Worker result FAILURE for %s", this.mWorkDescription));
         if (this.mWorkSpec.isPeriodic()) {
            this.resetPeriodicAndResolve();
         } else {
            this.setFailedAndResolve();
         }
      }

   }

   private void recursivelyFailWorkAndDependents(String var1) {
      Iterator var2 = this.mDependencyDao.getDependentWorkIds(var1).iterator();

      while(var2.hasNext()) {
         this.recursivelyFailWorkAndDependents((String)var2.next());
      }

      if (this.mWorkSpecDao.getState(var1) != WorkInfo.State.CANCELLED) {
         this.mWorkSpecDao.setState(WorkInfo.State.FAILED, var1);
      }

   }

   private void rescheduleAndResolve() {
      this.mWorkDatabase.beginTransaction();

      try {
         this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
         this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
         if (VERSION.SDK_INT < 23) {
            this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1L);
         }

         this.mWorkDatabase.setTransactionSuccessful();
      } finally {
         this.mWorkDatabase.endTransaction();
         this.resolve(true);
      }

   }

   private void resetPeriodicAndResolve() {
      this.mWorkDatabase.beginTransaction();

      try {
         this.mWorkSpecDao.setPeriodStartTime(this.mWorkSpecId, System.currentTimeMillis());
         this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, this.mWorkSpecId);
         this.mWorkSpecDao.resetWorkSpecRunAttemptCount(this.mWorkSpecId);
         if (VERSION.SDK_INT < 23) {
            this.mWorkSpecDao.markWorkSpecScheduled(this.mWorkSpecId, -1L);
         }

         this.mWorkDatabase.setTransactionSuccessful();
      } finally {
         this.mWorkDatabase.endTransaction();
         this.resolve(false);
      }

   }

   private void resolve(boolean var1) {
      label199: {
         Throwable var10000;
         label201: {
            boolean var10001;
            List var2;
            try {
               this.mWorkDatabase.beginTransaction();
               var2 = this.mWorkDatabase.workSpecDao().getAllUnfinishedWork();
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label201;
            }

            boolean var3;
            label194: {
               label193: {
                  if (var2 != null) {
                     try {
                        if (!var2.isEmpty()) {
                           break label193;
                        }
                     } catch (Throwable var22) {
                        var10000 = var22;
                        var10001 = false;
                        break label201;
                     }
                  }

                  var3 = true;
                  break label194;
               }

               var3 = false;
            }

            if (var3) {
               try {
                  PackageManagerHelper.setComponentEnabled(this.mAppContext, RescheduleReceiver.class, false);
               } catch (Throwable var21) {
                  var10000 = var21;
                  var10001 = false;
                  break label201;
               }
            }

            label182:
            try {
               this.mWorkDatabase.setTransactionSuccessful();
               break label199;
            } catch (Throwable var20) {
               var10000 = var20;
               var10001 = false;
               break label182;
            }
         }

         Throwable var24 = var10000;
         this.mWorkDatabase.endTransaction();
         throw var24;
      }

      this.mWorkDatabase.endTransaction();
      this.mFuture.set(var1);
   }

   private void resolveIncorrectStatus() {
      WorkInfo.State var1 = this.mWorkSpecDao.getState(this.mWorkSpecId);
      if (var1 == WorkInfo.State.RUNNING) {
         Logger.get().debug(TAG, String.format("Status for %s is RUNNING;not doing any work and rescheduling for later execution", this.mWorkSpecId));
         this.resolve(true);
      } else {
         Logger.get().debug(TAG, String.format("Status for %s is %s; not doing any work", this.mWorkSpecId, var1));
         this.resolve(false);
      }

   }

   private void runWorker() {
      if (!this.tryCheckForInterruptionAndResolve()) {
         this.mWorkDatabase.beginTransaction();

         label763: {
            label762: {
               label761: {
                  Throwable var10000;
                  label769: {
                     boolean var10001;
                     try {
                        this.mWorkSpec = this.mWorkSpecDao.getWorkSpec(this.mWorkSpecId);
                        if (this.mWorkSpec == null) {
                           Logger.get().error(TAG, String.format("Didn't find WorkSpec for id %s", this.mWorkSpecId));
                           this.resolve(false);
                           break label762;
                        }
                     } catch (Throwable var45) {
                        var10000 = var45;
                        var10001 = false;
                        break label769;
                     }

                     try {
                        if (this.mWorkSpec.state != WorkInfo.State.ENQUEUED) {
                           this.resolveIncorrectStatus();
                           this.mWorkDatabase.setTransactionSuccessful();
                           Logger.get().debug(TAG, String.format("%s is not in ENQUEUED state. Nothing more to do.", this.mWorkSpec.workerClassName));
                           break label761;
                        }
                     } catch (Throwable var44) {
                        var10000 = var44;
                        var10001 = false;
                        break label769;
                     }

                     label770: {
                        try {
                           if (!this.mWorkSpec.isPeriodic() && !this.mWorkSpec.isBackedOff()) {
                              break label770;
                           }
                        } catch (Throwable var47) {
                           var10000 = var47;
                           var10001 = false;
                           break label769;
                        }

                        long var1;
                        boolean var3;
                        label750: {
                           label749: {
                              try {
                                 var1 = System.currentTimeMillis();
                                 if (VERSION.SDK_INT >= 23 || this.mWorkSpec.intervalDuration == this.mWorkSpec.flexDuration || this.mWorkSpec.periodStartTime != 0L) {
                                    break label749;
                                 }
                              } catch (Throwable var46) {
                                 var10000 = var46;
                                 var10001 = false;
                                 break label769;
                              }

                              var3 = true;
                              break label750;
                           }

                           var3 = false;
                        }

                        if (!var3) {
                           try {
                              if (var1 < this.mWorkSpec.calculateNextRunTime()) {
                                 Logger.get().debug(TAG, String.format("Delaying execution for %s because it is being executed before schedule.", this.mWorkSpec.workerClassName));
                                 this.resolve(true);
                                 break label763;
                              }
                           } catch (Throwable var43) {
                              var10000 = var43;
                              var10001 = false;
                              break label769;
                           }
                        }
                     }

                     try {
                        this.mWorkDatabase.setTransactionSuccessful();
                     } catch (Throwable var42) {
                        var10000 = var42;
                        var10001 = false;
                        break label769;
                     }

                     this.mWorkDatabase.endTransaction();
                     Data var48;
                     if (this.mWorkSpec.isPeriodic()) {
                        var48 = this.mWorkSpec.input;
                     } else {
                        InputMerger var49 = InputMerger.fromClassName(this.mWorkSpec.inputMergerClassName);
                        if (var49 == null) {
                           Logger.get().error(TAG, String.format("Could not create Input Merger %s", this.mWorkSpec.inputMergerClassName));
                           this.setFailedAndResolve();
                           return;
                        }

                        ArrayList var5 = new ArrayList();
                        var5.add(this.mWorkSpec.input);
                        var5.addAll(this.mWorkSpecDao.getInputsFromPrerequisites(this.mWorkSpecId));
                        var48 = var49.merge(var5);
                     }

                     WorkerParameters var50 = new WorkerParameters(UUID.fromString(this.mWorkSpecId), var48, this.mTags, this.mRuntimeExtras, this.mWorkSpec.runAttemptCount, this.mConfiguration.getExecutor(), this.mWorkTaskExecutor, this.mConfiguration.getWorkerFactory());
                     if (this.mWorker == null) {
                        this.mWorker = this.mConfiguration.getWorkerFactory().createWorkerWithDefaultFallback(this.mAppContext, this.mWorkSpec.workerClassName, var50);
                     }

                     if (this.mWorker == null) {
                        Logger.get().error(TAG, String.format("Could not create Worker %s", this.mWorkSpec.workerClassName));
                        this.setFailedAndResolve();
                        return;
                     }

                     if (this.mWorker.isUsed()) {
                        Logger.get().error(TAG, String.format("Received an already-used Worker %s; WorkerFactory should return new instances", this.mWorkSpec.workerClassName));
                        this.setFailedAndResolve();
                        return;
                     }

                     this.mWorker.setUsed();
                     if (this.trySetRunning()) {
                        if (this.tryCheckForInterruptionAndResolve()) {
                           return;
                        }

                        final SettableFuture var51 = SettableFuture.create();
                        this.mWorkTaskExecutor.getMainThreadExecutor().execute(new Runnable() {
                           public void run() {
                              try {
                                 Logger.get().debug(WorkerWrapper.TAG, String.format("Starting work for %s", WorkerWrapper.this.mWorkSpec.workerClassName));
                                 WorkerWrapper.this.mInnerFuture = WorkerWrapper.this.mWorker.startWork();
                                 var51.setFuture(WorkerWrapper.this.mInnerFuture);
                              } catch (Throwable var2) {
                                 var51.setException(var2);
                              }

                           }
                        });
                        var51.addListener(new Runnable(this.mWorkDescription) {
                           // $FF: synthetic field
                           final String val$workDescription;

                           {
                              this.val$workDescription = var3;
                           }

                           @SuppressLint({"SyntheticAccessor"})
                           public void run() {
                              // $FF: Couldn't be decompiled
                           }
                        }, this.mWorkTaskExecutor.getBackgroundExecutor());
                     } else {
                        this.resolveIncorrectStatus();
                     }

                     return;
                  }

                  Throwable var4 = var10000;
                  this.mWorkDatabase.endTransaction();
                  throw var4;
               }

               this.mWorkDatabase.endTransaction();
               return;
            }

            this.mWorkDatabase.endTransaction();
            return;
         }

         this.mWorkDatabase.endTransaction();
      }
   }

   private void setFailedAndResolve() {
      this.mWorkDatabase.beginTransaction();

      try {
         this.recursivelyFailWorkAndDependents(this.mWorkSpecId);
         Data var1 = ((ListenableWorker.Result.Failure)this.mResult).getOutputData();
         this.mWorkSpecDao.setOutput(this.mWorkSpecId, var1);
         this.mWorkDatabase.setTransactionSuccessful();
      } finally {
         this.mWorkDatabase.endTransaction();
         this.resolve(false);
      }

   }

   private void setSucceededAndResolve() {
      this.mWorkDatabase.beginTransaction();

      label161: {
         Throwable var10000;
         label165: {
            boolean var10001;
            long var2;
            Iterator var4;
            try {
               this.mWorkSpecDao.setState(WorkInfo.State.SUCCEEDED, this.mWorkSpecId);
               Data var1 = ((ListenableWorker.Result.Success)this.mResult).getOutputData();
               this.mWorkSpecDao.setOutput(this.mWorkSpecId, var1);
               var2 = System.currentTimeMillis();
               var4 = this.mDependencyDao.getDependentWorkIds(this.mWorkSpecId).iterator();
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label165;
            }

            while(true) {
               try {
                  if (!var4.hasNext()) {
                     break;
                  }

                  String var17 = (String)var4.next();
                  if (this.mWorkSpecDao.getState(var17) == WorkInfo.State.BLOCKED && this.mDependencyDao.hasCompletedAllPrerequisites(var17)) {
                     Logger.get().info(TAG, String.format("Setting status to enqueued for %s", var17));
                     this.mWorkSpecDao.setState(WorkInfo.State.ENQUEUED, var17);
                     this.mWorkSpecDao.setPeriodStartTime(var17, var2);
                  }
               } catch (Throwable var16) {
                  var10000 = var16;
                  var10001 = false;
                  break label165;
               }
            }

            label145:
            try {
               this.mWorkDatabase.setTransactionSuccessful();
               break label161;
            } catch (Throwable var14) {
               var10000 = var14;
               var10001 = false;
               break label145;
            }
         }

         Throwable var18 = var10000;
         this.mWorkDatabase.endTransaction();
         this.resolve(false);
         throw var18;
      }

      this.mWorkDatabase.endTransaction();
      this.resolve(false);
   }

   private boolean tryCheckForInterruptionAndResolve() {
      if (this.mInterrupted) {
         Logger.get().debug(TAG, String.format("Work interrupted for %s", this.mWorkDescription));
         WorkInfo.State var1 = this.mWorkSpecDao.getState(this.mWorkSpecId);
         if (var1 == null) {
            this.resolve(false);
         } else {
            this.resolve(var1.isFinished() ^ true);
         }

         return true;
      } else {
         return false;
      }
   }

   private boolean trySetRunning() {
      this.mWorkDatabase.beginTransaction();

      boolean var3;
      label124: {
         Throwable var10000;
         label128: {
            WorkInfo.State var1;
            boolean var10001;
            WorkInfo.State var2;
            try {
               var1 = this.mWorkSpecDao.getState(this.mWorkSpecId);
               var2 = WorkInfo.State.ENQUEUED;
            } catch (Throwable var15) {
               var10000 = var15;
               var10001 = false;
               break label128;
            }

            var3 = true;
            if (var1 == var2) {
               try {
                  this.mWorkSpecDao.setState(WorkInfo.State.RUNNING, this.mWorkSpecId);
                  this.mWorkSpecDao.incrementWorkSpecRunAttemptCount(this.mWorkSpecId);
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label128;
               }
            } else {
               var3 = false;
            }

            label115:
            try {
               this.mWorkDatabase.setTransactionSuccessful();
               break label124;
            } catch (Throwable var13) {
               var10000 = var13;
               var10001 = false;
               break label115;
            }
         }

         Throwable var16 = var10000;
         this.mWorkDatabase.endTransaction();
         throw var16;
      }

      this.mWorkDatabase.endTransaction();
      return var3;
   }

   public ListenableFuture getFuture() {
      return this.mFuture;
   }

   public void interrupt(boolean var1) {
      this.mInterrupted = true;
      this.tryCheckForInterruptionAndResolve();
      if (this.mInnerFuture != null) {
         this.mInnerFuture.cancel(true);
      }

      if (this.mWorker != null) {
         this.mWorker.stop();
      }

   }

   void onWorkFinished() {
      this.assertBackgroundExecutorThread();
      boolean var1 = this.tryCheckForInterruptionAndResolve();
      boolean var2 = false;
      boolean var3 = false;
      if (!var1) {
         label458: {
            Throwable var10000;
            label464: {
               WorkInfo.State var4;
               boolean var10001;
               try {
                  this.mWorkDatabase.beginTransaction();
                  var4 = this.mWorkSpecDao.getState(this.mWorkSpecId);
               } catch (Throwable var34) {
                  var10000 = var34;
                  var10001 = false;
                  break label464;
               }

               if (var4 == null) {
                  try {
                     this.resolve(false);
                  } catch (Throwable var31) {
                     var10000 = var31;
                     var10001 = false;
                     break label464;
                  }

                  var2 = true;
               } else {
                  label465: {
                     try {
                        if (var4 == WorkInfo.State.RUNNING) {
                           this.handleResult(this.mResult);
                           var2 = this.mWorkSpecDao.getState(this.mWorkSpecId).isFinished();
                           break label465;
                        }
                     } catch (Throwable var33) {
                        var10000 = var33;
                        var10001 = false;
                        break label464;
                     }

                     var2 = var3;

                     try {
                        if (var4.isFinished()) {
                           break label465;
                        }

                        this.rescheduleAndResolve();
                     } catch (Throwable var32) {
                        var10000 = var32;
                        var10001 = false;
                        break label464;
                     }

                     var2 = var3;
                  }
               }

               label435:
               try {
                  this.mWorkDatabase.setTransactionSuccessful();
                  break label458;
               } catch (Throwable var30) {
                  var10000 = var30;
                  var10001 = false;
                  break label435;
               }
            }

            Throwable var35 = var10000;
            this.mWorkDatabase.endTransaction();
            throw var35;
         }

         this.mWorkDatabase.endTransaction();
      }

      if (this.mSchedulers != null) {
         if (var2) {
            Iterator var36 = this.mSchedulers.iterator();

            while(var36.hasNext()) {
               ((Scheduler)var36.next()).cancel(this.mWorkSpecId);
            }
         }

         Schedulers.schedule(this.mConfiguration, this.mWorkDatabase, this.mSchedulers);
      }

   }

   public void run() {
      this.mTags = this.mWorkTagDao.getTagsForWorkSpecId(this.mWorkSpecId);
      this.mWorkDescription = this.createWorkDescription(this.mTags);
      this.runWorker();
   }

   public static class Builder {
      Context mAppContext;
      Configuration mConfiguration;
      WorkerParameters.RuntimeExtras mRuntimeExtras = new WorkerParameters.RuntimeExtras();
      List mSchedulers;
      WorkDatabase mWorkDatabase;
      String mWorkSpecId;
      TaskExecutor mWorkTaskExecutor;
      ListenableWorker mWorker;

      public Builder(Context var1, Configuration var2, TaskExecutor var3, WorkDatabase var4, String var5) {
         this.mAppContext = var1.getApplicationContext();
         this.mWorkTaskExecutor = var3;
         this.mConfiguration = var2;
         this.mWorkDatabase = var4;
         this.mWorkSpecId = var5;
      }

      public WorkerWrapper build() {
         return new WorkerWrapper(this);
      }

      public WorkerWrapper.Builder withRuntimeExtras(WorkerParameters.RuntimeExtras var1) {
         if (var1 != null) {
            this.mRuntimeExtras = var1;
         }

         return this;
      }

      public WorkerWrapper.Builder withSchedulers(List var1) {
         this.mSchedulers = var1;
         return this;
      }
   }
}
