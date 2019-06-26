package org.telegram.ui.Adapters;

import org.telegram.messenger.MessagesStorage;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MentionsAdapter$HVDPv79fAM1RehGsWWysAxv_W1Q implements Runnable {
   // $FF: synthetic field
   private final MentionsAdapter f$0;
   // $FF: synthetic field
   private final String f$1;
   // $FF: synthetic field
   private final boolean f$2;
   // $FF: synthetic field
   private final TLObject f$3;
   // $FF: synthetic field
   private final TLRPC.User f$4;
   // $FF: synthetic field
   private final String f$5;
   // $FF: synthetic field
   private final MessagesStorage f$6;
   // $FF: synthetic field
   private final String f$7;

   // $FF: synthetic method
   public _$$Lambda$MentionsAdapter$HVDPv79fAM1RehGsWWysAxv_W1Q(MentionsAdapter var1, String var2, boolean var3, TLObject var4, TLRPC.User var5, String var6, MessagesStorage var7, String var8) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
   }

   public final void run() {
      this.f$0.lambda$null$3$MentionsAdapter(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
