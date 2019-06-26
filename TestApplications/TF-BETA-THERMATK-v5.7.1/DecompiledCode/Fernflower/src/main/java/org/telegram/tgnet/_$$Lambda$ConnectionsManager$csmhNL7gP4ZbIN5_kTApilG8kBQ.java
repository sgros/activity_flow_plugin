package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$csmhNL7gP4ZbIN5_kTApilG8kBQ implements Runnable {
   // $FF: synthetic field
   private final ConnectionsManager f$0;
   // $FF: synthetic field
   private final TLObject f$1;
   // $FF: synthetic field
   private final int f$2;
   // $FF: synthetic field
   private final RequestDelegate f$3;
   // $FF: synthetic field
   private final QuickAckDelegate f$4;
   // $FF: synthetic field
   private final WriteToSocketDelegate f$5;
   // $FF: synthetic field
   private final int f$6;
   // $FF: synthetic field
   private final int f$7;
   // $FF: synthetic field
   private final int f$8;
   // $FF: synthetic field
   private final boolean f$9;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$csmhNL7gP4ZbIN5_kTApilG8kBQ(ConnectionsManager var1, TLObject var2, int var3, RequestDelegate var4, QuickAckDelegate var5, WriteToSocketDelegate var6, int var7, int var8, int var9, boolean var10) {
      this.f$0 = var1;
      this.f$1 = var2;
      this.f$2 = var3;
      this.f$3 = var4;
      this.f$4 = var5;
      this.f$5 = var6;
      this.f$6 = var7;
      this.f$7 = var8;
      this.f$8 = var9;
      this.f$9 = var10;
   }

   public final void run() {
      this.f$0.lambda$sendRequest$2$ConnectionsManager(this.f$1, this.f$2, this.f$3, this.f$4, this.f$5, this.f$6, this.f$7, this.f$8, this.f$9);
   }
}
