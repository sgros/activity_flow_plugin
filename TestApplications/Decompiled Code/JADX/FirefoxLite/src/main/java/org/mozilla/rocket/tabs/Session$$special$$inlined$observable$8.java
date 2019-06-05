package org.mozilla.rocket.tabs;

import java.util.List;
import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;
import mozilla.components.browser.session.Session.FindResult;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$observable$8 extends ObservableProperty<List<? extends FindResult>> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$observable$8(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, List<? extends FindResult> list, List<? extends FindResult> list2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        List list3 = list2;
        this.this$0.notifyObservers(list, list3, new Session$$special$$inlined$observable$8$lambda$1(list3, this));
    }
}
