package org.mozilla.rocket.tabs;

import android.os.Bundle;
import kotlin.Pair;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.SessionManager.Observer;

/* compiled from: SessionManager.kt */
final class SessionManager$Notifier$addedTab$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ Pair $pair;

    SessionManager$Notifier$addedTab$1(Pair pair) {
        this.$pair = pair;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onSessionAdded((Session) this.$pair.getFirst(), (Bundle) this.$pair.getSecond());
    }
}
