package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MediaController$M715dCmB5sndyTLyXH8F6AQFBc4 */
public final /* synthetic */ class C0557-$$Lambda$MediaController$M715dCmB5sndyTLyXH8F6AQFBc4 implements Runnable {
    private final /* synthetic */ MediaController f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C0557-$$Lambda$MediaController$M715dCmB5sndyTLyXH8F6AQFBc4(MediaController mediaController, ArrayList arrayList) {
        this.f$0 = mediaController;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$processMediaObserver$4$MediaController(this.f$1);
    }
}
