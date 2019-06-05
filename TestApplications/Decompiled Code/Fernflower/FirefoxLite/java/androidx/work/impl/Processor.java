package androidx.work.impl;

import android.content.Context;
import androidx.work.Configuration;
import androidx.work.Logger;
import androidx.work.WorkerParameters;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ExecutionException;

public class Processor implements ExecutionListener {
   private static final String TAG = Logger.tagWithPrefix("Processor");
   private Context mAppContext;
   private Set mCancelledIds;
   private Configuration mConfiguration;
   private Map mEnqueuedWorkMap;
   private final Object mLock;
   private final List mOuterListeners;
   private List mSchedulers;
   private WorkDatabase mWorkDatabase;
   private TaskExecutor mWorkTaskExecutor;

   public Processor(Context var1, Configuration var2, TaskExecutor var3, WorkDatabase var4, List var5) {
      this.mAppContext = var1;
      this.mConfiguration = var2;
      this.mWorkTaskExecutor = var3;
      this.mWorkDatabase = var4;
      this.mEnqueuedWorkMap = new HashMap();
      this.mSchedulers = var5;
      this.mCancelledIds = new HashSet();
      this.mOuterListeners = new ArrayList();
      this.mLock = new Object();
   }

   public void addExecutionListener(ExecutionListener param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isCancelled(String param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean isEnqueued(String param1) {
      // $FF: Couldn't be decompiled
   }

   public void onExecuted(String var1, boolean var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label198: {
         Iterator var4;
         try {
            this.mEnqueuedWorkMap.remove(var1);
            Logger.get().debug(TAG, String.format("%s %s executed; reschedule = %s", this.getClass().getSimpleName(), var1, var2));
            var4 = this.mOuterListeners.iterator();
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label198;
         }

         while(true) {
            try {
               if (var4.hasNext()) {
                  ((ExecutionListener)var4.next()).onExecuted(var1, var2);
                  continue;
               }
            } catch (Throwable var24) {
               var10000 = var24;
               var10001 = false;
               break;
            }

            try {
               return;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break;
            }
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

   public void removeExecutionListener(ExecutionListener param1) {
      // $FF: Couldn't be decompiled
   }

   public boolean startWork(String var1) {
      return this.startWork(var1, (WorkerParameters.RuntimeExtras)null);
   }

   public boolean startWork(String var1, WorkerParameters.RuntimeExtras var2) {
      Object var3 = this.mLock;
      synchronized(var3){}

      Throwable var10000;
      boolean var10001;
      label137: {
         try {
            if (this.mEnqueuedWorkMap.containsKey(var1)) {
               Logger.get().debug(TAG, String.format("Work %s is already enqueued for processing", var1));
               return false;
            }
         } catch (Throwable var17) {
            var10000 = var17;
            var10001 = false;
            break label137;
         }

         WorkerWrapper var5;
         try {
            WorkerWrapper.Builder var4 = new WorkerWrapper.Builder(this.mAppContext, this.mConfiguration, this.mWorkTaskExecutor, this.mWorkDatabase, var1);
            var5 = var4.withSchedulers(this.mSchedulers).withRuntimeExtras(var2).build();
            ListenableFuture var20 = var5.getFuture();
            Processor.FutureListener var19 = new Processor.FutureListener(this, var1, var20);
            var20.addListener(var19, this.mWorkTaskExecutor.getMainThreadExecutor());
            this.mEnqueuedWorkMap.put(var1, var5);
         } catch (Throwable var16) {
            var10000 = var16;
            var10001 = false;
            break label137;
         }

         this.mWorkTaskExecutor.getBackgroundExecutor().execute(var5);
         Logger.get().debug(TAG, String.format("%s: processing %s", this.getClass().getSimpleName(), var1));
         return true;
      }

      while(true) {
         Throwable var18 = var10000;

         try {
            throw var18;
         } catch (Throwable var15) {
            var10000 = var15;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean stopAndCancelWork(String var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label164: {
         WorkerWrapper var3;
         try {
            Logger.get().debug(TAG, String.format("Processor cancelling %s", var1));
            this.mCancelledIds.add(var1);
            var3 = (WorkerWrapper)this.mEnqueuedWorkMap.remove(var1);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label164;
         }

         if (var3 != null) {
            label158:
            try {
               var3.interrupt(true);
               Logger.get().debug(TAG, String.format("WorkerWrapper cancelled for %s", var1));
               return true;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label158;
            }
         } else {
            label160:
            try {
               Logger.get().debug(TAG, String.format("WorkerWrapper could not be found for %s", var1));
               return false;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label160;
            }
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   public boolean stopWork(String var1) {
      Object var2 = this.mLock;
      synchronized(var2){}

      Throwable var10000;
      boolean var10001;
      label164: {
         WorkerWrapper var3;
         try {
            Logger.get().debug(TAG, String.format("Processor stopping %s", var1));
            var3 = (WorkerWrapper)this.mEnqueuedWorkMap.remove(var1);
         } catch (Throwable var23) {
            var10000 = var23;
            var10001 = false;
            break label164;
         }

         if (var3 != null) {
            label158:
            try {
               var3.interrupt(false);
               Logger.get().debug(TAG, String.format("WorkerWrapper stopped for %s", var1));
               return true;
            } catch (Throwable var21) {
               var10000 = var21;
               var10001 = false;
               break label158;
            }
         } else {
            label160:
            try {
               Logger.get().debug(TAG, String.format("WorkerWrapper could not be found for %s", var1));
               return false;
            } catch (Throwable var22) {
               var10000 = var22;
               var10001 = false;
               break label160;
            }
         }
      }

      while(true) {
         Throwable var24 = var10000;

         try {
            throw var24;
         } catch (Throwable var20) {
            var10000 = var20;
            var10001 = false;
            continue;
         }
      }
   }

   private static class FutureListener implements Runnable {
      private ExecutionListener mExecutionListener;
      private ListenableFuture mFuture;
      private String mWorkSpecId;

      FutureListener(ExecutionListener var1, String var2, ListenableFuture var3) {
         this.mExecutionListener = var1;
         this.mWorkSpecId = var2;
         this.mFuture = var3;
      }

      public void run() {
         boolean var1;
         try {
            var1 = (Boolean)this.mFuture.get();
         } catch (ExecutionException | InterruptedException var3) {
            var1 = true;
         }

         this.mExecutionListener.onExecuted(this.mWorkSpecId, var1);
      }
   }
}
