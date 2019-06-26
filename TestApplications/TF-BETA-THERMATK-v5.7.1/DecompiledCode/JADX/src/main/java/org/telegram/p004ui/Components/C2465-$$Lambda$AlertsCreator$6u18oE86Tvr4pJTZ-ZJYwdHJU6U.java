package org.telegram.p004ui.Components;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessagesController;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$6u18oE86Tvr4pJTZ-ZJYwdHJU6U */
public final /* synthetic */ class C2465-$$Lambda$AlertsCreator$6u18oE86Tvr4pJTZ-ZJYwdHJU6U implements OnClickListener {
    private final /* synthetic */ BaseFragment f$0;

    public /* synthetic */ C2465-$$Lambda$AlertsCreator$6u18oE86Tvr4pJTZ-ZJYwdHJU6U(BaseFragment baseFragment) {
        this.f$0 = baseFragment;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        MessagesController.getInstance(this.f$0.getCurrentAccount()).openByUserName("spambot", this.f$0, 1);
    }
}
