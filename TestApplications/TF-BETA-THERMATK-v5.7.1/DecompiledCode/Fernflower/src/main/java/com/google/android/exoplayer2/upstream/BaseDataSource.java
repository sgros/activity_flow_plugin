package com.google.android.exoplayer2.upstream;

import com.google.android.exoplayer2.util.Util;
import java.util.ArrayList;
import java.util.Map;

public abstract class BaseDataSource implements DataSource {
   private DataSpec dataSpec;
   private final boolean isNetwork;
   private int listenerCount;
   private final ArrayList listeners;

   protected BaseDataSource(boolean var1) {
      this.isNetwork = var1;
      this.listeners = new ArrayList(1);
   }

   public final void addTransferListener(TransferListener var1) {
      if (!this.listeners.contains(var1)) {
         this.listeners.add(var1);
         ++this.listenerCount;
      }

   }

   protected final void bytesTransferred(int var1) {
      DataSpec var2 = this.dataSpec;
      Util.castNonNull(var2);
      var2 = (DataSpec)var2;

      for(int var3 = 0; var3 < this.listenerCount; ++var3) {
         ((TransferListener)this.listeners.get(var3)).onBytesTransferred(this, var2, this.isNetwork, var1);
      }

   }

   // $FF: synthetic method
   public Map getResponseHeaders() {
      return DataSource$_CC.$default$getResponseHeaders(this);
   }

   protected final void transferEnded() {
      DataSpec var1 = this.dataSpec;
      Util.castNonNull(var1);
      var1 = (DataSpec)var1;

      for(int var2 = 0; var2 < this.listenerCount; ++var2) {
         ((TransferListener)this.listeners.get(var2)).onTransferEnd(this, var1, this.isNetwork);
      }

      this.dataSpec = null;
   }

   protected final void transferInitializing(DataSpec var1) {
      for(int var2 = 0; var2 < this.listenerCount; ++var2) {
         ((TransferListener)this.listeners.get(var2)).onTransferInitializing(this, var1, this.isNetwork);
      }

   }

   protected final void transferStarted(DataSpec var1) {
      this.dataSpec = var1;

      for(int var2 = 0; var2 < this.listenerCount; ++var2) {
         ((TransferListener)this.listeners.get(var2)).onTransferStart(this, var1, this.isNetwork);
      }

   }
}
