package org.mozilla.focus.urlinput;

import android.text.TextUtils;
import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.telemetry.TelemetryWrapper;
import org.mozilla.rocket.urlinput.QuickSearch;

/* compiled from: UrlInputFragment.kt */
final class UrlInputFragment$initQuickSearch$quickSearchAdapter$1 extends Lambda implements Function1<QuickSearch, Unit> {
    final /* synthetic */ UrlInputFragment this$0;

    UrlInputFragment$initQuickSearch$quickSearchAdapter$1(UrlInputFragment urlInputFragment) {
        this.this$0 = urlInputFragment;
        super(1);
    }

    public final void invoke(QuickSearch quickSearch) {
        Intrinsics.checkParameterIsNotNull(quickSearch, "quickSearch");
        if (TextUtils.isEmpty(UrlInputFragment.access$getUrlView$p(this.this$0).getText())) {
            this.this$0.openUrl(quickSearch.getHomeUrl());
        } else {
            this.this$0.openUrl(quickSearch.generateLink(UrlInputFragment.access$getUrlView$p(this.this$0).getOriginalText()));
        }
        TelemetryWrapper.clickQuickSearchEngine(quickSearch.getName());
    }
}
