// 
// Decompiled by Procyon v0.5.34
// 

package androidx.work.impl.constraints;

import androidx.work.impl.model.WorkSpec;
import java.util.Iterator;
import java.util.ArrayList;
import java.util.List;
import androidx.work.impl.constraints.controllers.NetworkMeteredController;
import androidx.work.impl.constraints.controllers.NetworkNotRoamingController;
import androidx.work.impl.constraints.controllers.NetworkUnmeteredController;
import androidx.work.impl.constraints.controllers.NetworkConnectedController;
import androidx.work.impl.constraints.controllers.StorageNotLowController;
import androidx.work.impl.constraints.controllers.BatteryNotLowController;
import androidx.work.impl.constraints.controllers.BatteryChargingController;
import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.controllers.ConstraintController;

public class WorkConstraintsTracker implements OnConstraintUpdatedCallback
{
    private static final String TAG;
    private final WorkConstraintsCallback mCallback;
    private final ConstraintController[] mConstraintControllers;
    private final Object mLock;
    
    static {
        TAG = Logger.tagWithPrefix("WorkConstraintsTracker");
    }
    
    public WorkConstraintsTracker(Context applicationContext, final WorkConstraintsCallback mCallback) {
        applicationContext = applicationContext.getApplicationContext();
        this.mCallback = mCallback;
        this.mConstraintControllers = new ConstraintController[] { new BatteryChargingController(applicationContext), new BatteryNotLowController(applicationContext), new StorageNotLowController(applicationContext), new NetworkConnectedController(applicationContext), new NetworkUnmeteredController(applicationContext), new NetworkNotRoamingController(applicationContext), new NetworkMeteredController(applicationContext) };
        this.mLock = new Object();
    }
    
    public boolean areAllConstraintsMet(final String s) {
        synchronized (this.mLock) {
            for (final ConstraintController constraintController : this.mConstraintControllers) {
                if (constraintController.isWorkSpecConstrained(s)) {
                    Logger.get().debug(WorkConstraintsTracker.TAG, String.format("Work %s constrained by %s", s, constraintController.getClass().getSimpleName()), new Throwable[0]);
                    return false;
                }
            }
            return true;
        }
    }
    
    @Override
    public void onConstraintMet(final List<String> list) {
        synchronized (this.mLock) {
            final ArrayList<String> list2 = new ArrayList<String>();
            for (final String s : list) {
                if (this.areAllConstraintsMet(s)) {
                    Logger.get().debug(WorkConstraintsTracker.TAG, String.format("Constraints met for %s", s), new Throwable[0]);
                    list2.add(s);
                }
            }
            if (this.mCallback != null) {
                this.mCallback.onAllConstraintsMet(list2);
            }
        }
    }
    
    @Override
    public void onConstraintNotMet(final List<String> list) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.onAllConstraintsNotMet(list);
            }
        }
    }
    
    public void replace(final List<WorkSpec> list) {
        synchronized (this.mLock) {
            final ConstraintController[] mConstraintControllers = this.mConstraintControllers;
            final int length = mConstraintControllers.length;
            final int n = 0;
            for (int i = 0; i < length; ++i) {
                mConstraintControllers[i].setCallback(null);
            }
            final ConstraintController[] mConstraintControllers2 = this.mConstraintControllers;
            for (int length2 = mConstraintControllers2.length, j = 0; j < length2; ++j) {
                mConstraintControllers2[j].replace(list);
            }
            final ConstraintController[] mConstraintControllers3 = this.mConstraintControllers;
            for (int length3 = mConstraintControllers3.length, k = n; k < length3; ++k) {
                mConstraintControllers3[k].setCallback((ConstraintController.OnConstraintUpdatedCallback)this);
            }
        }
    }
    
    public void reset() {
        synchronized (this.mLock) {
            final ConstraintController[] mConstraintControllers = this.mConstraintControllers;
            for (int length = mConstraintControllers.length, i = 0; i < length; ++i) {
                mConstraintControllers[i].reset();
            }
        }
    }
}
