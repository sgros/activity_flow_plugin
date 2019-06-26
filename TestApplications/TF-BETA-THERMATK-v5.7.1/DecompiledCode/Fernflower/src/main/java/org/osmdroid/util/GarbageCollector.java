package org.osmdroid.util;

import java.util.concurrent.atomic.AtomicBoolean;

public class GarbageCollector {
   private final Runnable mAction;
   private final AtomicBoolean mRunning = new AtomicBoolean(false);

   public GarbageCollector(Runnable var1) {
      this.mAction = var1;
   }

   public boolean gc() {
      if (this.mRunning.getAndSet(true)) {
         return false;
      } else {
         Thread var1 = new Thread(new Runnable() {
            public void run() {
               try {
                  GarbageCollector.this.mAction.run();
               } finally {
                  GarbageCollector.this.mRunning.set(false);
               }

            }
         });
         var1.setPriority(1);
         var1.start();
         return true;
      }
   }

   public boolean isRunning() {
      return this.mRunning.get();
   }
}
