package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$Client$updateFailingUrl$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ boolean $updateFromError;
    final /* synthetic */ String $url;

    SessionManager$Client$updateFailingUrl$1(String str, boolean z) {
        this.$url = str;
        this.$updateFromError = z;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.updateFailingUrl(this.$url, this.$updateFromError);
    }
}
