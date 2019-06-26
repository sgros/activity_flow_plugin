package org.telegram.messenger;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU */
public final /* synthetic */ class C0947-$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU implements Runnable {
    private final /* synthetic */ SecretChatHelper f$0;
    private final /* synthetic */ ArrayList f$1;

    public /* synthetic */ C0947-$$Lambda$SecretChatHelper$_doi6epvDK7bEAjlIQHt5tAd_wU(SecretChatHelper secretChatHelper, ArrayList arrayList) {
        this.f$0 = secretChatHelper;
        this.f$1 = arrayList;
    }

    public final void run() {
        this.f$0.lambda$processPendingEncMessages$0$SecretChatHelper(this.f$1);
    }
}
