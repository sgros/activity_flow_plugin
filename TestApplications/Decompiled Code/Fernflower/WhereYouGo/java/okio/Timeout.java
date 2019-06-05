package okio;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.util.concurrent.TimeUnit;

public class Timeout {
   public static final Timeout NONE = new Timeout() {
      public Timeout deadlineNanoTime(long var1) {
         return this;
      }

      public void throwIfReached() throws IOException {
      }

      public Timeout timeout(long var1, TimeUnit var3) {
         return this;
      }
   };
   private long deadlineNanoTime;
   private boolean hasDeadline;
   private long timeoutNanos;

   public Timeout clearDeadline() {
      this.hasDeadline = false;
      return this;
   }

   public Timeout clearTimeout() {
      this.timeoutNanos = 0L;
      return this;
   }

   public final Timeout deadline(long var1, TimeUnit var3) {
      if (var1 <= 0L) {
         throw new IllegalArgumentException("duration <= 0: " + var1);
      } else if (var3 == null) {
         throw new IllegalArgumentException("unit == null");
      } else {
         return this.deadlineNanoTime(System.nanoTime() + var3.toNanos(var1));
      }
   }

   public long deadlineNanoTime() {
      if (!this.hasDeadline) {
         throw new IllegalStateException("No deadline");
      } else {
         return this.deadlineNanoTime;
      }
   }

   public Timeout deadlineNanoTime(long var1) {
      this.hasDeadline = true;
      this.deadlineNanoTime = var1;
      return this;
   }

   public boolean hasDeadline() {
      return this.hasDeadline;
   }

   public void throwIfReached() throws IOException {
      if (Thread.interrupted()) {
         throw new InterruptedIOException("thread interrupted");
      } else if (this.hasDeadline && this.deadlineNanoTime - System.nanoTime() <= 0L) {
         throw new InterruptedIOException("deadline reached");
      }
   }

   public Timeout timeout(long var1, TimeUnit var3) {
      if (var1 < 0L) {
         throw new IllegalArgumentException("timeout < 0: " + var1);
      } else if (var3 == null) {
         throw new IllegalArgumentException("unit == null");
      } else {
         this.timeoutNanos = var3.toNanos(var1);
         return this;
      }
   }

   public long timeoutNanos() {
      return this.timeoutNanos;
   }

   public final void waitUntilNotified(Object var1) throws InterruptedIOException {
      boolean var10001;
      boolean var2;
      long var3;
      try {
         var2 = this.hasDeadline();
         var3 = this.timeoutNanos();
      } catch (InterruptedException var15) {
         var10001 = false;
         throw new InterruptedIOException("interrupted");
      }

      if (!var2 && var3 == 0L) {
         try {
            var1.wait();
            return;
         } catch (InterruptedException var14) {
            var10001 = false;
         }
      } else {
         long var5;
         try {
            var5 = System.nanoTime();
         } catch (InterruptedException var13) {
            var10001 = false;
            throw new InterruptedIOException("interrupted");
         }

         if (var2 && var3 != 0L) {
            try {
               var3 = Math.min(var3, this.deadlineNanoTime() - var5);
            } catch (InterruptedException var12) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }
         } else if (var2) {
            try {
               var3 = this.deadlineNanoTime();
            } catch (InterruptedException var11) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }

            var3 -= var5;
         }

         long var7 = 0L;
         if (var3 > 0L) {
            try {
               var7 = var3 / 1000000L;
               var1.wait(var7, (int)(var3 - 1000000L * var7));
               var7 = System.nanoTime() - var5;
            } catch (InterruptedException var10) {
               var10001 = false;
               throw new InterruptedIOException("interrupted");
            }
         }

         if (var7 < var3) {
            return;
         }

         try {
            InterruptedIOException var16 = new InterruptedIOException("timeout");
            throw var16;
         } catch (InterruptedException var9) {
            var10001 = false;
         }
      }

      throw new InterruptedIOException("interrupted");
   }
}
