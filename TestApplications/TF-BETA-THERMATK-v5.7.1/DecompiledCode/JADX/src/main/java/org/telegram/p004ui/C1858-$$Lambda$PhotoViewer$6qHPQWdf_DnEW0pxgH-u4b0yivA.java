package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH-u4b0yivA */
public final /* synthetic */ class C1858-$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH-u4b0yivA implements OnDismissListener {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C1858-$$Lambda$PhotoViewer$6qHPQWdf_DnEW0pxgH-u4b0yivA(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$showAlertDialog$33$PhotoViewer(dialogInterface);
    }
}
