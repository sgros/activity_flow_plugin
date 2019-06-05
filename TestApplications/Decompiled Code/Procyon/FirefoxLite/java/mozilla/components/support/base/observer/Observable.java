// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.observer;

import java.util.List;
import kotlin.jvm.functions.Function2;
import android.arch.lifecycle.LifecycleOwner;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;

public interface Observable<T>
{
    void notifyObservers(final Function1<? super T, Unit> p0);
    
    void register(final T p0);
    
    void register(final T p0, final LifecycleOwner p1, final boolean p2);
    
    void unregister(final T p0);
    
    void unregisterObservers();
    
     <R> List<Function1<R, Boolean>> wrapConsumers(final Function2<? super T, ? super R, Boolean> p0);
}
