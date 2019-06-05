package org.mozilla.cachedrequestloader;

import android.content.Context;

/* renamed from: org.mozilla.cachedrequestloader.-$$Lambda$BackgroundCachedRequestLoader$EHwI_s8H-FtNt9t2MTbwyEF0UBA */
public final /* synthetic */ class lambda implements Runnable {
    private final /* synthetic */ BackgroundCachedRequestLoader f$0;
    private final /* synthetic */ Context f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ ResponseData f$3;

    public /* synthetic */ lambda(BackgroundCachedRequestLoader backgroundCachedRequestLoader, Context context, String str, ResponseData responseData) {
        this.f$0 = backgroundCachedRequestLoader;
        this.f$1 = context;
        this.f$2 = str;
        this.f$3 = responseData;
    }

    public final void run() {
        BackgroundCachedRequestLoader.lambda$loadFromCache$0(this.f$0, this.f$1, this.f$2, this.f$3);
    }
}
