package org.telegram.p004ui;

import org.telegram.messenger.MessagesStorage;
import org.telegram.p004ui.ChatActivity.C240815;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$15$TWyfKj-zNuWPP70-9PkaiO4T_9Q */
public final /* synthetic */ class C3616-$$Lambda$ChatActivity$15$TWyfKj-zNuWPP70-9PkaiO4T_9Q implements RequestDelegate {
    private final /* synthetic */ C240815 f$0;
    private final /* synthetic */ MessagesStorage f$1;

    public /* synthetic */ C3616-$$Lambda$ChatActivity$15$TWyfKj-zNuWPP70-9PkaiO4T_9Q(C240815 c240815, MessagesStorage messagesStorage) {
        this.f$0 = c240815;
        this.f$1 = messagesStorage;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadLastUnreadMention$2$ChatActivity$15(this.f$1, tLObject, tL_error);
    }
}
