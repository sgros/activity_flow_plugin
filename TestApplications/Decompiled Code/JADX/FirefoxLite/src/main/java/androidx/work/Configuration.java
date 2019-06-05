package androidx.work;

import android.os.Build.VERSION;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Configuration {
    private final Executor mExecutor;
    private final int mLoggingLevel;
    private final int mMaxJobSchedulerId;
    private final int mMaxSchedulerLimit;
    private final int mMinJobSchedulerId;
    private final WorkerFactory mWorkerFactory;

    public static final class Builder {
        Executor mExecutor;
        int mLoggingLevel = 4;
        int mMaxJobSchedulerId = Integer.MAX_VALUE;
        int mMaxSchedulerLimit = 20;
        int mMinJobSchedulerId = 0;
        WorkerFactory mWorkerFactory;

        public Configuration build() {
            return new Configuration(this);
        }
    }

    Configuration(Builder builder) {
        if (builder.mExecutor == null) {
            this.mExecutor = createDefaultExecutor();
        } else {
            this.mExecutor = builder.mExecutor;
        }
        if (builder.mWorkerFactory == null) {
            this.mWorkerFactory = WorkerFactory.getDefaultWorkerFactory();
        } else {
            this.mWorkerFactory = builder.mWorkerFactory;
        }
        this.mLoggingLevel = builder.mLoggingLevel;
        this.mMinJobSchedulerId = builder.mMinJobSchedulerId;
        this.mMaxJobSchedulerId = builder.mMaxJobSchedulerId;
        this.mMaxSchedulerLimit = builder.mMaxSchedulerLimit;
    }

    public Executor getExecutor() {
        return this.mExecutor;
    }

    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }

    public int getMinimumLoggingLevel() {
        return this.mLoggingLevel;
    }

    public int getMinJobSchedulerId() {
        return this.mMinJobSchedulerId;
    }

    public int getMaxJobSchedulerId() {
        return this.mMaxJobSchedulerId;
    }

    public int getMaxSchedulerLimit() {
        if (VERSION.SDK_INT == 23) {
            return this.mMaxSchedulerLimit / 2;
        }
        return this.mMaxSchedulerLimit;
    }

    private Executor createDefaultExecutor() {
        return Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)));
    }
}
