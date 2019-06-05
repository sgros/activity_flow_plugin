package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import mozilla.components.concept.engine.EngineSession.Observer.DefaultImpls;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$ViewClient$onPageFinished$2 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ boolean $isSecure;

    TabViewEngineSession$ViewClient$onPageFinished$2(boolean z) {
        this.$isSecure = z;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        DefaultImpls.onSecurityChange$default(observer, this.$isSecure, null, null, 6, null);
    }
}
