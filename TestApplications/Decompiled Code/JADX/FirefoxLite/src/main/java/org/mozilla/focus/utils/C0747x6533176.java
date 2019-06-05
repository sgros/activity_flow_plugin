package org.mozilla.focus.utils;

import android.content.Context;
import org.mozilla.focus.utils.FirebaseWrapper.RemoteConfigFetchCallback;
import org.mozilla.threadutils.ThreadUtils;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.utils.-$$Lambda$FirebaseHelper$BlockingEnabler$KbJvRYq_uiQihzjwaWiFwpeUbX4 */
public final /* synthetic */ class C0747x6533176 implements RemoteConfigFetchCallback {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C0747x6533176(Context context) {
        this.f$0 = context;
    }

    public final void onFetched() {
        ThreadUtils.postToBackgroundThread(new C0536xb21e9f1d(this.f$0));
    }
}
