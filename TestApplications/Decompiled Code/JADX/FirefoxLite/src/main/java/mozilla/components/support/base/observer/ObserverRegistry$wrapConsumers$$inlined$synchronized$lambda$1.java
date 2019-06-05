package mozilla.components.support.base.observer;

import java.util.List;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Lambda;

/* compiled from: ObserverRegistry.kt */
final class ObserverRegistry$wrapConsumers$$inlined$synchronized$lambda$1 extends Lambda implements Function1<V, Boolean> {
    final /* synthetic */ Function2 $block$inlined;
    final /* synthetic */ List $consumers$inlined;
    final /* synthetic */ Object $observer;
    final /* synthetic */ ObserverRegistry this$0;

    ObserverRegistry$wrapConsumers$$inlined$synchronized$lambda$1(Object obj, ObserverRegistry observerRegistry, List list, Function2 function2) {
        this.$observer = obj;
        this.this$0 = observerRegistry;
        this.$consumers$inlined = list;
        this.$block$inlined = function2;
        super(1);
    }

    public final boolean invoke(V v) {
        return ((Boolean) this.$block$inlined.invoke(this.$observer, v)).booleanValue();
    }
}
