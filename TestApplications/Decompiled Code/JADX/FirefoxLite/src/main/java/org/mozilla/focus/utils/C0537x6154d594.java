package org.mozilla.focus.utils;

import org.mozilla.rocket.content.NewsSourceManager;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$FirebaseHelper$BlockingEnabler$v63GBN4VpioJ7nkexjM6ehiNugY */
public final /* synthetic */ class C0537x6154d594 implements Runnable {
    private final /* synthetic */ String f$0;

    public /* synthetic */ C0537x6154d594(String str) {
        this.f$0 = str;
    }

    public final void run() {
        NewsSourceManager.getInstance().setNewsSourceUrl(this.f$0);
    }
}
