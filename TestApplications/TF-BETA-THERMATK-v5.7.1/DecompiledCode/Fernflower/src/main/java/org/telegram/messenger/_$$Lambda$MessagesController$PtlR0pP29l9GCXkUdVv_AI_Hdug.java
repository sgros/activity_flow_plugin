package org.telegram.messenger;

import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;
import org.telegram.ui.ActionBar.AlertDialog;
import org.telegram.ui.ActionBar.BaseFragment;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug implements Runnable {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final AlertDialog[] f$1;
   // $FF: synthetic field
   private final BaseFragment f$2;
   // $FF: synthetic field
   private final TLRPC.TL_error f$3;
   // $FF: synthetic field
   private final TLObject f$4;
   // $FF: synthetic field
   private final int f$5;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$PtlR0pP29l9GCXkUdVv_AI_Hdug(MessagesController var1, AlertDialog[] var2, BaseFragment var3, TLRPC.TL_error var4, TLObject var5, int var6) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
   }

   public final void run() {
      this.f$0.lambda$null$259$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5);
   }
}
