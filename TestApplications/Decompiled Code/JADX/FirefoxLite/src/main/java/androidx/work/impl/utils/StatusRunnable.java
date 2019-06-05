package androidx.work.impl.utils;

import androidx.work.WorkInfo;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;

public abstract class StatusRunnable<T> implements Runnable {
    private final SettableFuture<T> mFuture = SettableFuture.create();

    public abstract T runInternal();

    public void run() {
        try {
            this.mFuture.set(runInternal());
        } catch (Throwable th) {
            this.mFuture.setException(th);
        }
    }

    public ListenableFuture<T> getFuture() {
        return this.mFuture;
    }

    public static StatusRunnable<List<WorkInfo>> forTag(final WorkManagerImpl workManagerImpl, final String str) {
        return new StatusRunnable<List<WorkInfo>>() {
            /* Access modifiers changed, original: 0000 */
            public List<WorkInfo> runInternal() {
                return (List) WorkSpec.WORK_INFO_MAPPER.apply(workManagerImpl.getWorkDatabase().workSpecDao().getWorkStatusPojoForTag(str));
            }
        };
    }
}
