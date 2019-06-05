package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.repository.-$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0 */
public final /* synthetic */ class C0504-$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0 implements Runnable {
    private final /* synthetic */ BookmarkRepository f$0;
    private final /* synthetic */ BookmarkModel f$1;

    public /* synthetic */ C0504-$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0(BookmarkRepository bookmarkRepository, BookmarkModel bookmarkModel) {
        this.f$0 = bookmarkRepository;
        this.f$1 = bookmarkModel;
    }

    public final void run() {
        this.f$0.bookmarksDatabase.bookmarkDao().updateBookmark(this.f$1);
    }
}
