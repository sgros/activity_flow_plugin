// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.rx;

import rx.Observable;
import java.util.concurrent.Callable;
import rx.Scheduler;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.annotation.apihint.Experimental;

@Experimental
public class RxTransaction extends RxBase
{
    private final AbstractDaoSession daoSession;
    
    public RxTransaction(final AbstractDaoSession daoSession) {
        this.daoSession = daoSession;
    }
    
    public RxTransaction(final AbstractDaoSession daoSession, final Scheduler scheduler) {
        super(scheduler);
        this.daoSession = daoSession;
    }
    
    @Experimental
    public <T> Observable<T> call(final Callable<T> callable) {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return RxTransaction.this.daoSession.callInTx(callable);
            }
        });
    }
    
    @Experimental
    public AbstractDaoSession getDaoSession() {
        return this.daoSession;
    }
    
    @Experimental
    public Observable<Void> run(final Runnable runnable) {
        return this.wrap((Callable<Void>)new Callable<Void>() {
            @Override
            public Void call() throws Exception {
                RxTransaction.this.daoSession.runInTx(runnable);
                return null;
            }
        });
    }
}
