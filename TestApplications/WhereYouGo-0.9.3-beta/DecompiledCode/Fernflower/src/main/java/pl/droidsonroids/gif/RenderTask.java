package pl.droidsonroids.gif;

import android.os.SystemClock;
import java.util.concurrent.TimeUnit;

class RenderTask extends SafeRunnable {
   RenderTask(GifDrawable var1) {
      super(var1);
   }

   public void doWork() {
      long var1 = this.mGifDrawable.mNativeInfoHandle.renderFrame(this.mGifDrawable.mBuffer);
      if (var1 >= 0L) {
         this.mGifDrawable.mNextFrameRenderTime = SystemClock.uptimeMillis() + var1;
         if (this.mGifDrawable.isVisible() && this.mGifDrawable.mIsRunning && !this.mGifDrawable.mIsRenderingTriggeredOnDraw) {
            this.mGifDrawable.mExecutor.remove(this);
            this.mGifDrawable.mRenderTaskSchedule = this.mGifDrawable.mExecutor.schedule(this, var1, TimeUnit.MILLISECONDS);
         }

         if (!this.mGifDrawable.mListeners.isEmpty() && this.mGifDrawable.getCurrentFrameIndex() == this.mGifDrawable.mNativeInfoHandle.getNumberOfFrames() - 1) {
            this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(this.mGifDrawable.getCurrentLoop(), this.mGifDrawable.mNextFrameRenderTime);
         }
      } else {
         this.mGifDrawable.mNextFrameRenderTime = Long.MIN_VALUE;
         this.mGifDrawable.mIsRunning = false;
      }

      if (this.mGifDrawable.isVisible() && !this.mGifDrawable.mInvalidationHandler.hasMessages(-1)) {
         this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
      }

   }
}
