package org.telegram.p004ui;

import org.telegram.messenger.MessagesStorage.BooleanCallback;
import org.telegram.tgnet.TLRPC.Chat;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg */
public final /* synthetic */ class C3681-$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg implements BooleanCallback {
    private final /* synthetic */ DialogsActivity f$0;
    private final /* synthetic */ int f$1;
    private final /* synthetic */ Chat f$2;
    private final /* synthetic */ long f$3;
    private final /* synthetic */ boolean f$4;

    public /* synthetic */ C3681-$$Lambda$DialogsActivity$N83TAKOOo9dN19vicqwdZcmBCMg(DialogsActivity dialogsActivity, int i, Chat chat, long j, boolean z) {
        this.f$0 = dialogsActivity;
        this.f$1 = i;
        this.f$2 = chat;
        this.f$3 = j;
        this.f$4 = z;
    }

    public final void run(boolean z) {
        this.f$0.lambda$perfromSelectedDialogsAction$14$DialogsActivity(this.f$1, this.f$2, this.f$3, this.f$4, z);
    }
}
