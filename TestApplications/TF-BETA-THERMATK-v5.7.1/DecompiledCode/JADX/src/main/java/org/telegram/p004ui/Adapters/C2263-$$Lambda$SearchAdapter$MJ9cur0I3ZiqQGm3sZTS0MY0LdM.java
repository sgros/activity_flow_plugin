package org.telegram.p004ui.Adapters;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$SearchAdapter$MJ9cur0I3ZiqQGm3sZTS0MY0LdM */
public final /* synthetic */ class C2263-$$Lambda$SearchAdapter$MJ9cur0I3ZiqQGm3sZTS0MY0LdM implements Runnable {
    private final /* synthetic */ SearchAdapter f$0;
    private final /* synthetic */ String f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ int f$3;

    public /* synthetic */ C2263-$$Lambda$SearchAdapter$MJ9cur0I3ZiqQGm3sZTS0MY0LdM(SearchAdapter searchAdapter, String str, ArrayList arrayList, int i) {
        this.f$0 = searchAdapter;
        this.f$1 = str;
        this.f$2 = arrayList;
        this.f$3 = i;
    }

    public final void run() {
        this.f$0.lambda$null$0$SearchAdapter(this.f$1, this.f$2, this.f$3);
    }
}
