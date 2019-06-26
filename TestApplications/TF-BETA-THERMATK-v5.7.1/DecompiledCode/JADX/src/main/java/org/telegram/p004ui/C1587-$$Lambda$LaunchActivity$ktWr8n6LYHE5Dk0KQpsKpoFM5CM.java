package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.tgnet.ConnectionsManager;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM */
public final /* synthetic */ class C1587-$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM implements OnCancelListener {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int[] f$1;

    public /* synthetic */ C1587-$$Lambda$LaunchActivity$ktWr8n6LYHE5Dk0KQpsKpoFM5CM(int i, int[] iArr) {
        this.f$0 = i;
        this.f$1 = iArr;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(this.f$0).cancelRequest(this.f$1[0], true);
    }
}
