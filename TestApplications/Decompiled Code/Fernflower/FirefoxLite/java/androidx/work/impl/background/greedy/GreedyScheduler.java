package androidx.work.impl.background.greedy;

import android.content.Context;
import android.os.Build.VERSION;
import android.text.TextUtils;
import androidx.work.Logger;
import androidx.work.WorkInfo;
import androidx.work.impl.ExecutionListener;
import androidx.work.impl.Scheduler;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.constraints.WorkConstraintsCallback;
import androidx.work.impl.constraints.WorkConstraintsTracker;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public class GreedyScheduler implements ExecutionListener, Scheduler, WorkConstraintsCallback {
   private static final String TAG = Logger.tagWithPrefix("GreedyScheduler");
   private List mConstrainedWorkSpecs = new ArrayList();
   private final Object mLock;
   private boolean mRegisteredExecutionListener;
   private WorkConstraintsTracker mWorkConstraintsTracker;
   private WorkManagerImpl mWorkManagerImpl;

   public GreedyScheduler(Context var1, WorkManagerImpl var2) {
      this.mWorkManagerImpl = var2;
      this.mWorkConstraintsTracker = new WorkConstraintsTracker(var1, this);
      this.mLock = new Object();
   }

   private void registerExecutionListenerIfNeeded() {
      if (!this.mRegisteredExecutionListener) {
         this.mWorkManagerImpl.getProcessor().addExecutionListener(this);
         this.mRegisteredExecutionListener = true;
      }

   }

   private void removeConstraintTrackingFor(String var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label255: {
         int var3;
         try {
            var3 = this.mConstrainedWorkSpecs.size();
         } catch (Throwable var24) {
            var10000 = var24;
            var10001 = false;
            break label255;
         }

         for(int var4 = 0; var4 < var3; ++var4) {
            try {
               if (((WorkSpec)this.mConstrainedWorkSpecs.get(var4)).id.equals(var1)) {
                  Logger.get().debug(TAG, String.format("Stopping tracking for %s", var1));
                  this.mConstrainedWorkSpecs.remove(var4);
                  this.mWorkConstraintsTracker.replace(this.mConstrainedWorkSpecs);
                  break;
               }
            } catch (Throwable var23) {
               var10000 = var23;
               var10001 = false;
               break label255;
            }
         }

         label233:
         try {
            return;
         } catch (Throwable var22) {
            var10000 = var22;
            var10001 = false;
            break label233;
         }
      }

      while(true) {
         Throwable var25 = var10000;

         try {
            throw var25;
         } catch (Throwable var21) {
            var10000 = var21;
            var10001 = false;
            continue;
         }
      }
   }

   public void cancel(String var1) {
      this.registerExecutionListenerIfNeeded();
      Logger.get().debug(TAG, String.format("Cancelling work ID %s", var1));
      this.mWorkManagerImpl.stopWork(var1);
   }

   public void onAllConstraintsMet(List var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         Logger.get().debug(TAG, String.format("Constraints met: Scheduling work ID %s", var2));
         this.mWorkManagerImpl.startWork(var2);
      }

   }

   public void onAllConstraintsNotMet(List var1) {
      Iterator var3 = var1.iterator();

      while(var3.hasNext()) {
         String var2 = (String)var3.next();
         Logger.get().debug(TAG, String.format("Constraints not met: Cancelling work ID %s", var2));
         this.mWorkManagerImpl.stopWork(var2);
      }

   }

   public void onExecuted(String var1, boolean var2) {
      this.removeConstraintTrackingFor(var1);
   }

   public void schedule(WorkSpec... var1) {
      this.registerExecutionListenerIfNeeded();
      ArrayList var2 = new ArrayList();
      ArrayList var3 = new ArrayList();
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         WorkSpec var6 = var1[var5];
         if (var6.state == WorkInfo.State.ENQUEUED && !var6.isPeriodic() && var6.initialDelay == 0L && !var6.isBackedOff()) {
            if (var6.hasConstraints()) {
               if (VERSION.SDK_INT < 24 || !var6.constraints.hasContentUriTriggers()) {
                  var2.add(var6);
                  var3.add(var6.id);
               }
            } else {
               Logger.get().debug(TAG, String.format("Starting work for %s", var6.id));
               this.mWorkManagerImpl.startWork(var6.id);
            }
         }
      }

      Object var19 = this.mLock;
      synchronized(var19){}

      Throwable var10000;
      boolean var10001;
      label233: {
         try {
            if (!var2.isEmpty()) {
               Logger.get().debug(TAG, String.format("Starting tracking for [%s]", TextUtils.join(",", var3)));
               this.mConstrainedWorkSpecs.addAll(var2);
               this.mWorkConstraintsTracker.replace(this.mConstrainedWorkSpecs);
            }
         } catch (Throwable var18) {
            var10000 = var18;
            var10001 = false;
            break label233;
         }

         label230:
         try {
            return;
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label230;
         }
      }

      while(true) {
         Throwable var20 = var10000;

         try {
            throw var20;
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            continue;
         }
      }
   }
}
