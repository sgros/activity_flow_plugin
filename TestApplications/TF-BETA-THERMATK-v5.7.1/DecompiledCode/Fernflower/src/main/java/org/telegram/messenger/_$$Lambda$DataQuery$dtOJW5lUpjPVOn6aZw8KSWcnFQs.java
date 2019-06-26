package org.telegram.messenger;

import java.util.ArrayList;
import java.util.Comparator;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$DataQuery$dtOJW5lUpjPVOn6aZw8KSWcnFQs implements Comparator {
   // $FF: synthetic field
   private final ArrayList f$0;

   // $FF: synthetic method
   public _$$Lambda$DataQuery$dtOJW5lUpjPVOn6aZw8KSWcnFQs(ArrayList var1) {
      this.f$0 = var1;
   }

   public final int compare(Object var1, Object var2) {
      return DataQuery.lambda$reorderStickers$16(this.f$0, (TLRPC.TL_messages_stickerSet)var1, (TLRPC.TL_messages_stickerSet)var2);
   }
}
