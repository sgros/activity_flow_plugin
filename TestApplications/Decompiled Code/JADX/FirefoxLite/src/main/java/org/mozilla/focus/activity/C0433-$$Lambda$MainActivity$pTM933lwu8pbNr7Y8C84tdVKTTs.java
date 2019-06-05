package org.mozilla.focus.activity;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.activity.-$$Lambda$MainActivity$pTM933lwu8pbNr7Y8C84tdVKTTs */
public final /* synthetic */ class C0433-$$Lambda$MainActivity$pTM933lwu8pbNr7Y8C84tdVKTTs implements OnCancelListener {
    private final /* synthetic */ MainActivity f$0;

    public /* synthetic */ C0433-$$Lambda$MainActivity$pTM933lwu8pbNr7Y8C84tdVKTTs(MainActivity mainActivity) {
        this.f$0 = mainActivity;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.dismissAllMenus();
    }
}
