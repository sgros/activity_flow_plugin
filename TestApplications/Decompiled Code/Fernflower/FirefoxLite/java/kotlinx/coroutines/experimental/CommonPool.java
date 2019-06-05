package kotlinx.coroutines.experimental;

import java.lang.reflect.Method;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;
import kotlin.coroutines.experimental.CoroutineContext;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;
import kotlin.text.StringsKt;

public final class CommonPool extends CoroutineDispatcher {
   public static final CommonPool INSTANCE = new CommonPool();
   private static final int parallelism;
   private static volatile Executor pool;
   private static boolean usePrivatePool;

   static {
      CommonPool var0 = INSTANCE;

      String var4;
      try {
         var4 = System.getProperty("kotlinx.coroutines.default.parallelism");
      } catch (Throwable var3) {
         var4 = null;
      }

      int var1;
      if (var4 == null) {
         var1 = RangesKt.coerceAtLeast(Runtime.getRuntime().availableProcessors() - 1, 1);
      } else {
         Integer var2 = StringsKt.toIntOrNull(var4);
         if (var2 == null || var2 < 1) {
            StringBuilder var5 = new StringBuilder();
            var5.append("Expected positive number in kotlinx.coroutines.default.parallelism, but has ");
            var5.append(var4);
            throw (Throwable)(new IllegalStateException(var5.toString().toString()));
         }

         var1 = var2;
      }

      parallelism = var1;
   }

   private CommonPool() {
   }

   private final ExecutorService createPlainPool() {
      final AtomicInteger var1 = new AtomicInteger();
      ExecutorService var2 = Executors.newFixedThreadPool(parallelism, (ThreadFactory)(new ThreadFactory() {
         public final Thread newThread(Runnable var1x) {
            StringBuilder var2 = new StringBuilder();
            var2.append("CommonPool-worker-");
            var2.append(var1.incrementAndGet());
            Thread var3 = new Thread(var1x, var2.toString());
            var3.setDaemon(true);
            return var3;
         }
      }));
      Intrinsics.checkExpressionValueIsNotNull(var2, "Executors.newFixedThread…Daemon = true }\n        }");
      return var2;
   }

   private final ExecutorService createPool() {
      if (System.getSecurityManager() != null) {
         return this.createPlainPool();
      } else {
         Object var1 = null;

         Class var2;
         try {
            var2 = Class.forName("java.util.concurrent.ForkJoinPool");
         } catch (Throwable var5) {
            var2 = null;
         }

         if (var2 == null) {
            return this.createPlainPool();
         } else {
            boolean var10001;
            Object var14;
            ExecutorService var15;
            if (!usePrivatePool) {
               label90: {
                  label98: {
                     Method var3;
                     try {
                        var3 = var2.getMethod("commonPool");
                     } catch (Throwable var12) {
                        var10001 = false;
                        break label98;
                     }

                     if (var3 != null) {
                        try {
                           var14 = var3.invoke((Object)null);
                        } catch (Throwable var11) {
                           var10001 = false;
                           break label98;
                        }
                     } else {
                        var14 = null;
                     }

                     Object var4 = var14;

                     label80: {
                        try {
                           if (var14 instanceof ExecutorService) {
                              break label80;
                           }
                        } catch (Throwable var10) {
                           var10001 = false;
                           break label98;
                        }

                        var4 = null;
                     }

                     try {
                        var15 = (ExecutorService)var4;
                        break label90;
                     } catch (Throwable var9) {
                        var10001 = false;
                     }
                  }

                  var15 = null;
               }

               if (var15 != null) {
                  return var15;
               }
            }

            label99: {
               Object var13;
               try {
                  var13 = var2.getConstructor(Integer.TYPE).newInstance(parallelism);
               } catch (Throwable var8) {
                  var10001 = false;
                  break label99;
               }

               var14 = var13;

               label65: {
                  try {
                     if (var13 instanceof ExecutorService) {
                        break label65;
                     }
                  } catch (Throwable var7) {
                     var10001 = false;
                     break label99;
                  }

                  var14 = null;
               }

               try {
                  var15 = (ExecutorService)var14;
                  return var15 != null ? var15 : this.createPlainPool();
               } catch (Throwable var6) {
                  var10001 = false;
               }
            }

            var15 = (ExecutorService)var1;
            return var15 != null ? var15 : this.createPlainPool();
         }
      }
   }

   private final Executor getOrCreatePoolSync() {
      synchronized(this){}

      Throwable var10000;
      label76: {
         Executor var1;
         boolean var10001;
         try {
            var1 = pool;
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label76;
         }

         if (var1 != null) {
            return var1;
         }

         label67:
         try {
            ExecutorService var9 = this.createPool();
            pool = (Executor)var9;
            var1 = (Executor)var9;
            return var1;
         } catch (Throwable var6) {
            var10000 = var6;
            var10001 = false;
            break label67;
         }
      }

      Throwable var8 = var10000;
      throw var8;
   }

   public void dispatch(CoroutineContext var1, Runnable var2) {
      Intrinsics.checkParameterIsNotNull(var1, "context");
      Intrinsics.checkParameterIsNotNull(var2, "block");

      label34: {
         boolean var10001;
         Executor var6;
         try {
            var6 = pool;
         } catch (RejectedExecutionException var5) {
            var10001 = false;
            break label34;
         }

         if (var6 == null) {
            try {
               var6 = this.getOrCreatePoolSync();
            } catch (RejectedExecutionException var4) {
               var10001 = false;
               break label34;
            }
         }

         try {
            var6.execute(TimeSourceKt.getTimeSource().trackTask(var2));
            return;
         } catch (RejectedExecutionException var3) {
            var10001 = false;
         }
      }

      TimeSourceKt.getTimeSource().unTrackTask();
      DefaultExecutor.INSTANCE.execute$kotlinx_coroutines_core(var2);
   }

   public String toString() {
      return "CommonPool";
   }
}
