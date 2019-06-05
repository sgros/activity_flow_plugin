package androidx.work.impl.workers;

import android.content.Context;
import android.text.TextUtils;
import androidx.work.ListenableWorker;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.WorkDatabase;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;

public class ConstraintTrackingWorker extends ListenableWorker implements WorkConstraintsCallback {
   private static final String TAG = Logger.tagWithPrefix("ConstraintTrkngWrkr");
   volatile boolean mAreConstraintsUnmet;
   private ListenableWorker mDelegate;
   SettableFuture mFuture;
   final Object mLock;
   private WorkerParameters mWorkerParameters;

   public ConstraintTrackingWorker(Context var1, WorkerParameters var2) {
      super(var1, var2);
      this.mWorkerParameters = var2;
      this.mLock = new Object();
      this.mAreConstraintsUnmet = false;
      this.mFuture = SettableFuture.create();
   }

   public WorkDatabase getWorkDatabase() {
      return WorkManagerImpl.getInstance().getWorkDatabase();
   }

   public void onAllConstraintsMet(List var1) {
   }

   public void onAllConstraintsNotMet(List param1) {
      // $FF: Couldn't be decompiled
   }

   public void onStopped() {
      super.onStopped();
      if (this.mDelegate != null) {
         this.mDelegate.stop();
      }

   }

   void setFutureFailed() {
      this.mFuture.set(ListenableWorker.Result.failure());
   }

   void setFutureRetry() {
      this.mFuture.set(ListenableWorker.Result.retry());
   }

   void setupAndRunConstraintTrackingWork() {
      String var1 = this.getInputData().getString("androidx.work.impl.workers.ConstraintTrackingWorker.ARGUMENT_CLASS_NAME");
      if (TextUtils.isEmpty(var1)) {
         Logger.get().error(TAG, "No worker to delegate to.");
         this.setFutureFailed();
      } else {
         this.mDelegate = this.getWorkerFactory().createWorkerWithDefaultFallback(this.getApplicationContext(), var1, this.mWorkerParameters);
         if (this.mDelegate == null) {
            Logger.get().debug(TAG, "No worker to delegate to.");
            this.setFutureFailed();
         } else {
            WorkSpec var2 = this.getWorkDatabase().workSpecDao().getWorkSpec(this.getId().toString());
            if (var2 == null) {
               this.setFutureFailed();
            } else {
               WorkConstraintsTracker var3 = new WorkConstraintsTracker(this.getApplicationContext(), this);
               var3.replace(Collections.singletonList(var2));
               if (var3.areAllConstraintsMet(this.getId().toString())) {
                  Logger.get().debug(TAG, String.format("Constraints met for delegate %s", var1));

                  try {
                     final ListenableFuture var30 = this.mDelegate.startWork();
                     Runnable var32 = new Runnable() {
                        public void run() {
                           Object var1 = ConstraintTrackingWorker.this.mLock;
                           synchronized(var1){}

                           Throwable var10000;
                           boolean var10001;
                           label195: {
                              label189: {
                                 try {
                                    if (ConstraintTrackingWorker.this.mAreConstraintsUnmet) {
                                       ConstraintTrackingWorker.this.setFutureRetry();
                                       break label189;
                                    }
                                 } catch (Throwable var22) {
                                    var10000 = var22;
                                    var10001 = false;
                                    break label195;
                                 }

                                 try {
                                    ConstraintTrackingWorker.this.mFuture.setFuture(var30);
                                 } catch (Throwable var21) {
                                    var10000 = var21;
                                    var10001 = false;
                                    break label195;
                                 }
                              }

                              label180:
                              try {
                                 return;
                              } catch (Throwable var20) {
                                 var10000 = var20;
                                 var10001 = false;
                                 break label180;
                              }
                           }

                           while(true) {
                              Throwable var2 = var10000;

                              try {
                                 throw var2;
                              } catch (Throwable var19) {
                                 var10000 = var19;
                                 var10001 = false;
                                 continue;
                              }
                           }
                        }
                     };
                     var30.addListener(var32, this.getBackgroundExecutor());
                  } catch (Throwable var28) {
                     Logger.get().debug(TAG, String.format("Delegated worker %s threw exception in startWork.", var1), var28);
                     Object var29 = this.mLock;
                     synchronized(var29){}

                     Throwable var10000;
                     boolean var10001;
                     label300: {
                        label318: {
                           try {
                              if (this.mAreConstraintsUnmet) {
                                 Logger.get().debug(TAG, "Constraints were unmet, Retrying.");
                                 this.setFutureRetry();
                                 break label318;
                              }
                           } catch (Throwable var27) {
                              var10000 = var27;
                              var10001 = false;
                              break label300;
                           }

                           try {
                              this.setFutureFailed();
                           } catch (Throwable var26) {
                              var10000 = var26;
                              var10001 = false;
                              break label300;
                           }
                        }

                        label290:
                        try {
                           return;
                        } catch (Throwable var25) {
                           var10000 = var25;
                           var10001 = false;
                           break label290;
                        }
                     }

                     while(true) {
                        Throwable var31 = var10000;

                        try {
                           throw var31;
                        } catch (Throwable var24) {
                           var10000 = var24;
                           var10001 = false;
                           continue;
                        }
                     }
                  }
               } else {
                  Logger.get().debug(TAG, String.format("Constraints not met for delegate %s. Requesting retry.", var1));
                  this.setFutureRetry();
               }

            }
         }
      }
   }

   public ListenableFuture startWork() {
      this.getBackgroundExecutor().execute(new Runnable() {
         public void run() {
            ConstraintTrackingWorker.this.setupAndRunConstraintTrackingWork();
         }
      });
      return this.mFuture;
   }
}
