package org.greenrobot.greendao.p005rx;

import java.util.Iterator;
import java.util.List;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Experimental;
import org.greenrobot.greendao.query.LazyList;
import org.greenrobot.greendao.query.Query;
import rx.Observable;
import rx.Observable.OnSubscribe;
import rx.Scheduler;
import rx.Subscriber;
import rx.exceptions.Exceptions;

@Experimental
/* renamed from: org.greenrobot.greendao.rx.RxQuery */
public class RxQuery<T> extends RxBase {
    private final Query<T> query;

    /* renamed from: org.greenrobot.greendao.rx.RxQuery$1 */
    class C04711 implements Callable<List<T>> {
        C04711() {
        }

        public List<T> call() throws Exception {
            return RxQuery.this.query.forCurrentThread().list();
        }
    }

    /* renamed from: org.greenrobot.greendao.rx.RxQuery$2 */
    class C04722 implements Callable<T> {
        C04722() {
        }

        public T call() throws Exception {
            return RxQuery.this.query.forCurrentThread().unique();
        }
    }

    /* renamed from: org.greenrobot.greendao.rx.RxQuery$3 */
    class C04733 implements OnSubscribe<T> {
        C04733() {
        }

        public void call(Subscriber<? super T> subscriber) {
            LazyList listLazyUncached;
            try {
                listLazyUncached = RxQuery.this.query.forCurrentThread().listLazyUncached();
                Iterator it = listLazyUncached.iterator();
                while (it.hasNext()) {
                    Object next = it.next();
                    if (subscriber.isUnsubscribed()) {
                        break;
                    }
                    subscriber.onNext(next);
                }
                listLazyUncached.close();
                if (!subscriber.isUnsubscribed()) {
                    subscriber.onCompleted();
                }
            } catch (Throwable th) {
                Exceptions.throwIfFatal(th);
                subscriber.onError(th);
            }
        }
    }

    @Experimental
    public /* bridge */ /* synthetic */ Scheduler getScheduler() {
        return super.getScheduler();
    }

    public RxQuery(Query<T> query) {
        this.query = query;
    }

    public RxQuery(Query<T> query, Scheduler scheduler) {
        super(scheduler);
        this.query = query;
    }

    @Experimental
    public Observable<List<T>> list() {
        return wrap((Callable) new C04711());
    }

    @Experimental
    public Observable<T> unique() {
        return wrap((Callable) new C04722());
    }

    public Observable<T> oneByOne() {
        return wrap(Observable.create(new C04733()));
    }
}
