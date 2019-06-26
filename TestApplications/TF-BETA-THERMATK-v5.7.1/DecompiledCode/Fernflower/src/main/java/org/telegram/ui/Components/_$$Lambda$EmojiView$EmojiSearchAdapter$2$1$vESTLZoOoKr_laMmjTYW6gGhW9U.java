package org.telegram.ui.Components;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BottomSheet;

// $FF: synthetic class
public final class _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$vESTLZoOoKr_laMmjTYW6gGhW9U implements RequestDelegate {
   // $FF: synthetic field
   private final <undefinedtype> f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final BottomSheet.Builder f$2;

   // $FF: synthetic method
   public _$$Lambda$EmojiView$EmojiSearchAdapter$2$1$vESTLZoOoKr_laMmjTYW6gGhW9U(Object var1, AlertDialog[] var2, BottomSheet.Builder var3) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$onClick$1$EmojiView$EmojiSearchAdapter$2$1(this.f$1, this.f$2, var1, var2);
   }
}
