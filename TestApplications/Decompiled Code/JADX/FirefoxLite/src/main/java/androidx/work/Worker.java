package androidx.work;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.annotation.Keep;
import androidx.work.ListenableWorker.Result;
import androidx.work.impl.utils.futures.SettableFuture;
import com.google.common.util.concurrent.ListenableFuture;

public abstract class Worker extends ListenableWorker {
    SettableFuture<Result> mFuture;

    /* renamed from: androidx.work.Worker$1 */
    class C02681 implements Runnable {
        C02681() {
        }

        public void run() {
            Worker.this.mFuture.set(Worker.this.doWork());
        }
    }

    public abstract Result doWork();

    @Keep
    @SuppressLint({"BanKeepAnnotation"})
    public Worker(Context context, WorkerParameters workerParameters) {
        super(context, workerParameters);
    }

    public final ListenableFuture<Result> startWork() {
        this.mFuture = SettableFuture.create();
        getBackgroundExecutor().execute(new C02681());
        return this.mFuture;
    }
}
