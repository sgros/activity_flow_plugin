// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.concurrent.ExecutionException;
import java.util.Iterator;
import android.util.Log;
import java.util.Collection;
import java.util.ArrayList;
import android.os.Looper;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
import java.util.concurrent.FutureTask;
import android.os.Handler;
import java.util.Set;
import java.util.concurrent.Executor;

public class LottieTask<T>
{
    public static Executor EXECUTOR;
    private final Set<LottieListener<Throwable>> failureListeners;
    private final Handler handler;
    private volatile LottieResult<T> result;
    private final Set<LottieListener<T>> successListeners;
    private final FutureTask<LottieResult<T>> task;
    private Thread taskObserver;
    
    static {
        LottieTask.EXECUTOR = Executors.newCachedThreadPool();
    }
    
    public LottieTask(final Callable<LottieResult<T>> callable) {
        this(callable, false);
    }
    
    LottieTask(final Callable<LottieResult<T>> callable, final boolean b) {
        this.successListeners = new LinkedHashSet<LottieListener<T>>(1);
        this.failureListeners = new LinkedHashSet<LottieListener<Throwable>>(1);
        this.handler = new Handler(Looper.getMainLooper());
        this.result = null;
        this.task = new FutureTask<LottieResult<T>>(callable);
        if (b) {
            try {
                this.setResult(callable.call());
            }
            catch (Throwable t) {
                this.setResult(new LottieResult<T>(t));
            }
        }
        else {
            LottieTask.EXECUTOR.execute(this.task);
            this.startTaskObserverIfNeeded();
        }
    }
    
    private void notifyFailureListeners(final Throwable t) {
        final ArrayList<LottieListener<Throwable>> list = (ArrayList<LottieListener<Throwable>>)new ArrayList<Object>(this.failureListeners);
        if (list.isEmpty()) {
            Log.w("LOTTIE", "Lottie encountered an error but no failure listener was added.", t);
            return;
        }
        final Iterator<Object> iterator = list.iterator();
        while (iterator.hasNext()) {
            iterator.next().onResult(t);
        }
    }
    
    private void notifyListeners() {
        this.handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (LottieTask.this.result != null && !LottieTask.this.task.isCancelled()) {
                    final LottieResult access$000 = LottieTask.this.result;
                    if (access$000.getValue() != null) {
                        LottieTask.this.notifySuccessListeners(access$000.getValue());
                    }
                    else {
                        LottieTask.this.notifyFailureListeners(access$000.getException());
                    }
                }
            }
        });
    }
    
    private void notifySuccessListeners(final T t) {
        final Iterator<LottieListener<T>> iterator = new ArrayList<LottieListener<T>>(this.successListeners).iterator();
        while (iterator.hasNext()) {
            iterator.next().onResult(t);
        }
    }
    
    private void setResult(final LottieResult<T> result) {
        if (this.result == null) {
            this.result = result;
            this.notifyListeners();
            return;
        }
        throw new IllegalStateException("A task may only be set once.");
    }
    
    private void startTaskObserverIfNeeded() {
        synchronized (this) {
            if (!this.taskObserverAlive() && this.result == null) {
                (this.taskObserver = new Thread("LottieTaskObserver") {
                    private boolean taskComplete = false;
                    
                    @Override
                    public void run() {
                        while (!this.isInterrupted() && !this.taskComplete) {
                            if (LottieTask.this.task.isDone()) {
                                try {
                                    LottieTask.this.setResult(LottieTask.this.task.get());
                                }
                                catch (InterruptedException | ExecutionException ex) {
                                    final Object o;
                                    LottieTask.this.setResult(new LottieResult((Throwable)o));
                                }
                                this.taskComplete = true;
                                LottieTask.this.stopTaskObserverIfNeeded();
                            }
                        }
                    }
                }).start();
                L.debug("Starting TaskObserver thread");
            }
        }
    }
    
    private void stopTaskObserverIfNeeded() {
        synchronized (this) {
            if (!this.taskObserverAlive()) {
                return;
            }
            if (this.successListeners.isEmpty() || this.result != null) {
                this.taskObserver.interrupt();
                this.taskObserver = null;
                L.debug("Stopping TaskObserver thread");
            }
        }
    }
    
    private boolean taskObserverAlive() {
        return this.taskObserver != null && this.taskObserver.isAlive();
    }
    
    public LottieTask<T> addFailureListener(final LottieListener<Throwable> lottieListener) {
        synchronized (this) {
            if (this.result != null && this.result.getException() != null) {
                lottieListener.onResult(this.result.getException());
            }
            this.failureListeners.add(lottieListener);
            this.startTaskObserverIfNeeded();
            return this;
        }
    }
    
    public LottieTask<T> addListener(final LottieListener<T> lottieListener) {
        synchronized (this) {
            if (this.result != null && this.result.getValue() != null) {
                lottieListener.onResult(this.result.getValue());
            }
            this.successListeners.add(lottieListener);
            this.startTaskObserverIfNeeded();
            return this;
        }
    }
    
    public LottieTask<T> removeFailureListener(final LottieListener<Throwable> lottieListener) {
        synchronized (this) {
            this.failureListeners.remove(lottieListener);
            this.stopTaskObserverIfNeeded();
            return this;
        }
    }
    
    public LottieTask<T> removeListener(final LottieListener<T> lottieListener) {
        synchronized (this) {
            this.successListeners.remove(lottieListener);
            this.stopTaskObserverIfNeeded();
            return this;
        }
    }
}
