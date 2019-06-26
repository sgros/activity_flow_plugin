package org.telegram.p004ui;

import org.telegram.p004ui.PhotoViewer.PlaceProviderObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SecretMediaViewer$SXGR0dGO1jG_N03wXFFoPW86sNo */
public final /* synthetic */ class C1969-$$Lambda$SecretMediaViewer$SXGR0dGO1jG_N03wXFFoPW86sNo implements Runnable {
    private final /* synthetic */ SecretMediaViewer f$0;
    private final /* synthetic */ PlaceProviderObject f$1;

    public /* synthetic */ C1969-$$Lambda$SecretMediaViewer$SXGR0dGO1jG_N03wXFFoPW86sNo(SecretMediaViewer secretMediaViewer, PlaceProviderObject placeProviderObject) {
        this.f$0 = secretMediaViewer;
        this.f$1 = placeProviderObject;
    }

    public final void run() {
        this.f$0.lambda$closePhoto$4$SecretMediaViewer(this.f$1);
    }
}
