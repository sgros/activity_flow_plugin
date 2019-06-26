package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1-tmM */
public final /* synthetic */ class C3490-$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1-tmM implements RequestDelegate {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ long f$3;

    public /* synthetic */ C3490-$$Lambda$MessagesController$YmKMKppARt1F8875D8216H1-tmM(MessagesController messagesController, long j, boolean z, long j2) {
        this.f$0 = messagesController;
        this.f$1 = j;
        this.f$2 = z;
        this.f$3 = j2;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$saveWallpaperToServer$60$MessagesController(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
