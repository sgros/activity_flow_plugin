package org.telegram.p004ui;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatLinkActivity$Eb3fJqb9-RHvQPpwLb48kNyZrZ8 */
public final /* synthetic */ class C1426-$$Lambda$ChatLinkActivity$Eb3fJqb9-RHvQPpwLb48kNyZrZ8 implements OnCancelListener {
    private final /* synthetic */ ChatLinkActivity f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C1426-$$Lambda$ChatLinkActivity$Eb3fJqb9-RHvQPpwLb48kNyZrZ8(ChatLinkActivity chatLinkActivity, int i) {
        this.f$0 = chatLinkActivity;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$null$2$ChatLinkActivity(this.f$1, dialogInterface);
    }
}
