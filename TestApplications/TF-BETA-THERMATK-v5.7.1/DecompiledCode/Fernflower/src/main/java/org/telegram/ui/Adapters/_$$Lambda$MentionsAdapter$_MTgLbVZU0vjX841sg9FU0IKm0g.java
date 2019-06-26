package org.telegram.ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$_MTgLbVZU0vjX841sg9FU0IKm0g implements Runnable {
   // $FF: synthetic field
   private final MentionsAdapter f$0;
   // $FF: synthetic field
   private final ArrayList f$1;
   // $FF: synthetic field
   private final SparseArray f$2;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$_MTgLbVZU0vjX841sg9FU0IKm0g(MentionsAdapter var1, ArrayList var2, SparseArray var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run() {
      this.f$0.lambda$searchUsernameOrHashtag$6$MentionsAdapter(this.f$1, this.f$2);
   }
}
