package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW-r4fPJ0A */
public final /* synthetic */ class C0616-$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW-r4fPJ0A implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ TL_error f$1;
    private final /* synthetic */ BaseFragment f$2;
    private final /* synthetic */ TLObject f$3;
    private final /* synthetic */ boolean f$4;
    private final /* synthetic */ boolean f$5;
    private final /* synthetic */ InputUser f$6;

    public /* synthetic */ C0616-$$Lambda$MessagesController$FzKpDwgMKXFwcQpN6PW-r4fPJ0A(MessagesController messagesController, TL_error tL_error, BaseFragment baseFragment, TLObject tLObject, boolean z, boolean z2, InputUser inputUser) {
        this.f$0 = messagesController;
        this.f$1 = tL_error;
        this.f$2 = baseFragment;
        this.f$3 = tLObject;
        this.f$4 = z;
        this.f$5 = z2;
        this.f$6 = inputUser;
    }

    public final void run() {
        this.f$0.lambda$null$178$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}