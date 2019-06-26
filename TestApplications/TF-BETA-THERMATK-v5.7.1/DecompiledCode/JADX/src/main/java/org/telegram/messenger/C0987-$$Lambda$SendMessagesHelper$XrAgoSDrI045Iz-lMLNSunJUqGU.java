package org.telegram.messenger;

import android.location.Location;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$XrAgoSDrI045Iz-lMLNSunJUqGU */
public final /* synthetic */ class C0987-$$Lambda$SendMessagesHelper$XrAgoSDrI045Iz-lMLNSunJUqGU implements Runnable {
    private final /* synthetic */ Location f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0987-$$Lambda$SendMessagesHelper$XrAgoSDrI045Iz-lMLNSunJUqGU(Location location, int i, long j) {
        this.f$0 = location;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        SendMessagesHelper.lambda$null$49(this.f$0, this.f$1, this.f$2);
    }
}
