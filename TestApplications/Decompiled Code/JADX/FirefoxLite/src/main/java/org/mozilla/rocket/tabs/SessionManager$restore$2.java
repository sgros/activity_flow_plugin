package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$restore$2 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ SessionManager this$0;

    SessionManager$restore$2(SessionManager sessionManager) {
        this.this$0 = sessionManager;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onSessionCountChanged(this.this$0.sessions.size());
    }
}
