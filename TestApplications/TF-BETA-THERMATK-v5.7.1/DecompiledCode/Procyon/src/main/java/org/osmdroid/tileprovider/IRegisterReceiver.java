// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider;

import android.content.Intent;
import android.content.IntentFilter;
import android.content.BroadcastReceiver;

public interface IRegisterReceiver
{
    void destroy();
    
    Intent registerReceiver(final BroadcastReceiver p0, final IntentFilter p1);
    
    void unregisterReceiver(final BroadcastReceiver p0);
}
