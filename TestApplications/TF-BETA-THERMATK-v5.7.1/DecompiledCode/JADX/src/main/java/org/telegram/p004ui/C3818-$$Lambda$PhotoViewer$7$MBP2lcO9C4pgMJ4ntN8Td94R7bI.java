package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.p004ui.PhotoViewer.C42907;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$7$MBP2lcO9C4pgMJ4ntN8Td94R7bI */
public final /* synthetic */ class C3818-$$Lambda$PhotoViewer$7$MBP2lcO9C4pgMJ4ntN8Td94R7bI implements DialogsActivityDelegate {
    private final /* synthetic */ C42907 f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C3818-$$Lambda$PhotoViewer$7$MBP2lcO9C4pgMJ4ntN8Td94R7bI(C42907 c42907, ArrayList arrayList) {
        this.f$0 = c42907;
        this.f$1 = arrayList;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$onItemClick$0$PhotoViewer$7(this.f$1, dialogsActivity, arrayList, charSequence, z);
    }
}
