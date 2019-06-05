package org.mozilla.focus.repository;

import org.mozilla.focus.persistence.BookmarkModel;

// $FF: synthetic class
public final class _$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0 implements Runnable {
   // $FF: synthetic field
   private final BookmarkRepository f$0;
   // $FF: synthetic field
   private final BookmarkModel f$1;

   // $FF: synthetic method
   public _$$Lambda$BookmarkRepository$yYrJZhrxaqhZq3J2ZPvJtIWwED0(BookmarkRepository var1, BookmarkModel var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      BookmarkRepository.lambda$updateBookmark$1(this.f$0, this.f$1);
   }
}
