package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.BotKeyboardView.BotKeyboardViewDelegate;
import org.telegram.tgnet.TLRPC.KeyboardButton;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$ChatActivityEnterView$b-Y8EmOjKWjLQcXw9E16k20A4eo */
public final /* synthetic */ class C4031-$$Lambda$ChatActivityEnterView$b-Y8EmOjKWjLQcXw9E16k20A4eo implements BotKeyboardViewDelegate {
    private final /* synthetic */ ChatActivityEnterView f$0;

    public /* synthetic */ C4031-$$Lambda$ChatActivityEnterView$b-Y8EmOjKWjLQcXw9E16k20A4eo(ChatActivityEnterView chatActivityEnterView) {
        this.f$0 = chatActivityEnterView;
    }

    public final void didPressedButton(KeyboardButton keyboardButton) {
        this.f$0.lambda$setButtons$15$ChatActivityEnterView(keyboardButton);
    }
}
