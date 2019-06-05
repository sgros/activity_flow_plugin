package org.mozilla.rocket.tabs;

import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$observable$4 extends ObservableProperty<Boolean> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$observable$4(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, Boolean bool, Boolean bool2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        boolean booleanValue = bool2.booleanValue();
        this.this$0.notifyObservers(Boolean.valueOf(bool.booleanValue()), Boolean.valueOf(booleanValue), new Session$$special$$inlined$observable$4$lambda$1(booleanValue, this));
    }
}
