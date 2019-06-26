// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.lang.ref.WeakReference;
import android.view.View;
import android.support.annotation.NonNull;
import android.graphics.drawable.Drawable;
import java.util.concurrent.CopyOnWriteArrayList;
import android.graphics.drawable.Drawable$Callback;

public class MultiCallback implements Drawable$Callback
{
    private final CopyOnWriteArrayList<CallbackWeakReference> mCallbacks;
    private final boolean mUseViewInvalidate;
    
    public MultiCallback() {
        this(false);
    }
    
    public MultiCallback(final boolean mUseViewInvalidate) {
        this.mCallbacks = new CopyOnWriteArrayList<CallbackWeakReference>();
        this.mUseViewInvalidate = mUseViewInvalidate;
    }
    
    public void addView(final Drawable$Callback drawable$Callback) {
        for (int i = 0; i < this.mCallbacks.size(); ++i) {
            final CallbackWeakReference o = this.mCallbacks.get(i);
            if (o.get() == null) {
                this.mCallbacks.remove(o);
            }
        }
        this.mCallbacks.addIfAbsent(new CallbackWeakReference(drawable$Callback));
    }
    
    public void invalidateDrawable(@NonNull final Drawable drawable) {
        for (int i = 0; i < this.mCallbacks.size(); ++i) {
            final CallbackWeakReference o = this.mCallbacks.get(i);
            final Drawable$Callback drawable$Callback = o.get();
            if (drawable$Callback != null) {
                if (this.mUseViewInvalidate && drawable$Callback instanceof View) {
                    ((View)drawable$Callback).invalidate();
                }
                else {
                    drawable$Callback.invalidateDrawable(drawable);
                }
            }
            else {
                this.mCallbacks.remove(o);
            }
        }
    }
    
    public void removeView(final Drawable$Callback drawable$Callback) {
        for (int i = 0; i < this.mCallbacks.size(); ++i) {
            final CallbackWeakReference o = this.mCallbacks.get(i);
            final Drawable$Callback drawable$Callback2 = o.get();
            if (drawable$Callback2 == null || drawable$Callback2 == drawable$Callback) {
                this.mCallbacks.remove(o);
            }
        }
    }
    
    public void scheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable, final long n) {
        for (int i = 0; i < this.mCallbacks.size(); ++i) {
            final CallbackWeakReference o = this.mCallbacks.get(i);
            final Drawable$Callback drawable$Callback = o.get();
            if (drawable$Callback != null) {
                drawable$Callback.scheduleDrawable(drawable, runnable, n);
            }
            else {
                this.mCallbacks.remove(o);
            }
        }
    }
    
    public void unscheduleDrawable(@NonNull final Drawable drawable, @NonNull final Runnable runnable) {
        for (int i = 0; i < this.mCallbacks.size(); ++i) {
            final CallbackWeakReference o = this.mCallbacks.get(i);
            final Drawable$Callback drawable$Callback = o.get();
            if (drawable$Callback != null) {
                drawable$Callback.unscheduleDrawable(drawable, runnable);
            }
            else {
                this.mCallbacks.remove(o);
            }
        }
    }
    
    static final class CallbackWeakReference extends WeakReference<Drawable$Callback>
    {
        CallbackWeakReference(final Drawable$Callback referent) {
            super(referent);
        }
        
        @Override
        public boolean equals(final Object o) {
            boolean b = true;
            if (this != o) {
                if (o == null || this.getClass() != o.getClass()) {
                    b = false;
                }
                else if (this.get() != ((CallbackWeakReference)o).get()) {
                    b = false;
                }
            }
            return b;
        }
        
        @Override
        public int hashCode() {
            final Drawable$Callback drawable$Callback = this.get();
            int hashCode;
            if (drawable$Callback != null) {
                hashCode = drawable$Callback.hashCode();
            }
            else {
                hashCode = 0;
            }
            return hashCode;
        }
    }
}
