// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.UUID;
import java.util.concurrent.Executor;
import android.support.annotation.Keep;
import android.annotation.SuppressLint;
import android.content.Context;

public abstract class ListenableWorker
{
    private Context mAppContext;
    private volatile boolean mStopped;
    private boolean mUsed;
    private WorkerParameters mWorkerParams;
    
    @SuppressLint({ "BanKeepAnnotation" })
    @Keep
    public ListenableWorker(final Context mAppContext, final WorkerParameters mWorkerParams) {
        if (mAppContext == null) {
            throw new IllegalArgumentException("Application Context is null");
        }
        if (mWorkerParams != null) {
            this.mAppContext = mAppContext;
            this.mWorkerParams = mWorkerParams;
            return;
        }
        throw new IllegalArgumentException("WorkerParameters is null");
    }
    
    public final Context getApplicationContext() {
        return this.mAppContext;
    }
    
    public Executor getBackgroundExecutor() {
        return this.mWorkerParams.getBackgroundExecutor();
    }
    
    public final UUID getId() {
        return this.mWorkerParams.getId();
    }
    
    public final Data getInputData() {
        return this.mWorkerParams.getInputData();
    }
    
    public WorkerFactory getWorkerFactory() {
        return this.mWorkerParams.getWorkerFactory();
    }
    
    public final boolean isUsed() {
        return this.mUsed;
    }
    
    public void onStopped() {
    }
    
    public final void setUsed() {
        this.mUsed = true;
    }
    
    public abstract ListenableFuture<Result> startWork();
    
    public final void stop() {
        this.mStopped = true;
        this.onStopped();
    }
    
    public abstract static class Result
    {
        Result() {
        }
        
        public static Result failure() {
            return new Failure();
        }
        
        public static Result retry() {
            return new Retry();
        }
        
        public static Result success() {
            return new Success();
        }
        
        public static Result success(final Data data) {
            return new Success(data);
        }
        
        public static final class Failure extends Result
        {
            private final Data mOutputData;
            
            public Failure() {
                this(Data.EMPTY);
            }
            
            public Failure(final Data mOutputData) {
                this.mOutputData = mOutputData;
            }
            
            @Override
            public boolean equals(final Object o) {
                return this == o || (o != null && this.getClass() == o.getClass() && this.mOutputData.equals(((Failure)o).mOutputData));
            }
            
            public Data getOutputData() {
                return this.mOutputData;
            }
            
            @Override
            public int hashCode() {
                return Failure.class.getName().hashCode() * 31 + this.mOutputData.hashCode();
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Failure {mOutputData=");
                sb.append(this.mOutputData);
                sb.append('}');
                return sb.toString();
            }
        }
        
        public static final class Retry extends Result
        {
            @Override
            public boolean equals(final Object o) {
                boolean b = true;
                if (this == o) {
                    return true;
                }
                if (o == null || this.getClass() != o.getClass()) {
                    b = false;
                }
                return b;
            }
            
            @Override
            public int hashCode() {
                return Retry.class.getName().hashCode();
            }
            
            @Override
            public String toString() {
                return "Retry";
            }
        }
        
        public static final class Success extends Result
        {
            private final Data mOutputData;
            
            public Success() {
                this(Data.EMPTY);
            }
            
            public Success(final Data mOutputData) {
                this.mOutputData = mOutputData;
            }
            
            @Override
            public boolean equals(final Object o) {
                return this == o || (o != null && this.getClass() == o.getClass() && this.mOutputData.equals(((Success)o).mOutputData));
            }
            
            public Data getOutputData() {
                return this.mOutputData;
            }
            
            @Override
            public int hashCode() {
                return Success.class.getName().hashCode() * 31 + this.mOutputData.hashCode();
            }
            
            @Override
            public String toString() {
                final StringBuilder sb = new StringBuilder();
                sb.append("Success {mOutputData=");
                sb.append(this.mOutputData);
                sb.append('}');
                return sb.toString();
            }
        }
    }
}
