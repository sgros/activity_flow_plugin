package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.InputUser;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4 */
public final /* synthetic */ class C3505-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4 implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ InputUser f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ BaseFragment f$4;
    private final /* synthetic */ TLObject f$5;
    private final /* synthetic */ boolean f$6;
    private final /* synthetic */ Runnable f$7;

    public /* synthetic */ C3505-$$Lambda$MessagesController$h5a2RdtziFUaSy5oo3g6ZX4SUu4(MessagesController messagesController, boolean z, InputUser inputUser, int i, BaseFragment baseFragment, TLObject tLObject, boolean z2, Runnable runnable) {
        this.f$0 = messagesController;
        this.f$1 = z;
        this.f$2 = inputUser;
        this.f$3 = i;
        this.f$4 = baseFragment;
        this.f$5 = tLObject;
        this.f$6 = z2;
        this.f$7 = runnable;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$addUserToChat$180$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, tLObject, tL_error);
    }
}