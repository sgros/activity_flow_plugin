package org.telegram.messenger;

import java.util.ArrayList;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$SendMessagesHelper$GqwnlURs2UJWXkcpzjv2EBiLrD0 implements Runnable {
   // $FF: synthetic field
   private final SendMessagesHelper f$0;
   // $FF: synthetic field
   private final TLRPC.TL_error f$1;
   // $FF: synthetic field
   private final ArrayList f$2;
   // $FF: synthetic field
   private final TLRPC.TL_messages_sendMultiMedia f$3;
   // $FF: synthetic field
   private final ArrayList f$4;
   // $FF: synthetic field
   private final ArrayList f$5;
   // $FF: synthetic field
   private final SendMessagesHelper.DelayedMessage f$6;
   // $FF: synthetic field
   private final TLObject f$7;

   // $FF: synthetic method
   public _$$Lambda$SendMessagesHelper$GqwnlURs2UJWXkcpzjv2EBiLrD0(SendMessagesHelper var1, TLRPC.TL_error var2, ArrayList var3, TLRPC.TL_messages_sendMultiMedia var4, ArrayList var5, ArrayList var6, SendMessagesHelper.DelayedMessage var7, TLObject var8) {
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
      this.f$0.lambda$null$28$SendMessagesHelper(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7);
   }
}
