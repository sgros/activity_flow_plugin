package org.telegram.p004ui;

import org.telegram.p004ui.ActionBar.AlertDialog;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$w4cBn7eVI-jA-9l5l0qJf-RFppg */
public final /* synthetic */ class C1378-$$Lambda$ChatActivity$w4cBn7eVI-jA-9l5l0qJf-RFppg implements Runnable {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ AlertDialog[] f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1378-$$Lambda$ChatActivity$w4cBn7eVI-jA-9l5l0qJf-RFppg(ChatActivity chatActivity, AlertDialog[] alertDialogArr, int i) {
        this.f$0 = chatActivity;
        this.f$1 = alertDialogArr;
        this.f$2 = i;
    }

    public final void run() {
        this.f$0.lambda$processSelectedOption$75$ChatActivity(this.f$1, this.f$2);
    }
}
