package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.tgnet.TLRPC.BotInlineResult;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$LmDH-h6B9Uggp2w_KTpB6c_fhcQ */
public final /* synthetic */ class C0973-$$Lambda$SendMessagesHelper$LmDH-h6B9Uggp2w_KTpB6c_fhcQ implements Runnable {
    private final /* synthetic */ long f$0;
    private final /* synthetic */ BotInlineResult f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ HashMap f$3;
    private final /* synthetic */ MessageObject f$4;

    public /* synthetic */ C0973-$$Lambda$SendMessagesHelper$LmDH-h6B9Uggp2w_KTpB6c_fhcQ(long j, BotInlineResult botInlineResult, int i, HashMap hashMap, MessageObject messageObject) {
        this.f$0 = j;
        this.f$1 = botInlineResult;
        this.f$2 = i;
        this.f$3 = hashMap;
        this.f$4 = messageObject;
    }

    public final void run() {
        SendMessagesHelper.lambda$prepareSendingBotContextResult$53(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
