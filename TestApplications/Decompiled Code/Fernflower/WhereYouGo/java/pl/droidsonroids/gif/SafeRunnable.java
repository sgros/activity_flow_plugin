package pl.droidsonroids.gif;

import java.lang.Thread.UncaughtExceptionHandler;

abstract class SafeRunnable implements Runnable {
   final GifDrawable mGifDrawable;

   SafeRunnable(GifDrawable var1) {
      this.mGifDrawable = var1;
   }

   abstract void doWork();

   public final void run() {
      try {
         if (!this.mGifDrawable.isRecycled()) {
            this.doWork();
         }

      } catch (Throwable var3) {
         UncaughtExceptionHandler var2 = Thread.getDefaultUncaughtExceptionHandler();
         if (var2 != null) {
            var2.uncaughtException(Thread.currentThread(), var3);
         }

         throw var3;
      }
   }
}
