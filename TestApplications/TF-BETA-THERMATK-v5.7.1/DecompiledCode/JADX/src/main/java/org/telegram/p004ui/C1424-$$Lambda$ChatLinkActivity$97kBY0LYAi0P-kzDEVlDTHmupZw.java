package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$97kBY0LYAi0P-kzDEVlDTHmupZw */
public final /* synthetic */ class C1424-$$Lambda$ChatLinkActivity$97kBY0LYAi0P-kzDEVlDTHmupZw implements Runnable {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1424-$$Lambda$ChatLinkActivity$97kBY0LYAi0P-kzDEVlDTHmupZw(ChatLinkActivity chatLinkActivity, AlertDialog[] alertDialogArr, int i) {
        this.f$0 = chatLinkActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$linkChat$13$ChatLinkActivity(this.f$1, this.f$2);
    }
}