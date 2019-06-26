package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;
import org.telegram.p004ui.ChannelCreateActivity.C39571;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelCreateActivity$1$q1TLoAihH5rBozhaNhmW5QGac-s */
public final /* synthetic */ class C1278-$$Lambda$ChannelCreateActivity$1$q1TLoAihH5rBozhaNhmW5QGac-s implements OnCancelListener {
    private final /* synthetic */ C39571 f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1278-$$Lambda$ChannelCreateActivity$1$q1TLoAihH5rBozhaNhmW5QGac-s(C39571 c39571, int i) {
        this.f$0 = c39571;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$onItemClick$1$ChannelCreateActivity$1(this.f$1, dialogInterface);
    }
}
