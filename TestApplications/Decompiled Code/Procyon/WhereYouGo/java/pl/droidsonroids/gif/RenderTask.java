// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.concurrent.TimeUnit;
import android.os.SystemClock;

class RenderTask extends SafeRunnable
{
    RenderTask(final GifDrawable gifDrawable) {
        super(gifDrawable);
    }
    
    public void doWork() {
        final long renderFrame = this.mGifDrawable.mNativeInfoHandle.renderFrame(this.mGifDrawable.mBuffer);
        if (renderFrame >= 0L) {
            this.mGifDrawable.mNextFrameRenderTime = SystemClock.uptimeMillis() + renderFrame;
            if (this.mGifDrawable.isVisible() && this.mGifDrawable.mIsRunning && !this.mGifDrawable.mIsRenderingTriggeredOnDraw) {
                this.mGifDrawable.mExecutor.remove(this);
                this.mGifDrawable.mRenderTaskSchedule = this.mGifDrawable.mExecutor.schedule(this, renderFrame, TimeUnit.MILLISECONDS);
            }
            if (!this.mGifDrawable.mListeners.isEmpty() && this.mGifDrawable.getCurrentFrameIndex() == this.mGifDrawable.mNativeInfoHandle.getNumberOfFrames() - 1) {
                this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(this.mGifDrawable.getCurrentLoop(), this.mGifDrawable.mNextFrameRenderTime);
            }
        }
        else {
            this.mGifDrawable.mNextFrameRenderTime = Long.MIN_VALUE;
            this.mGifDrawable.mIsRunning = false;
        }
        if (this.mGifDrawable.isVisible() && !this.mGifDrawable.mInvalidationHandler.hasMessages(-1)) {
            this.mGifDrawable.mInvalidationHandler.sendEmptyMessageAtTime(-1, 0L);
        }
    }
}
