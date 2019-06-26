package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$Uw1vUJCasvns-8emcerckL5C6rs */
public final /* synthetic */ class C1342-$$Lambda$ChatActivity$Uw1vUJCasvns-8emcerckL5C6rs implements OnCancelListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1342-$$Lambda$ChatActivity$Uw1vUJCasvns-8emcerckL5C6rs(ChatActivity chatActivity, int i) {
        this.f$0 = chatActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$null$74$ChatActivity(this.f$1, dialogInterface);
    }
}
