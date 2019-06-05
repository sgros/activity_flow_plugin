package android.support.p004v7.recyclerview.extensions;

import android.os.Handler;
import android.os.Looper;
import android.support.p004v7.util.DiffUtil;
import android.support.p004v7.util.DiffUtil.Callback;
import android.support.p004v7.util.DiffUtil.DiffResult;
import android.support.p004v7.util.ListUpdateCallback;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.Executor;

/* renamed from: android.support.v7.recyclerview.extensions.AsyncListDiffer */
public class AsyncListDiffer<T> {
    private static final Executor sMainThreadExecutor = new MainThreadExecutor();
    final AsyncDifferConfig<T> mConfig;
    private List<T> mList;
    final Executor mMainThreadExecutor;
    int mMaxScheduledGeneration;
    private List<T> mReadOnlyList = Collections.emptyList();
    private final ListUpdateCallback mUpdateCallback;

    /* renamed from: android.support.v7.recyclerview.extensions.AsyncListDiffer$MainThreadExecutor */
    private static class MainThreadExecutor implements Executor {
        final Handler mHandler = new Handler(Looper.getMainLooper());

        MainThreadExecutor() {
        }

        public void execute(Runnable runnable) {
            this.mHandler.post(runnable);
        }
    }

    public AsyncListDiffer(ListUpdateCallback listUpdateCallback, AsyncDifferConfig<T> asyncDifferConfig) {
        this.mUpdateCallback = listUpdateCallback;
        this.mConfig = asyncDifferConfig;
        if (asyncDifferConfig.getMainThreadExecutor() != null) {
            this.mMainThreadExecutor = asyncDifferConfig.getMainThreadExecutor();
        } else {
            this.mMainThreadExecutor = sMainThreadExecutor;
        }
    }

    public List<T> getCurrentList() {
        return this.mReadOnlyList;
    }

    public void submitList(final List<T> list) {
        final int i = this.mMaxScheduledGeneration + 1;
        this.mMaxScheduledGeneration = i;
        if (list != this.mList) {
            if (list == null) {
                int size = this.mList.size();
                this.mList = null;
                this.mReadOnlyList = Collections.emptyList();
                this.mUpdateCallback.onRemoved(0, size);
            } else if (this.mList == null) {
                this.mList = list;
                this.mReadOnlyList = Collections.unmodifiableList(list);
                this.mUpdateCallback.onInserted(0, list.size());
            } else {
                final List list2 = this.mList;
                this.mConfig.getBackgroundThreadExecutor().execute(new Runnable() {

                    /* renamed from: android.support.v7.recyclerview.extensions.AsyncListDiffer$1$1 */
                    class C01951 extends Callback {
                        C01951() {
                        }

                        public int getOldListSize() {
                            return list2.size();
                        }

                        public int getNewListSize() {
                            return list.size();
                        }

                        public boolean areItemsTheSame(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areItemsTheSame(obj, obj2);
                            }
                            boolean z = obj == null && obj2 == null;
                            return z;
                        }

                        public boolean areContentsTheSame(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().areContentsTheSame(obj, obj2);
                            }
                            if (obj == null && obj2 == null) {
                                return true;
                            }
                            throw new AssertionError();
                        }

                        public Object getChangePayload(int i, int i2) {
                            Object obj = list2.get(i);
                            Object obj2 = list.get(i2);
                            if (obj != null && obj2 != null) {
                                return AsyncListDiffer.this.mConfig.getDiffCallback().getChangePayload(obj, obj2);
                            }
                            throw new AssertionError();
                        }
                    }

                    public void run() {
                        final DiffResult calculateDiff = DiffUtil.calculateDiff(new C01951());
                        AsyncListDiffer.this.mMainThreadExecutor.execute(new Runnable() {
                            public void run() {
                                if (AsyncListDiffer.this.mMaxScheduledGeneration == i) {
                                    AsyncListDiffer.this.latchList(list, calculateDiff);
                                }
                            }
                        });
                    }
                });
            }
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void latchList(List<T> list, DiffResult diffResult) {
        this.mList = list;
        this.mReadOnlyList = Collections.unmodifiableList(list);
        diffResult.dispatchUpdatesTo(this.mUpdateCallback);
    }
}
