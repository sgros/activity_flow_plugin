package org.telegram.p004ui;

import android.view.View;
import org.telegram.p004ui.Components.RecyclerListView.OnItemLongClickListener;
import org.telegram.p004ui.ThemeActivity.ListAdapter;

/* compiled from: lambda */
/* renamed from: org.telegram.ui.-$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0 */
public final /* synthetic */ class C3874-$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0 implements OnItemLongClickListener {
    private final /* synthetic */ ListAdapter f$0;

    public /* synthetic */ C3874-$$Lambda$ThemeActivity$ListAdapter$pvw4GcZiIzN9zYxDAOmGDBqZDj0(ListAdapter listAdapter) {
        this.f$0 = listAdapter;
    }

    public final boolean onItemClick(View view, int i) {
        return this.f$0.lambda$onCreateViewHolder$4$ThemeActivity$ListAdapter(view, i);
    }
}
