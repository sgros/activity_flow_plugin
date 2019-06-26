package org.telegram.messenger;

import android.net.Uri;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$NotificationsController$hROO1aIM4eduzMv5uJ3U4yL97Bo */
public final /* synthetic */ class C0919-$$Lambda$NotificationsController$hROO1aIM4eduzMv5uJ3U4yL97Bo implements Runnable {
    private final /* synthetic */ Uri f$0;

    public /* synthetic */ C0919-$$Lambda$NotificationsController$hROO1aIM4eduzMv5uJ3U4yL97Bo(Uri uri) {
        this.f$0 = uri;
    }

    public final void run() {
        ApplicationLoader.applicationContext.revokeUriPermission(this.f$0, 1);
    }
}
