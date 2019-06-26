package org.telegram.p004ui;

import org.telegram.p004ui.WallpapersListActivity.SearchAdapter;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WallpapersListActivity$SearchAdapter$EUxupb60ZwBIv1eg9iblwmPpQ9k */
public final /* synthetic */ class C3896x597ca266 implements RequestDelegate {
    private final /* synthetic */ SearchAdapter f$0;
    private final /* synthetic */ int f$1;

    public /* synthetic */ C3896x597ca266(SearchAdapter searchAdapter, int i) {
        this.f$0 = searchAdapter;
        this.f$1 = i;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchImages$4$WallpapersListActivity$SearchAdapter(this.f$1, tLObject, tL_error);
    }
}
