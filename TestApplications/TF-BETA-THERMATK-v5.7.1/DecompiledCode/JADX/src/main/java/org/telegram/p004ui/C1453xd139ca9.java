package org.telegram.p004ui;

import java.util.ArrayList;
import org.telegram.p004ui.ChatUsersActivity.SearchAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ChatUsersActivity$SearchAdapter$Sw9CFmRc9E_mExIImd0E0Zq2CWY */
public final /* synthetic */ class C1453xd139ca9 implements Runnable {
    private final /* synthetic */ SearchAdapter f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ ArrayList f$2;
    private final /* synthetic */ ArrayList f$3;

    public /* synthetic */ C1453xd139ca9(SearchAdapter searchAdapter, ArrayList arrayList, ArrayList arrayList2, ArrayList arrayList3) {
        this.f$0 = searchAdapter;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
        this.f$3 = arrayList3;
    }

    public final void run() {
        this.f$0.lambda$updateSearchResults$3$ChatUsersActivity$SearchAdapter(this.f$1, this.f$2, this.f$3);
    }
}
