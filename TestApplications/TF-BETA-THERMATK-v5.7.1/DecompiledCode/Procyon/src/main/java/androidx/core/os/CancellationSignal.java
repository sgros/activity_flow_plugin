// 
// Decompiled by Procyon v0.5.34
// 

package androidx.core.os;

import android.os.Build$VERSION;

public final class CancellationSignal
{
    private boolean mCancelInProgress;
    private Object mCancellationSignalObj;
    private boolean mIsCanceled;
    private OnCancelListener mOnCancelListener;
    
    public void cancel() {
        synchronized (this) {
            if (this.mIsCanceled) {
                return;
            }
            this.mIsCanceled = true;
            this.mCancelInProgress = true;
            final OnCancelListener mOnCancelListener = this.mOnCancelListener;
            final Object mCancellationSignalObj = this.mCancellationSignalObj;
            // monitorexit(this)
            Label_0051: {
                if (mOnCancelListener == null) {
                    break Label_0051;
                }
                try {
                    mOnCancelListener.onCancel();
                    break Label_0051;
                }
                finally {
                    synchronized (this) {
                        this.mCancelInProgress = false;
                        this.notifyAll();
                    }
                    while (true) {
                        ((android.os.CancellationSignal)mCancellationSignalObj).cancel();
                        break Label_0051;
                        continue;
                    }
                }
                // iftrue(Label_0093:, mCancellationSignalObj == null || Build$VERSION.SDK_INT < 16)
            }
            synchronized (this) {
                this.mCancelInProgress = false;
                this.notifyAll();
            }
        }
    }
    
    public Object getCancellationSignalObject() {
        if (Build$VERSION.SDK_INT < 16) {
            return null;
        }
        synchronized (this) {
            if (this.mCancellationSignalObj == null) {
                this.mCancellationSignalObj = new android.os.CancellationSignal();
                if (this.mIsCanceled) {
                    ((android.os.CancellationSignal)this.mCancellationSignalObj).cancel();
                }
            }
            return this.mCancellationSignalObj;
        }
    }
    
    public interface OnCancelListener
    {
        void onCancel();
    }
}
