package org.telegram.messenger;

import org.telegram.p004ui.ChatActivity;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.KeyboardButton;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY */
public final /* synthetic */ class C3545-$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY implements RequestDelegate {
    private final /* synthetic */ SendMessagesHelper f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ boolean f$2;
    private final /* synthetic */ MessageObject f$3;
    private final /* synthetic */ KeyboardButton f$4;
    private final /* synthetic */ ChatActivity f$5;
    private final /* synthetic */ TLObject[] f$6;

    public /* synthetic */ C3545-$$Lambda$SendMessagesHelper$mrT1SKD1wBToK3yGrtE1zSzHHGY(SendMessagesHelper sendMessagesHelper, String str, boolean z, MessageObject messageObject, KeyboardButton keyboardButton, ChatActivity chatActivity, TLObject[] tLObjectArr) {
        this.f$0 = sendMessagesHelper;
        this.f$1 = str;
        this.f$2 = z;
        this.f$3 = messageObject;
        this.f$4 = keyboardButton;
        this.f$5 = chatActivity;
        this.f$6 = tLObjectArr;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$sendCallback$17$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, tLObject, tL_error);
    }
}
