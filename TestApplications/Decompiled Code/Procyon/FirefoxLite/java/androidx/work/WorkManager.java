// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import com.google.common.util.concurrent.ListenableFuture;
import java.util.List;
import java.util.Collections;
import android.content.Context;
import androidx.work.impl.WorkManagerImpl;

public abstract class WorkManager
{
    protected WorkManager() {
    }
    
    public static WorkManager getInstance() {
        final WorkManagerImpl instance = WorkManagerImpl.getInstance();
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("WorkManager is not initialized properly.  The most likely cause is that you disabled WorkManagerInitializer in your manifest but forgot to call WorkManager#initialize in your Application#onCreate or a ContentProvider.");
    }
    
    public static void initialize(final Context context, final Configuration configuration) {
        WorkManagerImpl.initialize(context, configuration);
    }
    
    public abstract Operation cancelAllWorkByTag(final String p0);
    
    public final Operation enqueue(final WorkRequest o) {
        return this.enqueue(Collections.singletonList(o));
    }
    
    public abstract Operation enqueue(final List<? extends WorkRequest> p0);
    
    public abstract ListenableFuture<List<WorkInfo>> getWorkInfosByTag(final String p0);
}
