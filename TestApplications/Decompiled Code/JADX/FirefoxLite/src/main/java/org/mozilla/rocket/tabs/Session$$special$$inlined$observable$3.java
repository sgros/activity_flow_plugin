package org.mozilla.rocket.tabs;

import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$observable$3 extends ObservableProperty<Integer> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$observable$3(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, Integer num, Integer num2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        int intValue = num2.intValue();
        this.this$0.notifyObservers(Integer.valueOf(num.intValue()), Integer.valueOf(intValue), new Session$$special$$inlined$observable$3$lambda$1(intValue, this));
    }
}
