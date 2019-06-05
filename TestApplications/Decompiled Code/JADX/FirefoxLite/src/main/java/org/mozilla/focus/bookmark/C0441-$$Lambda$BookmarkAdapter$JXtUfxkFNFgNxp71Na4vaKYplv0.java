package org.mozilla.focus.bookmark;

import android.view.View;
import android.view.View.OnClickListener;
import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.bookmark.-$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0 */
public final /* synthetic */ class C0441-$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0 implements OnClickListener {
    private final /* synthetic */ BookmarkAdapter f$0;
    private final /* synthetic */ BookmarkModel f$1;

    public /* synthetic */ C0441-$$Lambda$BookmarkAdapter$JXtUfxkFNFgNxp71Na4vaKYplv0(BookmarkAdapter bookmarkAdapter, BookmarkModel bookmarkModel) {
        this.f$0 = bookmarkAdapter;
        this.f$1 = bookmarkModel;
    }

    public final void onClick(View view) {
        this.f$0.listener.onItemClicked(this.f$1.getUrl());
    }
}
