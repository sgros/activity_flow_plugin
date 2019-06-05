// 
// Decompiled by Procyon v0.5.34
// 

package com.bumptech.glide.manager;

import android.support.v4.content.ContextCompat;
import android.content.Context;

public class DefaultConnectivityMonitorFactory implements ConnectivityMonitorFactory
{
    @Override
    public ConnectivityMonitor build(final Context context, final ConnectivityMonitor.ConnectivityListener connectivityListener) {
        ConnectivityMonitor connectivityMonitor;
        if (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE") == 0) {
            connectivityMonitor = new DefaultConnectivityMonitor(context, connectivityListener);
        }
        else {
            connectivityMonitor = new NullConnectivityMonitor();
        }
        return connectivityMonitor;
    }
}
