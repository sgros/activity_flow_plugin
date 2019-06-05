package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.browser.session.Session.SecurityInfo;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: Session.kt */
final class Session$$special$$inlined$observable$7$lambda$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ SecurityInfo $new;
    final /* synthetic */ Session$$special$$inlined$observable$7 this$0;

    Session$$special$$inlined$observable$7$lambda$1(SecurityInfo securityInfo, Session$$special$$inlined$observable$7 session$$special$$inlined$observable$7) {
        this.$new = securityInfo;
        this.this$0 = session$$special$$inlined$observable$7;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onSecurityChanged(this.this$0.this$0, this.$new.getSecure());
    }
}
