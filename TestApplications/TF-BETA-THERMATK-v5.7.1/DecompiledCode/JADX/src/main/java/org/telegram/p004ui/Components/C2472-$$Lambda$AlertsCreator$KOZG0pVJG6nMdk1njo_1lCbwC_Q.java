package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$KOZG0pVJG6nMdk1njo_1lCbwC_Q */
public final /* synthetic */ class C2472-$$Lambda$AlertsCreator$KOZG0pVJG6nMdk1njo_1lCbwC_Q implements OnClickListener {
    private final /* synthetic */ BaseFragment f$0;

    public /* synthetic */ C2472-$$Lambda$AlertsCreator$KOZG0pVJG6nMdk1njo_1lCbwC_Q(BaseFragment baseFragment) {
        this.f$0 = baseFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        AlertsCreator.performAskAQuestion(this.f$0);
    }
}
