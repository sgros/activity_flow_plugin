package org.telegram.p004ui;

import org.telegram.p004ui.SettingsActivity.SearchAdapter;
import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC.TL_error;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$SettingsActivity$SearchAdapter$mPyLXiUd_cjxJI7NTOZqkYVtkUM */
public final /* synthetic */ class C3867xee0b74c8 implements RequestDelegate {
    private final /* synthetic */ SearchAdapter f$0;

    public /* synthetic */ C3867xee0b74c8(SearchAdapter searchAdapter) {
        this.f$0 = searchAdapter;
    }

    public final void run(TLObject tLObject, TL_error tL_error) {
        this.f$0.lambda$loadFaqWebPage$82$SettingsActivity$SearchAdapter(tLObject, tL_error);
    }
}
