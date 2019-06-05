package org.mozilla.rocket.tabs;

import java.util.List;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.browser.session.Session.FindResult;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: Session.kt */
final class Session$$special$$inlined$observable$8$lambda$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ List $new;
    final /* synthetic */ Session$$special$$inlined$observable$8 this$0;

    Session$$special$$inlined$observable$8$lambda$1(List list, Session$$special$$inlined$observable$8 session$$special$$inlined$observable$8) {
        this.$new = list;
        this.this$0 = session$$special$$inlined$observable$8;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        if ((this.$new.isEmpty() ^ 1) != 0) {
            observer.onFindResult(this.this$0.this$0, (FindResult) CollectionsKt___CollectionsKt.last(this.this$0.this$0.getFindResults()));
        }
    }
}
