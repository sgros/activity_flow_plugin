package org.telegram.ui.Adapters;

import android.util.SparseArray;
import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY implements Comparator {
   // $FF: synthetic field
   private final SparseArray f$0;
   // $FF: synthetic field
   private final ArrayList f$1;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$RtUwcbbmysCllxWaHytbPO9oFzY(SparseArray var1, ArrayList var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final int compare(Object var1, Object var2) {
      return MentionsAdapter.lambda$searchUsernameOrHashtag$5(this.f$0, this.f$1, (TLRPC.User)var1, (TLRPC.User)var2);
   }
}
