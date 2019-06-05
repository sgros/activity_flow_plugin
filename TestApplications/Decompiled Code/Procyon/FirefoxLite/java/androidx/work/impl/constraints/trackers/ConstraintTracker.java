// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import java.util.Iterator;
import java.util.Collection;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import androidx.work.Logger;
import androidx.work.impl.constraints.ConstraintListener;
import java.util.Set;
import android.content.Context;

public abstract class ConstraintTracker<T>
{
    private static final String TAG;
    protected final Context mAppContext;
    private T mCurrentState;
    private final Set<ConstraintListener<T>> mListeners;
    private final Object mLock;
    
    static {
        TAG = Logger.tagWithPrefix("ConstraintTracker");
    }
    
    ConstraintTracker(final Context context) {
        this.mLock = new Object();
        this.mListeners = new LinkedHashSet<ConstraintListener<T>>();
        this.mAppContext = context.getApplicationContext();
    }
    
    public void addListener(final ConstraintListener<T> constraintListener) {
        synchronized (this.mLock) {
            if (this.mListeners.add(constraintListener)) {
                if (this.mListeners.size() == 1) {
                    this.mCurrentState = this.getInitialState();
                    Logger.get().debug(ConstraintTracker.TAG, String.format("%s: initial state = %s", this.getClass().getSimpleName(), this.mCurrentState), new Throwable[0]);
                    this.startTracking();
                }
                constraintListener.onConstraintChanged(this.mCurrentState);
            }
        }
    }
    
    public abstract T getInitialState();
    
    public void removeListener(final ConstraintListener<T> constraintListener) {
        synchronized (this.mLock) {
            if (this.mListeners.remove(constraintListener) && this.mListeners.isEmpty()) {
                this.stopTracking();
            }
        }
    }
    
    public void setState(final T t) {
        synchronized (this.mLock) {
            if (this.mCurrentState != t && (this.mCurrentState == null || !this.mCurrentState.equals(t))) {
                this.mCurrentState = t;
                final Iterator<ConstraintListener<T>> iterator = new ArrayList<ConstraintListener<T>>(this.mListeners).iterator();
                while (iterator.hasNext()) {
                    iterator.next().onConstraintChanged(this.mCurrentState);
                }
            }
        }
    }
    
    public abstract void startTracking();
    
    public abstract void stopTracking();
}
