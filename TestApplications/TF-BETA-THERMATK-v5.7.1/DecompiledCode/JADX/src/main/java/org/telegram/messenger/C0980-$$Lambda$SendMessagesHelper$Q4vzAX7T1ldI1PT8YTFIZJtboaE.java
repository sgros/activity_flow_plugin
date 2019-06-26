package org.telegram.messenger;

import android.location.Location;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE */
public final /* synthetic */ class C0980-$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE implements Runnable {
    private final /* synthetic */ Location f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ long f$2;

    public /* synthetic */ C0980-$$Lambda$SendMessagesHelper$Q4vzAX7T1ldI1PT8YTFIZJtboaE(Location location, int i, long j) {
        this.f$0 = location;
        this.f$1 = i;
        this.f$2 = j;
    }

    public final void run() {
        Utilities.stageQueue.postRunnable(new C0967-$$Lambda$SendMessagesHelper$CCL59p4Z-8FFpdg0kalcFKLSOEA(this.f$0, this.f$1, this.f$2));
    }
}
