package org.mozilla.focus.bookmark;

import android.support.p004v7.widget.PopupMenu.OnMenuItemClickListener;
import android.view.MenuItem;
import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.bookmark.-$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y */
public final /* synthetic */ class C0682-$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y implements OnMenuItemClickListener {
    private final /* synthetic */ BookmarkAdapter f$0;
    private final /* synthetic */ BookmarkModel f$1;

    public /* synthetic */ C0682-$$Lambda$BookmarkAdapter$TZbcuR8Z7wK7_KE_yTvlYO8Jg3Y(BookmarkAdapter bookmarkAdapter, BookmarkModel bookmarkModel) {
        this.f$0 = bookmarkAdapter;
        this.f$1 = bookmarkModel;
    }

    public final boolean onMenuItemClick(MenuItem menuItem) {
        return BookmarkAdapter.lambda$onBindViewHolder$1(this.f$0, this.f$1, menuItem);
    }
}
