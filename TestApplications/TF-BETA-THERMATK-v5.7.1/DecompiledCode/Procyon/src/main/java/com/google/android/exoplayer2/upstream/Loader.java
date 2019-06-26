// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.TraceUtil;
import com.google.android.exoplayer2.util.Log;
import android.os.Message;
import android.annotation.SuppressLint;
import android.os.Handler;
import android.os.SystemClock;
import com.google.android.exoplayer2.util.Assertions;
import android.os.Looper;
import com.google.android.exoplayer2.util.Util;
import java.io.IOException;
import java.util.concurrent.ExecutorService;

public final class Loader implements LoaderErrorThrower
{
    public static final LoadErrorAction DONT_RETRY;
    public static final LoadErrorAction DONT_RETRY_FATAL;
    public static final LoadErrorAction RETRY;
    public static final LoadErrorAction RETRY_RESET_ERROR_COUNT;
    private LoadTask<? extends Loadable> currentTask;
    private final ExecutorService downloadExecutorService;
    private IOException fatalError;
    
    static {
        RETRY = createRetryAction(false, -9223372036854775807L);
        RETRY_RESET_ERROR_COUNT = createRetryAction(true, -9223372036854775807L);
        DONT_RETRY = new LoadErrorAction(2, -9223372036854775807L);
        DONT_RETRY_FATAL = new LoadErrorAction(3, -9223372036854775807L);
    }
    
    public Loader(final String s) {
        this.downloadExecutorService = Util.newSingleThreadExecutor(s);
    }
    
    public static LoadErrorAction createRetryAction(final boolean b, final long n) {
        return new LoadErrorAction((int)(b ? 1 : 0), n);
    }
    
    public void cancelLoading() {
        this.currentTask.cancel(false);
    }
    
    public boolean isLoading() {
        return this.currentTask != null;
    }
    
    @Override
    public void maybeThrowError() throws IOException {
        this.maybeThrowError(Integer.MIN_VALUE);
    }
    
    public void maybeThrowError(final int n) throws IOException {
        final IOException fatalError = this.fatalError;
        if (fatalError == null) {
            final LoadTask<? extends Loadable> currentTask = this.currentTask;
            if (currentTask != null) {
                int defaultMinRetryCount;
                if ((defaultMinRetryCount = n) == Integer.MIN_VALUE) {
                    defaultMinRetryCount = currentTask.defaultMinRetryCount;
                }
                currentTask.maybeThrowError(defaultMinRetryCount);
            }
            return;
        }
        throw fatalError;
    }
    
    public void release() {
        this.release(null);
    }
    
    public void release(final ReleaseCallback releaseCallback) {
        final LoadTask<? extends Loadable> currentTask = this.currentTask;
        if (currentTask != null) {
            currentTask.cancel(true);
        }
        if (releaseCallback != null) {
            this.downloadExecutorService.execute(new ReleaseTask(releaseCallback));
        }
        this.downloadExecutorService.shutdown();
    }
    
    public <T extends Loadable> long startLoading(final T t, final Callback<T> callback, final int n) {
        final Looper myLooper = Looper.myLooper();
        Assertions.checkState(myLooper != null);
        this.fatalError = null;
        final long elapsedRealtime = SystemClock.elapsedRealtime();
        new LoadTask(myLooper, (Loadable)t, (Callback<Loadable>)callback, n, elapsedRealtime).start(0L);
        return elapsedRealtime;
    }
    
    public interface Callback<T extends Loadable>
    {
        void onLoadCanceled(final T p0, final long p1, final long p2, final boolean p3);
        
        void onLoadCompleted(final T p0, final long p1, final long p2);
        
        LoadErrorAction onLoadError(final T p0, final long p1, final long p2, final IOException p3, final int p4);
    }
    
    public static final class LoadErrorAction
    {
        private final long retryDelayMillis;
        private final int type;
        
        private LoadErrorAction(final int type, final long retryDelayMillis) {
            this.type = type;
            this.retryDelayMillis = retryDelayMillis;
        }
        
        public boolean isRetry() {
            final int type = this.type;
            boolean b = true;
            if (type != 0) {
                b = (type == 1 && b);
            }
            return b;
        }
    }
    
    @SuppressLint({ "HandlerLeak" })
    private final class LoadTask<T extends Loadable> extends Handler implements Runnable
    {
        private Callback<T> callback;
        private volatile boolean canceled;
        private IOException currentError;
        public final int defaultMinRetryCount;
        private int errorCount;
        private volatile Thread executorThread;
        private final T loadable;
        private volatile boolean released;
        private final long startTimeMs;
        
        public LoadTask(final Looper looper, final T loadable, final Callback<T> callback, final int defaultMinRetryCount, final long startTimeMs) {
            super(looper);
            this.loadable = loadable;
            this.callback = callback;
            this.defaultMinRetryCount = defaultMinRetryCount;
            this.startTimeMs = startTimeMs;
        }
        
        private void execute() {
            this.currentError = null;
            Loader.this.downloadExecutorService.execute(Loader.this.currentTask);
        }
        
        private void finish() {
            Loader.this.currentTask = null;
        }
        
        private long getRetryDelayMillis() {
            return Math.min((this.errorCount - 1) * 1000, 5000);
        }
        
