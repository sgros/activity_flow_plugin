package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Factor;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$Notifier$focusTab$2 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ Factor $factor;
    final /* synthetic */ Session $session;

    SessionManager$Notifier$focusTab$2(Session session, Factor factor) {
        this.$session = session;
        this.$factor = factor;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onFocusChanged(this.$session, this.$factor);
    }
}
