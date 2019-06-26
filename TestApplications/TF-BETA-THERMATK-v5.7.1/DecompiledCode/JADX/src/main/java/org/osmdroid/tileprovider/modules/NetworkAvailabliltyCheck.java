package org.osmdroid.tileprovider.modules;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Build;
import android.os.Build.VERSION;

public class NetworkAvailabliltyCheck implements INetworkAvailablityCheck {
    private final ConnectivityManager mConnectionManager;
    private final boolean mHasNetworkStatePermission;
    private final boolean mIsX86 = "Android-x86".equalsIgnoreCase(Build.BRAND);

    public NetworkAvailabliltyCheck(Context context) {
        this.mConnectionManager = (ConnectivityManager) context.getSystemService("connectivity");
        this.mHasNetworkStatePermission = context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0;
    }

    public boolean getNetworkAvailable() {
        boolean z = true;
        if (!this.mHasNetworkStatePermission) {
            return true;
        }
        NetworkInfo activeNetworkInfo = this.mConnectionManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        if (activeNetworkInfo.isConnected()) {
            return true;
        }
        if (VERSION.SDK_INT <= 13) {
            return false;
        }
        if (!(this.mIsX86 && activeNetworkInfo.getType() == 9)) {
            z = false;
        }
        return z;
    }
}
