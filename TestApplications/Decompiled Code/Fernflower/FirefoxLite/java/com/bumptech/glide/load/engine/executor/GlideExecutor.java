package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class GlideExecutor extends ThreadPoolExecutor {
   private static final long SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS;
   private final boolean executeSynchronously;

   static {
      SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
   }

   GlideExecutor(int var1, int var2, long var3, String var5, GlideExecutor.UncaughtThrowableStrategy var6, boolean var7, boolean var8) {
      this(var1, var2, var3, var5, var6, var7, var8, new PriorityBlockingQueue());
   }

   GlideExecutor(int var1, int var2, long var3, String var5, GlideExecutor.UncaughtThrowableStrategy var6, boolean var7, boolean var8, BlockingQueue var9) {
      super(var1, var2, var3, TimeUnit.MILLISECONDS, var9, new GlideExecutor.DefaultThreadFactory(var5, var6, var7));
      this.executeSynchronously = var8;
   }

   GlideExecutor(int var1, String var2, GlideExecutor.UncaughtThrowableStrategy var3, boolean var4, boolean var5) {
      this(var1, var1, 0L, var2, var3, var4, var5);
   }

   public static int calculateBestThreadCount() {
      ThreadPolicy var0 = StrictMode.allowThreadDiskReads();

      File[] var9;
      label61: {
         try {
            File var1 = new File("/sys/devices/system/cpu/");
            final Pattern var2 = Pattern.compile("cpu[0-9]+");
            FilenameFilter var3 = new FilenameFilter() {
               public boolean accept(File var1, String var2x) {
                  return var2.matcher(var2x).matches();
               }
            };
            var9 = var1.listFiles(var3);
            break label61;
         } catch (Throwable var7) {
            if (Log.isLoggable("GlideExecutor", 6)) {
               Log.e("GlideExecutor", "Failed to calculate accurate cpu count", var7);
            }
         } finally {
            StrictMode.setThreadPolicy(var0);
         }

         var9 = null;
      }

      int var4;
      if (var9 != null) {
         var4 = var9.length;
      } else {
         var4 = 0;
      }

      return Math.min(4, Math.max(Math.max(1, Runtime.getRuntime().availableProcessors()), var4));
   }

   private Future maybeWait(Future param1) {
      // $FF: Couldn't be decompiled
   }

   public static GlideExecutor newDiskCacheExecutor() {
      return newDiskCacheExecutor(1, "disk-cache", GlideExecutor.UncaughtThrowableStrategy.DEFAULT);
   }

   public static GlideExecutor newDiskCacheExecutor(int var0, String var1, GlideExecutor.UncaughtThrowableStrategy var2) {
      return new GlideExecutor(var0, var1, var2, true, false);
   }

   public static GlideExecutor newSourceExecutor() {
      return newSourceExecutor(calculateBestThreadCount(), "source", GlideExecutor.UncaughtThrowableStrategy.DEFAULT);
   }

   public static GlideExecutor newSourceExecutor(int var0, String var1, GlideExecutor.UncaughtThrowableStrategy var2) {
      return new GlideExecutor(var0, var1, var2, false, false);
   }

   public static GlideExecutor newUnlimitedSourceExecutor() {
      return new GlideExecutor(0, Integer.MAX_VALUE, SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS, "source-unlimited", GlideExecutor.UncaughtThrowableStrategy.DEFAULT, false, false, new SynchronousQueue());
   }

   public void execute(Runnable var1) {
      if (this.executeSynchronously) {
         var1.run();
      } else {
         super.execute(var1);
      }

   }

   public Future submit(Runnable var1) {
      return this.maybeWait(super.submit(var1));
   }

   public Future submit(Runnable var1, Object var2) {
      return this.maybeWait(super.submit(var1, var2));
   }

   public Future submit(Callable var1) {
      return this.maybeWait(super.submit(var1));
   }

   private static final class DefaultThreadFactory implements ThreadFactory {
      private final String name;
      final boolean preventNetworkOperations;
      private int threadNum;
      final GlideExecutor.UncaughtThrowableStrategy uncaughtThrowableStrategy;

      DefaultThreadFactory(String var1, GlideExecutor.UncaughtThrowableStrategy var2, boolean var3) {
         this.name = var1;
         this.uncaughtThrowableStrategy = var2;
         this.preventNetworkOperations = var3;
      }

      public Thread newThread(Runnable var1) {
         synchronized(this){}

         Thread var2;
         try {
            StringBuilder var3 = new StringBuilder();
            var3.append("glide-");
            var3.append(this.name);
            var3.append("-thread-");
            var3.append(this.threadNum);
            var2 = new Thread(var1, var3.toString()) {
               public void run() {
                  Process.setThreadPriority(9);
                  if (DefaultThreadFactory.this.preventNetworkOperations) {
                     StrictMode.setThreadPolicy((new Builder()).detectNetwork().penaltyDeath().build());
                  }

                  try {
                     super.run();
                  } catch (Throwable var2) {
                     DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(var2);
                  }

               }
            };
            ++this.threadNum;
         } finally {
            ;
         }

         return var2;
      }
   }

   public interface UncaughtThrowableStrategy {
      GlideExecutor.UncaughtThrowableStrategy DEFAULT = LOG;
      GlideExecutor.UncaughtThrowableStrategy IGNORE = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
         }
      };
      GlideExecutor.UncaughtThrowableStrategy LOG = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
            if (var1 != null && Log.isLoggable("GlideExecutor", 6)) {
               Log.e("GlideExecutor", "Request threw uncaught throwable", var1);
            }

         }
      };
      GlideExecutor.UncaughtThrowableStrategy THROW = new GlideExecutor.UncaughtThrowableStrategy() {
         public void handle(Throwable var1) {
            if (var1 != null) {
               throw new RuntimeException("Request threw uncaught throwable", var1);
            }
         }
      };

      void handle(Throwable var1);
   }
}
