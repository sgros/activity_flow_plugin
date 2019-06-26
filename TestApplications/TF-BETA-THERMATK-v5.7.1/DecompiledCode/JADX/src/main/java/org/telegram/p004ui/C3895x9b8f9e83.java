package org.telegram.p004ui;

import org.telegram.p004ui.WallpapersListActivity.SearchAdapter;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$WallpapersListActivity$SearchAdapter$4mWogDs9zLmdvnE5jd5DGo_Q4ak */
public final /* synthetic */ class C3895x9b8f9e83 implements RequestDelegate {
    private final /* synthetic */ SearchAdapter f$0;

    public /* synthetic */ C3895x9b8f9e83(SearchAdapter searchAdapter) {
        this.f$0 = searchAdapter;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$searchBotUser$2$WallpapersListActivity$SearchAdapter(tLObject, tL_error);
    }
}
