package org.telegram.p004ui.Components;

import java.util.ArrayList;
import org.telegram.messenger.MessageObject;
import org.telegram.p004ui.DialogsActivity;
import org.telegram.p004ui.DialogsActivity.DialogsActivityDelegate;
import org.telegram.tgnet.TLRPC.KeyboardButton;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87-dZmhZj8HOVnM */
public final /* synthetic */ class C4028-$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87-dZmhZj8HOVnM implements DialogsActivityDelegate {
    private final /* synthetic */ ChatActivityEnterView f$0;
    private final /* synthetic */ MessageObject f$1;
    private final /* synthetic */ KeyboardButton f$2;

    public /* synthetic */ C4028-$$Lambda$ChatActivityEnterView$G5pe20Ftkscr87-dZmhZj8HOVnM(ChatActivityEnterView chatActivityEnterView, MessageObject messageObject, KeyboardButton keyboardButton) {
        this.f$0 = chatActivityEnterView;
        this.f$1 = messageObject;
        this.f$2 = keyboardButton;
    }

    public final void didSelectDialogs(DialogsActivity dialogsActivity, ArrayList arrayList, CharSequence charSequence, boolean z) {
        this.f$0.lambda$didPressedBotButton$17$ChatActivityEnterView(this.f$1, this.f$2, dialogsActivity, arrayList, charSequence, z);
    }
}
