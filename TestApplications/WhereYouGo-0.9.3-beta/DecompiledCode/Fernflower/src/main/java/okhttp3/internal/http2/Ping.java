package okhttp3.internal.http2;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

final class Ping {
   private final CountDownLatch latch = new CountDownLatch(1);
   private long received = -1L;
   private long sent = -1L;

   void cancel() {
      if (this.received == -1L && this.sent != -1L) {
         this.received = this.sent - 1L;
         this.latch.countDown();
      } else {
         throw new IllegalStateException();
      }
   }

   void receive() {
      if (this.received == -1L && this.sent != -1L) {
         this.received = System.nanoTime();
         this.latch.countDown();
      } else {
         throw new IllegalStateException();
      }
   }

   public long roundTripTime() throws InterruptedException {
      this.latch.await();
      return this.received - this.sent;
   }

   public long roundTripTime(long var1, TimeUnit var3) throws InterruptedException {
      if (this.latch.await(var1, var3)) {
         var1 = this.received - this.sent;
      } else {
         var1 = -2L;
      }

      return var1;
   }

   void send() {
      if (this.sent != -1L) {
         throw new IllegalStateException();
      } else {
         this.sent = System.nanoTime();
      }
   }
}
