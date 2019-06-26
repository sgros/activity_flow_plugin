package org.telegram.messenger;

import android.graphics.Bitmap;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$Emoji$8PuKHwVD-cYGVxmUFCHOFHCQ5bM */
public final /* synthetic */ class C0429-$$Lambda$Emoji$8PuKHwVD-cYGVxmUFCHOFHCQ5bM implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Bitmap f$2;

    public /* synthetic */ C0429-$$Lambda$Emoji$8PuKHwVD-cYGVxmUFCHOFHCQ5bM(int i, int i2, Bitmap bitmap) {
        this.f$0 = i;
        this.f$1 = i2;
        this.f$2 = bitmap;
    }

    public final void run() {
        Emoji.lambda$loadEmoji$0(this.f$0, this.f$1, this.f$2);
    }
}
