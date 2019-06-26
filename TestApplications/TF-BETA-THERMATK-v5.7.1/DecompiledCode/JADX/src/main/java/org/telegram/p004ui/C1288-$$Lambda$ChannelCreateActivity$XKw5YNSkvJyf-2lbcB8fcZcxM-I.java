package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf-2lbcB8fcZcxM-I */
public final /* synthetic */ class C1288-$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf-2lbcB8fcZcxM-I implements OnClickListener {
    private final /* synthetic */ ChannelCreateActivity f$0;
    private final /* synthetic */ Chat f$1;

    public /* synthetic */ C1288-$$Lambda$ChannelCreateActivity$XKw5YNSkvJyf-2lbcB8fcZcxM-I(ChannelCreateActivity channelCreateActivity, Chat chat) {
        this.f$0 = channelCreateActivity;
        this.f$1 = chat;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$null$14$ChannelCreateActivity(this.f$1, dialogInterface, i);
    }
}
