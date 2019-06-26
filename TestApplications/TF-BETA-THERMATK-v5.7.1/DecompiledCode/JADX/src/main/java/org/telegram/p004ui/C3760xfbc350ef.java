package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.messenger.MessagesStorage.IntCallback;
import org.telegram.p004ui.NotificationsSettingsActivity.NotificationException;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$NotificationsCustomSettingsActivity$dRvEr5S0zYnz2y_9i0nz2iSedtM */
public final /* synthetic */ class C3760xfbc350ef implements IntCallback {
    private final /* synthetic */ NotificationsCustomSettingsActivity f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ NotificationException f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C3760xfbc350ef(NotificationsCustomSettingsActivity notificationsCustomSettingsActivity, ArrayList arrayList, NotificationException notificationException, int i) {
        this.f$0 = notificationsCustomSettingsActivity;
        this.f$1 = arrayList;
        this.f$2 = notificationException;
        this.f$3 = i;
    }

    public final void run(int i) {
        this.f$0.lambda$null$0$NotificationsCustomSettingsActivity(this.f$1, this.f$2, this.f$3, i);
    }
}
