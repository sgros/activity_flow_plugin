package org.telegram.messenger;

import android.graphics.Bitmap;
import java.util.HashMap;
import org.telegram.messenger.SendMessagesHelper.SendingMediaInfo;
import org.telegram.tgnet.TLRPC.TL_document;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$Os_hFoakwia_R62-A2sPxAENsvo */
public final /* synthetic */ class C0979-$$Lambda$SendMessagesHelper$Os_hFoakwia_R62-A2sPxAENsvo implements Runnable {
    private final /* synthetic */ Bitmap f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ MessageObject f$10;
    private final /* synthetic */ SendingMediaInfo f$11;
    private final /* synthetic */ MessageObject f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ VideoEditedInfo f$4;
    private final /* synthetic */ TL_document f$5;
    private final /* synthetic */ String f$6;
    private final /* synthetic */ HashMap f$7;
    private final /* synthetic */ String f$8;
    private final /* synthetic */ long f$9;

    public /* synthetic */ C0979-$$Lambda$SendMessagesHelper$Os_hFoakwia_R62-A2sPxAENsvo(Bitmap bitmap, String str, MessageObject messageObject, int i, VideoEditedInfo videoEditedInfo, TL_document tL_document, String str2, HashMap hashMap, String str3, long j, MessageObject messageObject2, SendingMediaInfo sendingMediaInfo) {
        this.f$0 = bitmap;
        this.f$1 = str;
        this.f$2 = messageObject;
        this.f$3 = i;
        this.f$4 = videoEditedInfo;
        this.f$5 = tL_document;
        this.f$6 = str2;
        this.f$7 = hashMap;
        this.f$8 = str3;
        this.f$9 = j;
        this.f$10 = messageObject2;
        this.f$11 = sendingMediaInfo;
    }

    public final void run() {
        SendMessagesHelper.lambda$null$60(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11);
    }
}
