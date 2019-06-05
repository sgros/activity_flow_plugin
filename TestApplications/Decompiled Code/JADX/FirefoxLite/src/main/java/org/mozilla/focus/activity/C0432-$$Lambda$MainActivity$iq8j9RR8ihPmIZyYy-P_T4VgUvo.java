package org.mozilla.focus.activity;

import org.mozilla.focus.utils.DialogUtils;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.activity.-$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy-P_T4VgUvo */
public final /* synthetic */ class C0432-$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy-P_T4VgUvo implements Runnable {
    private final /* synthetic */ MainActivity f$0;

    public /* synthetic */ C0432-$$Lambda$MainActivity$iq8j9RR8ihPmIZyYy-P_T4VgUvo(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    public final void run() {
        this.f$0.myshotOnBoardingDialog = DialogUtils.showMyShotOnBoarding(this.f$0, this.f$0.myshotButton, new C0433-$$Lambda$MainActivity$pTM933lwu8pbNr7Y8C84tdVKTTs(this.f$0), new C0428-$$Lambda$MainActivity$1S8GdtQw7QrO0XD-WCw0iUqwwXI(this.f$0));
    }
}
