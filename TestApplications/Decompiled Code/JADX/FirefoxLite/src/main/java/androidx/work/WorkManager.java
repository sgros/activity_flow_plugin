package androidx.work;

import android.content.Context;
import androidx.work.impl.WorkManagerImpl;
import com.google.common.util.concurrent.ListenableFuture;
import java.util.Collections;
import java.util.List;

public abstract class WorkManager {
    public abstract Operation cancelAllWorkByTag(String str);

    public abstract Operation enqueue(List<? extends WorkRequest> list);

    public abstract ListenableFuture<List<WorkInfo>> getWorkInfosByTag(String str);

    public static WorkManager getInstance() {
        WorkManagerImpl instance = WorkManagerImpl.getInstance();
        if (instance != null) {
            return instance;
        }
        throw new IllegalStateException("WorkManager is not initialized properly.  The most likely cause is that you disabled WorkManagerInitializer in your manifest but forgot to call WorkManager#initialize in your Application#onCreate or a ContentProvider.");
    }

    public static void initialize(Context context, Configuration configuration) {
        WorkManagerImpl.initialize(context, configuration);
    }

    public final Operation enqueue(WorkRequest workRequest) {
        return enqueue(Collections.singletonList(workRequest));
    }

    protected WorkManager() {
    }
}
