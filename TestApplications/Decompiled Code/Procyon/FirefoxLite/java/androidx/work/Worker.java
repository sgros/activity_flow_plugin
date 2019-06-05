// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work;

import com.google.common.util.concurrent.ListenableFuture;
import android.support.annotation.Keep;
import android.annotation.SuppressLint;
import android.content.Context;
import androidx.work.impl.utils.futures.SettableFuture;

public abstract class Worker extends ListenableWorker
{
    SettableFuture<Result> mFuture;
    
    @SuppressLint({ "BanKeepAnnotation" })
    @Keep
    public Worker(final Context context, final WorkerParameters workerParameters) {
        super(context, workerParameters);
    }
    
    public abstract Result doWork();
    
    @Override
    public final ListenableFuture<Result> startWork() {
        this.mFuture = SettableFuture.create();
        this.getBackgroundExecutor().execute(new Runnable() {
            @Override
            public void run() {
                Worker.this.mFuture.set(Worker.this.doWork());
            }
        });
        return this.mFuture;
    }
}
