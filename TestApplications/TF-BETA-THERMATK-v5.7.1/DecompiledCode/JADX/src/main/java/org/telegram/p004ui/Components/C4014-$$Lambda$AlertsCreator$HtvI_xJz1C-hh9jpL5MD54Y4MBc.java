package org.telegram.p004ui.Components;

import org.telegram.messenger.MessagesStorage.BooleanCallback;
import org.telegram.messenger.MessagesStorage.IntCallback;
import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLRPC.Chat;
import org.telegram.tgnet.TLRPC.User;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$AlertsCreator$HtvI_xJz1C-hh9jpL5MD54Y4MBc */
public final /* synthetic */ class C4014-$$Lambda$AlertsCreator$HtvI_xJz1C-hh9jpL5MD54Y4MBc implements IntCallback {
    private final /* synthetic */ BaseFragment f$0;
    private final /* synthetic */ boolean f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ Chat f$3;
    private final /* synthetic */ User f$4;
    private final /* synthetic */ boolean f$5;
    private final /* synthetic */ BooleanCallback f$6;
    private final /* synthetic */ boolean[] f$7;

    public /* synthetic */ C4014-$$Lambda$AlertsCreator$HtvI_xJz1C-hh9jpL5MD54Y4MBc(BaseFragment baseFragment, boolean z, boolean z2, Chat chat, User user, boolean z3, BooleanCallback booleanCallback, boolean[] zArr) {
        this.f$0 = baseFragment;
        this.f$1 = z;
        this.f$2 = z2;
        this.f$3 = chat;
        this.f$4 = user;
        this.f$5 = z3;
        this.f$6 = booleanCallback;
        this.f$7 = zArr;
    }

    public final void run(int i) {
        AlertsCreator.lambda$null$10(this.f$0, this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, i);
    }
}