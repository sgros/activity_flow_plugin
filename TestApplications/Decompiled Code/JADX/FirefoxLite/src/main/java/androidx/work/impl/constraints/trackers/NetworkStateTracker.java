package androidx.work.impl.constraints.trackers;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.ConnectivityManager.NetworkCallback;
import android.net.Network;
import android.net.NetworkCapabilities;
import android.net.NetworkInfo;
import android.os.Build.VERSION;
import android.support.p001v4.net.ConnectivityManagerCompat;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;

public class NetworkStateTracker extends ConstraintTracker<NetworkState> {
    static final String TAG = Logger.tagWithPrefix("NetworkStateTracker");
    private NetworkStateBroadcastReceiver mBroadcastReceiver;
    private final ConnectivityManager mConnectivityManager = ((ConnectivityManager) this.mAppContext.getSystemService("connectivity"));
    private NetworkStateCallback mNetworkCallback;

    private class NetworkStateBroadcastReceiver extends BroadcastReceiver {
        NetworkStateBroadcastReceiver() {
        }

        /* JADX WARNING: Missing block: B:7:0x002f, code skipped:
            return;
     */
        public void onReceive(android.content.Context r3, android.content.Intent r4) {
            /*
            r2 = this;
            if (r4 == 0) goto L_0x002f;
        L_0x0002:
            r3 = r4.getAction();
            if (r3 != 0) goto L_0x0009;
        L_0x0008:
            goto L_0x002f;
        L_0x0009:
            r3 = r4.getAction();
            r4 = "android.net.conn.CONNECTIVITY_CHANGE";
            r3 = r3.equals(r4);
            if (r3 == 0) goto L_0x002e;
        L_0x0015:
            r3 = androidx.work.Logger.get();
            r4 = androidx.work.impl.constraints.trackers.NetworkStateTracker.TAG;
            r0 = "Network broadcast received";
            r1 = 0;
            r1 = new java.lang.Throwable[r1];
            r3.debug(r4, r0, r1);
            r3 = androidx.work.impl.constraints.trackers.NetworkStateTracker.this;
            r4 = androidx.work.impl.constraints.trackers.NetworkStateTracker.this;
            r4 = r4.getActiveNetworkState();
            r3.setState(r4);
        L_0x002e:
            return;
        L_0x002f:
            return;
            */
            throw new UnsupportedOperationException("Method not decompiled: androidx.work.impl.constraints.trackers.NetworkStateTracker$NetworkStateBroadcastReceiver.onReceive(android.content.Context, android.content.Intent):void");
        }
    }

    private class NetworkStateCallback extends NetworkCallback {
        NetworkStateCallback() {
        }

        public void onCapabilitiesChanged(Network network, NetworkCapabilities networkCapabilities) {
            Logger.get().debug(NetworkStateTracker.TAG, String.format("Network capabilities changed: %s", new Object[]{networkCapabilities}), new Throwable[0]);
            NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
        }

        public void onLost(Network network) {
            Logger.get().debug(NetworkStateTracker.TAG, "Network connection lost", new Throwable[0]);
            NetworkStateTracker.this.setState(NetworkStateTracker.this.getActiveNetworkState());
        }
    }

    public NetworkStateTracker(Context context) {
        super(context);
        if (isNetworkCallbackSupported()) {
            this.mNetworkCallback = new NetworkStateCallback();
        } else {
            this.mBroadcastReceiver = new NetworkStateBroadcastReceiver();
        }
    }

    public NetworkState getInitialState() {
        return getActiveNetworkState();
    }

    public void startTracking() {
        if (isNetworkCallbackSupported()) {
            Logger.get().debug(TAG, "Registering network callback", new Throwable[0]);
            this.mConnectivityManager.registerDefaultNetworkCallback(this.mNetworkCallback);
            return;
        }
        Logger.get().debug(TAG, "Registering broadcast receiver", new Throwable[0]);
        this.mAppContext.registerReceiver(this.mBroadcastReceiver, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
    }

    public void stopTracking() {
        if (isNetworkCallbackSupported()) {
            try {
                Logger.get().debug(TAG, "Unregistering network callback", new Throwable[0]);
                this.mConnectivityManager.unregisterNetworkCallback(this.mNetworkCallback);
                return;
            } catch (IllegalArgumentException e) {
                Logger.get().error(TAG, "Received exception while unregistering network callback", e);
                return;
            }
        }
        Logger.get().debug(TAG, "Unregistering broadcast receiver", new Throwable[0]);
        this.mAppContext.unregisterReceiver(this.mBroadcastReceiver);
    }

    private static boolean isNetworkCallbackSupported() {
        return VERSION.SDK_INT >= 24;
    }

    /* Access modifiers changed, original: 0000 */
    public NetworkState getActiveNetworkState() {
        NetworkInfo activeNetworkInfo = this.mConnectivityManager.getActiveNetworkInfo();
        boolean z = true;
        boolean z2 = activeNetworkInfo != null && activeNetworkInfo.isConnected();
        boolean isActiveNetworkValidated = isActiveNetworkValidated();
        boolean isActiveNetworkMetered = ConnectivityManagerCompat.isActiveNetworkMetered(this.mConnectivityManager);
        if (activeNetworkInfo == null || activeNetworkInfo.isRoaming()) {
            z = false;
        }
        return new NetworkState(z2, isActiveNetworkValidated, isActiveNetworkMetered, z);
    }

    private boolean isActiveNetworkValidated() {
        boolean z = false;
        if (VERSION.SDK_INT < 23) {
            return false;
        }
        NetworkCapabilities networkCapabilities = this.mConnectivityManager.getNetworkCapabilities(this.mConnectivityManager.getActiveNetwork());
        if (networkCapabilities != null && networkCapabilities.hasCapability(16)) {
            z = true;
        }
        return z;
    }
}
