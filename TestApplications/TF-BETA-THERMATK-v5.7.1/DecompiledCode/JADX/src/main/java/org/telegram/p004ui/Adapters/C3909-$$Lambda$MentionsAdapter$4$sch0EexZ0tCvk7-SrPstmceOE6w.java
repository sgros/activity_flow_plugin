package org.telegram.p004ui.Adapters;

import org.telegram.messenger.MessagesController;
import org.telegram.messenger.MessagesStorage;
import org.telegram.p004ui.Adapters.MentionsAdapter.C22774;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7-SrPstmceOE6w */
public final /* synthetic */ class C3909-$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7-SrPstmceOE6w implements RequestDelegate {
    private final /* synthetic */ C22774 f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ MessagesController f$2;
    private final /* synthetic */ MessagesStorage f$3;

    public /* synthetic */ C3909-$$Lambda$MentionsAdapter$4$sch0EexZ0tCvk7-SrPstmceOE6w(C22774 c22774, String str, MessagesController messagesController, MessagesStorage messagesStorage) {
        this.f$0 = c22774;
        this.f$1 = str;
        this.f$2 = messagesController;
        this.f$3 = messagesStorage;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$1$MentionsAdapter$4(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
