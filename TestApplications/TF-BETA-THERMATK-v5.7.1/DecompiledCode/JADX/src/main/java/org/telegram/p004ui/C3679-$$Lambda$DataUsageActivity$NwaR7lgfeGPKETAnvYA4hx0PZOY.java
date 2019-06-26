package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$DataUsageActivity$NwaR7lgfeGPKETAnvYA4hx0PZOY */
public final /* synthetic */ class C3679-$$Lambda$DataUsageActivity$NwaR7lgfeGPKETAnvYA4hx0PZOY implements OnItemClickListener {
    private final /* synthetic */ DataUsageActivity f$0;
    private final /* synthetic */ RecyclerListView f$1;

    public /* synthetic */ C3679-$$Lambda$DataUsageActivity$NwaR7lgfeGPKETAnvYA4hx0PZOY(DataUsageActivity dataUsageActivity, RecyclerListView recyclerListView) {
        this.f$0 = dataUsageActivity;
        this.f$1 = recyclerListView;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$createView$2$DataUsageActivity(this.f$1, view, i);
    }
}
