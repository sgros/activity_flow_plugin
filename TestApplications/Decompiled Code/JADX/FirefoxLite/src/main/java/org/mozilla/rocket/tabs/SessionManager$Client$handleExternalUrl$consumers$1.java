package org.mozilla.rocket.tabs;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$Client$handleExternalUrl$consumers$1 extends Lambda implements Function2<Observer, String, Boolean> {
    public static final SessionManager$Client$handleExternalUrl$consumers$1 INSTANCE = new SessionManager$Client$handleExternalUrl$consumers$1();

    SessionManager$Client$handleExternalUrl$consumers$1() {
        super(2);
    }

    public final boolean invoke(Observer observer, String str) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        return observer.handleExternalUrl(str);
    }
}
