// 
// Decompiled by Procyon v0.5.34
// 

package org.osmdroid.tileprovider.modules;

import android.net.NetworkInfo;
import android.os.Build$VERSION;
import android.os.Build;
import android.content.Context;
import android.net.ConnectivityManager;

public class NetworkAvailabliltyCheck implements INetworkAvailablityCheck
{
    private final ConnectivityManager mConnectionManager;
    private final boolean mHasNetworkStatePermission;
    private final boolean mIsX86;
    
    public NetworkAvailabliltyCheck(final Context context) {
        this.mConnectionManager = (ConnectivityManager)context.getSystemService("connectivity");
        this.mIsX86 = "Android-x86".equalsIgnoreCase(Build.BRAND);
        this.mHasNetworkStatePermission = (context.getPackageManager().checkPermission("android.permission.ACCESS_NETWORK_STATE", context.getPackageName()) == 0);
    }
    
    @Override
    public boolean getNetworkAvailable() {
        final boolean mHasNetworkStatePermission = this.mHasNetworkStatePermission;
        boolean b = true;
        if (!mHasNetworkStatePermission) {
            return true;
        }
        final NetworkInfo activeNetworkInfo = this.mConnectionManager.getActiveNetworkInfo();
        if (activeNetworkInfo == null) {
            return false;
        }
        if (activeNetworkInfo.isConnected()) {
            return true;
        }
        if (Build$VERSION.SDK_INT > 13) {
            if (!this.mIsX86 || activeNetworkInfo.getType() != 9) {
                b = false;
            }
            return b;
        }
        return false;
    }
}
