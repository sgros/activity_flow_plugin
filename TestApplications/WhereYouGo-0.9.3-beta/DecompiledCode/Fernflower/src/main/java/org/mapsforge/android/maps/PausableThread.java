package org.mapsforge.android.maps;

public abstract class PausableThread extends Thread {
   private boolean pausing;
   private boolean shouldPause;

   protected void afterPause() {
   }

   protected void afterRun() {
   }

   public final void awaitPausing() {
      // $FF: Couldn't be decompiled
   }

   protected abstract void doWork() throws InterruptedException;

   protected abstract String getThreadName();

   protected abstract PausableThread.ThreadPriority getThreadPriority();

   protected abstract boolean hasWork();

   public void interrupt() {
      // $FF: Couldn't be decompiled
   }

   public final boolean isPausing() {
      synchronized(this){}

      boolean var1;
      try {
         var1 = this.pausing;
      } finally {
         ;
      }

      return var1;
   }

   public final void pause() {
      synchronized(this){}

      try {
         if (!this.shouldPause) {
            this.shouldPause = true;
            this.notify();
         }
      } finally {
         ;
      }

   }

   public final void proceed() {
      synchronized(this){}

      try {
         if (this.shouldPause) {
            this.shouldPause = false;
            this.pausing = false;
            this.afterPause();
            this.notify();
         }
      } finally {
         ;
      }

   }

   public final void run() {
      // $FF: Couldn't be decompiled
   }

   protected static enum ThreadPriority {
      ABOVE_NORMAL(7),
      BELOW_NORMAL(3),
      HIGHEST(10),
      LOWEST(1),
      NORMAL(5);

      final int priority;

      private ThreadPriority(int var3) {
         if (var3 >= 1 && var3 <= 10) {
            this.priority = var3;
         } else {
            throw new IllegalArgumentException("invalid priority: " + var3);
         }
      }
   }
}
