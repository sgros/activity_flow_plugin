package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import com.airbnb.lottie.utils.Logger;
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

   public LottieTask(Callable var1) {
      this(var1, false);
   }

   LottieTask(Callable var1, boolean var2) {
      this.successListeners = new LinkedHashSet(1);
      this.failureListeners = new LinkedHashSet(1);
      this.handler = new Handler(Looper.getMainLooper());
      this.result = null;
      if (var2) {
         try {
            this.setResult((LottieResult)var1.call());
         } catch (Throwable var3) {
            this.setResult(new LottieResult(var3));
         }
      } else {
         EXECUTOR.execute(new LottieTask.LottieFutureTask(var1));
      }

   }

   private void notifyFailureListeners(Throwable var1) {
      synchronized(this){}

      Throwable var10000;
      label168: {
         boolean var10001;
         ArrayList var2;
         try {
            var2 = new ArrayList(this.failureListeners);
            if (var2.isEmpty()) {
               Logger.warning("Lottie encountered an error but no failure listener was added:", var1);
               return;
            }
         } catch (Throwable var14) {
            var10000 = var14;
            var10001 = false;
            break label168;
         }

         Iterator var15;
         try {
            var15 = var2.iterator();
         } catch (Throwable var13) {
            var10000 = var13;
            var10001 = false;
            break label168;
         }

         while(true) {
            try {
               if (var15.hasNext()) {
                  ((LottieListener)var15.next()).onResult(var1);
                  continue;
               }
            } catch (Throwable var12) {
               var10000 = var12;
               var10001 = false;
               break;
            }

            return;
         }
      }

      var1 = var10000;
      throw var1;
   }

   private void notifyListeners() {
      this.handler.post(new Runnable() {
         public void run() {
            if (LottieTask.this.result != null) {
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
      synchronized(this){}

      Throwable var10000;
      label77: {
         boolean var10001;
         Iterator var10;
         try {
            ArrayList var2 = new ArrayList(this.successListeners);
            var10 = var2.iterator();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label77;
         }

         while(true) {
            try {
               if (!var10.hasNext()) {
                  return;
               }

               ((LottieListener)var10.next()).onResult(var1);
            } catch (Throwable var7) {
               var10000 = var7;
               var10001 = false;
               break;
            }
         }
      }

      Throwable var9 = var10000;
      throw var9;
   }

   private void setResult(LottieResult var1) {
      if (this.result == null) {
         this.result = var1;
         this.notifyListeners();
      } else {
         throw new IllegalStateException("A task may only be set once.");
      }
   }

   public LottieTask addFailureListener(LottieListener var1) {
      synchronized(this){}

      try {
         if (this.result != null && this.result.getException() != null) {
            var1.onResult(this.result.getException());
         }

         this.failureListeners.add(var1);
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
      } finally {
         ;
      }

      return this;
   }

   public LottieTask removeFailureListener(LottieListener var1) {
      synchronized(this){}

      try {
         this.failureListeners.remove(var1);
      } finally {
         ;
      }

      return this;
   }

   public LottieTask removeListener(LottieListener var1) {
      synchronized(this){}

      try {
         this.successListeners.remove(var1);
      } finally {
         ;
      }

      return this;
   }

   private class LottieFutureTask extends FutureTask {
      LottieFutureTask(Callable var2) {
         super(var2);
      }

      protected void done() {
         if (!this.isCancelled()) {
            Object var1;
            try {
               LottieTask.this.setResult((LottieResult)this.get());
               return;
            } catch (InterruptedException var2) {
               var1 = var2;
            } catch (ExecutionException var3) {
               var1 = var3;
            }

            LottieTask.this.setResult(new LottieResult((Throwable)var1));
         }
      }
   }
}
