package org.telegram.tgnet;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$-MqDe5Su-gCe6Qmn9_j7mUamUVo */
public final /* synthetic */ class C1141-$$Lambda$ConnectionsManager$-MqDe5Su-gCe6Qmn9_j7mUamUVo implements Runnable {
    private final /* synthetic */ ConnectionsManager f$0;
    private final /* synthetic */ boolean f$1;

    public /* synthetic */ C1141-$$Lambda$ConnectionsManager$-MqDe5Su-gCe6Qmn9_j7mUamUVo(ConnectionsManager connectionsManager, boolean z) {
        this.f$0 = connectionsManager;
        this.f$1 = z;
    }

    public final void run() {
        this.f$0.lambda$setIsUpdating$11$ConnectionsManager(this.f$1);
    }
}