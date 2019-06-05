package org.mozilla.rocket.home.pinsite;

import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.focus.history.model.Site;

/* compiled from: PinSiteManager.kt */
final class SharedPreferencePinSiteDelegate$unpinned$1 extends Lambda implements Function1<Site, Boolean> {
    final /* synthetic */ Site $site;

    SharedPreferencePinSiteDelegate$unpinned$1(Site site) {
        this.$site = site;
        super(1);
    }

    public final boolean invoke(Site site) {
        Intrinsics.checkParameterIsNotNull(site, "it");
        return site.getId() == this.$site.getId();
    }
}
