package org.telegram.messenger;

import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.tgnet.TLRPC.TL_photo;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg */
public final /* synthetic */ class C0992-$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg implements Runnable {
    private final /* synthetic */ MessageObject f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ TL_photo f$2;
    private final /* synthetic */ boolean f$3;
    private final /* synthetic */ SendingMediaInfo f$4;
    private final /* synthetic */ HashMap f$5;
    private final /* synthetic */ String f$6;
    private final /* synthetic */ long f$7;
    private final /* synthetic */ MessageObject f$8;

    public /* synthetic */ C0992-$$Lambda$SendMessagesHelper$bbGr2Hpgnu2is3Sf6gpH4E4chEg(MessageObject messageObject, int i, TL_photo tL_photo, boolean z, SendingMediaInfo sendingMediaInfo, HashMap hashMap, String str, long j, MessageObject messageObject2) {
        this.f$0 = messageObject;
        this.f$1 = i;
        this.f$2 = tL_photo;
        this.f$3 = z;
        this.f$4 = sendingMediaInfo;
        this.f$5 = hashMap;
        this.f$6 = str;
        this.f$7 = j;
        this.f$8 = messageObject2;
    }

    public final void run() {
        SendMessagesHelper.lambda$null$59(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8);
    }
}
