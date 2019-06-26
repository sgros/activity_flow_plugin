package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw */
public final /* synthetic */ class C0961-$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw implements Runnable {
    private final /* synthetic */ ArrayList f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ MessageObject f$3;
    private final /* synthetic */ MessageObject f$4;

    public /* synthetic */ C0961-$$Lambda$SendMessagesHelper$4kP0iTjQJZDiTNhQsrN7GEn6QHw(ArrayList arrayList, long j, int i, MessageObject messageObject, MessageObject messageObject2) {
        this.f$0 = arrayList;
        this.f$1 = j;
        this.f$2 = i;
        this.f$3 = messageObject;
        this.f$4 = messageObject2;
    }

    public final void run() {
        SendMessagesHelper.lambda$prepareSendingAudioDocuments$46(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
