package org.telegram.p004ui;

import org.telegram.p004ui.PhotoViewer.PlaceProviderObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$SQuc4TyZ0dWDiWuazMyrUv4byaU */
public final /* synthetic */ class C1876-$$Lambda$PhotoViewer$SQuc4TyZ0dWDiWuazMyrUv4byaU implements Runnable {
    private final /* synthetic */ PhotoViewer f$0;
    private final /* synthetic */ PlaceProviderObject f$1;

    public /* synthetic */ C1876-$$Lambda$PhotoViewer$SQuc4TyZ0dWDiWuazMyrUv4byaU(PhotoViewer photoViewer, PlaceProviderObject placeProviderObject) {
        this.f$0 = photoViewer;
        this.f$1 = placeProviderObject;
    }

    public final void run() {
        this.f$0.lambda$closePhoto$42$PhotoViewer(this.f$1);
    }
}
