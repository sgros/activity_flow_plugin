package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.repository.-$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk */
public final /* synthetic */ class C0502-$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk implements Runnable {
    private final /* synthetic */ BookmarkRepository f$0;
    private final /* synthetic */ BookmarkModel f$1;

    public /* synthetic */ C0502-$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk(BookmarkRepository bookmarkRepository, BookmarkModel bookmarkModel) {
        this.f$0 = bookmarkRepository;
        this.f$1 = bookmarkModel;
    }

    public final void run() {
        this.f$0.bookmarksDatabase.bookmarkDao().deleteBookmark(this.f$1);
    }
}
