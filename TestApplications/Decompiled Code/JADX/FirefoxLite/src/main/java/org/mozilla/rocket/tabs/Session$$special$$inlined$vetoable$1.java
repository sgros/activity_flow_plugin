package org.mozilla.rocket.tabs;

import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;
import mozilla.components.browser.session.Download;
import mozilla.components.support.base.observer.Consumable;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$vetoable$1 extends ObservableProperty<Consumable<Download>> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$vetoable$1(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public boolean beforeChange(KProperty<?> kProperty, Consumable<Download> consumable, Consumable<Download> consumable2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        Consumable consumable3 = consumable;
        return consumable2.consumeBy(this.this$0.wrapConsumers(new Session$$special$$inlined$vetoable$1$lambda$1(this))) ^ 1;
    }
}
