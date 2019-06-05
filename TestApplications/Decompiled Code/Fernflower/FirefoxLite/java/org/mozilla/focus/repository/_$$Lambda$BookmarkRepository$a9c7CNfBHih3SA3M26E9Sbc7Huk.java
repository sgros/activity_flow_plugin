package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

// $FF: synthetic class
public final class _$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk implements Runnable {
   // $FF: synthetic field
   private final BookmarkRepository f$0;
   // $FF: synthetic field
   private final BookmarkModel f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkRepository$a9c7CNfBHih3SA3M26E9Sbc7Huk(BookmarkRepository var1, BookmarkModel var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      BookmarkRepository.lambda$deleteBookmark$2(this.f$0, this.f$1);
   }
}
