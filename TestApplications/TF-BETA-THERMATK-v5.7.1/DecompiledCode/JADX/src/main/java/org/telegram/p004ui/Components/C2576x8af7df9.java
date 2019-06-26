package org.telegram.p004ui.Components;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.p004ui.Components.EmojiView.StickersSearchGridAdapter.C28431;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_messages_getStickers;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EmojiView$StickersSearchGridAdapter$1$0O6pCOi0jii8pwBAvqjd7az_Jp8 */
public final /* synthetic */ class C2576x8af7df9 implements Runnable {
    private final /* synthetic */ C28431 f$0;
    private final /* synthetic */ TL_messages_getStickers f$1;
    private final /* synthetic */ TLObject f$2;
    private final /* synthetic */ ArrayList f$3;
    private final /* synthetic */ LongSparseArray f$4;

    public /* synthetic */ C2576x8af7df9(C28431 c28431, TL_messages_getStickers tL_messages_getStickers, TLObject tLObject, ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0 = c28431;
        this.f$1 = tL_messages_getStickers;
        this.f$2 = tLObject;
        this.f$3 = arrayList;
        this.f$4 = longSparseArray;
    }

    public final void run() {
        this.f$0.lambda$null$2$EmojiView$StickersSearchGridAdapter$1(this.f$1, this.f$2, this.f$3, this.f$4);
    }
}
