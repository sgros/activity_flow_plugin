// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import android.os.Build$VERSION;
import java.util.concurrent.Executors;
import java.util.concurrent.Executor;

public final class Configuration
{
    private final Executor mExecutor;
    private final int mLoggingLevel;
    private final int mMaxJobSchedulerId;
    private final int mMaxSchedulerLimit;
    private final int mMinJobSchedulerId;
    private final WorkerFactory mWorkerFactory;
    
    Configuration(final Builder builder) {
        if (builder.mExecutor == null) {
            this.mExecutor = this.createDefaultExecutor();
        }
        else {
            this.mExecutor = builder.mExecutor;
        }
        if (builder.mWorkerFactory == null) {
            this.mWorkerFactory = WorkerFactory.getDefaultWorkerFactory();
        }
        else {
            this.mWorkerFactory = builder.mWorkerFactory;
        }
        this.mLoggingLevel = builder.mLoggingLevel;
        this.mMinJobSchedulerId = builder.mMinJobSchedulerId;
        this.mMaxJobSchedulerId = builder.mMaxJobSchedulerId;
        this.mMaxSchedulerLimit = builder.mMaxSchedulerLimit;
    }
    
    private Executor createDefaultExecutor() {
        return Executors.newFixedThreadPool(Math.max(2, Math.min(Runtime.getRuntime().availableProcessors() - 1, 4)));
    }
    
    public Executor getExecutor() {
        return this.mExecutor;
    }
    
    public int getMaxJobSchedulerId() {
        return this.mMaxJobSchedulerId;
    }
    
    public int getMaxSchedulerLimit() {
        if (Build$VERSION.SDK_INT == 23) {
            return this.mMaxSchedulerLimit / 2;
        }
        return this.mMaxSchedulerLimit;
    }
    
    public int getMinJobSchedulerId() {
        return this.mMinJobSchedulerId;
    }
    
    public int getMinimumLoggingLevel() {
        return this.mLoggingLevel;
    }
    
    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }
    
    public static final class Builder
    {
        Executor mExecutor;
        int mLoggingLevel;
        int mMaxJobSchedulerId;
        int mMaxSchedulerLimit;
        int mMinJobSchedulerId;
        WorkerFactory mWorkerFactory;
        
        public Builder() {
            this.mLoggingLevel = 4;
            this.mMinJobSchedulerId = 0;
            this.mMaxJobSchedulerId = Integer.MAX_VALUE;
            this.mMaxSchedulerLimit = 20;
        }
        
        public Configuration build() {
            return new Configuration(this);
        }
    }
}
