package org.telegram.messenger;

import android.graphics.drawable.Drawable;
import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE */
public final /* synthetic */ class C0482-$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE implements Runnable {
    private final /* synthetic */ CacheImage f$0;
    private final /* synthetic */ Drawable f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ String f$3;

    public /* synthetic */ C0482-$$Lambda$ImageLoader$CacheImage$DfnPkD34YpkvdtADQzUhGGpSRuE(CacheImage cacheImage, Drawable drawable, ArrayList arrayList, String str) {
        this.f$0 = cacheImage;
        this.f$1 = drawable;
        this.f$2 = arrayList;
        this.f$3 = str;
    }

    public final void run() {
        this.f$0.lambda$setImageAndClear$0$ImageLoader$CacheImage(this.f$1, this.f$2, this.f$3);
    }
}
