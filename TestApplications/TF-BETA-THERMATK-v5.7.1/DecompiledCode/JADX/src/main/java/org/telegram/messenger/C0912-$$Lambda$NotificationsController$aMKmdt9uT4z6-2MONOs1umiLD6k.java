package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$NotificationsController$aMKmdt9uT4z6-2MONOs1umiLD6k */
public final /* synthetic */ class C0912-$$Lambda$NotificationsController$aMKmdt9uT4z6-2MONOs1umiLD6k implements Runnable {
    private final /* synthetic */ NotificationsController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0912-$$Lambda$NotificationsController$aMKmdt9uT4z6-2MONOs1umiLD6k(NotificationsController notificationsController, int i) {
        this.f$0 = notificationsController;
        this.f$1 = i;
    }

    public final void run() {
        this.f$0.lambda$setLastOnlineFromOtherDevice$3$NotificationsController(this.f$1);
    }
}
