package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class AsyncTimeout extends Timeout {
   private static final long IDLE_TIMEOUT_MILLIS;
   private static final long IDLE_TIMEOUT_NANOS;
   private static final int TIMEOUT_WRITE_SIZE = 65536;
   private static AsyncTimeout head;
   private boolean inQueue;
   private AsyncTimeout next;
   private long timeoutAt;

   static {
      IDLE_TIMEOUT_MILLIS = TimeUnit.SECONDS.toMillis(60L);
      IDLE_TIMEOUT_NANOS = TimeUnit.MILLISECONDS.toNanos(IDLE_TIMEOUT_MILLIS);
   }

   // $FF: synthetic method
   static AsyncTimeout access$000() {
      return head;
   }

   // $FF: synthetic method
   static AsyncTimeout access$002(AsyncTimeout var0) {
      head = var0;
      return var0;
   }

   static AsyncTimeout awaitTimeout() throws InterruptedException {
      Object var0 = null;
      AsyncTimeout var1 = head.next;
      long var2;
      if (var1 == null) {
         var2 = System.nanoTime();
         AsyncTimeout.class.wait(IDLE_TIMEOUT_MILLIS);
         var1 = (AsyncTimeout)var0;
         if (head.next == null) {
            var1 = (AsyncTimeout)var0;
            if (System.nanoTime() - var2 >= IDLE_TIMEOUT_NANOS) {
               var1 = head;
            }
         }
      } else {
         var2 = var1.remainingNanos(System.nanoTime());
         if (var2 > 0L) {
            long var4 = var2 / 1000000L;
            AsyncTimeout.class.wait(var4, (int)(var2 - var4 * 1000000L));
            var1 = (AsyncTimeout)var0;
         } else {
            head.next = var1.next;
            var1.next = null;
         }
      }

      return var1;
   }

   private static boolean cancelScheduledTimeout(AsyncTimeout var0) {
      synchronized(AsyncTimeout.class){}

      boolean var2;
      label153: {
         label152: {
            Throwable var10000;
            label151: {
               AsyncTimeout var1;
               boolean var10001;
               try {
                  var1 = head;
               } catch (Throwable var14) {
                  var10000 = var14;
                  var10001 = false;
                  break label151;
               }

               while(true) {
                  if (var1 == null) {
                     var2 = true;
                     break label153;
                  }

                  try {
                     if (var1.next == var0) {
                        var1.next = var0.next;
                        var0.next = null;
                        break label152;
                     }
                  } catch (Throwable var13) {
                     var10000 = var13;
                     var10001 = false;
                     break;
                  }

                  try {
                     var1 = var1.next;
                  } catch (Throwable var12) {
                     var10000 = var12;
                     var10001 = false;
                     break;
                  }
               }
            }

            Throwable var15 = var10000;
            throw var15;
         }

         var2 = false;
      }

      return var2;
   }

   private long remainingNanos(long var1) {
      return this.timeoutAt - var1;
   }

   private static void scheduleTimeout(AsyncTimeout var0, long var1, boolean var3) {
      synchronized(AsyncTimeout.class){}

      Throwable var10000;
      label969: {
         AsyncTimeout var4;
         boolean var10001;
         try {
            if (head == null) {
               var4 = new AsyncTimeout();
               head = var4;
               AsyncTimeout.Watchdog var119 = new AsyncTimeout.Watchdog();
               var119.start();
            }
         } catch (Throwable var116) {
            var10000 = var116;
            var10001 = false;
            break label969;
         }

         long var5;
         try {
            var5 = System.nanoTime();
         } catch (Throwable var115) {
            var10000 = var115;
            var10001 = false;
            break label969;
         }

         if (var1 != 0L && var3) {
            try {
               var0.timeoutAt = Math.min(var1, var0.deadlineNanoTime() - var5) + var5;
            } catch (Throwable var114) {
               var10000 = var114;
               var10001 = false;
               break label969;
            }
         } else if (var1 != 0L) {
            try {
               var0.timeoutAt = var5 + var1;
            } catch (Throwable var113) {
               var10000 = var113;
               var10001 = false;
               break label969;
            }
         } else {
            if (!var3) {
               try {
                  AssertionError var117 = new AssertionError();
                  throw var117;
               } catch (Throwable var111) {
                  var10000 = var111;
                  var10001 = false;
                  break label969;
               }
            }

            try {
               var0.timeoutAt = var0.deadlineNanoTime();
            } catch (Throwable var112) {
               var10000 = var112;
               var10001 = false;
               break label969;
            }
         }

         try {
            var1 = var0.remainingNanos(var5);
            var4 = head;
         } catch (Throwable var109) {
            var10000 = var109;
            var10001 = false;
            break label969;
         }

         while(true) {
            try {
               if (var4.next == null || var1 < var4.next.remainingNanos(var5)) {
                  break;
               }
            } catch (Throwable var110) {
               var10000 = var110;
               var10001 = false;
               break label969;
            }

            try {
               var4 = var4.next;
            } catch (Throwable var108) {
               var10000 = var108;
               var10001 = false;
               break label969;
            }
         }

         try {
            var0.next = var4.next;
            var4.next = var0;
            if (var4 == head) {
               AsyncTimeout.class.notify();
            }
         } catch (Throwable var107) {
            var10000 = var107;
            var10001 = false;
            break label969;
         }

         return;
      }

      Throwable var118 = var10000;
      throw var118;
   }

   public final void enter() {
      if (this.inQueue) {
         throw new IllegalStateException("Unbalanced enter/exit");
      } else {
         long var1 = this.timeoutNanos();
         boolean var3 = this.hasDeadline();
         if (var1 != 0L || var3) {
            this.inQueue = true;
            scheduleTimeout(this, var1, var3);
         }

      }
   }

   final IOException exit(IOException var1) throws IOException {
      if (this.exit()) {
         var1 = this.newTimeoutException(var1);
      }

      return var1;
   }

   final void exit(boolean var1) throws IOException {
      if (this.exit() && var1) {
         throw this.newTimeoutException((IOException)null);
      }
   }

   public final boolean exit() {
      boolean var1 = false;
      if (this.inQueue) {
         this.inQueue = false;
         var1 = cancelScheduledTimeout(this);
      }

      return var1;
   }

   protected IOException newTimeoutException(IOException var1) {
      InterruptedIOException var2 = new InterruptedIOException("timeout");
      if (var1 != null) {
         var2.initCause(var1);
      }

      return var2;
   }

   public final Sink sink(final Sink var1) {
      return new Sink() {
         public void close() throws IOException {
            AsyncTimeout.this.enter();
            boolean var4 = false;

            try {
               var4 = true;
               var1.close();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public void flush() throws IOException {
            AsyncTimeout.this.enter();
            boolean var4 = false;

            try {
               var4 = true;
               var1.flush();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public Timeout timeout() {
            return AsyncTimeout.this;
         }

         public String toString() {
            return "AsyncTimeout.sink(" + var1 + ")";
         }

         public void write(Buffer var1x, long var2) throws IOException {
            Util.checkOffsetAndCount(var1x.size, 0L, var2);

            while(var2 > 0L) {
               long var4 = 0L;
               Segment var6 = var1x.head;

               long var7;
               while(true) {
                  var7 = var4;
                  if (var4 >= 65536L) {
                     break;
                  }

                  var4 += (long)(var1x.head.limit - var1x.head.pos);
                  if (var4 >= var2) {
                     var7 = var2;
                     break;
                  }

                  var6 = var6.next;
               }

               AsyncTimeout.this.enter();
               boolean var11 = false;

               try {
                  var11 = true;
                  var1.write(var1x, var7);
                  var11 = false;
               } catch (IOException var12) {
                  throw AsyncTimeout.this.exit(var12);
               } finally {
                  if (var11) {
                     AsyncTimeout.this.exit(false);
                  }
               }

               var2 -= var7;
               AsyncTimeout.this.exit(true);
            }

         }
      };
   }

   public final Source source(final Source var1) {
      return new Source() {
         public void close() throws IOException {
            boolean var4 = false;

            try {
               var4 = true;
               var1.close();
               var4 = false;
            } catch (IOException var5) {
               throw AsyncTimeout.this.exit(var5);
            } finally {
               if (var4) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
         }

         public long read(Buffer var1x, long var2) throws IOException {
            AsyncTimeout.this.enter();
            boolean var6 = false;

            try {
               var6 = true;
               var2 = var1.read(var1x, var2);
               var6 = false;
            } catch (IOException var7) {
               throw AsyncTimeout.this.exit(var7);
            } finally {
               if (var6) {
                  AsyncTimeout.this.exit(false);
               }
            }

            AsyncTimeout.this.exit(true);
            return var2;
         }

         public Timeout timeout() {
            return AsyncTimeout.this;
         }

         public String toString() {
            return "AsyncTimeout.source(" + var1 + ")";
         }
      };
   }

   protected void timedOut() {
   }

   private static final class Watchdog extends Thread {
      public Watchdog() {
         super("Okio Watchdog");
         this.setDaemon(true);
      }

      public void run() {
         // $FF: Couldn't be decompiled
      }
   }
}
