package org.telegram.p004ui;

import org.telegram.messenger.MessagesController;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ArticleViewer$Cq82CY1mMDVABDekDXqZnXYfB8o */
public final /* synthetic */ class C1177-$$Lambda$ArticleViewer$Cq82CY1mMDVABDekDXqZnXYfB8o implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C1177-$$Lambda$ArticleViewer$Cq82CY1mMDVABDekDXqZnXYfB8o(int i, Chat chat) {
        this.f$0 = i;
        this.f$1 = chat;
    }

    public final void run() {
        MessagesController.getInstance(this.f$0).loadFullChat(this.f$1.f434id, 0, true);
    }
}
