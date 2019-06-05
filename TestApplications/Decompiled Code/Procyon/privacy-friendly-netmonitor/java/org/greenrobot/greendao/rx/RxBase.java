// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.rx;

import rx.Observable;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import rx.Scheduler;
import org.greenrobot.greendao.annotation.apihint.Internal;

@Internal
class RxBase
{
    protected final Scheduler scheduler;
    
    RxBase() {
        this.scheduler = null;
    }
    
    @Experimental
    RxBase(final Scheduler scheduler) {
        this.scheduler = scheduler;
    }
    
    @Experimental
    public Scheduler getScheduler() {
        return this.scheduler;
    }
    
    protected <R> Observable<R> wrap(final Callable<R> callable) {
        return this.wrap((rx.Observable<R>)RxUtils.fromCallable((Callable<R>)callable));
    }
    
    protected <R> Observable<R> wrap(final Observable<R> observable) {
        if (this.scheduler != null) {
            return (Observable<R>)observable.subscribeOn(this.scheduler);
        }
        return observable;
    }
}
