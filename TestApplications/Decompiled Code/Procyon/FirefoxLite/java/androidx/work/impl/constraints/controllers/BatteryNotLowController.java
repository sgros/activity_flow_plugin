// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints.controllers;

import androidx.work.impl.model.WorkSpec;
import androidx.work.impl.constraints.trackers.ConstraintTracker;
import androidx.work.impl.constraints.trackers.Trackers;
import android.content.Context;

public class BatteryNotLowController extends ConstraintController<Boolean>
{
    public BatteryNotLowController(final Context context) {
        super(Trackers.getInstance(context).getBatteryNotLowTracker());
    }
    
    @Override
    boolean hasConstraint(final WorkSpec workSpec) {
        return workSpec.constraints.requiresBatteryNotLow();
    }
    
    @Override
    boolean isConstrained(final Boolean b) {
        return b ^ true;
    }
}
