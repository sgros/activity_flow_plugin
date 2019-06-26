package org.telegram.p004ui;

import org.telegram.p004ui.ArticleViewer.PlaceProviderObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$bddzRYkreqx-hk8WrDDgX9mS_Fw */
public final /* synthetic */ class C1192-$$Lambda$ArticleViewer$bddzRYkreqx-hk8WrDDgX9mS_Fw implements Runnable {
    private final /* synthetic */ ArticleViewer f$0;
    private final /* synthetic */ PlaceProviderObject f$1;

    public /* synthetic */ C1192-$$Lambda$ArticleViewer$bddzRYkreqx-hk8WrDDgX9mS_Fw(ArticleViewer articleViewer, PlaceProviderObject placeProviderObject) {
        this.f$0 = articleViewer;
        this.f$1 = placeProviderObject;
    }

    public final void run() {
        this.f$0.lambda$closePhoto$40$ArticleViewer(this.f$1);
    }
}
