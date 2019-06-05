// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import java.util.Collections;
import android.net.Uri;
import java.util.List;
import android.net.Network;
import java.util.HashSet;
import java.util.Collection;
import androidx.work.impl.utils.taskexecutor.TaskExecutor;
import java.util.Set;
import java.util.UUID;
import java.util.concurrent.Executor;

public final class WorkerParameters
{
    private Executor mBackgroundExecutor;
    private UUID mId;
    private Data mInputData;
    private int mRunAttemptCount;
    private RuntimeExtras mRuntimeExtras;
    private Set<String> mTags;
    private TaskExecutor mWorkTaskExecutor;
    private WorkerFactory mWorkerFactory;
    
    public WorkerParameters(final UUID mId, final Data mInputData, final Collection<String> c, final RuntimeExtras mRuntimeExtras, final int mRunAttemptCount, final Executor mBackgroundExecutor, final TaskExecutor mWorkTaskExecutor, final WorkerFactory mWorkerFactory) {
        this.mId = mId;
        this.mInputData = mInputData;
        this.mTags = new HashSet<String>(c);
        this.mRuntimeExtras = mRuntimeExtras;
        this.mRunAttemptCount = mRunAttemptCount;
        this.mBackgroundExecutor = mBackgroundExecutor;
        this.mWorkTaskExecutor = mWorkTaskExecutor;
        this.mWorkerFactory = mWorkerFactory;
    }
    
    public Executor getBackgroundExecutor() {
        return this.mBackgroundExecutor;
    }
    
    public UUID getId() {
        return this.mId;
    }
    
    public Data getInputData() {
        return this.mInputData;
    }
    
    public WorkerFactory getWorkerFactory() {
        return this.mWorkerFactory;
    }
    
    public static class RuntimeExtras
    {
        public Network network;
        public List<String> triggeredContentAuthorities;
        public List<Uri> triggeredContentUris;
        
        public RuntimeExtras() {
            this.triggeredContentAuthorities = Collections.emptyList();
            this.triggeredContentUris = Collections.emptyList();
        }
    }
}
