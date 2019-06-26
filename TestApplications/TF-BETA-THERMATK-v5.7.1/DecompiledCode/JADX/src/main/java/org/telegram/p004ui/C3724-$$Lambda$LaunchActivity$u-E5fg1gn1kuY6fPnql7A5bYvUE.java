package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.tgnet.TLRPC.TL_contacts_resolvedPeer;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$u-E5fg1gn1kuY6fPnql7A5bYvUE */
public final /* synthetic */ class C3724-$$Lambda$LaunchActivity$u-E5fg1gn1kuY6fPnql7A5bYvUE implements DialogsActivityDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ TL_contacts_resolvedPeer f$3;

    public /* synthetic */ C3724-$$Lambda$LaunchActivity$u-E5fg1gn1kuY6fPnql7A5bYvUE(LaunchActivity launchActivity, String str, int i, TL_contacts_resolvedPeer tL_contacts_resolvedPeer) {
        this.f$0 = launchActivity;
        this.f$1 = str;
        this.f$2 = i;
        this.f$3 = tL_contacts_resolvedPeer;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$null$9$LaunchActivity(this.f$1, this.f$2, this.f$3, dialogsActivity, arrayList, charSequence, z);
    }
}
