package org.telegram.messenger;

import org.telegram.p004ui.ActionBar.BaseFragment;
import org.telegram.tgnet.TLObject;

/* compiled from: lambda */
/* renamed from: org.telegram.messenger.-$$Lambda$DataQuery$O3U-V0n7MR7QFur96I7yoxXpLvQ */
public final /* synthetic */ class C0367-$$Lambda$DataQuery$O3U-V0n7MR7QFur96I7yoxXpLvQ implements Runnable {
    private final /* synthetic */ DataQuery f$0;
    private final /* synthetic */ TLObject f$1;
    private final /* synthetic */ int f$2;
    private final /* synthetic */ int f$3;
    private final /* synthetic */ BaseFragment f$4;
    private final /* synthetic */ boolean f$5;

    public /* synthetic */ C0367-$$Lambda$DataQuery$O3U-V0n7MR7QFur96I7yoxXpLvQ(DataQuery dataQuery, TLObject tLObject, int i, int i2, BaseFragment baseFragment, boolean z) {
        this.f$0 = dataQuery;
        this.f$1 = tLObject;
        this.f$2 = i;
        this.f$3 = i2;
        this.f$4 = baseFragment;
        this.f$5 = z;
    }

    public final void run() {
        this.f$0.lambda$null$42$DataQuery(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
    }
}
