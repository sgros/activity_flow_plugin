package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_channels_inviteToChannel;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$TfiNuUXCjyH-kDhosYCjp7Cg5Gk */
public final /* synthetic */ class C3480-$$Lambda$MessagesController$TfiNuUXCjyH-kDhosYCjp7Cg5Gk implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ BaseFragment f$1;
    private final /* synthetic */ TL_channels_inviteToChannel f$2;

    public /* synthetic */ C3480-$$Lambda$MessagesController$TfiNuUXCjyH-kDhosYCjp7Cg5Gk(MessagesController messagesController, BaseFragment baseFragment, TL_channels_inviteToChannel tL_channels_inviteToChannel) {
        this.f$0 = messagesController;
        this.f$1 = baseFragment;
        this.f$2 = tL_channels_inviteToChannel;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$addUsersToChannel$167$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}