package android.support.p004v7.recyclerview.extensions;

import android.support.p004v7.util.DiffUtil.ItemCallback;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

/* renamed from: android.support.v7.recyclerview.extensions.AsyncDifferConfig */
public final class AsyncDifferConfig<T> {
    private final Executor mBackgroundThreadExecutor;
    private final ItemCallback<T> mDiffCallback;
    private final Executor mMainThreadExecutor;

    /* renamed from: android.support.v7.recyclerview.extensions.AsyncDifferConfig$Builder */
    public static final class Builder<T> {
        private static Executor sDiffExecutor = null;
        private static final Object sExecutorLock = new Object();
        private Executor mBackgroundThreadExecutor;
        private final ItemCallback<T> mDiffCallback;
        private Executor mMainThreadExecutor;

        public Builder(ItemCallback<T> itemCallback) {
            this.mDiffCallback = itemCallback;
        }

        public AsyncDifferConfig<T> build() {
            if (this.mBackgroundThreadExecutor == null) {
                synchronized (sExecutorLock) {
                    if (sDiffExecutor == null) {
                        sDiffExecutor = Executors.newFixedThreadPool(2);
                    }
                }
                this.mBackgroundThreadExecutor = sDiffExecutor;
            }
            return new AsyncDifferConfig(this.mMainThreadExecutor, this.mBackgroundThreadExecutor, this.mDiffCallback);
        }
    }

    AsyncDifferConfig(Executor executor, Executor executor2, ItemCallback<T> itemCallback) {
        this.mMainThreadExecutor = executor;
        this.mBackgroundThreadExecutor = executor2;
        this.mDiffCallback = itemCallback;
    }

    public Executor getMainThreadExecutor() {
        return this.mMainThreadExecutor;
    }

    public Executor getBackgroundThreadExecutor() {
        return this.mBackgroundThreadExecutor;
    }

    public ItemCallback<T> getDiffCallback() {
        return this.mDiffCallback;
    }
}
