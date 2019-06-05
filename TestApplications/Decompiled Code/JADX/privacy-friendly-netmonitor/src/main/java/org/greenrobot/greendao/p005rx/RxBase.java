package org.greenrobot.greendao.p005rx;

import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.annotation.apihint.Internal;
import rx.Observable;
import rx.Scheduler;

@Internal
/* renamed from: org.greenrobot.greendao.rx.RxBase */
class RxBase {
    protected final Scheduler scheduler;

    RxBase() {
        this.scheduler = null;
    }

    @Experimental
    RxBase(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Experimental
    public Scheduler getScheduler() {
        return this.scheduler;
    }

    /* Access modifiers changed, original: protected */
    public <R> Observable<R> wrap(Callable<R> callable) {
        return wrap(RxUtils.fromCallable(callable));
    }

    /* Access modifiers changed, original: protected */
    public <R> Observable<R> wrap(Observable<R> observable) {
        return this.scheduler != null ? observable.subscribeOn(this.scheduler) : observable;
    }
}
