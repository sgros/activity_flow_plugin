package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: Session.kt */
final class Session$$special$$inlined$observable$2$lambda$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ String $new;
    final /* synthetic */ Session$$special$$inlined$observable$2 this$0;

    Session$$special$$inlined$observable$2$lambda$1(String str, Session$$special$$inlined$observable$2 session$$special$$inlined$observable$2) {
        this.$new = str;
        this.this$0 = session$$special$$inlined$observable$2;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onTitleChanged(this.this$0.this$0, this.$new);
    }
}
