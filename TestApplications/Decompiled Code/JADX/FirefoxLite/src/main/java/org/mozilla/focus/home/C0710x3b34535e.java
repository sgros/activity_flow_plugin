package org.mozilla.focus.home;

import android.support.p004v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import org.mozilla.focus.history.model.Site;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.home.-$$Lambda$HomeFragment$SiteItemClickListener$4IB3ndGZ-ufqnkodgjuU-qaCzyA */
public final /* synthetic */ class C0710x3b34535e implements OnMenuItemClickListener {
    private final /* synthetic */ SiteItemClickListener f$0;
    private final /* synthetic */ Site f$1;

    public /* synthetic */ C0710x3b34535e(SiteItemClickListener siteItemClickListener, Site site) {
        this.f$0 = siteItemClickListener;
        this.f$1 = site;
    }

    public final boolean onMenuItemClick(MenuItem menuItem) {
        return SiteItemClickListener.lambda$onLongClick$1(this.f$0, this.f$1, menuItem);
    }
}
