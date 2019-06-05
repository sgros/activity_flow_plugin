package org.mozilla.threadutils;

import android.os.Handler;
import android.os.Looper;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.atomic.AtomicInteger;

public class ThreadUtils {
    private static final ExecutorService backgroundExecutorService = Executors.newSingleThreadExecutor(getIoPrioritisedFactory());
    private static final Handler handler = new Handler(Looper.getMainLooper());
    private static final Thread uiThread = Looper.getMainLooper().getThread();

    private static class CustomThreadFactory implements ThreadFactory {
        private final AtomicInteger mNumber = new AtomicInteger();
        private final String threadName;
        private final int threadPriority;

        public CustomThreadFactory(String str, int i) {
            this.threadName = str;
            this.threadPriority = i;
        }

        public Thread newThread(Runnable runnable) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append(this.threadName);
            stringBuilder.append("-");
            stringBuilder.append(this.mNumber.getAndIncrement());
            Thread thread = new Thread(runnable, stringBuilder.toString());
            thread.setPriority(this.threadPriority);
            return thread;
        }
    }

    public static void postToBackgroundThread(Runnable runnable) {
        backgroundExecutorService.submit(runnable);
    }

    public static <V> Future<V> postToBackgroundThread(Callable<V> callable) {
        return backgroundExecutorService.submit(callable);
    }

    public static void postToMainThread(Runnable runnable) {
        handler.post(runnable);
    }

    public static void postToMainThreadDelayed(Runnable runnable, long j) {
        handler.postDelayed(runnable, j);
    }

    private static ThreadFactory getIoPrioritisedFactory() {
        return new CustomThreadFactory("pool-io-background", 4);
    }
}
