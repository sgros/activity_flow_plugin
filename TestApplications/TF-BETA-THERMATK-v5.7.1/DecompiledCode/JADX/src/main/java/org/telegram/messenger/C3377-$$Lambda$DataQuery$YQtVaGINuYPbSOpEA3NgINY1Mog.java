package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_search;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog */
public final /* synthetic */ class C3377-$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog implements RequestDelegate {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TL_messages_search f$2;
    private final /* synthetic */ long f$3;
    private final /* synthetic */ long f$4;
    private final /* synthetic */ int f$5;
    private final /* synthetic */ long f$6;
    private final /* synthetic */ User f$7;

    public /* synthetic */ C3377-$$Lambda$DataQuery$YQtVaGINuYPbSOpEA3NgINY1Mog(DataQuery dataQuery, int i, TL_messages_search tL_messages_search, long j, long j2, int i2, long j3, User user) {
        this.f$0 = dataQuery;
        this.f$1 = i;
        this.f$2 = tL_messages_search;
        this.f$3 = j;
        this.f$4 = j2;
        this.f$5 = i2;
        this.f$6 = j3;
        this.f$7 = user;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchMessagesInChat$50$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, tLObject, tL_error);
    }
}