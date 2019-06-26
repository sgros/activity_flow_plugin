package org.telegram.messenger;

import java.io.File;
import org.telegram.tgnet.TLRPC.Document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk */
public final /* synthetic */ class C0996-$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ DelayedMessage f$1;
    private final /* synthetic */ File f$2;
    private final /* synthetic */ Document f$3;
    private final /* synthetic */ MessageObject f$4;

    public /* synthetic */ C0996-$$Lambda$SendMessagesHelper$ivsY9c3O0F76RgXSqAIraHVU0Fk(SendMessagesHelper sendMessagesHelper, DelayedMessage delayedMessage, File file, Document document, MessageObject messageObject) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = delayedMessage;
        this.f$2 = file;
        this.f$3 = document;
        this.f$4 = messageObject;
    }

    public final void run() {
        this.f$0.lambda$null$3$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
