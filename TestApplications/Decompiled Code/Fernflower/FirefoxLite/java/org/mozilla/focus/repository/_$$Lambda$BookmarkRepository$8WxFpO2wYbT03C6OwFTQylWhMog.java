package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

// $FF: synthetic class
public final class _$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog implements Runnable {
   // $FF: synthetic field
   private final BookmarkRepository f$0;
   // $FF: synthetic field
   private final BookmarkModel f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkRepository$8WxFpO2wYbT03C6OwFTQylWhMog(BookmarkRepository var1, BookmarkModel var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      BookmarkRepository.lambda$addBookmark$0(this.f$0, this.f$1);
   }
}
