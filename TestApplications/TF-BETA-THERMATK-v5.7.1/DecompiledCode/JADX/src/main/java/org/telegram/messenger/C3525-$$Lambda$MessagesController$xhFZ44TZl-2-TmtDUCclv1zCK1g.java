package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.FileLocation;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$xhFZ44TZl-2-TmtDUCclv1zCK1g */
public final /* synthetic */ class C3525-$$Lambda$MessagesController$xhFZ44TZl-2-TmtDUCclv1zCK1g implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ FileLocation f$1;
    private final /* synthetic */ FileLocation f$2;

    public /* synthetic */ C3525-$$Lambda$MessagesController$xhFZ44TZl-2-TmtDUCclv1zCK1g(MessagesController messagesController, FileLocation fileLocation, FileLocation fileLocation2) {
        this.f$0 = messagesController;
        this.f$1 = fileLocation;
        this.f$2 = fileLocation2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$changeChatAvatar$184$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
