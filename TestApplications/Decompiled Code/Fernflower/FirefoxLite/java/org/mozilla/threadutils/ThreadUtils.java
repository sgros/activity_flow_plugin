package org.mozilla.threadutils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
   private static final ExecutorService backgroundExecutorService = Executors.newSingleThreadExecutor(getIoPrioritisedFactory());
   private static final Handler handler = new Handler(Looper.getMainLooper());
   private static final Thread uiThread = Looper.getMainLooper().getThread();

   private static ThreadFactory getIoPrioritisedFactory() {
      return new ThreadUtils.CustomThreadFactory("pool-io-background", 4);
   }

   public static Future postToBackgroundThread(Callable var0) {
      return backgroundExecutorService.submit(var0);
   }

   public static void postToBackgroundThread(Runnable var0) {
      backgroundExecutorService.submit(var0);
   }

   public static void postToMainThread(Runnable var0) {
      handler.post(var0);
   }

   public static void postToMainThreadDelayed(Runnable var0, long var1) {
      handler.postDelayed(var0, var1);
   }

   private static class CustomThreadFactory implements ThreadFactory {
      private final AtomicInteger mNumber = new AtomicInteger();
      private final String threadName;
      private final int threadPriority;

      public CustomThreadFactory(String var1, int var2) {
         this.threadName = var1;
         this.threadPriority = var2;
      }

      public Thread newThread(Runnable var1) {
         StringBuilder var2 = new StringBuilder();
         var2.append(this.threadName);
         var2.append("-");
         var2.append(this.mNumber.getAndIncrement());
         Thread var3 = new Thread(var1, var2.toString());
         var3.setPriority(this.threadPriority);
         return var3;
      }
   }
}
