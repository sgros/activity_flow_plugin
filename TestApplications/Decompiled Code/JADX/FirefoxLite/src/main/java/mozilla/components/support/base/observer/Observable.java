package mozilla.components.support.base.observer;

import android.arch.lifecycle.LifecycleOwner;
import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;

/* compiled from: Observable.kt */
public interface Observable<T> {
    void notifyObservers(Function1<? super T, Unit> function1);

    void register(T t);

    void register(T t, LifecycleOwner lifecycleOwner, boolean z);

    void unregister(T t);

    void unregisterObservers();

    <R> List<Function1<R, Boolean>> wrapConsumers(Function2<? super T, ? super R, Boolean> function2);
}
