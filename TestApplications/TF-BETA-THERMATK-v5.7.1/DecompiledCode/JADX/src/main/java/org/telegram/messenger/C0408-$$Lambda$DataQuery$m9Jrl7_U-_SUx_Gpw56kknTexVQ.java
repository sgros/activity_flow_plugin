package org.telegram.messenger;

import android.util.LongSparseArray;
import java.util.ArrayList;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.StickerSet;
import org.telegram.tgnet.TLRPC.TL_messages_allStickers;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$m9Jrl7_U-_SUx_Gpw56kknTexVQ */
public final /* synthetic */ class C0408-$$Lambda$DataQuery$m9Jrl7_U-_SUx_Gpw56kknTexVQ implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ LongSparseArray f$4;
    private final /* synthetic */ StickerSet f$5;
    private final /* synthetic */ TL_messages_allStickers f$6;
    private final /* synthetic */ int f$7;

    public /* synthetic */ C0408-$$Lambda$DataQuery$m9Jrl7_U-_SUx_Gpw56kknTexVQ(DataQuery dataQuery, TLObject tLObject, ArrayList arrayList, int i, LongSparseArray longSparseArray, StickerSet stickerSet, TL_messages_allStickers tL_messages_allStickers, int i2) {
        this.f$0 = dataQuery;
        this.f$1 = tLObject;
        this.f$2 = arrayList;
        this.f$3 = i;
        this.f$4 = longSparseArray;
        this.f$5 = stickerSet;
        this.f$6 = tL_messages_allStickers;
        this.f$7 = i2;
    }

    public final void run() {
        this.f$0.lambda$null$31$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
    }
}