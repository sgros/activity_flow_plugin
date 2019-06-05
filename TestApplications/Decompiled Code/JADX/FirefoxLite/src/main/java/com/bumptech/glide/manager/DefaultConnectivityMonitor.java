package com.bumptech.glide.manager;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import com.bumptech.glide.manager.ConnectivityMonitor.ConnectivityListener;

class DefaultConnectivityMonitor implements ConnectivityMonitor {
    private final BroadcastReceiver connectivityReceiver = new C04091();
    private final Context context;
    boolean isConnected;
    private boolean isRegistered;
    final ConnectivityListener listener;

    /* renamed from: com.bumptech.glide.manager.DefaultConnectivityMonitor$1 */
    class C04091 extends BroadcastReceiver {
        C04091() {
        }

        public void onReceive(Context context, Intent intent) {
            boolean z = DefaultConnectivityMonitor.this.isConnected;
            DefaultConnectivityMonitor.this.isConnected = DefaultConnectivityMonitor.this.isConnected(context);
            if (z != DefaultConnectivityMonitor.this.isConnected) {
                DefaultConnectivityMonitor.this.listener.onConnectivityChanged(DefaultConnectivityMonitor.this.isConnected);
            }
        }
    }

    public void onDestroy() {
    }

    public DefaultConnectivityMonitor(Context context, ConnectivityListener connectivityListener) {
        this.context = context.getApplicationContext();
        this.listener = connectivityListener;
    }

    private void register() {
        if (!this.isRegistered) {
            this.isConnected = isConnected(this.context);
            this.context.registerReceiver(this.connectivityReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            this.isRegistered = true;
        }
    }

    private void unregister() {
        if (this.isRegistered) {
            this.context.unregisterReceiver(this.connectivityReceiver);
            this.isRegistered = false;
        }
    }

    /* Access modifiers changed, original: 0000 */
    @SuppressLint({"MissingPermission"})
    public boolean isConnected(Context context) {
        NetworkInfo activeNetworkInfo = ((ConnectivityManager) context.getSystemService("connectivity")).getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }

    public void onStart() {
        register();
    }

    public void onStop() {
        unregister();
    }
}
