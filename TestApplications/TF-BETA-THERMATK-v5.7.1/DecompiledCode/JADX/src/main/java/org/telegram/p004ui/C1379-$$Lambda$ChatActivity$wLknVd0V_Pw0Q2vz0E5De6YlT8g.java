package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;
import org.telegram.tgnet.TLRPC.TL_game;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$wLknVd0V_Pw0Q2vz0E5De6YlT8g */
public final /* synthetic */ class C1379-$$Lambda$ChatActivity$wLknVd0V_Pw0Q2vz0E5De6YlT8g implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ TL_game f$1;
    private final /* synthetic */ MessageObject f$2;
    private final /* synthetic */ String f$3;
    private final /* synthetic */ int f$4;

    public /* synthetic */ C1379-$$Lambda$ChatActivity$wLknVd0V_Pw0Q2vz0E5De6YlT8g(ChatActivity chatActivity, TL_game tL_game, MessageObject messageObject, String str, int i) {
        this.f$0 = chatActivity;
        this.f$1 = tL_game;
        this.f$2 = messageObject;
        this.f$3 = str;
        this.f$4 = i;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showOpenGameAlert$83$ChatActivity(this.f$1, this.f$2, this.f$3, this.f$4, dialogInterface, i);
    }
}
