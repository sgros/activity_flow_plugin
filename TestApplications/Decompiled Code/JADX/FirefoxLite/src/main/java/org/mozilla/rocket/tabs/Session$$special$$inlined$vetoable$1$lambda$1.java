package org.mozilla.rocket.tabs;

import kotlin.jvm.functions.Function2;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.browser.session.Download;
import org.mozilla.rocket.tabs.Session.Observer;

/* compiled from: Session.kt */
final class Session$$special$$inlined$vetoable$1$lambda$1 extends Lambda implements Function2<Observer, Download, Boolean> {
    final /* synthetic */ Session$$special$$inlined$vetoable$1 this$0;

    Session$$special$$inlined$vetoable$1$lambda$1(Session$$special$$inlined$vetoable$1 session$$special$$inlined$vetoable$1) {
        this.this$0 = session$$special$$inlined$vetoable$1;
        super(2);
    }

    public final boolean invoke(Observer observer, Download download) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        Intrinsics.checkParameterIsNotNull(download, "it");
        return observer.onDownload(this.this$0.this$0, download);
    }
}
