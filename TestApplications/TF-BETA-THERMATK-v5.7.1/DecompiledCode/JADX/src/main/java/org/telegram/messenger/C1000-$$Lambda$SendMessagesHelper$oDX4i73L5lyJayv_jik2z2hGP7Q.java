package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLRPC.Message;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q */
public final /* synthetic */ class C1000-$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q implements Runnable {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ Message f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ boolean f$3;
    private final /* synthetic */ ArrayList f$4;
    private final /* synthetic */ int f$5;
    private final /* synthetic */ String f$6;

    public /* synthetic */ C1000-$$Lambda$SendMessagesHelper$oDX4i73L5lyJayv_jik2z2hGP7Q(SendMessagesHelper sendMessagesHelper, Message message, int i, boolean z, ArrayList arrayList, int i2, String str) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = message;
        this.f$2 = i;
        this.f$3 = z;
        this.f$4 = arrayList;
        this.f$5 = i2;
        this.f$6 = str;
    }

    public final void run() {
        this.f$0.lambda$null$38$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6);
    }
}
