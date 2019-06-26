package org.telegram.messenger;

import org.telegram.tgnet.TLRPC.TL_emojiKeywordsDifference;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc */
public final /* synthetic */ class C0387-$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ TL_emojiKeywordsDifference f$1;
    private final /* synthetic */ String f$2;

    public /* synthetic */ C0387-$$Lambda$DataQuery$_y89sXvt9qXhyQF6eZF_c0a7Hdc(DataQuery dataQuery, TL_emojiKeywordsDifference tL_emojiKeywordsDifference, String str) {
        this.f$0 = dataQuery;
        this.f$1 = tL_emojiKeywordsDifference;
        this.f$2 = str;
    }

    public final void run() {
        this.f$0.lambda$putEmojiKeywords$118$DataQuery(this.f$1, this.f$2);
    }
}
