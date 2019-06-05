// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.rocket.download;

import android.util.Log;
import android.arch.lifecycle.Observer;
import android.arch.lifecycle.LifecycleOwner;
import java.util.concurrent.atomic.AtomicBoolean;
import android.arch.lifecycle.MutableLiveData;

public class SingleLiveEvent<T> extends MutableLiveData<T>
{
    private final AtomicBoolean mPending;
    
    public SingleLiveEvent() {
        this.mPending = new AtomicBoolean(false);
    }
    
    @Override
    public void observe(final LifecycleOwner lifecycleOwner, final Observer<T> observer) {
        if (this.hasActiveObservers()) {
            Log.w("SingleLiveEvent", "Multiple observers registered but only one will be notified of changes.");
        }
        super.observe(lifecycleOwner, new Observer<T>() {
            @Override
            public void onChanged(final T t) {
                if (SingleLiveEvent.this.mPending.compareAndSet(true, false)) {
                    observer.onChanged(t);
                }
            }
        });
    }
    
    @Override
    public void setValue(final T value) {
        this.mPending.set(true);
        super.setValue(value);
    }
}
