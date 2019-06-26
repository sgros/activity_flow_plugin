package org.telegram.tgnet;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$wMpd1-zDWgiLp6x8fjImjIX349A */
public final /* synthetic */ class C1155-$$Lambda$ConnectionsManager$wMpd1-zDWgiLp6x8fjImjIX349A implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1155-$$Lambda$ConnectionsManager$wMpd1-zDWgiLp6x8fjImjIX349A(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void run() {
        ConnectionsManager.lambda$onConnectionStateChanged$6(this.f$0, this.f$1);
    }
}
