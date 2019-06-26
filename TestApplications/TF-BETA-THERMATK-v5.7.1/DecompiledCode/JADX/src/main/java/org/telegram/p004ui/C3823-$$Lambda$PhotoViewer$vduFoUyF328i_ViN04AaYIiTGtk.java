package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemLongClickListener;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$PhotoViewer$vduFoUyF328i_ViN04AaYIiTGtk */
public final /* synthetic */ class C3823-$$Lambda$PhotoViewer$vduFoUyF328i_ViN04AaYIiTGtk implements OnItemLongClickListener {
    private final /* synthetic */ PhotoViewer f$0;

    public /* synthetic */ C3823-$$Lambda$PhotoViewer$vduFoUyF328i_ViN04AaYIiTGtk(PhotoViewer photoViewer) {
        this.f$0 = photoViewer;
    }

    public final boolean onItemClick(View view, int i) {
        return this.f$0.lambda$setParentActivity$28$PhotoViewer(view, i);
    }
}
