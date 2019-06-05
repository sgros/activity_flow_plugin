// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

abstract class SafeRunnable implements Runnable
{
    final GifDrawable mGifDrawable;
    
    SafeRunnable(final GifDrawable mGifDrawable) {
        this.mGifDrawable = mGifDrawable;
    }
    
    abstract void doWork();
    
    @Override
    public final void run() {
        try {
            if (!this.mGifDrawable.isRecycled()) {
                this.doWork();
            }
        }
        catch (Throwable t) {
            final Thread.UncaughtExceptionHandler defaultUncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
            if (defaultUncaughtExceptionHandler != null) {
                defaultUncaughtExceptionHandler.uncaughtException(Thread.currentThread(), t);
            }
            throw t;
        }
    }
}
