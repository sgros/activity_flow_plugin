package com.adjust.sdk;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class CustomScheduledExecutor {
   private ScheduledThreadPoolExecutor executor;
   private String source;
   private final AtomicInteger threadCounter = new AtomicInteger(1);

   public CustomScheduledExecutor(final String var1, boolean var2) {
      this.source = var1;
      this.executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {
         public Thread newThread(Runnable var1x) {
            Thread var2 = Executors.defaultThreadFactory().newThread(CustomScheduledExecutor.this.new RunnableWrapper(var1x));
            var2.setPriority(1);
            StringBuilder var3 = new StringBuilder();
            var3.append("Adjust-");
            var3.append(var2.getName());
            var3.append("-");
            var3.append(var1);
            var2.setName(var3.toString());
            var2.setDaemon(true);
            var2.setUncaughtExceptionHandler(new UncaughtExceptionHandler() {
               public void uncaughtException(Thread var1x, Throwable var2) {
                  AdjustFactory.getLogger().error("Thread %s with error %s", var1x.getName(), var2.getMessage());
               }
            });
            return var2;
         }
      }, new RejectedExecutionHandler() {
         public void rejectedExecution(Runnable var1x, ThreadPoolExecutor var2) {
            AdjustFactory.getLogger().warn("Runnable %s rejected from %s ", var1x.toString(), var1);
         }
      });
      if (!var2) {
         this.executor.setKeepAliveTime(10L, TimeUnit.MILLISECONDS);
         this.executor.allowCoreThreadTimeOut(true);
      }

   }

   public ScheduledFuture schedule(Runnable var1, long var2, TimeUnit var4) {
      return this.executor.schedule(var1, var2, var4);
   }

   public ScheduledFuture scheduleWithFixedDelay(Runnable var1, long var2, long var4, TimeUnit var6) {
      return this.executor.scheduleWithFixedDelay(var1, var2, var4, var6);
   }

   public void shutdownNow() {
      this.executor.shutdownNow();
   }

   public Future submit(Runnable var1) {
      return this.executor.submit(var1);
   }

   private class RunnableWrapper implements Runnable {
      private Runnable runnable;

      public RunnableWrapper(Runnable var2) {
         this.runnable = var2;
      }

      public void run() {
         try {
            this.runnable.run();
         } catch (Throwable var2) {
            AdjustFactory.getLogger().error("Runnable error %s", var2.getMessage());
         }

      }
   }
}
