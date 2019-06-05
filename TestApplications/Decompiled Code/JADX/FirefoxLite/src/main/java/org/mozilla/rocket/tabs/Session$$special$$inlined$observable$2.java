package org.mozilla.rocket.tabs;

import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$observable$2 extends ObservableProperty<String> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$observable$2(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, String str, String str2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        str2 = str2;
        this.this$0.notifyObservers(str, str2, new Session$$special$$inlined$observable$2$lambda$1(str2, this));
    }
}
