package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: Session.kt */
final class Session$$special$$inlined$observable$3$lambda$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ int $new;
    final /* synthetic */ Session$$special$$inlined$observable$3 this$0;

    Session$$special$$inlined$observable$3$lambda$1(int i, Session$$special$$inlined$observable$3 session$$special$$inlined$observable$3) {
        this.$new = i;
        this.this$0 = session$$special$$inlined$observable$3;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onProgress(this.this$0.this$0, this.$new);
    }
}
