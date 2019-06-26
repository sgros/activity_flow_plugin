package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_saveRecentSticker;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$Ac6iMOZ3v5XiuSoBQXhRvLk2mds */
public final /* synthetic */ class C3459-$$Lambda$MessagesController$Ac6iMOZ3v5XiuSoBQXhRvLk2mds implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ Object f$1;
    private final /* synthetic */ TL_messages_saveRecentSticker f$2;

    public /* synthetic */ C3459-$$Lambda$MessagesController$Ac6iMOZ3v5XiuSoBQXhRvLk2mds(MessagesController messagesController, Object obj, TL_messages_saveRecentSticker tL_messages_saveRecentSticker) {
        this.f$0 = messagesController;
        this.f$1 = obj;
        this.f$2 = tL_messages_saveRecentSticker;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$saveRecentSticker$72$MessagesController(this.f$1, this.f$2, tLObject, tL_error);
    }
}
