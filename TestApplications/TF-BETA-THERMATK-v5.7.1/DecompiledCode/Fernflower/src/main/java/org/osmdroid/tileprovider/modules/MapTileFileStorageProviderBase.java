package org.osmdroid.tileprovider.modules;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import org.osmdroid.tileprovider.IRegisterReceiver;

public abstract class MapTileFileStorageProviderBase extends MapTileModuleProviderBase {
   private MapTileFileStorageProviderBase.MyBroadcastReceiver mBroadcastReceiver;
   private final IRegisterReceiver mRegisterReceiver;

   public MapTileFileStorageProviderBase(IRegisterReceiver var1, int var2, int var3) {
      super(var2, var3);
      this.mRegisterReceiver = var1;
      this.mBroadcastReceiver = new MapTileFileStorageProviderBase.MyBroadcastReceiver();
      IntentFilter var4 = new IntentFilter();
      var4.addAction("android.intent.action.MEDIA_MOUNTED");
      var4.addAction("android.intent.action.MEDIA_UNMOUNTED");
      var4.addDataScheme("file");
      var1.registerReceiver(this.mBroadcastReceiver, var4);
   }

   public void detach() {
      MapTileFileStorageProviderBase.MyBroadcastReceiver var1 = this.mBroadcastReceiver;
      if (var1 != null) {
         this.mRegisterReceiver.unregisterReceiver(var1);
         this.mBroadcastReceiver = null;
      }

      super.detach();
   }

   protected void onMediaMounted() {
   }

   protected void onMediaUnmounted() {
   }

   private class MyBroadcastReceiver extends BroadcastReceiver {
      private MyBroadcastReceiver() {
      }

      // $FF: synthetic method
      MyBroadcastReceiver(Object var2) {
         this();
      }

      public void onReceive(Context var1, Intent var2) {
         String var3 = var2.getAction();
         if ("android.intent.action.MEDIA_MOUNTED".equals(var3)) {
            MapTileFileStorageProviderBase.this.onMediaMounted();
         } else if ("android.intent.action.MEDIA_UNMOUNTED".equals(var3)) {
            MapTileFileStorageProviderBase.this.onMediaUnmounted();
         }

      }
   }
}
