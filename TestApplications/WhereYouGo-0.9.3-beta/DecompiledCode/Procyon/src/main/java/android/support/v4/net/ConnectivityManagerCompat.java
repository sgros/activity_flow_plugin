// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.net;

import android.support.annotation.RestrictTo;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Annotation;
import android.net.NetworkInfo;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.os.Build$VERSION;

public final class ConnectivityManagerCompat
{
    private static final ConnectivityManagerCompatImpl IMPL;
    public static final int RESTRICT_BACKGROUND_STATUS_DISABLED = 1;
    public static final int RESTRICT_BACKGROUND_STATUS_ENABLED = 3;
    public static final int RESTRICT_BACKGROUND_STATUS_WHITELISTED = 2;
    
    static {
        if (Build$VERSION.SDK_INT >= 24) {
            IMPL = (ConnectivityManagerCompatImpl)new Api24ConnectivityManagerCompatImpl();
        }
        else if (Build$VERSION.SDK_INT >= 16) {
            IMPL = (ConnectivityManagerCompatImpl)new JellyBeanConnectivityManagerCompatImpl();
        }
        else if (Build$VERSION.SDK_INT >= 13) {
            IMPL = (ConnectivityManagerCompatImpl)new HoneycombMR2ConnectivityManagerCompatImpl();
        }
        else {
            IMPL = (ConnectivityManagerCompatImpl)new BaseConnectivityManagerCompatImpl();
        }
    }
    
    private ConnectivityManagerCompat() {
    }
    
    public static NetworkInfo getNetworkInfoFromBroadcast(final ConnectivityManager connectivityManager, final Intent intent) {
        final NetworkInfo networkInfo = (NetworkInfo)intent.getParcelableExtra("networkInfo");
        NetworkInfo networkInfo2;
        if (networkInfo != null) {
            networkInfo2 = connectivityManager.getNetworkInfo(networkInfo.getType());
        }
        else {
            networkInfo2 = null;
        }
        return networkInfo2;
    }
    
    public static int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
        return ConnectivityManagerCompat.IMPL.getRestrictBackgroundStatus(connectivityManager);
    }
    
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        return ConnectivityManagerCompat.IMPL.isActiveNetworkMetered(connectivityManager);
    }
    
    static class Api24ConnectivityManagerCompatImpl extends JellyBeanConnectivityManagerCompatImpl
    {
        @Override
        public int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatApi24.getRestrictBackgroundStatus(connectivityManager);
        }
    }
    
    static class BaseConnectivityManagerCompatImpl implements ConnectivityManagerCompatImpl
    {
        @Override
        public int getRestrictBackgroundStatus(final ConnectivityManager connectivityManager) {
            return 3;
        }
        
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            final boolean b = true;
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            boolean b2;
            if (activeNetworkInfo == null) {
                b2 = b;
            }
            else {
                b2 = b;
                switch (activeNetworkInfo.getType()) {
                    case 1: {
                        b2 = false;
                    }
                    case 0:
                    case 2:
                    case 3:
                    case 4:
                    case 5:
                    case 6: {
                        break;
                    }
                    default: {
                        b2 = b;
                        break;
                    }
                }
            }
            return b2;
        }
    }
    
    interface ConnectivityManagerCompatImpl
    {
        int getRestrictBackgroundStatus(final ConnectivityManager p0);
        
        boolean isActiveNetworkMetered(final ConnectivityManager p0);
    }
    
    static class HoneycombMR2ConnectivityManagerCompatImpl extends BaseConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatHoneycombMR2.isActiveNetworkMetered(connectivityManager);
        }
    }
    
    static class JellyBeanConnectivityManagerCompatImpl extends HoneycombMR2ConnectivityManagerCompatImpl
    {
        @Override
        public boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
            return ConnectivityManagerCompatJellyBean.isActiveNetworkMetered(connectivityManager);
        }
    }
    
    @Retention(RetentionPolicy.SOURCE)
    @RestrictTo({ RestrictTo.Scope.LIBRARY_GROUP })
    public @interface RestrictBackgroundStatus {
    }
}
