package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$ydKQSIv3UJTKVK2ad12P2kFNfXM */
public final /* synthetic */ class C3548-$$Lambda$SendMessagesHelper$ydKQSIv3UJTKVK2ad12P2kFNfXM implements RequestDelegate {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ long f$1;

    public /* synthetic */ C3548-$$Lambda$SendMessagesHelper$ydKQSIv3UJTKVK2ad12P2kFNfXM(SendMessagesHelper sendMessagesHelper, long j) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = j;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendGame$18$SendMessagesHelper(this.f$1, tLObject, tL_error);
    }
}
