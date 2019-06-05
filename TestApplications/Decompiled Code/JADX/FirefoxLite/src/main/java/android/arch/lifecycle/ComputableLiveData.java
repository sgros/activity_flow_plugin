package android.arch.lifecycle;

import android.arch.core.executor.ArchTaskExecutor;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicBoolean;

public abstract class ComputableLiveData<T> {
    private AtomicBoolean mComputing;
    private final Executor mExecutor;
    private AtomicBoolean mInvalid;
    final Runnable mInvalidationRunnable;
    private final LiveData<T> mLiveData;
    final Runnable mRefreshRunnable;

    /* renamed from: android.arch.lifecycle.ComputableLiveData$2 */
    class C00032 implements Runnable {
        C00032() {
        }

        public void run() {
            do {
                Object obj;
                if (ComputableLiveData.this.mComputing.compareAndSet(false, true)) {
                    Object obj2 = null;
                    obj = null;
                    while (ComputableLiveData.this.mInvalid.compareAndSet(true, false)) {
                        try {
                            obj2 = ComputableLiveData.this.compute();
                            obj = 1;
                        } catch (Throwable th) {
                            ComputableLiveData.this.mComputing.set(false);
                        }
                    }
                    if (obj != null) {
                        ComputableLiveData.this.mLiveData.postValue(obj2);
                    }
                    ComputableLiveData.this.mComputing.set(false);
                } else {
                    obj = null;
                }
                if (obj == null) {
                    return;
                }
            } while (ComputableLiveData.this.mInvalid.get());
        }
    }

    /* renamed from: android.arch.lifecycle.ComputableLiveData$3 */
    class C00043 implements Runnable {
        C00043() {
        }

        public void run() {
            boolean hasActiveObservers = ComputableLiveData.this.mLiveData.hasActiveObservers();
            if (ComputableLiveData.this.mInvalid.compareAndSet(false, true) && hasActiveObservers) {
                ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
            }
        }
    }

    /* renamed from: android.arch.lifecycle.ComputableLiveData$1 */
    class C00051 extends LiveData<T> {
        C00051() {
        }

        /* Access modifiers changed, original: protected */
        public void onActive() {
            ComputableLiveData.this.mExecutor.execute(ComputableLiveData.this.mRefreshRunnable);
        }
    }

    public abstract T compute();

    public ComputableLiveData() {
        this(ArchTaskExecutor.getIOThreadExecutor());
    }

    public ComputableLiveData(Executor executor) {
        this.mInvalid = new AtomicBoolean(true);
        this.mComputing = new AtomicBoolean(false);
        this.mRefreshRunnable = new C00032();
        this.mInvalidationRunnable = new C00043();
        this.mExecutor = executor;
        this.mLiveData = new C00051();
    }

    public LiveData<T> getLiveData() {
        return this.mLiveData;
    }

    public void invalidate() {
        ArchTaskExecutor.getInstance().executeOnMainThread(this.mInvalidationRunnable);
    }
}
