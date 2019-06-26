// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.content.Intent;
import android.content.Context;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import org.osmdroid.tileprovider.IRegisterReceiver;

public abstract class MapTileFileStorageProviderBase extends MapTileModuleProviderBase
{
    private MyBroadcastReceiver mBroadcastReceiver;
    private final IRegisterReceiver mRegisterReceiver;
    
    public MapTileFileStorageProviderBase(final IRegisterReceiver mRegisterReceiver, final int n, final int n2) {
        super(n, n2);
        this.mRegisterReceiver = mRegisterReceiver;
        this.mBroadcastReceiver = new MyBroadcastReceiver();
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("android.intent.action.MEDIA_MOUNTED");
        intentFilter.addAction("android.intent.action.MEDIA_UNMOUNTED");
        intentFilter.addDataScheme("file");
        mRegisterReceiver.registerReceiver(this.mBroadcastReceiver, intentFilter);
    }
    
    @Override
    public void detach() {
        final MyBroadcastReceiver mBroadcastReceiver = this.mBroadcastReceiver;
        if (mBroadcastReceiver != null) {
            this.mRegisterReceiver.unregisterReceiver(mBroadcastReceiver);
            this.mBroadcastReceiver = null;
        }
        super.detach();
    }
    
    protected void onMediaMounted() {
    }
    
    protected void onMediaUnmounted() {
    }
    
    private class MyBroadcastReceiver extends BroadcastReceiver
    {
        public void onReceive(final Context context, final Intent intent) {
            final String action = intent.getAction();
            if ("android.intent.action.MEDIA_MOUNTED".equals(action)) {
                MapTileFileStorageProviderBase.this.onMediaMounted();
            }
            else if ("android.intent.action.MEDIA_UNMOUNTED".equals(action)) {
                MapTileFileStorageProviderBase.this.onMediaUnmounted();
            }
        }
    }
}
