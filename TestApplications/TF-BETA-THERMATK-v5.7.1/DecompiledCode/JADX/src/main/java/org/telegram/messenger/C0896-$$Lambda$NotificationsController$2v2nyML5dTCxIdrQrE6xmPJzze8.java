package org.telegram.messenger;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8 */
public final /* synthetic */ class C0896-$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8 implements Runnable {
    public static final /* synthetic */ C0896-$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8 INSTANCE = new C0896-$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8();

    private /* synthetic */ C0896-$$Lambda$NotificationsController$2v2nyML5dTCxIdrQrE6xmPJzze8() {
    }

    public final void run() {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.pushMessagesUpdated, new Object[0]);
    }
}
