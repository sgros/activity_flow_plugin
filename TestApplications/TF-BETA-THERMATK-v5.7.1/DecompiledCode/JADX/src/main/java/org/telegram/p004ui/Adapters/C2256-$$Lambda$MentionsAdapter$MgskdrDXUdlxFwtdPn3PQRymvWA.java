package org.telegram.p004ui.Adapters;

import android.content.DialogInterface;
import android.content.DialogInterface.OnDismissListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA */
public final /* synthetic */ class C2256-$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA implements OnDismissListener {
    private final /* synthetic */ MentionsAdapter f$0;
    private final /* synthetic */ boolean[] f$1;

    public /* synthetic */ C2256-$$Lambda$MentionsAdapter$MgskdrDXUdlxFwtdPn3PQRymvWA(MentionsAdapter mentionsAdapter, boolean[] zArr) {
        this.f$0 = mentionsAdapter;
        this.f$1 = zArr;
    }

    public final void onDismiss(DialogInterface dialogInterface) {
        this.f$0.lambda$processFoundUser$2$MentionsAdapter(this.f$1, dialogInterface);
    }
}
