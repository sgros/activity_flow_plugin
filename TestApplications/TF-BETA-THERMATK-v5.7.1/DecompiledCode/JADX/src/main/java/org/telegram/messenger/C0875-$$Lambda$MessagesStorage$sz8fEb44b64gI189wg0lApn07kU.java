package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.Document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU */
public final /* synthetic */ class C0875-$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU implements Runnable {
    private final /* synthetic */ MessagesStorage f$0;
    private final /* synthetic */ Document f$1;
    private final /* synthetic */ String f$2;
    private final /* synthetic */ String f$3;

    public /* synthetic */ C0875-$$Lambda$MessagesStorage$sz8fEb44b64gI189wg0lApn07kU(MessagesStorage messagesStorage, Document document, String str, String str2) {
        this.f$0 = messagesStorage;
        this.f$1 = document;
        this.f$2 = str;
        this.f$3 = str2;
    }

    public final void run() {
        this.f$0.lambda$addRecentLocalFile$35$MessagesStorage(this.f$1, this.f$2, this.f$3);
    }
}
