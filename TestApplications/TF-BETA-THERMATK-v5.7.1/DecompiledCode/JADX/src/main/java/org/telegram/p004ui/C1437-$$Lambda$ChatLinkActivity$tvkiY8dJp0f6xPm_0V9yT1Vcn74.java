package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.ChatFull;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74 */
public final /* synthetic */ class C1437-$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74 implements OnClickListener {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ ChatFull f$1;
    private final /* synthetic */ Chat f$2;

    public /* synthetic */ C1437-$$Lambda$ChatLinkActivity$tvkiY8dJp0f6xPm_0V9yT1Vcn74(ChatLinkActivity chatLinkActivity, ChatFull chatFull, Chat chat) {
        this.f$0 = chatLinkActivity;
        this.f$1 = chatFull;
        this.f$2 = chat;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$showLinkAlert$8$ChatLinkActivity(this.f$1, this.f$2, dialogInterface, i);
    }
}
