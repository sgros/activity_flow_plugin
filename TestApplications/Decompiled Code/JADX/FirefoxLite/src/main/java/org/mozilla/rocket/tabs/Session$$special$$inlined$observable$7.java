package org.mozilla.rocket.tabs;

import kotlin.jvm.internal.Intrinsics;
import kotlin.properties.ObservableProperty;
import kotlin.reflect.KProperty;
import mozilla.components.browser.session.Session.SecurityInfo;

/* compiled from: Delegates.kt */
public final class Session$$special$$inlined$observable$7 extends ObservableProperty<SecurityInfo> {
    final /* synthetic */ Object $initialValue;
    final /* synthetic */ Session this$0;

    public Session$$special$$inlined$observable$7(Object obj, Object obj2, Session session) {
        this.$initialValue = obj;
        this.this$0 = session;
        super(obj2);
    }

    /* Access modifiers changed, original: protected */
    public void afterChange(KProperty<?> kProperty, SecurityInfo securityInfo, SecurityInfo securityInfo2) {
        Intrinsics.checkParameterIsNotNull(kProperty, "property");
        securityInfo2 = securityInfo2;
        this.this$0.notifyObservers(securityInfo, securityInfo2, new Session$$special$$inlined$observable$7$lambda$1(securityInfo2, this));
    }
}
