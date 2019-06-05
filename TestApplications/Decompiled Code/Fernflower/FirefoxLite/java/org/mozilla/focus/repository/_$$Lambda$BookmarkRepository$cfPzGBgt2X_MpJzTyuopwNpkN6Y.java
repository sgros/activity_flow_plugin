package org.mozilla.focus.repository;

// $FF: synthetic class
public final class _$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y implements Runnable {
   // $FF: synthetic field
   private final BookmarkRepository f$0;
   // $FF: synthetic field
   private final String f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkRepository$cfPzGBgt2X_MpJzTyuopwNpkN6Y(BookmarkRepository var1, String var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      BookmarkRepository.lambda$deleteBookmarksByUrl$3(this.f$0, this.f$1);
   }
}
