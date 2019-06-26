package org.telegram.p004ui.Adapters;

import java.util.ArrayList;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.Adapters.-$$Lambda$SearchAdapter$6uwOh4k7ujlooXYRN4wF38fqlek */
public final /* synthetic */ class C2262-$$Lambda$SearchAdapter$6uwOh4k7ujlooXYRN4wF38fqlek implements Runnable {
    private final /* synthetic */ SearchAdapter f$0;
    private final /* synthetic */ ArrayList f$1;
    private final /* synthetic */ ArrayList f$2;

    public /* synthetic */ C2262-$$Lambda$SearchAdapter$6uwOh4k7ujlooXYRN4wF38fqlek(SearchAdapter searchAdapter, ArrayList arrayList, ArrayList arrayList2) {
        this.f$0 = searchAdapter;
        this.f$1 = arrayList;
        this.f$2 = arrayList2;
    }

    public final void run() {
        this.f$0.lambda$updateSearchResults$2$SearchAdapter(this.f$1, this.f$2);
    }
}
