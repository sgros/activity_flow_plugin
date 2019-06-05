// 
// Decompiled by Procyon v0.5.34
// 

package android.support.design.widget;

import java.lang.ref.WeakReference;
import android.os.Message;
import android.os.Handler$Callback;
import android.os.Looper;
import android.os.Handler;

class SnackbarManager
{
    private static SnackbarManager snackbarManager;
    private SnackbarRecord currentSnackbar;
    private final Handler handler;
    private final Object lock;
    private SnackbarRecord nextSnackbar;
    
    private SnackbarManager() {
        this.lock = new Object();
        this.handler = new Handler(Looper.getMainLooper(), (Handler$Callback)new Handler$Callback() {
            public boolean handleMessage(final Message message) {
                if (message.what != 0) {
                    return false;
                }
                SnackbarManager.this.handleTimeout((SnackbarRecord)message.obj);
                return true;
            }
        });
    }
    
    private boolean cancelSnackbarLocked(final SnackbarRecord snackbarRecord, final int n) {
        final Callback callback = snackbarRecord.callback.get();
        if (callback != null) {
            this.handler.removeCallbacksAndMessages((Object)snackbarRecord);
            callback.dismiss(n);
            return true;
        }
        return false;
    }
    
    static SnackbarManager getInstance() {
        if (SnackbarManager.snackbarManager == null) {
            SnackbarManager.snackbarManager = new SnackbarManager();
        }
        return SnackbarManager.snackbarManager;
    }
    
    private boolean isCurrentSnackbarLocked(final Callback callback) {
        return this.currentSnackbar != null && this.currentSnackbar.isSnackbar(callback);
    }
    
    private boolean isNextSnackbarLocked(final Callback callback) {
        return this.nextSnackbar != null && this.nextSnackbar.isSnackbar(callback);
    }
    
    private void scheduleTimeoutLocked(final SnackbarRecord snackbarRecord) {
        if (snackbarRecord.duration == -2) {
            return;
        }
        int duration = 2750;
        if (snackbarRecord.duration > 0) {
            duration = snackbarRecord.duration;
        }
        else if (snackbarRecord.duration == -1) {
            duration = 1500;
        }
        this.handler.removeCallbacksAndMessages((Object)snackbarRecord);
        this.handler.sendMessageDelayed(Message.obtain(this.handler, 0, (Object)snackbarRecord), (long)duration);
    }
    
    private void showNextSnackbarLocked() {
        if (this.nextSnackbar != null) {
            this.currentSnackbar = this.nextSnackbar;
            this.nextSnackbar = null;
            final Callback callback = this.currentSnackbar.callback.get();
            if (callback != null) {
                callback.show();
            }
            else {
                this.currentSnackbar = null;
            }
        }
    }
    
    public void dismiss(final Callback callback, final int n) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.cancelSnackbarLocked(this.currentSnackbar, n);
            }
            else if (this.isNextSnackbarLocked(callback)) {
                this.cancelSnackbarLocked(this.nextSnackbar, n);
            }
        }
    }
    
    void handleTimeout(final SnackbarRecord snackbarRecord) {
        synchronized (this.lock) {
            if (this.currentSnackbar == snackbarRecord || this.nextSnackbar == snackbarRecord) {
                this.cancelSnackbarLocked(snackbarRecord, 2);
            }
        }
    }
    
    public boolean isCurrentOrNext(final Callback callback) {
        synchronized (this.lock) {
            return this.isCurrentSnackbarLocked(callback) || this.isNextSnackbarLocked(callback);
        }
    }
    
    public void onDismissed(final Callback callback) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar = null;
                if (this.nextSnackbar != null) {
                    this.showNextSnackbarLocked();
                }
            }
        }
    }
    
    public void onShown(final Callback callback) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.scheduleTimeoutLocked(this.currentSnackbar);
            }
        }
    }
    
    public void pauseTimeout(final Callback callback) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback) && !this.currentSnackbar.paused) {
                this.currentSnackbar.paused = true;
                this.handler.removeCallbacksAndMessages((Object)this.currentSnackbar);
            }
        }
    }
    
    public void restoreTimeoutIfPaused(final Callback callback) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback) && this.currentSnackbar.paused) {
                this.currentSnackbar.paused = false;
                this.scheduleTimeoutLocked(this.currentSnackbar);
            }
        }
    }
    
    public void show(final int n, final Callback callback) {
        synchronized (this.lock) {
            if (this.isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar.duration = n;
                this.handler.removeCallbacksAndMessages((Object)this.currentSnackbar);
                this.scheduleTimeoutLocked(this.currentSnackbar);
                return;
            }
            if (this.isNextSnackbarLocked(callback)) {
                this.nextSnackbar.duration = n;
            }
            else {
                this.nextSnackbar = new SnackbarRecord(n, callback);
            }
            if (this.currentSnackbar != null && this.cancelSnackbarLocked(this.currentSnackbar, 4)) {
                return;
            }
            this.currentSnackbar = null;
            this.showNextSnackbarLocked();
        }
    }
    
    interface Callback
    {
        void dismiss(final int p0);
        
        void show();
    }
    
    private static class SnackbarRecord
    {
        final WeakReference<Callback> callback;
        int duration;
        boolean paused;
        
        SnackbarRecord(final int duration, final Callback referent) {
            this.callback = new WeakReference<Callback>(referent);
            this.duration = duration;
        }
        
        boolean isSnackbar(final Callback callback) {
            return callback != null && this.callback.get() == callback;
        }
    }
}
