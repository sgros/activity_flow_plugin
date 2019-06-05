// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.controllers;

import androidx.work.NetworkType;
import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import androidx.work.impl.constraints.trackers.Trackers;
import android.content.Context;
import androidx.work.impl.constraints.NetworkState;

public class NetworkUnmeteredController extends ConstraintController<NetworkState>
{
    public NetworkUnmeteredController(final Context context) {
        super(Trackers.getInstance(context).getNetworkStateTracker());
    }
    
    @Override
    boolean hasConstraint(final WorkSpec workSpec) {
        return workSpec.constraints.getRequiredNetworkType() == NetworkType.UNMETERED;
    }
    
    @Override
    boolean isConstrained(final NetworkState networkState) {
        return !networkState.isConnected() || networkState.isMetered();
    }
}
