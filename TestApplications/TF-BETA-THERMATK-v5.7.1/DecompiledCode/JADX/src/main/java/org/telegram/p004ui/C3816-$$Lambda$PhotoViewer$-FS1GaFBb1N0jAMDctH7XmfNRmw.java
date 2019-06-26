package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$-FS1GaFBb1N0jAMDctH7XmfNRmw */
public final /* synthetic */ class C3816-$$Lambda$PhotoViewer$-FS1GaFBb1N0jAMDctH7XmfNRmw implements OnItemClickListener {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C3816-$$Lambda$PhotoViewer$-FS1GaFBb1N0jAMDctH7XmfNRmw(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final void onItemClick(View view, int i) {
        this.f$0.lambda$setParentActivity$25$PhotoViewer(view, i);
    }
}
