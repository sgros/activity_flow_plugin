package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$DnsTxtLoadTask$Y_uiONB1DXfH_CyjIpbAd_79WxM implements Runnable {
   // $FF: synthetic field
   private final ConnectionsManager.DnsTxtLoadTask f$0;
   // $FF: synthetic field
   private final NativeByteBuffer f$1;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$DnsTxtLoadTask$Y_uiONB1DXfH_CyjIpbAd_79WxM(ConnectionsManager.DnsTxtLoadTask var1, NativeByteBuffer var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onPostExecute$1$ConnectionsManager$DnsTxtLoadTask(this.f$1);
   }
}
