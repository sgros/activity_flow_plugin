package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ */
public final /* synthetic */ class C3707-$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ implements DialogsActivityDelegate {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ User f$2;
    private final /* synthetic */ String f$3;

    public /* synthetic */ C3707-$$Lambda$LaunchActivity$4SPCx7X84qe3LzSdcC5uytKxGYQ(LaunchActivity launchActivity, int i, User user, String str) {
        this.f$0 = launchActivity;
        this.f$1 = i;
        this.f$2 = user;
        this.f$3 = str;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$null$10$LaunchActivity(this.f$1, this.f$2, this.f$3, dialogsActivity, arrayList, charSequence, z);
    }
}
