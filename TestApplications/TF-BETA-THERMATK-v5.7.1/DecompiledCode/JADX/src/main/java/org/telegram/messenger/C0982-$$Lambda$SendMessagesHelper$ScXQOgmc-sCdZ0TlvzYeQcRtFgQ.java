package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC.TL_document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$ScXQOgmc-sCdZ0TlvzYeQcRtFgQ */
public final /* synthetic */ class C0982-$$Lambda$SendMessagesHelper$ScXQOgmc-sCdZ0TlvzYeQcRtFgQ implements Runnable {
    private final /* synthetic */ MessageObject f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TL_document f$2;
    private final /* synthetic */ MessageObject f$3;
    private final /* synthetic */ HashMap f$4;
    private final /* synthetic */ String f$5;
    private final /* synthetic */ long f$6;
    private final /* synthetic */ MessageObject f$7;

    public /* synthetic */ C0982-$$Lambda$SendMessagesHelper$ScXQOgmc-sCdZ0TlvzYeQcRtFgQ(MessageObject messageObject, int i, TL_document tL_document, MessageObject messageObject2, HashMap hashMap, String str, long j, MessageObject messageObject3) {
        this.f$0 = messageObject;
        this.f$1 = i;
        this.f$2 = tL_document;
        this.f$3 = messageObject2;
        this.f$4 = hashMap;
        this.f$5 = str;
        this.f$6 = j;
        this.f$7 = messageObject3;
    }

    public final void run() {
        SendMessagesHelper.lambda$null$45(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}
