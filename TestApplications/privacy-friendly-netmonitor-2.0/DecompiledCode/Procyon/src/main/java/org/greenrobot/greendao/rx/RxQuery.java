// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.rx;

import java.util.Iterator;
import org.greenrobot.greendao.query.LazyList;
import rx.exceptions.Exceptions;
import rx.Subscriber;
import rx.Observable$OnSubscribe;
import java.util.concurrent.Callable;
import java.util.List;
import rx.Observable;
import rx.Scheduler;
import org.greenrobot.greendao.query.Query;
import org.greenrobot.greendao.annotation.apihint.Experimental;

@Experimental
public class RxQuery<T> extends RxBase
{
    private final Query<T> query;
    
    public RxQuery(final Query<T> query) {
        this.query = query;
    }
    
    public RxQuery(final Query<T> query, final Scheduler scheduler) {
        super(scheduler);
        this.query = query;
    }
    
    @Experimental
    public Observable<List<T>> list() {
        return this.wrap((Callable<List<T>>)new Callable<List<T>>() {
            @Override
            public List<T> call() throws Exception {
                return RxQuery.this.query.forCurrentThread().list();
            }
        });
    }
    
    public Observable<T> oneByOne() {
        return this.wrap((rx.Observable<T>)Observable.create((Observable$OnSubscribe)new Observable$OnSubscribe<T>() {
            public void call(final Subscriber<? super T> subscriber) {
                try {
                    final LazyList<Object> listLazyUncached = RxQuery.this.query.forCurrentThread().listLazyUncached();
                    try {
                        for (final Object next : listLazyUncached) {
                            if (subscriber.isUnsubscribed()) {
                                break;
                            }
                            subscriber.onNext(next);
                        }
                        listLazyUncached.close();
                        if (!subscriber.isUnsubscribed()) {
                            subscriber.onCompleted();
                        }
                    }
                    finally {
                        listLazyUncached.close();
                    }
                }
                catch (Throwable t) {
                    Exceptions.throwIfFatal(t);
                    subscriber.onError(t);
                }
            }
        }));
    }
    
    @Experimental
    public Observable<T> unique() {
        return this.wrap((Callable<T>)new Callable<T>() {
            @Override
            public T call() throws Exception {
                return RxQuery.this.query.forCurrentThread().unique();
            }
        });
    }
}
