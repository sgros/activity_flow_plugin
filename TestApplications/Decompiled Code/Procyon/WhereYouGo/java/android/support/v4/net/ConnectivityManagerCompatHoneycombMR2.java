// 
// Decompiled by Procyon v0.5.34
// 

package android.support.v4.net;

import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.support.annotation.RequiresApi;
import android.annotation.TargetApi;

@TargetApi(13)
@RequiresApi(13)
class ConnectivityManagerCompatHoneycombMR2
{
    public static boolean isActiveNetworkMetered(final ConnectivityManager connectivityManager) {
        final boolean b = true;
        final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        boolean b2;
        if (activeNetworkInfo == null) {
            b2 = b;
        }
        else {
            b2 = b;
            switch (activeNetworkInfo.getType()) {
                case 1:
                case 7:
                case 9: {
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
