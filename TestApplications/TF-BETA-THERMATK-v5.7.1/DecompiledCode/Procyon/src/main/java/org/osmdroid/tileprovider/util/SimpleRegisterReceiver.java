// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.util;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;
import android.content.Context;
import org.osmdroid.tileprovider.IRegisterReceiver;

public class SimpleRegisterReceiver implements IRegisterReceiver
{
    private Context mContext;
    
    public SimpleRegisterReceiver(final Context mContext) {
        this.mContext = mContext;
    }
    
    @Override
    public void destroy() {
        this.mContext = null;
    }
    
    @Override
    public Intent registerReceiver(final BroadcastReceiver broadcastReceiver, final IntentFilter intentFilter) {
        return this.mContext.registerReceiver(broadcastReceiver, intentFilter);
    }
    
    @Override
    public void unregisterReceiver(final BroadcastReceiver broadcastReceiver) {
        this.mContext.unregisterReceiver(broadcastReceiver);
    }
}
