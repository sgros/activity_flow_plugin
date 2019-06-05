package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.NetworkType;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class NetworkUnmeteredController extends ConstraintController<NetworkState> {
    public NetworkUnmeteredController(Context context) {
        super(Trackers.getInstance(context).getNetworkStateTracker());
    }

    /* Access modifiers changed, original: 0000 */
    public boolean hasConstraint(WorkSpec workSpec) {
        return workSpec.constraints.getRequiredNetworkType() == NetworkType.UNMETERED;
    }

    /* Access modifiers changed, original: 0000 */
    public boolean isConstrained(NetworkState networkState) {
        return !networkState.isConnected() || networkState.isMetered();
    }
}
