package com.bumptech.glide.manager;

import android.content.Context;
import android.support.p001v4.content.ContextCompat;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;

public class DefaultConnectivityMonitorFactory implements ConnectivityMonitorFactory {
    public ConnectivityMonitor build(Context context, ConnectivityListener connectivityListener) {
        return (ContextCompat.checkSelfPermission(context, "android.permission.ACCESS_NETWORK_STATE") == 0 ? 1 : null) != null ? new DefaultConnectivityMonitor(context, connectivityListener) : new NullConnectivityMonitor();
    }
}
