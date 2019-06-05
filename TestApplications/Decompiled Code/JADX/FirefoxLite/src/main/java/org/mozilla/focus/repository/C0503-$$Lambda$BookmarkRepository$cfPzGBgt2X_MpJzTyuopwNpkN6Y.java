package org.mozilla.focus.repository;

/* compiled from: lambda */
/* renamed from: org.mozilla.focus.repository.-$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y */
public final /* synthetic */ class C0503-$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y implements Runnable {
    private final /* synthetic */ BookmarkRepository f$0;
    private final /* synthetic */ String f$1;

    public /* synthetic */ C0503-$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y(BookmarkRepository bookmarkRepository, String str) {
        this.f$0 = bookmarkRepository;
        this.f$1 = str;
    }

    public final void run() {
        this.f$0.bookmarksDatabase.bookmarkDao().deleteBookmarksByUrl(this.f$1);
    }
}
