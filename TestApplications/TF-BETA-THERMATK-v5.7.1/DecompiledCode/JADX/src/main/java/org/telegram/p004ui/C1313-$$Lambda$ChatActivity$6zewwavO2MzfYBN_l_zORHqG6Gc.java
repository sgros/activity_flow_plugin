package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$6zewwavO2MzfYBN_l_zORHqG6Gc */
public final /* synthetic */ class C1313-$$Lambda$ChatActivity$6zewwavO2MzfYBN_l_zORHqG6Gc implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ MessageObject f$1;

    public /* synthetic */ C1313-$$Lambda$ChatActivity$6zewwavO2MzfYBN_l_zORHqG6Gc(ChatActivity chatActivity, MessageObject messageObject) {
        this.f$0 = chatActivity;
        this.f$1 = messageObject;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$shareMyContact$41$ChatActivity(this.f$1, dialogInterface, i);
    }
}
