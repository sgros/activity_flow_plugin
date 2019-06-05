package androidx.work.impl.utils;

import androidx.work.Operation;
import androidx.work.WorkInfo;
import androidx.work.impl.OperationImpl;
import androidx.work.impl.Scheduler;
import androidx.work.impl.Schedulers;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpecDao;
import java.util.Iterator;

public abstract class CancelWorkRunnable implements Runnable {
   private final OperationImpl mOperation = new OperationImpl();

   public static CancelWorkRunnable forName(final String var0, final WorkManagerImpl var1, final boolean var2) {
      return new CancelWorkRunnable() {
         void runInternal() {
            WorkDatabase var1x = var1.getWorkDatabase();
            var1x.beginTransaction();

            label149: {
               Throwable var10000;
               label148: {
                  boolean var10001;
                  Iterator var2x;
                  try {
                     var2x = var1x.workSpecDao().getUnfinishedWorkWithName(var0).iterator();
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label148;
                  }

                  while(true) {
                     try {
                        if (var2x.hasNext()) {
                           String var16 = (String)var2x.next();
                           this.cancel(var1, var16);
                           continue;
                        }
                     } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break;
                     }

                     try {
                        var1x.setTransactionSuccessful();
                        break label149;
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }
               }

               Throwable var3 = var10000;
               var1x.endTransaction();
               throw var3;
            }

            var1x.endTransaction();
            if (var2) {
               this.reschedulePendingWorkers(var1);
            }

         }
      };
   }

   public static CancelWorkRunnable forTag(final String var0, final WorkManagerImpl var1) {
      return new CancelWorkRunnable() {
         void runInternal() {
            WorkDatabase var1x = var1.getWorkDatabase();
            var1x.beginTransaction();

            label133: {
               Throwable var10000;
               label132: {
                  boolean var10001;
                  Iterator var2;
                  try {
                     var2 = var1x.workSpecDao().getUnfinishedWorkWithTag(var0).iterator();
                  } catch (Throwable var14) {
                     var10000 = var14;
                     var10001 = false;
                     break label132;
                  }

                  while(true) {
                     try {
                        if (var2.hasNext()) {
                           String var16 = (String)var2.next();
                           this.cancel(var1, var16);
                           continue;
                        }
                     } catch (Throwable var15) {
                        var10000 = var15;
                        var10001 = false;
                        break;
                     }

                     try {
                        var1x.setTransactionSuccessful();
                        break label133;
                     } catch (Throwable var13) {
                        var10000 = var13;
                        var10001 = false;
                        break;
                     }
                  }
               }

               Throwable var3 = var10000;
               var1x.endTransaction();
               throw var3;
            }

            var1x.endTransaction();
            this.reschedulePendingWorkers(var1);
         }
      };
   }

   private void recursivelyCancelWorkAndDependents(WorkDatabase var1, String var2) {
      WorkSpecDao var3 = var1.workSpecDao();
      Iterator var4 = var1.dependencyDao().getDependentWorkIds(var2).iterator();

      while(var4.hasNext()) {
         this.recursivelyCancelWorkAndDependents(var1, (String)var4.next());
      }

      WorkInfo.State var5 = var3.getState(var2);
      if (var5 != WorkInfo.State.SUCCEEDED && var5 != WorkInfo.State.FAILED) {
         var3.setState(WorkInfo.State.CANCELLED, var2);
      }

   }

   void cancel(WorkManagerImpl var1, String var2) {
      this.recursivelyCancelWorkAndDependents(var1.getWorkDatabase(), var2);
      var1.getProcessor().stopAndCancelWork(var2);
      Iterator var3 = var1.getSchedulers().iterator();

      while(var3.hasNext()) {
         ((Scheduler)var3.next()).cancel(var2);
      }

   }

   public Operation getOperation() {
      return this.mOperation;
   }

   void reschedulePendingWorkers(WorkManagerImpl var1) {
      Schedulers.schedule(var1.getConfiguration(), var1.getWorkDatabase(), var1.getSchedulers());
   }

   public void run() {
      try {
         this.runInternal();
         this.mOperation.setState(Operation.SUCCESS);
      } catch (Throwable var2) {
         this.mOperation.setState(new Operation.State.FAILURE(var2));
      }

   }

   abstract void runInternal();
}
