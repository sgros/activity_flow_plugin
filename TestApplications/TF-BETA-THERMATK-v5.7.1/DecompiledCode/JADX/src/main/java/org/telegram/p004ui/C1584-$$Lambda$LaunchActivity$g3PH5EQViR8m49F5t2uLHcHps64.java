package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.HashMap;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64 */
public final /* synthetic */ class C1584-$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64 implements OnClickListener {
    private final /* synthetic */ LaunchActivity f$0;
    private final /* synthetic */ HashMap f$1;
    private final /* synthetic */ int f$2;

    public /* synthetic */ C1584-$$Lambda$LaunchActivity$g3PH5EQViR8m49F5t2uLHcHps64(LaunchActivity launchActivity, HashMap hashMap, int i) {
        this.f$0 = launchActivity;
        this.f$1 = hashMap;
        this.f$2 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$didReceivedNotification$43$LaunchActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}