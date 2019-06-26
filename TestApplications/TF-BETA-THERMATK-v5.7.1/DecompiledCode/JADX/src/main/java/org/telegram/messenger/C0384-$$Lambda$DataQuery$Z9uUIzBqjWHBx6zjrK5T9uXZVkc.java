package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.BotInfo;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc */
public final /* synthetic */ class C0384-$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ BotInfo f$1;

    public /* synthetic */ C0384-$$Lambda$DataQuery$Z9uUIzBqjWHBx6zjrK5T9uXZVkc(DataQuery dataQuery, BotInfo botInfo) {
        this.f$0 = dataQuery;
        this.f$1 = botInfo;
    }

    public final void run() {
        this.f$0.lambda$putBotInfo$110$DataQuery(this.f$1);
    }
}
