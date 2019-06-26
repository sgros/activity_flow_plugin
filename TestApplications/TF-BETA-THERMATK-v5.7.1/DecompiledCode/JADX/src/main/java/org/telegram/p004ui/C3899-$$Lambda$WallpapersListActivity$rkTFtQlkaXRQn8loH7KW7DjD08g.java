package org.telegram.p004ui;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g */
public final /* synthetic */ class C3899-$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g implements RequestDelegate {
    private final /* synthetic */ WallpapersListActivity f$0;

    public /* synthetic */ C3899-$$Lambda$WallpapersListActivity$rkTFtQlkaXRQn8loH7KW7DjD08g(WallpapersListActivity wallpapersListActivity) {
        this.f$0 = wallpapersListActivity;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadWallpapers$5$WallpapersListActivity(tLObject, tL_error);
    }
}
