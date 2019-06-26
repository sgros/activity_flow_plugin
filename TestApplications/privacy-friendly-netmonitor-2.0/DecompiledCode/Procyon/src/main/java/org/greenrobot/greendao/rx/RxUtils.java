// 
// Decompiled by Procyon v0.5.34
// 

package org.greenrobot.greendao.rx;

import rx.functions.Func0;
import rx.Observable;
import java.util.concurrent.Callable;
import org.greenrobot.greendao.annotation.apihint.Internal;

@Internal
class RxUtils
{
    @Internal
    static <T> Observable<T> fromCallable(final Callable<T> callable) {
        return (Observable<T>)Observable.defer((Func0)new Func0<Observable<T>>() {
            public Observable<T> call() {
                try {
                    return (Observable<T>)Observable.just(callable.call());
                }
                catch (Exception ex) {
                    return (Observable<T>)Observable.error((Throwable)ex);
                }
            }
        });
    }
}
