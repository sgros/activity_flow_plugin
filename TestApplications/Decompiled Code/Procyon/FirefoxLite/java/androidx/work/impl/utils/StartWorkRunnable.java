// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.utils;

import androidx.work.impl.WorkManagerImpl;
import androidx.work.WorkerParameters;

public class StartWorkRunnable implements Runnable
{
    private WorkerParameters.RuntimeExtras mRuntimeExtras;
    private WorkManagerImpl mWorkManagerImpl;
    private String mWorkSpecId;
    
    public StartWorkRunnable(final WorkManagerImpl mWorkManagerImpl, final String mWorkSpecId, final WorkerParameters.RuntimeExtras mRuntimeExtras) {
        this.mWorkManagerImpl = mWorkManagerImpl;
        this.mWorkSpecId = mWorkSpecId;
        this.mRuntimeExtras = mRuntimeExtras;
    }
    
    @Override
    public void run() {
        this.mWorkManagerImpl.getProcessor().startWork(this.mWorkSpecId, this.mRuntimeExtras);
    }
}
