package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$JG3xW1rvxoBoP1ShppmfpfTlhQo */
public final /* synthetic */ class C3821-$$Lambda$PhotoViewer$JG3xW1rvxoBoP1ShppmfpfTlhQo implements OnItemClickListener {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C3821-$$Lambda$PhotoViewer$JG3xW1rvxoBoP1ShppmfpfTlhQo(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$setParentActivity$26$PhotoViewer(view, i);
    }
}
