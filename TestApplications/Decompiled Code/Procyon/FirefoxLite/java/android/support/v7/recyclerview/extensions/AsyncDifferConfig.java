// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.recyclerview.extensions;

import java.util.concurrent.Executors;
import android.support.v7.util.DiffUtil;
import java.util.concurrent.Executor;

public final class AsyncDifferConfig<T>
{
    private final Executor mBackgroundThreadExecutor;
    private final DiffUtil.ItemCallback<T> mDiffCallback;
    private final Executor mMainThreadExecutor;
    
    AsyncDifferConfig(final Executor mMainThreadExecutor, final Executor mBackgroundThreadExecutor, final DiffUtil.ItemCallback<T> mDiffCallback) {
        this.mMainThreadExecutor = mMainThreadExecutor;
        this.mBackgroundThreadExecutor = mBackgroundThreadExecutor;
        this.mDiffCallback = mDiffCallback;
    }
    
    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }
    
    public DiffUtil.ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }
    
    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }
    
    public static final class Builder<T>
    {
        private static Executor sDiffExecutor;
        private static final Object sExecutorLock;
        private Executor mBackgroundThreadExecutor;
        private final DiffUtil.ItemCallback<T> mDiffCallback;
        private Executor mMainThreadExecutor;
        
        static {
            sExecutorLock = new Object();
            Builder.sDiffExecutor = null;
        }
        
        public Builder(final DiffUtil.ItemCallback<T> mDiffCallback) {
            this.mDiffCallback = mDiffCallback;
        }
        
        public AsyncDifferConfig<T> build() {
            if (this.mBackgroundThreadExecutor == null) {
                synchronized (Builder.sExecutorLock) {
                    if (Builder.sDiffExecutor == null) {
                        Builder.sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                    // monitorexit(Builder.sExecutorLock)
                    this.mBackgroundThreadExecutor = Builder.sDiffExecutor;
                }
            }
            return new AsyncDifferConfig<T>(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
        }
    }
}
