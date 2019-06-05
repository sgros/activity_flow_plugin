package android.support.design.widget;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import java.lang.ref.WeakReference;

class SnackbarManager {
    private static SnackbarManager snackbarManager;
    private SnackbarRecord currentSnackbar;
    private final Handler handler = new Handler(Looper.getMainLooper(), new C00751());
    private final Object lock = new Object();
    private SnackbarRecord nextSnackbar;

    /* renamed from: android.support.design.widget.SnackbarManager$1 */
    class C00751 implements android.os.Handler.Callback {
        C00751() {
        }

        public boolean handleMessage(Message message) {
            if (message.what != 0) {
                return false;
            }
            SnackbarManager.this.handleTimeout((SnackbarRecord) message.obj);
            return true;
        }
    }

    interface Callback {
        void dismiss(int i);

        void show();
    }

    private static class SnackbarRecord {
        final WeakReference<Callback> callback;
        int duration;
        boolean paused;

        SnackbarRecord(int i, Callback callback) {
            this.callback = new WeakReference(callback);
            this.duration = i;
        }

        /* Access modifiers changed, original: 0000 */
        public boolean isSnackbar(Callback callback) {
            return callback != null && this.callback.get() == callback;
        }
    }

    static SnackbarManager getInstance() {
        if (snackbarManager == null) {
            snackbarManager = new SnackbarManager();
        }
        return snackbarManager;
    }

    private SnackbarManager() {
    }

    public void show(int i, Callback callback) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar.duration = i;
                this.handler.removeCallbacksAndMessages(this.currentSnackbar);
                scheduleTimeoutLocked(this.currentSnackbar);
                return;
            }
            if (isNextSnackbarLocked(callback)) {
                this.nextSnackbar.duration = i;
            } else {
                this.nextSnackbar = new SnackbarRecord(i, callback);
            }
            if (this.currentSnackbar == null || !cancelSnackbarLocked(this.currentSnackbar, 4)) {
                this.currentSnackbar = null;
                showNextSnackbarLocked();
                return;
            }
        }
    }

    public void dismiss(Callback callback, int i) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback)) {
                cancelSnackbarLocked(this.currentSnackbar, i);
            } else if (isNextSnackbarLocked(callback)) {
                cancelSnackbarLocked(this.nextSnackbar, i);
            }
        }
    }

    public void onDismissed(Callback callback) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback)) {
                this.currentSnackbar = null;
                if (this.nextSnackbar != null) {
                    showNextSnackbarLocked();
                }
            }
        }
    }

    public void onShown(Callback callback) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback)) {
                scheduleTimeoutLocked(this.currentSnackbar);
            }
        }
    }

    public void pauseTimeout(Callback callback) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback) && !this.currentSnackbar.paused) {
                this.currentSnackbar.paused = true;
                this.handler.removeCallbacksAndMessages(this.currentSnackbar);
            }
        }
    }

    public void restoreTimeoutIfPaused(Callback callback) {
        synchronized (this.lock) {
            if (isCurrentSnackbarLocked(callback) && this.currentSnackbar.paused) {
                this.currentSnackbar.paused = false;
                scheduleTimeoutLocked(this.currentSnackbar);
            }
        }
    }

    public boolean isCurrentOrNext(Callback callback) {
        boolean z;
        synchronized (this.lock) {
            if (!isCurrentSnackbarLocked(callback)) {
                if (!isNextSnackbarLocked(callback)) {
                    z = false;
                }
            }
            z = true;
        }
        return z;
    }

    private void showNextSnackbarLocked() {
        if (this.nextSnackbar != null) {
            this.currentSnackbar = this.nextSnackbar;
            this.nextSnackbar = null;
            Callback callback = (Callback) this.currentSnackbar.callback.get();
            if (callback != null) {
                callback.show();
            } else {
                this.currentSnackbar = null;
            }
        }
    }

    private boolean cancelSnackbarLocked(SnackbarRecord snackbarRecord, int i) {
        Callback callback = (Callback) snackbarRecord.callback.get();
        if (callback == null) {
            return false;
        }
        this.handler.removeCallbacksAndMessages(snackbarRecord);
        callback.dismiss(i);
        return true;
    }

    private boolean isCurrentSnackbarLocked(Callback callback) {
        return this.currentSnackbar != null && this.currentSnackbar.isSnackbar(callback);
    }

    private boolean isNextSnackbarLocked(Callback callback) {
        return this.nextSnackbar != null && this.nextSnackbar.isSnackbar(callback);
    }

    private void scheduleTimeoutLocked(SnackbarRecord snackbarRecord) {
        if (snackbarRecord.duration != -2) {
            int i = 2750;
            if (snackbarRecord.duration > 0) {
                i = snackbarRecord.duration;
            } else if (snackbarRecord.duration == -1) {
                i = 1500;
            }
            this.handler.removeCallbacksAndMessages(snackbarRecord);
            this.handler.sendMessageDelayed(Message.obtain(this.handler, 0, snackbarRecord), (long) i);
        }
    }

    /* Access modifiers changed, original: 0000 */
    public void handleTimeout(SnackbarRecord snackbarRecord) {
        synchronized (this.lock) {
            if (this.currentSnackbar == snackbarRecord || this.nextSnackbar == snackbarRecord) {
                cancelSnackbarLocked(snackbarRecord, 2);
            }
        }
    }
}
