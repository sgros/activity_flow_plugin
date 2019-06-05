package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class LottieTask {
   public static Executor EXECUTOR = Executors.newCachedThreadPool();
   private final Set failureListeners;
   private final Handler handler;
   private volatile LottieResult result;
   private final Set successListeners;
   private final FutureTask task;
   private Thread taskObserver;

   public LottieTask(Callable var1) {
      this(var1, false);
   }

   LottieTask(Callable var1, boolean var2) {
      this.successListeners = new LinkedHashSet(1);
      this.failureListeners = new LinkedHashSet(1);
      this.handler = new Handler(Looper.getMainLooper());
      this.result = null;
      this.task = new FutureTask(var1);
      if (var2) {
         try {
            this.setResult((LottieResult)var1.call());
         } catch (Throwable var3) {
            this.setResult(new LottieResult(var3));
         }
      } else {
         EXECUTOR.execute(this.task);
         this.startTaskObserverIfNeeded();
      }

   }

   private void notifyFailureListeners(Throwable var1) {
      ArrayList var2 = new ArrayList(this.failureListeners);
      if (var2.isEmpty()) {
         Log.w("LOTTIE", "Lottie encountered an error but no failure listener was added.", var1);
      } else {
         Iterator var3 = var2.iterator();

         while(var3.hasNext()) {
            ((LottieListener)var3.next()).onResult(var1);
         }

      }
   }

   private void notifyListeners() {
      this.handler.post(new Runnable() {
         public void run() {
            if (LottieTask.this.result != null && !LottieTask.this.task.isCancelled()) {
               LottieResult var1 = LottieTask.this.result;
               if (var1.getValue() != null) {
                  LottieTask.this.notifySuccessListeners(var1.getValue());
               } else {
                  LottieTask.this.notifyFailureListeners(var1.getException());
               }

            }
         }
      });
   }

   private void notifySuccessListeners(Object var1) {
      Iterator var2 = (new ArrayList(this.successListeners)).iterator();

      while(var2.hasNext()) {
         ((LottieListener)var2.next()).onResult(var1);
      }

   }

   private void setResult(LottieResult var1) {
      if (this.result == null) {
         this.result = var1;
         this.notifyListeners();
      } else {
         throw new IllegalStateException("A task may only be set once.");
      }
   }

   private void startTaskObserverIfNeeded() {
      synchronized(this){}

      try {
         if (this.taskObserverAlive() || this.result != null) {
            return;
         }

         Thread var1 = new Thread("LottieTaskObserver") {
            private boolean taskComplete = false;

            public void run() {
               while(!this.isInterrupted() && !this.taskComplete) {
                  if (LottieTask.this.task.isDone()) {
                     try {
                        LottieTask.this.setResult((LottieResult)LottieTask.this.task.get());
                     } catch (ExecutionException | InterruptedException var2) {
                        LottieTask.this.setResult(new LottieResult(var2));
                     }

                     this.taskComplete = true;
                     LottieTask.this.stopTaskObserverIfNeeded();
                  }
               }

            }
         };
         this.taskObserver = var1;
         this.taskObserver.start();
         L.debug("Starting TaskObserver thread");
      } finally {
         ;
      }

   }

   private void stopTaskObserverIfNeeded() {
      synchronized(this){}

      Throwable var10000;
      label144: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.taskObserverAlive();
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label144;
         }

         if (!var1) {
            return;
         }

         try {
            if (!this.successListeners.isEmpty() && this.result == null) {
               return;
            }
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label144;
         }

         label132:
         try {
            this.taskObserver.interrupt();
            this.taskObserver = null;
            L.debug("Stopping TaskObserver thread");
            return;
         } catch (Throwable var12) {
            var10000 = var12;
            var10001 = false;
            break label132;
         }
      }

      Throwable var2 = var10000;
      throw var2;
   }

   private boolean taskObserverAlive() {
      boolean var1;
      if (this.taskObserver != null && this.taskObserver.isAlive()) {
         var1 = true;
      } else {
         var1 = false;
      }

      return var1;
   }

   public LottieTask addFailureListener(LottieListener var1) {
      synchronized(this){}

      try {
         if (this.result != null && this.result.getException() != null) {
            var1.onResult(this.result.getException());
         }

         this.failureListeners.add(var1);
         this.startTaskObserverIfNeeded();
      } finally {
         ;
      }

      return this;
   }

   public LottieTask addListener(LottieListener var1) {
      synchronized(this){}

      try {
         if (this.result != null && this.result.getValue() != null) {
            var1.onResult(this.result.getValue());
         }

         this.successListeners.add(var1);
         this.startTaskObserverIfNeeded();
      } finally {
         ;
      }

      return this;
   }

   public LottieTask removeFailureListener(LottieListener var1) {
      synchronized(this){}

      try {
         this.failureListeners.remove(var1);
         this.stopTaskObserverIfNeeded();
      } finally {
         ;
      }

      return this;
   }

   public LottieTask removeListener(LottieListener var1) {
      synchronized(this){}

      try {
         this.successListeners.remove(var1);
         this.stopTaskObserverIfNeeded();
      } finally {
         ;
      }

      return this;
   }
}
