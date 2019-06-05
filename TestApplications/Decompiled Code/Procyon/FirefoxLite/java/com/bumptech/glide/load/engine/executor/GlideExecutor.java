// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.load.engine.executor;

import android.os.StrictMode$ThreadPolicy$Builder;
import android.os.Process;
import java.util.concurrent.Callable;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import android.os.StrictMode$ThreadPolicy;
import android.util.Log;
import java.io.FilenameFilter;
import java.util.regex.Pattern;
import java.io.File;
import android.os.StrictMode;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.ThreadPoolExecutor;

public final class GlideExecutor extends ThreadPoolExecutor
{
    private static final long SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS;
    private final boolean executeSynchronously;
    
    static {
        SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10L);
    }
    
    GlideExecutor(final int n, final int n2, final long n3, final String s, final UncaughtThrowableStrategy uncaughtThrowableStrategy, final boolean b, final boolean b2) {
        this(n, n2, n3, s, uncaughtThrowableStrategy, b, b2, new PriorityBlockingQueue<Runnable>());
    }
    
    GlideExecutor(final int corePoolSize, final int maximumPoolSize, final long keepAliveTime, final String s, final UncaughtThrowableStrategy uncaughtThrowableStrategy, final boolean b, final boolean executeSynchronously, final BlockingQueue<Runnable> workQueue) {
        super(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue, new DefaultThreadFactory(s, uncaughtThrowableStrategy, b));
        this.executeSynchronously = executeSynchronously;
    }
    
    GlideExecutor(final int n, final String s, final UncaughtThrowableStrategy uncaughtThrowableStrategy, final boolean b, final boolean b2) {
        this(n, n, 0L, s, uncaughtThrowableStrategy, b, b2);
    }
    
    public static int calculateBestThreadCount() {
        final StrictMode$ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        Throwable t2 = null;
        try {
            try {
                new File("/sys/devices/system/cpu/").listFiles(new FilenameFilter() {
                    final /* synthetic */ Pattern val$cpuNamePattern = Pattern.compile("cpu[0-9]+");
                    
                    @Override
                    public boolean accept(final File file, final String input) {
                        return this.val$cpuNamePattern.matcher(input).matches();
                    }
                });
                StrictMode.setThreadPolicy(allowThreadDiskReads);
            }
            finally {}
        }
        catch (Throwable t) {
            if (Log.isLoggable("GlideExecutor", 6)) {
                Log.e("GlideExecutor", "Failed to calculate accurate cpu count", t);
            }
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            t2 = null;
        }
        int length;
        if (t2 != null) {
            length = t2.length;
        }
        else {
            length = 0;
        }
        return Math.min(4, Math.max(Math.max(1, Runtime.getRuntime().availableProcessors()), length));
        StrictMode.setThreadPolicy(allowThreadDiskReads);
    }
    
    private <T> Future<T> maybeWait(final Future<T> future) {
        if (this.executeSynchronously) {
            boolean b = false;
            try {
                while (!future.isDone()) {
                    try {
                        future.get();
                        continue;
                    }
                    catch (InterruptedException ex) {
                        b = true;
                        continue;
                    }
                    catch (ExecutionException cause) {
                        throw new RuntimeException(cause);
                    }
                    break;
                }
            }
            finally {
                if (b) {
                    Thread.currentThread().interrupt();
                }
            }
        }
        return future;
    }
    
    public static GlideExecutor newDiskCacheExecutor() {
        return newDiskCacheExecutor(1, "disk-cache", UncaughtThrowableStrategy.DEFAULT);
    }
    
    public static GlideExecutor newDiskCacheExecutor(final int n, final String s, final UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(n, s, uncaughtThrowableStrategy, true, false);
    }
    
    public static GlideExecutor newSourceExecutor() {
        return newSourceExecutor(calculateBestThreadCount(), "source", UncaughtThrowableStrategy.DEFAULT);
    }
    
    public static GlideExecutor newSourceExecutor(final int n, final String s, final UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(n, s, uncaughtThrowableStrategy, false, false);
    }
    
    public static GlideExecutor newUnlimitedSourceExecutor() {
        return new GlideExecutor(0, Integer.MAX_VALUE, GlideExecutor.SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS, "source-unlimited", UncaughtThrowableStrategy.DEFAULT, false, false, new SynchronousQueue<Runnable>());
    }
    
    @Override
    public void execute(final Runnable command) {
        if (this.executeSynchronously) {
            command.run();
        }
        else {
            super.execute(command);
        }
    }
    
    @Override
    public Future<?> submit(final Runnable task) {
        return this.maybeWait(super.submit(task));
    }
    
    @Override
    public <T> Future<T> submit(final Runnable task, final T result) {
        return this.maybeWait((Future<T>)super.submit(task, (T)result));
    }
    
    @Override
    public <T> Future<T> submit(final Callable<T> task) {
        return this.maybeWait((Future<T>)super.submit((Callable<T>)task));
    }
    
    private static final class DefaultThreadFactory implements ThreadFactory
    {
        private final String name;
        final boolean preventNetworkOperations;
        private int threadNum;
        final UncaughtThrowableStrategy uncaughtThrowableStrategy;
        
        DefaultThreadFactory(final String name, final UncaughtThrowableStrategy uncaughtThrowableStrategy, final boolean preventNetworkOperations) {
            this.name = name;
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            this.preventNetworkOperations = preventNetworkOperations;
        }
        
        @Override
        public Thread newThread(final Runnable runnable) {
            synchronized (this) {
                final StringBuilder sb = new StringBuilder();
                sb.append("glide-");
                sb.append(this.name);
                sb.append("-thread-");
                sb.append(this.threadNum);
                final Thread thread = new Thread(runnable, sb.toString()) {
                    @Override
                    public void run() {
                        Process.setThreadPriority(9);
                        if (DefaultThreadFactory.this.preventNetworkOperations) {
                            StrictMode.setThreadPolicy(new StrictMode$ThreadPolicy$Builder().detectNetwork().penaltyDeath().build());
                        }
                        try {
                            super.run();
                        }
                        catch (Throwable t) {
                            DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(t);
                        }
                    }
                };
                ++this.threadNum;
                return thread;
            }
        }
    }
    
    public interface UncaughtThrowableStrategy
    {
        public static final UncaughtThrowableStrategy DEFAULT = UncaughtThrowableStrategy.LOG;
        public static final UncaughtThrowableStrategy IGNORE = new UncaughtThrowableStrategy() {
            @Override
            public void handle(final Throwable t) {
            }
        };
        public static final UncaughtThrowableStrategy LOG = new UncaughtThrowableStrategy() {
            @Override
            public void handle(final Throwable t) {
                if (t != null && Log.isLoggable("GlideExecutor", 6)) {
                    Log.e("GlideExecutor", "Request threw uncaught throwable", t);
                }
            }
        };
        public static final UncaughtThrowableStrategy THROW = new UncaughtThrowableStrategy() {
            @Override
            public void handle(final Throwable cause) {
                if (cause == null) {
                    return;
                }
                throw new RuntimeException("Request threw uncaught throwable", cause);
            }
        };
        
        void handle(final Throwable p0);
    }
}
