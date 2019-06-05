package org.mozilla.cachedrequestloader;

import android.content.Context;

/* compiled from: lambda */
/* renamed from: org.mozilla.cachedrequestloader.-$$Lambda$BackgroundCachedRequestLoader$b0Blxy5LETn8C0N3GuPfyvuqAPM */
public final /* synthetic */ class C0422x2468b964 implements Runnable {
    private final /* synthetic */ BackgroundCachedRequestLoader f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Context f$2;
    private final /* synthetic */ String f$3;
    private final /* synthetic */ String f$4;
    private final /* synthetic */ ResponseData f$5;

    public /* synthetic */ C0422x2468b964(BackgroundCachedRequestLoader backgroundCachedRequestLoader, int i, Context context, String str, String str2, ResponseData responseData) {
        this.f$0 = backgroundCachedRequestLoader;
        this.f$1 = i;
        this.f$2 = context;
        this.f$3 = str;
        this.f$4 = str2;
        this.f$5 = responseData;
    }

    public final void run() {
        BackgroundCachedRequestLoader.lambda$loadFromRemote$1(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
