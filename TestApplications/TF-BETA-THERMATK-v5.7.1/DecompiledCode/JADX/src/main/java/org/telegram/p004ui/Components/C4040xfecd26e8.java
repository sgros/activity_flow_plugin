package org.telegram.p004ui.Components;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.p004ui.Components.EmojiView.StickersSearchGridAdapter.C28431;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;
import org.telegram.tgnet.TLRPC.TL_messages_getStickers;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Components.-$$Lambda$EmojiView$StickersSearchGridAdapter$1$D2YaPWN88kYmZJFvtUqCfq785Bw */
public final /* synthetic */ class C4040xfecd26e8 implements RequestDelegate {
    private final /* synthetic */ C28431 f$0;
    private final /* synthetic */ TL_messages_getStickers f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ LongSparseArray f$3;

    public /* synthetic */ C4040xfecd26e8(C28431 c28431, TL_messages_getStickers tL_messages_getStickers, ArrayList arrayList, LongSparseArray longSparseArray) {
        this.f$0 = c28431;
        this.f$1 = tL_messages_getStickers;
        this.f$2 = arrayList;
        this.f$3 = longSparseArray;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$run$3$EmojiView$StickersSearchGridAdapter$1(this.f$1, this.f$2, this.f$3, tLObject, tL_error);
    }
}
