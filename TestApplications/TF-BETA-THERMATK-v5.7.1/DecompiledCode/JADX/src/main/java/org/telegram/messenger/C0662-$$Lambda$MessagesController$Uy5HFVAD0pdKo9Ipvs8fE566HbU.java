package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.p004ui.ActionBar.BaseFragment;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU */
public final /* synthetic */ class C0662-$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU implements OnCancelListener {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ BaseFragment f$2;

    public /* synthetic */ C0662-$$Lambda$MessagesController$Uy5HFVAD0pdKo9Ipvs8fE566HbU(MessagesController messagesController, int i, BaseFragment baseFragment) {
        this.f$0 = messagesController;
        this.f$1 = i;
        this.f$2 = baseFragment;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$checkCanOpenChat$258$MessagesController(this.f$1, this.f$2, dialogInterface);
    }
}
