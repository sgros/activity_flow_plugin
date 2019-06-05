// 
// Decompiled by Procyon v0.5.34
// 

package mozilla.components.support.base.observer;

import java.util.Iterator;
import java.util.ArrayList;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.functions.Function1;
import java.util.List;

public final class Consumable<T>
{
    public static final Companion Companion;
    private T value;
    
    static {
        Companion = new Companion(null);
    }
    
    private Consumable(final T value) {
        this.value = value;
    }
    
    public final boolean consumeBy(final List<? extends Function1<? super T, Boolean>> list) {
        synchronized (this) {
            Intrinsics.checkParameterIsNotNull(list, "consumers");
            final T value = this.value;
            if (value != null) {
                final List<? extends Function1<? super T, Boolean>> list2 = list;
                final ArrayList<Boolean> list3 = new ArrayList<Boolean>(CollectionsKt__IterablesKt.collectionSizeOrDefault((Iterable<?>)list2, 10));
                final Iterator<Function1<Object, Boolean>> iterator = list2.iterator();
                while (iterator.hasNext()) {
                    list3.add((boolean)iterator.next().invoke(value));
                }
                final ArrayList<Boolean> list4 = list3;
                boolean b = true;
                if (list4.contains(true)) {
                    this.value = null;
                }
                else {
                    b = false;
                }
                return b;
            }
            return false;
        }
    }
    
    public static final class Companion
    {
        private Companion() {
        }
        
        public final <T> Consumable<T> empty() {
            return new Consumable<T>(null, null);
        }
        
        public final <T> Consumable<T> from(final T t) {
            return new Consumable<T>(t, null);
        }
    }
}
