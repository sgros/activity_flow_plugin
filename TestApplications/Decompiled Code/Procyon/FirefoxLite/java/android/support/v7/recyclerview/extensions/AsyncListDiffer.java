// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v7.recyclerview.extensions;

import android.os.Looper;
import android.os.Handler;
import android.support.v7.util.DiffUtil;
import java.util.Collections;
import android.support.v7.util.ListUpdateCallback;
import java.util.List;
import java.util.concurrent.Executor;

public class AsyncListDiffer<T>
{
    private static final Executor sMainThreadExecutor;
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    final Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList;
    private final ListUpdateCallback mUpdateCallback;
    
    static {
        sMainThreadExecutor = new MainThreadExecutor();
    }
    
    public AsyncListDiffer(final ListUpdateCallback mUpdateCallback, final AsyncDifferConfig<T> mConfig) {
        this.mReadOnlyList = Collections.emptyList();
        this.mUpdateCallback = mUpdateCallback;
        this.mConfig = mConfig;
        if (mConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = mConfig.getMainThreadExecutor();
        }
        else {
            this.mMainThreadExecutor = AsyncListDiffer.sMainThreadExecutor;
        }
    }
    
    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }
    
    void latchList(final List<T> list, final DiffUtil.DiffResult diffResult) {
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }
    
    public void submitList(final List<T> list) {
        final int mMaxScheduledGeneration = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = mMaxScheduledGeneration;
        if (list == this.mList) {
            return;
        }
        if (list == null) {
            final int size = this.mList.size();
            this.mList = null;
            this.mReadOnlyList = Collections.emptyList();
            this.mUpdateCallback.onRemoved(0, size);
            return;
        }
        if (this.mList == null) {
            this.mList = list;
            this.mReadOnlyList = Collections.unmodifiableList((List<? extends T>)list);
            this.mUpdateCallback.onInserted(0, list.size());
            return;
        }
        this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {
            final /* synthetic */ List val$oldList = AsyncListDiffer.this.mList;
            
            @Override
            public void run() {
                AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                    final /* synthetic */ DiffUtil.DiffResult val$result = DiffUtil.calculateDiff((DiffUtil.Callback)new DiffUtil.Callback(this) {
                        @Override
                        public boolean areContentsTheSame(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = list.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(value, value2);
                            }
                            if (value == null && value2 == null) {
                                return true;
                            }
                            throw new AssertionError();
                        }
                        
                        @Override
                        public boolean areItemsTheSame(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = list.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(value, value2);
                            }
                            return value == null && value2 == null;
                        }
                        
                        @Override
                        public Object getChangePayload(final int n, final int n2) {
                            final T value = Runnable.this.val$oldList.get(n);
                            final T value2 = list.get(n2);
                            if (value != null && value2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(value, value2);
                            }
                            throw new AssertionError();
                        }
                        
                        @Override
                        public int getNewListSize() {
                            return list.size();
                        }
                        
                        @Override
                        public int getOldListSize() {
                            return Runnable.this.val$oldList.size();
                        }
                    });
                    
                    @Override
                    public void run() {
                        if (AsyncListDiffer.this.mMaxScheduledGeneration == mMaxScheduledGeneration) {
                            AsyncListDiffer.this.latchList(list, this.val$result);
                        }
                    }
                });
            }
        });
    }
    
    private static class MainThreadExecutor implements Executor
    {
        final Handler mHandler;
        
        MainThreadExecutor() {
            this.mHandler = new Handler(Looper.getMainLooper());
        }
        
        @Override
        public void execute(final Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }
}
