package org.telegram.messenger;

import android.content.DialogInterface;
import android.content.DialogInterface.OnCancelListener;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$MessagesController$TMnAL0Zt--AXB8RK3P6tM9ad784 */
public final /* synthetic */ class C0658-$$Lambda$MessagesController$TMnAL0Zt--AXB8RK3P6tM9ad784 implements OnCancelListener {
    private final /* synthetic */ MessagesController f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C0658-$$Lambda$MessagesController$TMnAL0Zt--AXB8RK3P6tM9ad784(MessagesController messagesController, int i) {
        this.f$0 = messagesController;
        this.f$1 = i;
    }

    public final void onCancel(DialogInterface dialogInterface) {
        this.f$0.lambda$null$261$MessagesController(this.f$1, dialogInterface);
    }
}
