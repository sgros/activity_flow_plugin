package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg */
public final /* synthetic */ class C1422-$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg implements OnClickListener {
    private final /* synthetic */ ChatEditTypeActivity f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C1422-$$Lambda$ChatEditTypeActivity$oi4gxdiAY04rSah4zOjzmc63oJg(ChatEditTypeActivity chatEditTypeActivity, Chat chat) {
        this.f$0 = chatEditTypeActivity;
        this.f$1 = chat;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$13$ChatEditTypeActivity(this.f$1, dialogInterface, i);
    }
}
