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
import androidx.work.impl.constraints.NetworkState;

public class NetworkConnectedController extends ConstraintController<NetworkState>
{
    public NetworkConnectedController(final Context context) {
        super(Trackers.getInstance(context).getNetworkStateTracker());
    }
    
    @Override
    boolean hasConstraint(final WorkSpec workSpec) {
        return workSpec.constraints.getRequiredNetworkType() == NetworkType.CONNECTED;
    }
    
    @Override
    boolean isConstrained(final NetworkState networkState) {
        final int sdk_INT = Build$VERSION.SDK_INT;
        final boolean b = true;
        if (sdk_INT >= 26) {
            boolean b2 = b;
            if (networkState.isConnected()) {
                b2 = (!networkState.isValidated() && b);
            }
            return b2;
        }
        return networkState.isConnected() ^ true;
    }
}
