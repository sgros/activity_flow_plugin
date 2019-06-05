package org.mozilla.rocket.tabs;

import kotlin.Unit;
import kotlin.jvm.functions.Function1;
import kotlin.jvm.internal.Intrinsics;
import kotlin.jvm.internal.Lambda;
import org.mozilla.rocket.tabs.TabViewEngineSession.Observer;
import org.mozilla.rocket.tabs.web.Download;

/* compiled from: TabViewEngineSession.kt */
final class TabViewEngineSession$DownloadCallback$onDownloadStart$1 extends Lambda implements Function1<Observer, Unit> {
    final /* synthetic */ String $cookie;
    final /* synthetic */ Download $download;

    TabViewEngineSession$DownloadCallback$onDownloadStart$1(Download download, String str) {
        this.$download = download;
        this.$cookie = str;
        super(1);
    }

    public final void invoke(Observer observer) {
        Intrinsics.checkParameterIsNotNull(observer, "receiver$0");
        String url = this.$download.getUrl();
        Intrinsics.checkExpressionValueIsNotNull(url, "download.url");
        observer.onExternalResource(url, this.$download.getName(), Long.valueOf(this.$download.getContentLength()), this.$download.getMimeType(), this.$cookie, this.$download.getUserAgent());
    }
}
