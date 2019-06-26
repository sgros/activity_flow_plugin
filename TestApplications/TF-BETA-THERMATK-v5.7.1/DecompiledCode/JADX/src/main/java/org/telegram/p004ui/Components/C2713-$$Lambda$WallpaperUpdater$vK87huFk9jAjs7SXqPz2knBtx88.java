package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$WallpaperUpdater$vK87huFk9jAjs7SXqPz2knBtx88 */
public final /* synthetic */ class C2713-$$Lambda$WallpaperUpdater$vK87huFk9jAjs7SXqPz2knBtx88 implements OnClickListener {
    private final /* synthetic */ WallpaperUpdater f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C2713-$$Lambda$WallpaperUpdater$vK87huFk9jAjs7SXqPz2knBtx88(WallpaperUpdater wallpaperUpdater, boolean z) {
        this.f$0 = wallpaperUpdater;
        this.f$1 = z;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showAlert$0$WallpaperUpdater(this.f$1, dialogInterface, i);
    }
}
