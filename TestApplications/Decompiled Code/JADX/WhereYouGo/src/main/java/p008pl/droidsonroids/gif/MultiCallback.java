package p008pl.droidsonroids.gif;

import android.graphics.drawable.Drawable;
import android.graphics.drawable.Drawable.Callback;
import android.support.annotation.NonNull;
import android.view.View;
import java.lang.ref.WeakReference;
import java.util.concurrent.CopyOnWriteArrayList;

/* renamed from: pl.droidsonroids.gif.MultiCallback */
public class MultiCallback implements Callback {
    private final CopyOnWriteArrayList<CallbackWeakReference> mCallbacks;
    private final boolean mUseViewInvalidate;

    /* renamed from: pl.droidsonroids.gif.MultiCallback$CallbackWeakReference */
    static final class CallbackWeakReference extends WeakReference<Callback> {
        CallbackWeakReference(Callback r) {
            super(r);
        }

        public boolean equals(Object o) {
            if (this == o) {
                return true;
            }
            if (o == null || getClass() != o.getClass()) {
                return false;
            }
            if (get() != ((CallbackWeakReference) o).get()) {
                return false;
            }
            return true;
        }

        public int hashCode() {
            Callback callback = (Callback) get();
            return callback != null ? callback.hashCode() : 0;
        }
    }

    public MultiCallback() {
        this(false);
    }

    public MultiCallback(boolean useViewInvalidate) {
        this.mCallbacks = new CopyOnWriteArrayList();
        this.mUseViewInvalidate = useViewInvalidate;
    }

    public void invalidateDrawable(@NonNull Drawable who) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference reference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) reference.get();
            if (callback == null) {
                this.mCallbacks.remove(reference);
            } else if (this.mUseViewInvalidate && (callback instanceof View)) {
                ((View) callback).invalidate();
            } else {
                callback.invalidateDrawable(who);
            }
        }
    }

    public void scheduleDrawable(@NonNull Drawable who, @NonNull Runnable what, long when) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference reference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) reference.get();
            if (callback != null) {
                callback.scheduleDrawable(who, what, when);
            } else {
                this.mCallbacks.remove(reference);
            }
        }
    }

    public void unscheduleDrawable(@NonNull Drawable who, @NonNull Runnable what) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference reference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback callback = (Callback) reference.get();
            if (callback != null) {
                callback.unscheduleDrawable(who, what);
            } else {
                this.mCallbacks.remove(reference);
            }
        }
    }

    public void addView(Callback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference reference = (CallbackWeakReference) this.mCallbacks.get(i);
            if (((Callback) reference.get()) == null) {
                this.mCallbacks.remove(reference);
            }
        }
        this.mCallbacks.addIfAbsent(new CallbackWeakReference(callback));
    }

    public void removeView(Callback callback) {
        for (int i = 0; i < this.mCallbacks.size(); i++) {
            CallbackWeakReference reference = (CallbackWeakReference) this.mCallbacks.get(i);
            Callback item = (Callback) reference.get();
            if (item == null || item == callback) {
                this.mCallbacks.remove(reference);
            }
        }
    }
}
