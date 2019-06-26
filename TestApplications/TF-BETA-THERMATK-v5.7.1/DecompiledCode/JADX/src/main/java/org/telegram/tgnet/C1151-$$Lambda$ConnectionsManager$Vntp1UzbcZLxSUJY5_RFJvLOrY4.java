package org.telegram.tgnet;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4 */
public final /* synthetic */ class C1151-$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4 implements Runnable {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1151-$$Lambda$ConnectionsManager$Vntp1UzbcZLxSUJY5_RFJvLOrY4(int i, int i2) {
        this.f$0 = i;
        this.f$1 = i2;
    }

    public final void run() {
        ConnectionsManager.lambda$onRequestNewServerIpAndPort$8(this.f$0, this.f$1);
    }
}
