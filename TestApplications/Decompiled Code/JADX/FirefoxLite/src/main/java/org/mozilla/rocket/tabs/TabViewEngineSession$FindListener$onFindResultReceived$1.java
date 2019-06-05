package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$FindListener$onFindResultReceived$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ int $activeMatchOrdinal;
    final /* synthetic */ boolean $isDoneCounting;
    final /* synthetic */ int $numberOfMatches;

    TabViewEngineSession$FindListener$onFindResultReceived$1(int i, int i2, boolean z) {
        this.$activeMatchOrdinal = i;
        this.$numberOfMatches = i2;
        this.$isDoneCounting = z;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        observer.onFindResult(this.$activeMatchOrdinal, this.$numberOfMatches, this.$isDoneCounting);
    }
}
