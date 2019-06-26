package org.telegram.p004ui.Components;

import android.view.ViewTreeObserver.OnPreDrawListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY */
public final /* synthetic */ class C2722-$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY implements Runnable {
    private final /* synthetic */ OnPreDrawListener f$0;

    public /* synthetic */ C2722-$$Lambda$qzh_QoBZ7K2XdUWK2VAJcGTe1OY(OnPreDrawListener onPreDrawListener) {
        this.f$0 = onPreDrawListener;
    }

    public final void run() {
        this.f$0.onPreDraw();
    }
}
