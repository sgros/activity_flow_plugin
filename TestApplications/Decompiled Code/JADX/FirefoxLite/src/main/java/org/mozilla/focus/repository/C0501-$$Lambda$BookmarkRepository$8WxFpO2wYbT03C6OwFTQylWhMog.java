package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.repository.-$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog */
public final /* synthetic */ class C0501-$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog implements Runnable {
    private final /* synthetic */ BookmarkRepository f$0;
    private final /* synthetic */ BookmarkModel f$1;

    public /* synthetic */ C0501-$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog(BookmarkRepository bookmarkRepository, BookmarkModel bookmarkModel) {
        this.f$0 = bookmarkRepository;
        this.f$1 = bookmarkModel;
    }

    public final void run() {
        this.f$0.bookmarksDatabase.bookmarkDao().addBookmarks(this.f$1);
    }
}
