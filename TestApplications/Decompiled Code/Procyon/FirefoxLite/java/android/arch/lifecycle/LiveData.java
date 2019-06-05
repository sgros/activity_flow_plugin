// 
// Decompiled by Procyon v0.5.34
// 

package android.arch.lifecycle;

import java.util.Iterator;
import java.util.Map;
import android.arch.core.executor.ArchTaskExecutor;
import android.arch.core.internal.SafeIterableMap;

public abstract class LiveData<T>
{
    private static final Object NOT_SET;
    private int mActiveCount;
    private volatile Object mData;
    private final Object mDataLock;
    private boolean mDispatchInvalidated;
    private boolean mDispatchingValue;
    private SafeIterableMap<Observer<T>, ObserverWrapper> mObservers;
    private volatile Object mPendingData;
    private final Runnable mPostValueRunnable;
    private int mVersion;
    
    static {
        NOT_SET = new Object();
    }
    
    public LiveData() {
        this.mDataLock = new Object();
        this.mObservers = new SafeIterableMap<Observer<T>, ObserverWrapper>();
        this.mActiveCount = 0;
        this.mData = LiveData.NOT_SET;
        this.mPendingData = LiveData.NOT_SET;
        this.mVersion = -1;
        this.mPostValueRunnable = new Runnable() {
            @Override
            public void run() {
                synchronized (LiveData.this.mDataLock) {
                    final Object access$100 = LiveData.this.mPendingData;
                    LiveData.this.mPendingData = LiveData.NOT_SET;
                    // monitorexit(LiveData.access$000(this.this$0))
                    LiveData.this.setValue(access$100);
                }
            }
        };
    }
    
    private static void assertMainThread(final String str) {
        if (ArchTaskExecutor.getInstance().isMainThread()) {
            return;
        }
        final StringBuilder sb = new StringBuilder();
        sb.append("Cannot invoke ");
        sb.append(str);
        sb.append(" on a background");
        sb.append(" thread");
        throw new IllegalStateException(sb.toString());
    }
    
    private void considerNotify(final ObserverWrapper observerWrapper) {
        if (!observerWrapper.mActive) {
            return;
        }
        if (!observerWrapper.shouldBeActive()) {
            observerWrapper.activeStateChanged(false);
            return;
        }
        if (observerWrapper.mLastVersion >= this.mVersion) {
            return;
        }
        observerWrapper.mLastVersion = this.mVersion;
        observerWrapper.mObserver.onChanged((T)this.mData);
    }
    
    private void dispatchingValue(ObserverWrapper observerWrapper) {
        if (this.mDispatchingValue) {
            this.mDispatchInvalidated = true;
            return;
        }
        this.mDispatchingValue = true;
        ObserverWrapper observerWrapper2 = observerWrapper;
        do {
            this.mDispatchInvalidated = false;
            Label_0088: {
                if (observerWrapper2 != null) {
                    this.considerNotify(observerWrapper2);
                    observerWrapper = null;
                }
                else {
                    final SafeIterableMap.IteratorWithAdditions iteratorWithAdditions = this.mObservers.iteratorWithAdditions();
                    do {
                        observerWrapper = observerWrapper2;
                        if (!iteratorWithAdditions.hasNext()) {
                            break Label_0088;
                        }
                        this.considerNotify(((Iterator<Map.Entry<K, ObserverWrapper>>)iteratorWithAdditions).next().getValue());
                    } while (!this.mDispatchInvalidated);
                    observerWrapper = observerWrapper2;
                }
            }
            observerWrapper2 = observerWrapper;
        } while (this.mDispatchInvalidated);
        this.mDispatchingValue = false;
    }
    
    public T getValue() {
        final Object mData = this.mData;
        if (mData != LiveData.NOT_SET) {
            return (T)mData;
        }
        return null;
    }
    
    int getVersion() {
        return this.mVersion;
    }
    
    public boolean hasActiveObservers() {
        return this.mActiveCount > 0;
    }
    
