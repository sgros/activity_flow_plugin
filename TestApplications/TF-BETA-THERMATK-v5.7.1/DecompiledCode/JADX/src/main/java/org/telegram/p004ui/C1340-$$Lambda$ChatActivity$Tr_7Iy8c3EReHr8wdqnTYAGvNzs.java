package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import org.telegram.messenger.MessageObject;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatActivity$Tr_7Iy8c3EReHr8wdqnTYAGvNzs */
public final /* synthetic */ class C1340-$$Lambda$ChatActivity$Tr_7Iy8c3EReHr8wdqnTYAGvNzs implements OnClickListener {
    private final /* synthetic */ ChatActivity f$0;
    private final /* synthetic */ MessageObject f$1;

    public /* synthetic */ C1340-$$Lambda$ChatActivity$Tr_7Iy8c3EReHr8wdqnTYAGvNzs(ChatActivity chatActivity, MessageObject messageObject) {
        this.f$0 = chatActivity;
        this.f$1 = messageObject;
    }

    public final void onClick(DialogInterface dialogInterface, int i) {
        this.f$0.lambda$processSelectedOption$81$ChatActivity(this.f$1, dialogInterface, i);
    }
}
