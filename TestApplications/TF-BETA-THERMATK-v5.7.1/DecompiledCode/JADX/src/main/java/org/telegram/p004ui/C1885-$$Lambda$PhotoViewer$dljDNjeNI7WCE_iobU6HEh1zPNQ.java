package org.telegram.p004ui;

import android.view.View;
import android.view.View.OnApplyWindowInsetsListener;
import android.view.WindowInsets;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$dljDNjeNI7WCE_iobU6HEh1zPNQ */
public final /* synthetic */ class C1885-$$Lambda$PhotoViewer$dljDNjeNI7WCE_iobU6HEh1zPNQ implements OnApplyWindowInsetsListener {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C1885-$$Lambda$PhotoViewer$dljDNjeNI7WCE_iobU6HEh1zPNQ(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final WindowInsets onApplyWindowInsets(View view, WindowInsets windowInsets) {
        return this.f$0.lambda$setParentActivity$1$PhotoViewer(view, windowInsets);
    }
}
