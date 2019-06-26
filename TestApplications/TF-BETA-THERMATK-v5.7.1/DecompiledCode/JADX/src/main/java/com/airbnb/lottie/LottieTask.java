package com.airbnb.lottie;

import android.os.Handler;
import android.os.Looper;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;

public class LottieTask<T> {
    public static Executor EXECUTOR = Executors.newCachedThreadPool();
    private final Set<LottieListener<Throwable>> failureListeners;
    private final Handler handler;
    private volatile LottieResult<T> result;
    private final Set<LottieListener<T>> successListeners;

    /* renamed from: com.airbnb.lottie.LottieTask$1 */
    class C01171 implements Runnable {
        C01171() {
        }

        public void run() {
            if (LottieTask.this.result != null) {
                LottieResult access$000 = LottieTask.this.result;
                if (access$000.getValue() != null) {
                    LottieTask.this.notifySuccessListeners(access$000.getValue());
                } else {
                    LottieTask.this.notifyFailureListeners(access$000.getException());
                }
            }
        }
    }

    private class LottieFutureTask extends FutureTask<LottieResult<T>> {
        LottieFutureTask(Callable<LottieResult<T>> callable) {
            super(callable);
        }

        /* Access modifiers changed, original: protected */
        public void done() {
            if (!isCancelled()) {
                try {
                    LottieTask.this.setResult((LottieResult) get());
                } catch (InterruptedException | ExecutionException e) {
                    LottieTask.this.setResult(new LottieResult(e));
                }
            }
        }
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:18:0x002e in {6, 12, 14, 17} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private synchronized void notifyFailureListeners(java.lang.Throwable r3) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x002b }
        r1 = r2.failureListeners;	 Catch:{ all -> 0x002b }
        r0.<init>(r1);	 Catch:{ all -> 0x002b }
        r1 = r0.isEmpty();	 Catch:{ all -> 0x002b }
        if (r1 == 0) goto L_0x0015;	 Catch:{ all -> 0x002b }
        r0 = "Lottie encountered an error but no failure listener was added:";	 Catch:{ all -> 0x002b }
        com.airbnb.lottie.utils.Logger.warning(r0, r3);	 Catch:{ all -> 0x002b }
        monitor-exit(r2);
        return;
        r0 = r0.iterator();	 Catch:{ all -> 0x002b }
        r1 = r0.hasNext();	 Catch:{ all -> 0x002b }
        if (r1 == 0) goto L_0x0029;	 Catch:{ all -> 0x002b }
        r1 = r0.next();	 Catch:{ all -> 0x002b }
        r1 = (com.airbnb.lottie.LottieListener) r1;	 Catch:{ all -> 0x002b }
        r1.onResult(r3);	 Catch:{ all -> 0x002b }
        goto L_0x0019;
        monitor-exit(r2);
        return;
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieTask.notifyFailureListeners(java.lang.Throwable):void");
    }

    /*  JADX ERROR: JadxRuntimeException in pass: BlockProcessor
        jadx.core.utils.exceptions.JadxRuntimeException: Can't find immediate dominator for block B:12:0x0021 in {6, 8, 11} preds:[]
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.computeDominators(BlockProcessor.java:242)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.processBlocksTree(BlockProcessor.java:52)
        	at jadx.core.dex.visitors.blocksmaker.BlockProcessor.visit(BlockProcessor.java:42)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:27)
        	at jadx.core.dex.visitors.DepthTraversal.lambda$visit$1(DepthTraversal.java:14)
        	at java.base/java.util.ArrayList.forEach(ArrayList.java:1378)
        	at jadx.core.dex.visitors.DepthTraversal.visit(DepthTraversal.java:14)
        	at jadx.core.ProcessClass.process(ProcessClass.java:32)
        	at jadx.core.ProcessClass.lambda$processDependencies$0(ProcessClass.java:51)
        	at java.base/java.lang.Iterable.forEach(Iterable.java:75)
        	at jadx.core.ProcessClass.processDependencies(ProcessClass.java:51)
        	at jadx.core.ProcessClass.process(ProcessClass.java:37)
        	at jadx.api.JadxDecompiler.processClass(JadxDecompiler.java:292)
        	at jadx.api.JavaClass.decompile(JavaClass.java:62)
        	at jadx.api.JadxDecompiler.lambda$appendSourcesSave$0(JadxDecompiler.java:200)
        */
    private synchronized void notifySuccessListeners(T r3) {
        /*
        r2 = this;
        monitor-enter(r2);
        r0 = new java.util.ArrayList;	 Catch:{ all -> 0x001e }
        r1 = r2.successListeners;	 Catch:{ all -> 0x001e }
        r0.<init>(r1);	 Catch:{ all -> 0x001e }
        r0 = r0.iterator();	 Catch:{ all -> 0x001e }
        r1 = r0.hasNext();	 Catch:{ all -> 0x001e }
        if (r1 == 0) goto L_0x001c;	 Catch:{ all -> 0x001e }
        r1 = r0.next();	 Catch:{ all -> 0x001e }
        r1 = (com.airbnb.lottie.LottieListener) r1;	 Catch:{ all -> 0x001e }
        r1.onResult(r3);	 Catch:{ all -> 0x001e }
        goto L_0x000c;
        monitor-exit(r2);
        return;
        r3 = move-exception;
        monitor-exit(r2);
        throw r3;
        return;
        */
        throw new UnsupportedOperationException("Method not decompiled: com.airbnb.lottie.LottieTask.notifySuccessListeners(java.lang.Object):void");
    }

    public LottieTask(Callable<LottieResult<T>> callable) {
        this(callable, false);
    }

    LottieTask(Callable<LottieResult<T>> callable, boolean z) {
        this.successListeners = new LinkedHashSet(1);
        this.failureListeners = new LinkedHashSet(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        if (z) {
            try {
                setResult((LottieResult) callable.call());
                return;
            } catch (Throwable th) {
                setResult(new LottieResult(th));
                return;
            }
        }
        EXECUTOR.execute(new LottieFutureTask(callable));
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
        return this;
    }

    public synchronized LottieTask<T> removeListener(LottieListener<T> lottieListener) {
        this.successListeners.remove(lottieListener);
        return this;
    }

    public synchronized LottieTask<T> addFailureListener(LottieListener<Throwable> lottieListener) {
        if (!(this.result == null || this.result.getException() == null)) {
            lottieListener.onResult(this.result.getException());
        }
        this.failureListeners.add(lottieListener);
        return this;
    }

    public synchronized LottieTask<T> removeFailureListener(LottieListener<Throwable> lottieListener) {
        this.failureListeners.remove(lottieListener);
        return this;
    }

    private void notifyListeners() {
        this.handler.post(new C01171());
    }
}
