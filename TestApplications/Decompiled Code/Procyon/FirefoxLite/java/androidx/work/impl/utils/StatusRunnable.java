// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import com.google.common.util.concurrent.ListenableFuture;
import androidx.work.impl.model.WorkSpec;
import androidx.work.WorkInfo;
import java.util.List;
import androidx.work.impl.WorkManagerImpl;
import androidx.work.impl.utils.futures.SettableFuture;

public abstract class StatusRunnable<T> implements Runnable
{
    private final SettableFuture<T> mFuture;
    
    public StatusRunnable() {
        this.mFuture = SettableFuture.create();
    }
    
    public static StatusRunnable<List<WorkInfo>> forTag(final WorkManagerImpl workManagerImpl, final String s) {
        return new StatusRunnable<List<WorkInfo>>() {
            @Override
            List<WorkInfo> runInternal() {
                return WorkSpec.WORK_INFO_MAPPER.apply(workManagerImpl.getWorkDatabase().workSpecDao().getWorkStatusPojoForTag(s));
            }
        };
    }
    
    public ListenableFuture<T> getFuture() {
        return this.mFuture;
    }
    
    @Override
    public void run() {
        try {
            this.mFuture.set(this.runInternal());
        }
        catch (Throwable exception) {
            this.mFuture.setException(exception);
        }
    }
    
    abstract T runInternal();
}
