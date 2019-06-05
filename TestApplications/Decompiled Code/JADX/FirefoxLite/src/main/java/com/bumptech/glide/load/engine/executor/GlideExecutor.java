package com.bumptech.glide.load.engine.executor;

import android.os.Process;
import android.os.StrictMode;
import android.os.StrictMode.ThreadPolicy;
import android.os.StrictMode.ThreadPolicy.Builder;
import android.util.Log;
import java.io.File;
import java.io.FilenameFilter;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.PriorityBlockingQueue;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.regex.Pattern;

public final class GlideExecutor extends ThreadPoolExecutor {
    private static final long SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS = TimeUnit.SECONDS.toMillis(10);
    private final boolean executeSynchronously;

    private static final class DefaultThreadFactory implements ThreadFactory {
        private final String name;
        final boolean preventNetworkOperations;
        private int threadNum;
        final UncaughtThrowableStrategy uncaughtThrowableStrategy;

        DefaultThreadFactory(String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z) {
            this.name = str;
            this.uncaughtThrowableStrategy = uncaughtThrowableStrategy;
            this.preventNetworkOperations = z;
        }

        public synchronized Thread newThread(Runnable runnable) {
            C04021 c04021;
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("glide-");
            stringBuilder.append(this.name);
            stringBuilder.append("-thread-");
            stringBuilder.append(this.threadNum);
            c04021 = new Thread(runnable, stringBuilder.toString()) {
                public void run() {
                    Process.setThreadPriority(9);
                    if (DefaultThreadFactory.this.preventNetworkOperations) {
                        StrictMode.setThreadPolicy(new Builder().detectNetwork().penaltyDeath().build());
                    }
                    try {
                        super.run();
                    } catch (Throwable th) {
                        DefaultThreadFactory.this.uncaughtThrowableStrategy.handle(th);
                    }
                }
            };
            this.threadNum++;
            return c04021;
        }
    }

    public interface UncaughtThrowableStrategy {
        public static final UncaughtThrowableStrategy DEFAULT = LOG;
        public static final UncaughtThrowableStrategy IGNORE = new C04031();
        public static final UncaughtThrowableStrategy LOG = new C04042();
        public static final UncaughtThrowableStrategy THROW = new C04053();

        /* renamed from: com.bumptech.glide.load.engine.executor.GlideExecutor$UncaughtThrowableStrategy$1 */
        static class C04031 implements UncaughtThrowableStrategy {
            public void handle(Throwable th) {
            }

            C04031() {
            }
        }

        /* renamed from: com.bumptech.glide.load.engine.executor.GlideExecutor$UncaughtThrowableStrategy$2 */
        static class C04042 implements UncaughtThrowableStrategy {
            C04042() {
            }

            public void handle(Throwable th) {
                if (th != null && Log.isLoggable("GlideExecutor", 6)) {
                    Log.e("GlideExecutor", "Request threw uncaught throwable", th);
                }
            }
        }

        /* renamed from: com.bumptech.glide.load.engine.executor.GlideExecutor$UncaughtThrowableStrategy$3 */
        static class C04053 implements UncaughtThrowableStrategy {
            C04053() {
            }

            public void handle(Throwable th) {
                if (th != null) {
                    throw new RuntimeException("Request threw uncaught throwable", th);
                }
            }
        }

        void handle(Throwable th);
    }

    public static GlideExecutor newDiskCacheExecutor() {
        return newDiskCacheExecutor(1, "disk-cache", UncaughtThrowableStrategy.DEFAULT);
    }

    public static GlideExecutor newDiskCacheExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(i, str, uncaughtThrowableStrategy, true, false);
    }

    public static GlideExecutor newSourceExecutor() {
        return newSourceExecutor(calculateBestThreadCount(), "source", UncaughtThrowableStrategy.DEFAULT);
    }

    public static GlideExecutor newSourceExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy) {
        return new GlideExecutor(i, str, uncaughtThrowableStrategy, false, false);
    }

    public static GlideExecutor newUnlimitedSourceExecutor() {
        return new GlideExecutor(0, Integer.MAX_VALUE, SOURCE_UNLIMITED_EXECUTOR_KEEP_ALIVE_TIME_MS, "source-unlimited", UncaughtThrowableStrategy.DEFAULT, false, false, new SynchronousQueue());
    }

    GlideExecutor(int i, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2) {
        this(i, i, 0, str, uncaughtThrowableStrategy, z, z2);
    }

    GlideExecutor(int i, int i2, long j, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2) {
        this(i, i2, j, str, uncaughtThrowableStrategy, z, z2, new PriorityBlockingQueue());
    }

    GlideExecutor(int i, int i2, long j, String str, UncaughtThrowableStrategy uncaughtThrowableStrategy, boolean z, boolean z2, BlockingQueue<Runnable> blockingQueue) {
        String str2 = str;
        UncaughtThrowableStrategy uncaughtThrowableStrategy2 = uncaughtThrowableStrategy;
        boolean z3 = z;
        super(i, i2, j, TimeUnit.MILLISECONDS, blockingQueue, new DefaultThreadFactory(str, uncaughtThrowableStrategy, z));
        this.executeSynchronously = z2;
    }

    public void execute(Runnable runnable) {
        if (this.executeSynchronously) {
            runnable.run();
        } else {
            super.execute(runnable);
        }
    }

    public Future<?> submit(Runnable runnable) {
        return maybeWait(super.submit(runnable));
    }

    private <T> Future<T> maybeWait(Future<T> future) {
        if (this.executeSynchronously) {
            Object obj = null;
            while (!future.isDone()) {
                try {
                    future.get();
                } catch (ExecutionException e) {
                    throw new RuntimeException(e);
                } catch (InterruptedException unused) {
                    obj = 1;
                } catch (Throwable th) {
                    if (obj != null) {
                        Thread.currentThread().interrupt();
                    }
                }
            }
            if (obj != null) {
                Thread.currentThread().interrupt();
            }
        }
        return future;
    }

    public <T> Future<T> submit(Runnable runnable, T t) {
        return maybeWait(super.submit(runnable, t));
    }

    public <T> Future<T> submit(Callable<T> callable) {
        return maybeWait(super.submit(callable));
    }

    public static int calculateBestThreadCount() {
        File[] listFiles;
        ThreadPolicy allowThreadDiskReads = StrictMode.allowThreadDiskReads();
        try {
            File file = new File("/sys/devices/system/cpu/");
            final Pattern compile = Pattern.compile("cpu[0-9]+");
            listFiles = file.listFiles(new FilenameFilter() {
                public boolean accept(File file, String str) {
                    return compile.matcher(str).matches();
                }
            });
            StrictMode.setThreadPolicy(allowThreadDiskReads);
        } catch (Throwable th) {
            StrictMode.setThreadPolicy(allowThreadDiskReads);
            throw th;
        }
        return Math.min(4, Math.max(Math.max(1, Runtime.getRuntime().availableProcessors()), listFiles != null ? listFiles.length : 0));
    }
}