        public void cancel(final boolean released) {
            this.released = released;
            this.currentError = null;
            if (this.hasMessages(0)) {
                this.removeMessages(0);
                if (!released) {
                    this.sendEmptyMessage(1);
                }
            }
            else {
                this.canceled = true;
                ((Loadable)this.loadable).cancelLoad();
                if (this.executorThread != null) {
                    this.executorThread.interrupt();
                }
            }
            if (released) {
                this.finish();
                final long elapsedRealtime = SystemClock.elapsedRealtime();
                this.callback.onLoadCanceled(this.loadable, elapsedRealtime, elapsedRealtime - this.startTimeMs, true);
                this.callback = null;
            }
        }
        
        public void handleMessage(final Message message) {
            if (this.released) {
                return;
            }
            final int what = message.what;
            if (what == 0) {
                this.execute();
                return;
            }
            if (what == 4) {
                throw (Error)message.obj;
            }
            this.finish();
            final long elapsedRealtime = SystemClock.elapsedRealtime();
            final long n = elapsedRealtime - this.startTimeMs;
            if (this.canceled) {
                this.callback.onLoadCanceled(this.loadable, elapsedRealtime, n, false);
                return;
            }
            final int what2 = message.what;
            if (what2 != 1) {
                if (what2 != 2) {
                    if (what2 == 3) {
                        this.currentError = (IOException)message.obj;
                        ++this.errorCount;
                        final LoadErrorAction onLoadError = this.callback.onLoadError(this.loadable, elapsedRealtime, n, this.currentError, this.errorCount);
                        if (onLoadError.type == 3) {
                            Loader.this.fatalError = this.currentError;
                        }
                        else if (onLoadError.type != 2) {
                            if (onLoadError.type == 1) {
                                this.errorCount = 1;
                            }
                            long n2;
                            if (onLoadError.retryDelayMillis != -9223372036854775807L) {
                                n2 = onLoadError.retryDelayMillis;
                            }
                            else {
                                n2 = this.getRetryDelayMillis();
                            }
                            this.start(n2);
                        }
                    }
                }
                else {
                    try {
                        this.callback.onLoadCompleted(this.loadable, elapsedRealtime, n);
                    }
                    catch (RuntimeException ex) {
                        Log.e("LoadTask", "Unexpected exception handling load completed", ex);
                        Loader.this.fatalError = new UnexpectedLoaderException(ex);
                    }
                }
            }
            else {
                this.callback.onLoadCanceled(this.loadable, elapsedRealtime, n, false);
            }
        }
        
        public void maybeThrowError(final int n) throws IOException {
            final IOException currentError = this.currentError;
            if (currentError != null && this.errorCount > n) {
                throw currentError;
            }
        }
        
        public void run() {
            try {
                this.executorThread = Thread.currentThread();
                if (!this.canceled) {
                    final StringBuilder sb = new StringBuilder();
                    sb.append("load:");
                    sb.append(this.loadable.getClass().getSimpleName());
                    TraceUtil.beginSection(sb.toString());
                    try {
                        ((Loadable)this.loadable).load();
                    }
                    finally {
                        TraceUtil.endSection();
                    }
                }
                if (!this.released) {
                    this.sendEmptyMessage(2);
                }
            }
            catch (Error error) {
                Log.e("LoadTask", "Unexpected error loading stream", error);
                if (!this.released) {
                    this.obtainMessage(4, (Object)error).sendToTarget();
                }
                throw error;
            }
            catch (OutOfMemoryError outOfMemoryError) {
                Log.e("LoadTask", "OutOfMemory error loading stream", outOfMemoryError);
                if (!this.released) {
                    this.obtainMessage(3, (Object)new UnexpectedLoaderException(outOfMemoryError)).sendToTarget();
                }
            }
            catch (Exception ex) {
                Log.e("LoadTask", "Unexpected exception loading stream", ex);
                if (!this.released) {
                    this.obtainMessage(3, (Object)new UnexpectedLoaderException(ex)).sendToTarget();
                }
            }
            catch (InterruptedException ex3) {
                Assertions.checkState(this.canceled);
                if (!this.released) {
                    this.sendEmptyMessage(2);
                }
            }
            catch (IOException ex2) {
                if (!this.released) {
                    this.obtainMessage(3, (Object)ex2).sendToTarget();
                }
            }
        }
        
        public void start(final long n) {
            Assertions.checkState(Loader.this.currentTask == null);
            Loader.this.currentTask = (LoadTask<? extends Loadable>)this;
            if (n > 0L) {
                this.sendEmptyMessageDelayed(0, n);
            }
            else {
                this.execute();
            }
        }
    }
    
    public interface Loadable
    {
        void cancelLoad();
        
        void load() throws IOException, InterruptedException;
    }
    
    public interface ReleaseCallback
    {
        void onLoaderReleased();
    }
    
    private static final class ReleaseTask implements Runnable
    {
        private final ReleaseCallback callback;
        
        public ReleaseTask(final ReleaseCallback callback) {
            this.callback = callback;
        }
        
        @Override
        public void run() {
            this.callback.onLoaderReleased();
        }
    }
    
    public static final class UnexpectedLoaderException extends IOException
    {
        public UnexpectedLoaderException(final Throwable cause) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Unexpected ");
            sb.append(cause.getClass().getSimpleName());
            sb.append(": ");
            sb.append(cause.getMessage());
            super(sb.toString(), cause);
        }
    }
}
