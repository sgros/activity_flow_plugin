package org.telegram.p004ui;

import org.telegram.p004ui.WallpaperActivity.WallpaperActivityDelegate;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$tPCre3L2K_38M9O_G5mv57D0Uc4 */
public final /* synthetic */ class C3902-$$Lambda$tPCre3L2K_38M9O_G5mv57D0Uc4 implements WallpaperActivityDelegate {
    private final /* synthetic */ WallpapersListActivity f$0;

    public /* synthetic */ C3902-$$Lambda$tPCre3L2K_38M9O_G5mv57D0Uc4(WallpapersListActivity wallpapersListActivity) {
        this.f$0 = wallpapersListActivity;
    }

    public final void didSetNewBackground() {
        this.f$0.removeSelfFromStack();
    }
}
