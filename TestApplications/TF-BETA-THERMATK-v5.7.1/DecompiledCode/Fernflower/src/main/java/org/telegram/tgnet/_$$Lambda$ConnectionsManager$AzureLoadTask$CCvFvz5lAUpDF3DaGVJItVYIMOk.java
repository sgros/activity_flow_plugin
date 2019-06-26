package org.telegram.tgnet;

// $FF: synthetic class
public final class _$$Lambda$ConnectionsManager$AzureLoadTask$CCvFvz5lAUpDF3DaGVJItVYIMOk implements Runnable {
   // $FF: synthetic field
   private final ConnectionsManager.AzureLoadTask f$0;
   // $FF: synthetic field
   private final NativeByteBuffer f$1;

   // $FF: synthetic method
   public _$$Lambda$ConnectionsManager$AzureLoadTask$CCvFvz5lAUpDF3DaGVJItVYIMOk(ConnectionsManager.AzureLoadTask var1, NativeByteBuffer var2) {
      this.f$0 = var1;
      this.f$1 = var2;
   }

   public final void run() {
      this.f$0.lambda$onPostExecute$0$ConnectionsManager$AzureLoadTask(this.f$1);
   }
}
