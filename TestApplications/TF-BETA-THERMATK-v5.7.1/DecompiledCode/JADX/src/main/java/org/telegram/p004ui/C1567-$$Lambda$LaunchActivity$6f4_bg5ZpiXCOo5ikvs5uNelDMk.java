package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import java.util.HashMap;
import org.telegram.messenger.ContactsController;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk */
public final /* synthetic */ class C1567-$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk implements OnClickListener {
    private final /* synthetic */ int f$0;
    private final /* synthetic */ HashMap f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ boolean f$3;

    public /* synthetic */ C1567-$$Lambda$LaunchActivity$6f4_bg5ZpiXCOo5ikvs5uNelDMk(int i, HashMap hashMap, boolean z, boolean z2) {
        this.f$0 = i;
        this.f$1 = hashMap;
        this.f$2 = z;
        this.f$3 = z2;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        ContactsController.getInstance(this.f$0).syncPhoneBookByAlert(this.f$1, this.f$2, this.f$3, true);
    }
}
