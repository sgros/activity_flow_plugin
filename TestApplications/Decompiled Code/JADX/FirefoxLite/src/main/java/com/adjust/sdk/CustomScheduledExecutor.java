package com.adjust.sdk;

import java.lang.Thread.UncaughtExceptionHandler;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.ScheduledFuture;
import java.util.concurrent.ScheduledThreadPoolExecutor;
import java.util.concurrent.ThreadFactory;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

public final class CustomScheduledExecutor {
    private ScheduledThreadPoolExecutor executor;
    private String source;
    private final AtomicInteger threadCounter = new AtomicInteger(1);

    private class RunnableWrapper implements Runnable {
        private Runnable runnable;

        public RunnableWrapper(Runnable runnable) {
            this.runnable = runnable;
        }

        public void run() {
            try {
                this.runnable.run();
            } catch (Throwable th) {
                AdjustFactory.getLogger().error("Runnable error %s", th.getMessage());
            }
        }
    }

    public CustomScheduledExecutor(final String str, boolean z) {
        this.source = str;
        this.executor = new ScheduledThreadPoolExecutor(1, new ThreadFactory() {

            /* renamed from: com.adjust.sdk.CustomScheduledExecutor$1$1 */
            class C03351 implements UncaughtExceptionHandler {
                C03351() {
                }

                public void uncaughtException(Thread thread, Throwable th) {
                    AdjustFactory.getLogger().error("Thread %s with error %s", thread.getName(), th.getMessage());
                }
            }

            public Thread newThread(Runnable runnable) {
                Thread newThread = Executors.defaultThreadFactory().newThread(new RunnableWrapper(runnable));
                newThread.setPriority(1);
                StringBuilder stringBuilder = new StringBuilder();
                stringBuilder.append(Constants.THREAD_PREFIX);
                stringBuilder.append(newThread.getName());
                stringBuilder.append("-");
                stringBuilder.append(str);
                newThread.setName(stringBuilder.toString());
                newThread.setDaemon(true);
                newThread.setUncaughtExceptionHandler(new C03351());
                return newThread;
            }
        }, new RejectedExecutionHandler() {
            public void rejectedExecution(Runnable runnable, ThreadPoolExecutor threadPoolExecutor) {
                AdjustFactory.getLogger().warn("Runnable %s rejected from %s ", runnable.toString(), str);
            }
        });
        if (!z) {
            this.executor.setKeepAliveTime(10, TimeUnit.MILLISECONDS);
            this.executor.allowCoreThreadTimeOut(true);
        }
    }

    public Future<?> submit(Runnable runnable) {
        return this.executor.submit(runnable);
    }

    public void shutdownNow() {
        this.executor.shutdownNow();
    }

    public ScheduledFuture<?> scheduleWithFixedDelay(Runnable runnable, long j, long j2, TimeUnit timeUnit) {
        return this.executor.scheduleWithFixedDelay(runnable, j, j2, timeUnit);
    }

    public ScheduledFuture<?> schedule(Runnable runnable, long j, TimeUnit timeUnit) {
        return this.executor.schedule(runnable, j, timeUnit);
    }
}
