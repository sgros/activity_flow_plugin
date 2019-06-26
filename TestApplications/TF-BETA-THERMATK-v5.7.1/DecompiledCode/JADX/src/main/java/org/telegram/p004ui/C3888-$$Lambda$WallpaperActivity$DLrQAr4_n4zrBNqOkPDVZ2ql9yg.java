package org.telegram.p004ui;

import org.telegram.messenger.ImageReceiver;
import org.telegram.messenger.ImageReceiver.ImageReceiverDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WallpaperActivity$DLrQAr4_n4zrBNqOkPDVZ2ql9yg */
public final /* synthetic */ class C3888-$$Lambda$WallpaperActivity$DLrQAr4_n4zrBNqOkPDVZ2ql9yg implements ImageReceiverDelegate {
    private final /* synthetic */ WallpaperActivity f$0;

    public /* synthetic */ C3888-$$Lambda$WallpaperActivity$DLrQAr4_n4zrBNqOkPDVZ2ql9yg(WallpaperActivity wallpaperActivity) {
        this.f$0 = wallpaperActivity;
    }

    public final void didSetImage(ImageReceiver imageReceiver, boolean z, boolean z2) {
        this.f$0.lambda$createView$0$WallpaperActivity(imageReceiver, z, z2);
    }
}
