// 
// Decompiled by Procyon v0.5.34
// 

package pl.droidsonroids.gif;

import java.util.Iterator;
import android.os.Message;
import android.os.Looper;
import java.lang.ref.WeakReference;
import android.os.Handler;

class InvalidationHandler extends Handler
{
    static final int MSG_TYPE_INVALIDATION = -1;
    private final WeakReference<GifDrawable> mDrawableRef;
    
    public InvalidationHandler(final GifDrawable referent) {
        super(Looper.getMainLooper());
        this.mDrawableRef = new WeakReference<GifDrawable>(referent);
    }
    
    public void handleMessage(final Message message) {
        final GifDrawable gifDrawable = this.mDrawableRef.get();
        if (gifDrawable != null) {
            if (message.what == -1) {
                gifDrawable.invalidateSelf();
            }
            else {
                final Iterator<AnimationListener> iterator = gifDrawable.mListeners.iterator();
                while (iterator.hasNext()) {
                    iterator.next().onAnimationCompleted(message.what);
                }
            }
        }
    }
}
