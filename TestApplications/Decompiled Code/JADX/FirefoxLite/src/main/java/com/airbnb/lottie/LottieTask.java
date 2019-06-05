package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;
import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class LottieTask<T> {
    public static Executor EXECUTOR = Executors.newCachedThreadPool();
    private final Set<LottieListener<Throwable>> failureListeners;
    private final Handler handler;
    private volatile LottieResult<T> result;
    private final Set<LottieListener<T>> successListeners;
    private final FutureTask<LottieResult<T>> task;
    private Thread taskObserver;

    /* renamed from: com.airbnb.lottie.LottieTask$1 */
    class C03701 implements Runnable {
        C03701() {
        }

        public void run() {
            if (LottieTask.this.result != null && !LottieTask.this.task.isCancelled()) {
                LottieResult access$000 = LottieTask.this.result;
                if (access$000.getValue() != null) {
                    LottieTask.this.notifySuccessListeners(access$000.getValue());
                } else {
                    LottieTask.this.notifyFailureListeners(access$000.getException());
                }
            }
        }
    }

    public LottieTask(Callable<LottieResult<T>> callable) {
        this(callable, false);
    }

    LottieTask(Callable<LottieResult<T>> callable, boolean z) {
        this.successListeners = new LinkedHashSet(1);
        this.failureListeners = new LinkedHashSet(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        this.task = new FutureTask(callable);
        if (z) {
            try {
                setResult((LottieResult) callable.call());
                return;
            } catch (Throwable th) {
                setResult(new LottieResult(th));
                return;
            }
        }
        EXECUTOR.execute(this.task);
        startTaskObserverIfNeeded();
    }

    private void setResult(LottieResult<T> lottieResult) {
        if (this.result == null) {
            this.result = lottieResult;
            notifyListeners();
            return;
        }
        throw new IllegalStateException("A task may only be set once.");
    }

    public synchronized LottieTask<T> addListener(LottieListener<T> lottieListener) {
        if (!(this.result == null || this.result.getValue() == null)) {
            lottieListener.onResult(this.result.getValue());
        }
        this.successListeners.add(lottieListener);
        startTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> removeListener(LottieListener<T> lottieListener) {
        this.successListeners.remove(lottieListener);
        stopTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> addFailureListener(LottieListener<Throwable> lottieListener) {
        if (!(this.result == null || this.result.getException() == null)) {
            lottieListener.onResult(this.result.getException());
        }
        this.failureListeners.add(lottieListener);
        startTaskObserverIfNeeded();
        return this;
    }

    public synchronized LottieTask<T> removeFailureListener(LottieListener<Throwable> lottieListener) {
        this.failureListeners.remove(lottieListener);
        stopTaskObserverIfNeeded();
        return this;
    }

    private void notifyListeners() {
        this.handler.post(new C03701());
    }

    private void notifySuccessListeners(T t) {
        for (LottieListener onResult : new ArrayList(this.successListeners)) {
            onResult.onResult(t);
        }
    }

    private void notifyFailureListeners(Throwable th) {
        ArrayList<LottieListener> arrayList = new ArrayList(this.failureListeners);
        if (arrayList.isEmpty()) {
            Log.w("LOTTIE", "Lottie encountered an error but no failure listener was added.", th);
            return;
        }
        for (LottieListener onResult : arrayList) {
            onResult.onResult(th);
        }
    }

    /* JADX WARNING: Missing block: B:11:0x0022, code skipped:
            return;
     */
    private synchronized void startTaskObserverIfNeeded() {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = r2.taskObserverAlive();	 Catch:{ all -> 0x0023 }
        if (r0 != 0) goto L_0x0021;
    L_0x0007:
        r0 = r2.result;	 Catch:{ all -> 0x0023 }
        if (r0 == 0) goto L_0x000c;
    L_0x000b:
        goto L_0x0021;
    L_0x000c:
        r0 = new com.airbnb.lottie.LottieTask$2;	 Catch:{ all -> 0x0023 }
        r1 = "LottieTaskObserver";
        r0.<init>(r1);	 Catch:{ all -> 0x0023 }
        r2.taskObserver = r0;	 Catch:{ all -> 0x0023 }
        r0 = r2.taskObserver;	 Catch:{ all -> 0x0023 }
        r0.start();	 Catch:{ all -> 0x0023 }
        r0 = "Starting TaskObserver thread";
        com.airbnb.lottie.C0352L.debug(r0);	 Catch:{ all -> 0x0023 }
        monitor-exit(r2);
        return;
    L_0x0021:
        monitor-exit(r2);
        return;
    L_0x0023:
        r0 = move-exception;
        monitor-exit(r2);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieTask.startTaskObserverIfNeeded():void");
    }

    /* JADX WARNING: Missing block: B:13:0x0023, code skipped:
            return;
     */
    private synchronized void stopTaskObserverIfNeeded() {
        /*
        r1 = this;
        monitor-enter(r1);
        r0 = r1.taskObserverAlive();	 Catch:{ all -> 0x0024 }
        if (r0 != 0) goto L_0x0009;
    L_0x0007:
        monitor-exit(r1);
        return;
    L_0x0009:
        r0 = r1.successListeners;	 Catch:{ all -> 0x0024 }
        r0 = r0.isEmpty();	 Catch:{ all -> 0x0024 }
        if (r0 != 0) goto L_0x0015;
    L_0x0011:
        r0 = r1.result;	 Catch:{ all -> 0x0024 }
        if (r0 == 0) goto L_0x0022;
    L_0x0015:
        r0 = r1.taskObserver;	 Catch:{ all -> 0x0024 }
        r0.interrupt();	 Catch:{ all -> 0x0024 }
        r0 = 0;
        r1.taskObserver = r0;	 Catch:{ all -> 0x0024 }
        r0 = "Stopping TaskObserver thread";
        com.airbnb.lottie.C0352L.debug(r0);	 Catch:{ all -> 0x0024 }
    L_0x0022:
        monitor-exit(r1);
        return;
    L_0x0024:
        r0 = move-exception;
        monitor-exit(r1);
        throw r0;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieTask.stopTaskObserverIfNeeded():void");
    }

    private boolean taskObserverAlive() {
        return this.taskObserver != null && this.taskObserver.isAlive();
    }
}
