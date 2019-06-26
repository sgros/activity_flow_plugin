package org.telegram.p004ui;

import android.content.Context;
import org.telegram.p004ui.ActionBar.Theme;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DialogsActivity$Wz8eSYJLAR-xfNaDl4aQWUwW9N0 */
public final /* synthetic */ class C1503-$$Lambda$DialogsActivity$Wz8eSYJLAR-xfNaDl4aQWUwW9N0 implements Runnable {
    private final /* synthetic */ Context f$0;

    public /* synthetic */ C1503-$$Lambda$DialogsActivity$Wz8eSYJLAR-xfNaDl4aQWUwW9N0(Context context) {
        this.f$0 = context;
    }

    public final void run() {
        Theme.createChatResources(this.f$0, false);
    }
}
