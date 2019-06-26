// 
// Decompiled by Procyon v0.5.34
// 

package com.airbnb.lottie;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.FutureTask;
import java.util.Iterator;
import com.airbnb.lottie.utils.Logger;
import java.util.Collection;
import java.util.ArrayList;
import android.os.Looper;
import java.util.LinkedHashSet;
import java.util.concurrent.Callable;
import java.util.concurrent.Executors;
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
        if (b) {
            try {
                this.setResult(callable.call());
            }
            catch (Throwable t) {
                this.setResult(new LottieResult<T>(t));
            }
        }
        else {
            LottieTask.EXECUTOR.execute(new LottieFutureTask(callable));
        }
    }
    
    private void notifyFailureListeners(final Throwable t) {
        synchronized (this) {
            final ArrayList<LottieListener<Throwable>> list = new ArrayList<LottieListener<Throwable>>(this.failureListeners);
            if (list.isEmpty()) {
                Logger.warning("Lottie encountered an error but no failure listener was added:", t);
                return;
            }
            final Iterator<Object> iterator = list.iterator();
            while (iterator.hasNext()) {
                iterator.next().onResult(t);
            }
        }
    }
    
    private void notifyListeners() {
        this.handler.post((Runnable)new Runnable() {
            @Override
            public void run() {
                if (LottieTask.this.result == null) {
                    return;
                }
                final LottieResult access$000 = LottieTask.this.result;
                if (access$000.getValue() != null) {
                    LottieTask.this.notifySuccessListeners(access$000.getValue());
                }
                else {
                    LottieTask.this.notifyFailureListeners(access$000.getException());
                }
            }
        });
    }
    
    private void notifySuccessListeners(final T t) {
        synchronized (this) {
            final Iterator<LottieListener<T>> iterator = new ArrayList<LottieListener<T>>(this.successListeners).iterator();
            while (iterator.hasNext()) {
                iterator.next().onResult(t);
            }
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
    
    public LottieTask<T> addFailureListener(final LottieListener<Throwable> lottieListener) {
        synchronized (this) {
            if (this.result != null && this.result.getException() != null) {
                lottieListener.onResult(this.result.getException());
            }
            this.failureListeners.add(lottieListener);
            return this;
        }
    }
    
    public LottieTask<T> addListener(final LottieListener<T> lottieListener) {
        synchronized (this) {
            if (this.result != null && this.result.getValue() != null) {
                lottieListener.onResult(this.result.getValue());
            }
            this.successListeners.add(lottieListener);
            return this;
        }
    }
    
    public LottieTask<T> removeFailureListener(final LottieListener<Throwable> lottieListener) {
        synchronized (this) {
            this.failureListeners.remove(lottieListener);
            return this;
        }
    }
    
    public LottieTask<T> removeListener(final LottieListener<T> lottieListener) {
        synchronized (this) {
            this.successListeners.remove(lottieListener);
            return this;
        }
    }
    
    private class LottieFutureTask extends FutureTask<LottieResult<T>>
    {
        LottieFutureTask(final Callable<LottieResult<T>> callable) {
            super(callable);
        }
        
        @Override
        protected void done() {
            if (this.isCancelled()) {
                return;
            }
            try {
                LottieTask.this.setResult(((FutureTask<LottieResult>)this).get());
                return;
            }
            catch (ExecutionException ex) {}
            catch (InterruptedException ex2) {}
            final ExecutionException ex;
            LottieTask.this.setResult(new LottieResult(ex));
        }
    }
}
