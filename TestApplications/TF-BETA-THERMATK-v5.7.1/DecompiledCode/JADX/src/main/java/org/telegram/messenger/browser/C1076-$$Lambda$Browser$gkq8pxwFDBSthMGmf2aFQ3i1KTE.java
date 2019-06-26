package org.telegram.messenger.browser;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.messenger.UserConfig;
import org.telegram.tgnet.ConnectionsManager;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.browser.-$$Lambda$Browser$gkq8pxwFDBSthMGmf2aFQ3i1KTE */
public final /* synthetic */ class C1076-$$Lambda$Browser$gkq8pxwFDBSthMGmf2aFQ3i1KTE implements OnCancelListener {
    private final /* synthetic */ int f$0;

    public /* synthetic */ C1076-$$Lambda$Browser$gkq8pxwFDBSthMGmf2aFQ3i1KTE(int i) {
        this.f$0 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        ConnectionsManager.getInstance(UserConfig.selectedAccount).cancelRequest(this.f$0, true);
    }
}
