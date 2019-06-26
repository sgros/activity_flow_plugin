package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$jD-CpG8ofMpECWClk02OvBwC11M */
public final /* synthetic */ class C1365-$$Lambda$ChatActivity$jD-CpG8ofMpECWClk02OvBwC11M implements OnCancelListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1365-$$Lambda$ChatActivity$jD-CpG8ofMpECWClk02OvBwC11M(ChatActivity chatActivity, int i) {
        this.f$0 = chatActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$null$88$ChatActivity(this.f$1, dialogInterface);
    }
}
