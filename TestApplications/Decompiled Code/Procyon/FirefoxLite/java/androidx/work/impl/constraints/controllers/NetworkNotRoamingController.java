// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.controllers;

import android.os.Build$VERSION;
import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import androidx.work.impl.constraints.trackers.Trackers;
import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.NetworkState;

public class NetworkNotRoamingController extends ConstraintController<NetworkState>
{
    private static final String TAG;
    
    static {
        TAG = Logger.tagWithPrefix("NetworkNotRoamingCtrlr");
    }
    
    public NetworkNotRoamingController(final Context context) {
        super(Trackers.getInstance(context).getNetworkStateTracker());
    }
    
    @Override
    boolean hasConstraint(final WorkSpec workSpec) {
        return workSpec.constraints.getRequiredNetworkType() == NetworkType.NOT_ROAMING;
    }
    
    @Override
    boolean isConstrained(final NetworkState networkState) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        if (sdk_INT < 24) {
            Logger.get().debug(NetworkNotRoamingController.TAG, "Not-roaming network constraint is not supported before API 24, only checking for connected state.", new Throwable[0]);
            return networkState.isConnected() ^ true;
        }
        boolean b2 = b;
        if (networkState.isConnected()) {
            b2 = (!networkState.isNotRoaming() && b);
        }
        return b2;
    }
}
