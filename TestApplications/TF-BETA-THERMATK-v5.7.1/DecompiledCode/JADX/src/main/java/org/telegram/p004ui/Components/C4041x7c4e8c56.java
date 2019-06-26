package org.telegram.p004ui.Components;

import org.telegram.p004ui.Components.EmojiView.StickersSearchGridAdapter.C28431;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_searchStickerSets;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EmojiView$StickersSearchGridAdapter$1$PMXaBA9vcwjG4TQSEF8AuHh_dEU */
public final /* synthetic */ class C4041x7c4e8c56 implements RequestDelegate {
    private final /* synthetic */ C28431 f$0;
    private final /* synthetic */ TL_messages_searchStickerSets f$1;

    public /* synthetic */ C4041x7c4e8c56(C28431 c28431, TL_messages_searchStickerSets tL_messages_searchStickerSets) {
        this.f$0 = c28431;
        this.f$1 = tL_messages_searchStickerSets;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$1$EmojiView$StickersSearchGridAdapter$1(this.f$1, tLObject, tL_error);
    }
}
