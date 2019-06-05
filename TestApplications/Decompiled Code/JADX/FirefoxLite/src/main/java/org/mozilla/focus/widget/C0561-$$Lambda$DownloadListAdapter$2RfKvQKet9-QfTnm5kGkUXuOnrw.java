package org.mozilla.focus.widget;

import android.view.MenuItem;
import android.widget.PopupMenu;
import android.widget.PopupMenu.OnMenuItemClickListener;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.widget.-$$Lambda$DownloadListAdapter$2RfKvQKet9-QfTnm5kGkUXuOnrw */
public final /* synthetic */ class C0561-$$Lambda$DownloadListAdapter$2RfKvQKet9-QfTnm5kGkUXuOnrw implements OnMenuItemClickListener {
    private final /* synthetic */ DownloadListAdapter f$0;
    private final /* synthetic */ long f$1;
    private final /* synthetic */ PopupMenu f$2;

    public /* synthetic */ C0561-$$Lambda$DownloadListAdapter$2RfKvQKet9-QfTnm5kGkUXuOnrw(DownloadListAdapter downloadListAdapter, long j, PopupMenu popupMenu) {
        this.f$0 = downloadListAdapter;
        this.f$1 = j;
        this.f$2 = popupMenu;
    }

    public final boolean onMenuItemClick(MenuItem menuItem) {
        return DownloadListAdapter.lambda$null$0(this.f$0, this.f$1, this.f$2, menuItem);
    }
}
