package kotlinx.coroutines.experimental;

import java.util.concurrent.TimeUnit;
import kotlin.jvm.internal.Intrinsics;
import kotlin.ranges.RangesKt;

public final class DefaultExecutor extends EventLoopBase implements Runnable {
   public static final DefaultExecutor INSTANCE = new DefaultExecutor();
   private static final long KEEP_ALIVE_NANOS;
   private static volatile Thread _thread;
   private static volatile int debugStatus;

   static {
      TimeUnit var0 = TimeUnit.MILLISECONDS;

      Long var1;
      try {
         var1 = Long.getLong("kotlinx.coroutines.DefaultExecutor.keepAlive", 1000L);
      } catch (SecurityException var2) {
         var1 = 1000L;
      }

      Intrinsics.checkExpressionValueIsNotNull(var1, "try {\n            java.l…AULT_KEEP_ALIVE\n        }");
      KEEP_ALIVE_NANOS = var0.toNanos(var1);
   }

   private DefaultExecutor() {
   }

   private final void acknowledgeShutdownIfNeeded() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isShutdownRequested();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (!var1) {
            return;
         }

         try {
            debugStatus = 3;
            this.resetAll();
            ((Object)this).notifyAll();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   private final Thread createThreadSync() {
      synchronized(this){}

      Throwable var10000;
      label76: {
         Thread var1;
         boolean var10001;
         try {
            var1 = _thread;
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
            var1 = new Thread((Runnable)this, "kotlinx.coroutines.DefaultExecutor");
            _thread = var1;
            var1.setDaemon(true);
            var1.start();
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

   private final boolean isShutdownRequested() {
      int var1 = debugStatus;
      boolean var2;
      if (var1 != 2 && var1 != 3) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }

   private final boolean notifyStartup() {
      synchronized(this){}

      Throwable var10000;
      label78: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.isShutdownRequested();
         } catch (Throwable var8) {
            var10000 = var8;
            var10001 = false;
            break label78;
         }

         if (var1) {
            return false;
         }

         try {
            debugStatus = 1;
            ((Object)this).notifyAll();
         } catch (Throwable var7) {
            var10000 = var7;
            var10001 = false;
            break label78;
         }

         return true;
      }

      Throwable var2 = var10000;
      throw var2;
   }

   private final Thread thread() {
      Thread var1 = _thread;
      if (var1 == null) {
         var1 = this.createThreadSync();
      }

      return var1;
   }

   protected boolean isCompleted() {
      return false;
   }

   protected boolean isCorrectThread() {
      return true;
   }

   public void run() {
      TimeSourceKt.getTimeSource().registerTimeLoopThread();

      Throwable var10000;
      label835: {
         boolean var1;
         boolean var10001;
         try {
            var1 = this.notifyStartup();
         } catch (Throwable var83) {
            var10000 = var83;
            var10001 = false;
            break label835;
         }

         if (!var1) {
            _thread = (Thread)null;
            this.acknowledgeShutdownIfNeeded();
            TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
            if (!this.isEmpty()) {
               this.thread();
            }

            return;
         }

         long var2 = Long.MAX_VALUE;

         while(true) {
            long var4;
            try {
               Thread.interrupted();
               var4 = this.processNextEvent();
            } catch (Throwable var82) {
               var10000 = var82;
               var10001 = false;
               break;
            }

            long var6 = var2;
            long var8 = var4;
            if (var4 == Long.MAX_VALUE) {
               long var84;
               int var10 = (var84 = var2 - Long.MAX_VALUE) == 0L ? 0 : (var84 < 0L ? -1 : 1);
               if (var10 == 0) {
                  try {
                     var8 = TimeSourceKt.getTimeSource().nanoTime();
                  } catch (Throwable var81) {
                     var10000 = var81;
                     var10001 = false;
                     break;
                  }

                  if (var10 == 0) {
                     try {
                        var2 = KEEP_ALIVE_NANOS;
                     } catch (Throwable var80) {
                        var10000 = var80;
                        var10001 = false;
                        break;
                     }

                     var2 += var8;
                  }

                  var8 = var2 - var8;
                  if (var8 <= 0L) {
                     _thread = (Thread)null;
                     this.acknowledgeShutdownIfNeeded();
                     TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                     if (!this.isEmpty()) {
                        this.thread();
                     }

                     return;
                  }

                  try {
                     var8 = RangesKt.coerceAtMost(var4, var8);
                  } catch (Throwable var79) {
                     var10000 = var79;
                     var10001 = false;
                     break;
                  }

                  var6 = var2;
               } else {
                  try {
                     var8 = RangesKt.coerceAtMost(var4, KEEP_ALIVE_NANOS);
                  } catch (Throwable var78) {
                     var10000 = var78;
                     var10001 = false;
                     break;
                  }

                  var6 = var2;
               }
            }

            var2 = var6;
            if (var8 > 0L) {
               try {
                  var1 = this.isShutdownRequested();
               } catch (Throwable var77) {
                  var10000 = var77;
                  var10001 = false;
                  break;
               }

               if (var1) {
                  _thread = (Thread)null;
                  this.acknowledgeShutdownIfNeeded();
                  TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
                  if (!this.isEmpty()) {
                     this.thread();
                  }

                  return;
               }

               try {
                  TimeSourceKt.getTimeSource().parkNanos(this, var8);
               } catch (Throwable var76) {
                  var10000 = var76;
                  var10001 = false;
                  break;
               }

               var2 = var6;
            }
         }
      }

      Throwable var11 = var10000;
      _thread = (Thread)null;
      this.acknowledgeShutdownIfNeeded();
      TimeSourceKt.getTimeSource().unregisterTimeLoopThread();
      if (!this.isEmpty()) {
         this.thread();
      }

      throw var11;
   }

   protected void unpark() {
      TimeSourceKt.getTimeSource().unpark(this.thread());
   }
}
