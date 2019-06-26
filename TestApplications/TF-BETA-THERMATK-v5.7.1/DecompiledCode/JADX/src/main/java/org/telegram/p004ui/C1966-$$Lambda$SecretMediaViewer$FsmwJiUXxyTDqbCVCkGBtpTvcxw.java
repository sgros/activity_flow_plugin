package org.telegram.p004ui;

import org.telegram.p004ui.PhotoViewer.PlaceProviderObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SecretMediaViewer$FsmwJiUXxyTDqbCVCkGBtpTvcxw */
public final /* synthetic */ class C1966-$$Lambda$SecretMediaViewer$FsmwJiUXxyTDqbCVCkGBtpTvcxw implements Runnable {
    private final /* synthetic */ SecretMediaViewer f$0;
    private final /* synthetic */ PlaceProviderObject f$1;

    public /* synthetic */ C1966-$$Lambda$SecretMediaViewer$FsmwJiUXxyTDqbCVCkGBtpTvcxw(SecretMediaViewer secretMediaViewer, PlaceProviderObject placeProviderObject) {
        this.f$0 = secretMediaViewer;
        this.f$1 = placeProviderObject;
    }

    public final void run() {
        this.f$0.lambda$closePhoto$3$SecretMediaViewer(this.f$1);
    }
}
