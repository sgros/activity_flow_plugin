package androidx.work.impl.utils;

import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.work.Constraints;
import androidx.work.Data;
import androidx.work.ExistingWorkPolicy;
import androidx.work.Logger;
import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.WorkRequest;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkContinuationImpl;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.background.systemalarm.RescheduleReceiver;
import androidx.work.impl.model.Dependency;
import androidx.work.impl.model.DependencyDao;
import androidx.work.impl.model.WorkName;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.model.WorkSpecDao;
import androidx.work.impl.model.WorkTag;
import androidx.work.impl.workers.ConstraintTrackingWorker;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class EnqueueRunnable implements Runnable {
   private static final String TAG = Logger.tagWithPrefix("EnqueueRunnable");
   private final OperationImpl mOperation;
   private final WorkContinuationImpl mWorkContinuation;

   public EnqueueRunnable(WorkContinuationImpl var1) {
      this.mWorkContinuation = var1;
      this.mOperation = new OperationImpl();
   }

   private static boolean enqueueContinuation(WorkContinuationImpl var0) {
      Set var1 = WorkContinuationImpl.prerequisitesFor(var0);
      boolean var2 = enqueueWorkWithPrerequisites(var0.getWorkManagerImpl(), var0.getWork(), (String[])var1.toArray(new String[0]), var0.getName(), var0.getExistingWorkPolicy());
      var0.markEnqueued();
      return var2;
   }

   private static boolean enqueueWorkWithPrerequisites(WorkManagerImpl var0, List var1, String[] var2, String var3, ExistingWorkPolicy var4) {
      long var5 = System.currentTimeMillis();
      WorkDatabase var7 = var0.getWorkDatabase();
      boolean var8;
      if (var2 != null && var2.length > 0) {
         var8 = true;
      } else {
         var8 = false;
      }

      boolean var11;
      boolean var12;
      boolean var13;
      boolean var14;
      boolean var15;
      boolean var16;
      String var17;
      if (var8) {
         int var9 = var2.length;
         int var10 = 0;
         var11 = true;
         var12 = false;
         var13 = false;

         while(true) {
            var14 = var11;
            var15 = var12;
            var16 = var13;
            if (var10 >= var9) {
               break;
            }

            var17 = var2[var10];
            WorkSpec var18 = var7.workSpecDao().getWorkSpec(var17);
            if (var18 == null) {
               Logger.get().error(TAG, String.format("Prerequisite %s doesn't exist; not enqueuing", var17));
               return false;
            }

            WorkInfo.State var37 = var18.state;
            if (var37 == WorkInfo.State.SUCCEEDED) {
               var16 = true;
            } else {
               var16 = false;
            }

            var11 &= var16;
            if (var37 == WorkInfo.State.FAILED) {
               var16 = true;
            } else {
               var16 = var12;
               if (var37 == WorkInfo.State.CANCELLED) {
                  var13 = true;
                  var16 = var12;
               }
            }

            ++var10;
            var12 = var16;
         }
      } else {
         var14 = true;
         var15 = false;
         var16 = false;
      }

      boolean var19 = TextUtils.isEmpty(var3) ^ true;
      boolean var34;
      if (var19 && !var8) {
         var34 = true;
      } else {
         var34 = false;
      }

      boolean var20;
      String[] var22;
      label166: {
         String[] var39 = var2;
         var13 = var8;
         var12 = var14;
         var11 = var15;
         boolean var35 = var16;
         if (var34) {
            List var40 = var7.workSpecDao().getWorkSpecIdAndStatesForName(var3);
            var39 = var2;
            var13 = var8;
            var12 = var14;
            var11 = var15;
            var35 = var16;
            if (!var40.isEmpty()) {
               if (var4 != ExistingWorkPolicy.APPEND) {
                  if (var4 == ExistingWorkPolicy.KEEP) {
                     label195: {
                        Iterator var43 = var40.iterator();

                        WorkSpec.IdAndState var31;
                        do {
                           if (!var43.hasNext()) {
                              break label195;
                           }

                           var31 = (WorkSpec.IdAndState)var43.next();
                        } while(var31.state != WorkInfo.State.ENQUEUED && var31.state != WorkInfo.State.RUNNING);

                        return false;
                     }
                  }

                  CancelWorkRunnable.forName(var3, var0, false).run();
                  WorkSpecDao var23 = var7.workSpecDao();
                  Iterator var32 = var40.iterator();

                  while(var32.hasNext()) {
                     var23.delete(((WorkSpec.IdAndState)var32.next()).id);
                  }

                  var20 = true;
                  var22 = var2;
                  break label166;
               }

               DependencyDao var30 = var7.dependencyDao();
               ArrayList var21 = new ArrayList();

               for(Iterator var42 = var40.iterator(); var42.hasNext(); var16 = var13) {
                  WorkSpec.IdAndState var41 = (WorkSpec.IdAndState)var42.next();
                  var12 = var14;
                  var8 = var15;
                  var13 = var16;
                  if (!var30.hasDependents(var41.id)) {
                     if (var41.state == WorkInfo.State.SUCCEEDED) {
                        var13 = true;
                     } else {
                        var13 = false;
                     }

                     if (var41.state == WorkInfo.State.FAILED) {
                        var8 = true;
                     } else {
                        var8 = var15;
                        if (var41.state == WorkInfo.State.CANCELLED) {
                           var16 = true;
                           var8 = var15;
                        }
                     }

                     var21.add(var41.id);
                     var12 = var13 & var14;
                     var13 = var16;
                  }

                  var14 = var12;
                  var15 = var8;
               }

               var39 = (String[])var21.toArray(var2);
               if (var39.length > 0) {
                  var13 = true;
                  var12 = var14;
                  var11 = var15;
                  var35 = var16;
               } else {
                  var13 = false;
                  var12 = var14;
                  var11 = var15;
                  var35 = var16;
               }
            }
         }

         var20 = false;
         var16 = var35;
         var15 = var11;
         var14 = var12;
         var8 = var13;
         var22 = var39;
      }

      String[] var25;
      for(Iterator var27 = var1.iterator(); var27.hasNext(); var22 = var25) {
         WorkRequest var33 = (WorkRequest)var27.next();
         WorkSpec var24 = var33.getWorkSpec();
         if (var8 && !var14) {
            if (var15) {
               var24.state = WorkInfo.State.FAILED;
            } else if (var16) {
               var24.state = WorkInfo.State.CANCELLED;
            } else {
               var24.state = WorkInfo.State.BLOCKED;
            }
         } else if (!var24.isPeriodic()) {
            var24.periodStartTime = var5;
         } else {
            var24.periodStartTime = 0L;
         }

         if (VERSION.SDK_INT >= 23 && VERSION.SDK_INT <= 25) {
            tryDelegateConstrainedWorkSpec(var24);
         }

         if (var24.state == WorkInfo.State.ENQUEUED) {
            var20 = true;
         }

         var7.workSpecDao().insertWorkSpec(var24);
         var25 = var22;
         if (var8) {
            int var36 = var22.length;
            int var38 = 0;

            while(true) {
               var25 = var22;
               if (var38 >= var36) {
                  break;
               }

               String var28 = var22[var38];
               Dependency var29 = new Dependency(var33.getStringId(), var28);
               var7.dependencyDao().insertDependency(var29);
               ++var38;
            }
         }

         Iterator var26 = var33.getTags().iterator();

         while(var26.hasNext()) {
            var17 = (String)var26.next();
            var7.workTagDao().insert(new WorkTag(var17, var33.getStringId()));
         }

         if (var19) {
            var7.workNameDao().insert(new WorkName(var3, var33.getStringId()));
         }
      }

      return var20;
   }

   private static boolean processContinuation(WorkContinuationImpl var0) {
      List var1 = var0.getParents();
      boolean var2 = false;
      if (var1 != null) {
         Iterator var3 = var1.iterator();
         var2 = false;

         while(var3.hasNext()) {
            WorkContinuationImpl var4 = (WorkContinuationImpl)var3.next();
            if (!var4.isEnqueued()) {
               var2 |= processContinuation(var4);
            } else {
               Logger.get().warning(TAG, String.format("Already enqueued work ids (%s).", TextUtils.join(", ", var4.getIds())));
            }
         }
      }

      return enqueueContinuation(var0) | var2;
   }

   private static void tryDelegateConstrainedWorkSpec(WorkSpec var0) {
      Constraints var1 = var0.constraints;
      if (var1.requiresBatteryNotLow() || var1.requiresStorageNotLow()) {
         String var2 = var0.workerClassName;
         Data.Builder var3 = new Data.Builder();
         var3.putAll(var0.input).putString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME", var2);
         var0.workerClassName = ConstraintTrackingWorker.class.getName();
         var0.input = var3.build();
      }

   }

   public boolean addToDatabase() {
      WorkDatabase var1 = this.mWorkContinuation.getWorkManagerImpl().getWorkDatabase();
      var1.beginTransaction();

      boolean var2;
      try {
         var2 = processContinuation(this.mWorkContinuation);
         var1.setTransactionSuccessful();
      } finally {
         var1.endTransaction();
      }

      return var2;
   }

   public Operation getOperation() {
      return this.mOperation;
   }

   public void run() {
      try {
         if (this.mWorkContinuation.hasCycles()) {
            IllegalStateException var1 = new IllegalStateException(String.format("WorkContinuation has cycles (%s)", this.mWorkContinuation));
            throw var1;
         }

         if (this.addToDatabase()) {
            PackageManagerHelper.setComponentEnabled(this.mWorkContinuation.getWorkManagerImpl().getApplicationContext(), RescheduleReceiver.class, true);
            this.scheduleWorkInBackground();
         }

         this.mOperation.setState(Operation.SUCCESS);
      } catch (Throwable var2) {
         this.mOperation.setState(new Operation.State.FAILURE(var2));
      }

   }

   public void scheduleWorkInBackground() {
      WorkManagerImpl var1 = this.mWorkContinuation.getWorkManagerImpl();
      Schedulers.schedule(var1.getConfiguration(), var1.getWorkDatabase(), var1.getSchedulers());
   }
}
