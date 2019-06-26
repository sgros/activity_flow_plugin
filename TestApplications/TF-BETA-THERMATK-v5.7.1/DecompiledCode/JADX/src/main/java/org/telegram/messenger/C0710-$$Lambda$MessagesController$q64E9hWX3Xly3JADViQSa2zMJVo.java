package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.messages_Dialogs;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo */
public final /* synthetic */ class C0710-$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo implements Runnable {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ messages_Dialogs f$1;

    public /* synthetic */ C0710-$$Lambda$MessagesController$q64E9hWX3Xly3JADViQSa2zMJVo(MessagesController messagesController, messages_Dialogs messages_dialogs) {
        this.f$0 = messagesController;
        this.f$1 = messages_dialogs;
    }

    public final void run() {
        this.f$0.lambda$processDialogsUpdate$138$MessagesController(this.f$1);
    }
}
