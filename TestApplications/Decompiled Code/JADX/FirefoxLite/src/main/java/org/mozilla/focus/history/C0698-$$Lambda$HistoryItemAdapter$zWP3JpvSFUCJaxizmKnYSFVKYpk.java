package org.mozilla.focus.history;

import android.support.p004v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import org.mozilla.focus.history.model.Site;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.history.-$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk */
public final /* synthetic */ class C0698-$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk implements OnMenuItemClickListener {
    private final /* synthetic */ HistoryItemAdapter f$0;
    private final /* synthetic */ Site f$1;

    public /* synthetic */ C0698-$$Lambda$HistoryItemAdapter$zWP3JpvSFUCJaxizmKnYSFVKYpk(HistoryItemAdapter historyItemAdapter, Site site) {
        this.f$0 = historyItemAdapter;
        this.f$1 = site;
    }

    public final boolean onMenuItemClick(MenuItem menuItem) {
        return HistoryItemAdapter.lambda$onBindViewHolder$0(this.f$0, this.f$1, menuItem);
    }
}
