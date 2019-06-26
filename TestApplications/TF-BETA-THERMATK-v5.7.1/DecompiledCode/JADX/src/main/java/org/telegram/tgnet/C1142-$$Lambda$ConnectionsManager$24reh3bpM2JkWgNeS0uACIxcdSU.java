package org.telegram.tgnet;

import org.telegram.messenger.NotificationCenter;

/* compiled from: lambda */
/* renamed from: org.telegram.tgnet.-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU */
public final /* synthetic */ class C1142-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU implements Runnable {
    public static final /* synthetic */ C1142-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU INSTANCE = new C1142-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU();

    private /* synthetic */ C1142-$$Lambda$ConnectionsManager$24reh3bpM2JkWgNeS0uACIxcdSU() {
    }

    public final void run() {
        NotificationCenter.getGlobalInstance().postNotificationName(NotificationCenter.needShowAlert, Integer.valueOf(3));
    }
}
