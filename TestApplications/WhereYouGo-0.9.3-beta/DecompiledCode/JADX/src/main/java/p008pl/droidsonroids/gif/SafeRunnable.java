package p008pl.droidsonroids.gif;

import java.lang.Thread.UncaughtExceptionHandler;

/* renamed from: pl.droidsonroids.gif.SafeRunnable */
abstract class SafeRunnable implements Runnable {
    final GifDrawable mGifDrawable;

    public abstract void doWork();

    SafeRunnable(GifDrawable gifDrawable) {
        this.mGifDrawable = gifDrawable;
    }

    public final void run() {
        try {
            if (!this.mGifDrawable.isRecycled()) {
                doWork();
            }
        } catch (Throwable throwable) {
            UncaughtExceptionHandler uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (uncaughtExceptionHandler != null) {
                uncaughtExceptionHandler.uncaughtException(Thread.currentThread(), throwable);
            }
        }
    }
}
