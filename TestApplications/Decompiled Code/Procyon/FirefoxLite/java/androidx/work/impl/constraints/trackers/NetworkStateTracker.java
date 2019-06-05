// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.trackers;

import android.net.Network;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.content.IntentFilter;
import android.net.ConnectivityManager$NetworkCallback;
import android.net.NetworkInfo;
import android.support.v4.net.ConnectivityManagerCompat;
import android.net.NetworkCapabilities;
import android.os.Build$VERSION;
import android.content.Context;
import androidx.work.Logger;
import android.net.ConnectivityManager;
import androidx.work.impl.constraints.NetworkState;

public class NetworkStateTracker extends ConstraintTracker<NetworkState>
{
    static final String TAG;
    private NetworkStateBroadcastReceiver mBroadcastReceiver;
    private final ConnectivityManager mConnectivityManager;
    private NetworkStateCallback mNetworkCallback;
    
    static {
        TAG = Logger.tagWithPrefix("NetworkStateTracker");
    }
    
    public NetworkStateTracker(final Context context) {
        super(context);
        this.mConnectivityManager = (ConnectivityManager)this.mAppContext.getSystemService("connectivity");
        if (isNetworkCallbackSupported()) {
            this.mNetworkCallback = new NetworkStateCallback();
        }
        else {
            this.mBroadcastReceiver = new NetworkStateBroadcastReceiver();
        }
    }
    
    private boolean isActiveNetworkValidated() {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = false;
        if (sdk_INT < 23) {
            return false;
        }
        final NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mConnectivityManager.getActiveNetwork());
        boolean b2 = b;
        if (networkCapabilities != null) {
            b2 = b;
            if (networkCapabilities.hasCapability(16)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    private static boolean isNetworkCallbackSupported() {
        return Build$VERSION.SDK_INT >= 24;
    }
    
    NetworkState getActiveNetworkState() {
        final NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        boolean b = true;
        final boolean b2 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        final boolean activeNetworkValidated = this.isActiveNetworkValidated();
        final boolean activeNetworkMetered = ConnectivityManagerCompat.isActiveNetworkMetered(this.mConnectivityManager);
        if (activeNetworkInfo == null || activeNetworkInfo.isRoaming()) {
            b = false;
        }
        return new NetworkState(b2, activeNetworkValidated, activeNetworkMetered, b);
    }
    
    @Override
    public NetworkState getInitialState() {
        return this.getActiveNetworkState();
    }
    
    @Override
    public void startTracking() {
        if (isNetworkCallbackSupported()) {
            Logger.get().debug(NetworkStateTracker.TAG, "Registering network callback", new Throwable[0]);
            this.mConnectivityManager.registerDefaultNetworkCallback((ConnectivityManager$NetworkCallback)this.mNetworkCallback);
        }
        else {
            Logger.get().debug(NetworkStateTracker.TAG, "Registering broadcast receiver", new Throwable[0]);
            this.mAppContext.registerReceiver((BroadcastReceiver)this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
    }
    
    @Override
    public void stopTracking() {
        if (isNetworkCallbackSupported()) {
            try {
                Logger.get().debug(NetworkStateTracker.TAG, "Unregistering network callback", new Throwable[0]);
                this.mConnectivityManager.unregisterNetworkCallback((ConnectivityManager$NetworkCallback)this.mNetworkCallback);
            }
            catch (IllegalArgumentException ex) {
                Logger.get().error(NetworkStateTracker.TAG, "Received exception while unregistering network callback", ex);
            }
        }
        else {
            Logger.get().debug(NetworkStateTracker.TAG, "Unregistering broadcast receiver", new Throwable[0]);
            this.mAppContext.unregisterReceiver((BroadcastReceiver)this.mBroadcastReceiver);
        }
    }
    
    private class NetworkStateBroadcastReceiver extends BroadcastReceiver
    {
        NetworkStateBroadcastReceiver() {
        }
        
        public void onReceive(final Context context, final Intent intent) {
            if (intent != null && intent.getAction() != null) {
                if (intent.getAction().equals("android.net.conn.CONNECTIVITY_CHANGE")) {
                    Logger.get().debug(NetworkStateTracker.TAG, "Network broadcast received", new Throwable[0]);
                    NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
                }
            }
        }
    }
    
    private class NetworkStateCallback extends ConnectivityManager$NetworkCallback
    {
        NetworkStateCallback() {
        }
        
        public void onCapabilitiesChanged(final Network network, final NetworkCapabilities networkCapabilities) {
            Logger.get().debug(NetworkStateTracker.TAG, String.format("Network capabilities changed: %s", networkCapabilities), new Throwable[0]);
            NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
        }
        
        public void onLost(final Network network) {
            Logger.get().debug(NetworkStateTracker.TAG, "Network connection lost", new Throwable[0]);
            NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
        }
    }
}
