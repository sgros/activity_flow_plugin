// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.threadutils;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.Future;
import java.util.concurrent.Callable;
import java.util.concurrent.ThreadFactory;
import android.os.Looper;
import java.util.concurrent.Executors;
import android.os.Handler;
import java.util.concurrent.ExecutorService;

public class ThreadUtils
{
    private static final ExecutorService backgroundExecutorService;
    private static final Handler handler;
    private static final Thread uiThread;
    
    static {
        backgroundExecutorService = Executors.newSingleThreadExecutor(getIoPrioritisedFactory());
        handler = new Handler(Looper.getMainLooper());
        uiThread = Looper.getMainLooper().getThread();
    }
    
    private static ThreadFactory getIoPrioritisedFactory() {
        return new CustomThreadFactory("pool-io-background", 4);
    }
    
    public static <V> Future<V> postToBackgroundThread(final Callable<V> callable) {
        return ThreadUtils.backgroundExecutorService.submit(callable);
    }
    
    public static void postToBackgroundThread(final Runnable runnable) {
        ThreadUtils.backgroundExecutorService.submit(runnable);
    }
    
    public static void postToMainThread(final Runnable runnable) {
        ThreadUtils.handler.post(runnable);
    }
    
    public static void postToMainThreadDelayed(final Runnable runnable, final long n) {
        ThreadUtils.handler.postDelayed(runnable, n);
    }
    
    private static class CustomThreadFactory implements ThreadFactory
    {
        private final AtomicInteger mNumber;
        private final String threadName;
        private final int threadPriority;
        
        public CustomThreadFactory(final String threadName, final int threadPriority) {
            this.mNumber = new AtomicInteger();
            this.threadName = threadName;
            this.threadPriority = threadPriority;
        }
        
        @Override
        public Thread newThread(final Runnable target) {
            final StringBuilder sb = new StringBuilder();
            sb.append(this.threadName);
            sb.append("-");
            sb.append(this.mNumber.getAndIncrement());
            final Thread thread = new Thread(target, sb.toString());
            thread.setPriority(this.threadPriority);
            return thread;
        }
    }
}
