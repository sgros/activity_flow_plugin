package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.EmojiView.GifSearchAdapter;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8 */
public final /* synthetic */ class C4038-$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8 implements RequestDelegate {
    private final /* synthetic */ GifSearchAdapter f$0;

    public /* synthetic */ C4038-$$Lambda$EmojiView$GifSearchAdapter$rB6iGH2IIIWpfXKf_CfIBRZyOE8(GifSearchAdapter gifSearchAdapter) {
        this.f$0 = gifSearchAdapter;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchBotUser$1$EmojiView$GifSearchAdapter(tLObject, tL_error);
    }
}