    public void observe(final LifecycleOwner lifecycleOwner, final Observer<T> observer) {
        if (lifecycleOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
            return;
        }
        final LifecycleBoundObserver lifecycleBoundObserver = new LifecycleBoundObserver(lifecycleOwner, observer);
        final ObserverWrapper observerWrapper = this.mObservers.putIfAbsent(observer, (ObserverWrapper)lifecycleBoundObserver);
        if (observerWrapper != null && !observerWrapper.isAttachedTo(lifecycleOwner)) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapper != null) {
            return;
        }
        lifecycleOwner.getLifecycle().addObserver(lifecycleBoundObserver);
    }
    
    public void observeForever(final Observer<T> observer) {
        final AlwaysActiveObserver alwaysActiveObserver = new AlwaysActiveObserver(observer);
        final ObserverWrapper observerWrapper = this.mObservers.putIfAbsent(observer, (ObserverWrapper)alwaysActiveObserver);
        if (observerWrapper != null && observerWrapper instanceof LifecycleBoundObserver) {
            throw new IllegalArgumentException("Cannot add the same observer with different lifecycles");
        }
        if (observerWrapper != null) {
            return;
        }
        ((ObserverWrapper)alwaysActiveObserver).activeStateChanged(true);
    }
    
    protected void onActive() {
    }
    
    protected void onInactive() {
    }
    
    protected void postValue(final T mPendingData) {
        synchronized (this.mDataLock) {
            final boolean b = this.mPendingData == LiveData.NOT_SET;
            this.mPendingData = mPendingData;
            // monitorexit(this.mDataLock)
            if (!b) {
                return;
            }
            ArchTaskExecutor.getInstance().postToMainThread(this.mPostValueRunnable);
        }
    }
    
    public void removeObserver(final Observer<T> observer) {
        assertMainThread("removeObserver");
        final ObserverWrapper observerWrapper = this.mObservers.remove(observer);
        if (observerWrapper == null) {
            return;
        }
        observerWrapper.detachObserver();
        observerWrapper.activeStateChanged(false);
    }
    
    public void removeObservers(final LifecycleOwner lifecycleOwner) {
        assertMainThread("removeObservers");
        for (final Map.Entry<Observer<T>, ObserverWrapper> entry : this.mObservers) {
            if (entry.getValue().isAttachedTo(lifecycleOwner)) {
                this.removeObserver(entry.getKey());
            }
        }
    }
    
    protected void setValue(final T mData) {
        assertMainThread("setValue");
        ++this.mVersion;
        this.mData = mData;
        this.dispatchingValue(null);
    }
    
    private class AlwaysActiveObserver extends ObserverWrapper
    {
        AlwaysActiveObserver(final Observer<T> observer) {
            super(observer);
        }
        
        @Override
        boolean shouldBeActive() {
            return true;
        }
    }
    
    class LifecycleBoundObserver extends ObserverWrapper implements GenericLifecycleObserver
    {
        final LifecycleOwner mOwner;
        
        LifecycleBoundObserver(final LifecycleOwner mOwner, final Observer<T> observer) {
            super(observer);
            this.mOwner = mOwner;
        }
        
        @Override
        void detachObserver() {
            this.mOwner.getLifecycle().removeObserver(this);
        }
        
        @Override
        boolean isAttachedTo(final LifecycleOwner lifecycleOwner) {
            return this.mOwner == lifecycleOwner;
        }
        
        @Override
        public void onStateChanged(final LifecycleOwner lifecycleOwner, final Lifecycle.Event event) {
            if (this.mOwner.getLifecycle().getCurrentState() == Lifecycle.State.DESTROYED) {
                LiveData.this.removeObserver(this.mObserver);
                return;
            }
            ((ObserverWrapper)this).activeStateChanged(this.shouldBeActive());
        }
        
        @Override
        boolean shouldBeActive() {
            return this.mOwner.getLifecycle().getCurrentState().isAtLeast(Lifecycle.State.STARTED);
        }
    }
    
    private abstract class ObserverWrapper
    {
        boolean mActive;
        int mLastVersion;
        final Observer<T> mObserver;
        
        ObserverWrapper(final Observer<T> mObserver) {
            this.mLastVersion = -1;
            this.mObserver = mObserver;
        }
        
        void activeStateChanged(final boolean mActive) {
            if (mActive == this.mActive) {
                return;
            }
            this.mActive = mActive;
            final int access$300 = LiveData.this.mActiveCount;
            int n = 1;
            final boolean b = access$300 == 0;
            final LiveData this$0 = LiveData.this;
            final int access$301 = this$0.mActiveCount;
            if (!this.mActive) {
                n = -1;
            }
            this$0.mActiveCount = access$301 + n;
            if (b && this.mActive) {
                LiveData.this.onActive();
            }
            if (LiveData.this.mActiveCount == 0 && !this.mActive) {
                LiveData.this.onInactive();
            }
            if (this.mActive) {
                LiveData.this.dispatchingValue(this);
            }
        }
        
        void detachObserver() {
        }
        
        boolean isAttachedTo(final LifecycleOwner lifecycleOwner) {
            return false;
        }
        
        abstract boolean shouldBeActive();
    }
}
