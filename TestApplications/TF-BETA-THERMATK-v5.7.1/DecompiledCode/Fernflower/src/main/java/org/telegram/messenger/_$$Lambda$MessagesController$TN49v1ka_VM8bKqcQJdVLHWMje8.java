package org.telegram.messenger;

import org.telegram.tgnet.RequestDelegate;
import org.telegram.tgnet.TLObject;
import org.telegram.tgnet.TLRPC;

// $FF: synthetic class
public final class _$$Lambda$MessagesController$TN49v1ka_VM8bKqcQJdVLHWMje8 implements RequestDelegate {
   // $FF: synthetic field
   private final MessagesController f$0;
   // $FF: synthetic field
   private final long f$1;
   // $FF: synthetic field
   private final int f$10;
   // $FF: synthetic field
   private final int f$11;
   // $FF: synthetic field
   private final boolean f$12;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final int f$3;
   // $FF: synthetic field
   private final int f$4;
   // $FF: synthetic field
   private final int f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final int f$7;
   // $FF: synthetic field
   private final boolean f$8;
   // $FF: synthetic field
   private final int f$9;

   // $FF: synthetic method
   public _$$Lambda$MessagesController$TN49v1ka_VM8bKqcQJdVLHWMje8(MessagesController var1, long var2, int var4, int var5, int var6, int var7, int var8, int var9, boolean var10, int var11, int var12, int var13, boolean var14) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var4;
      this.f$3 = var5;
      this.f$4 = var6;
      this.f$5 = var7;
      this.f$6 = var8;
      this.f$7 = var9;
      this.f$8 = var10;
      this.f$9 = var11;
      this.f$10 = var12;
      this.f$11 = var13;
      this.f$12 = var14;
   }

   public final void run(TLObject var1, TLRPC.TL_error var2) {
      this.f$0.lambda$loadMessagesInternal$102$MessagesController(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9, this.f$10, this.f$11, this.f$12, var1, var2);
   }
}
