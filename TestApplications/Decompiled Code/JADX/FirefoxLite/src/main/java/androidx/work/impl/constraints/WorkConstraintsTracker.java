package androidx.work.impl.constraints;

import android.content.Context;
import androidx.work.Logger;
import androidx.work.impl.constraints.controllers.BatteryChargingController;
import androidx.work.impl.constraints.controllers.BatteryNotLowController;
import androidx.work.impl.constraints.controllers.ConstraintController;
import androidx.work.impl.constraints.controllers.ConstraintController.OnConstraintUpdatedCallback;
import androidx.work.impl.constraints.controllers.NetworkConnectedController;
import androidx.work.impl.constraints.controllers.NetworkMeteredController;
import androidx.work.impl.constraints.controllers.NetworkNotRoamingController;
import androidx.work.impl.constraints.controllers.NetworkUnmeteredController;
import androidx.work.impl.constraints.controllers.StorageNotLowController;
import androidx.work.impl.model.WorkSpec;
import java.util.ArrayList;
import java.util.List;

public class WorkConstraintsTracker implements OnConstraintUpdatedCallback {
    private static final String TAG = Logger.tagWithPrefix("WorkConstraintsTracker");
    private final WorkConstraintsCallback mCallback;
    private final ConstraintController[] mConstraintControllers;
    private final Object mLock = new Object();

    public WorkConstraintsTracker(Context context, WorkConstraintsCallback workConstraintsCallback) {
        context = context.getApplicationContext();
        this.mCallback = workConstraintsCallback;
        this.mConstraintControllers = new ConstraintController[]{new BatteryChargingController(context), new BatteryNotLowController(context), new StorageNotLowController(context), new NetworkConnectedController(context), new NetworkUnmeteredController(context), new NetworkNotRoamingController(context), new NetworkMeteredController(context)};
    }

    public void replace(List<WorkSpec> list) {
        synchronized (this.mLock) {
            for (ConstraintController callback : this.mConstraintControllers) {
                callback.setCallback(null);
            }
            for (ConstraintController callback2 : this.mConstraintControllers) {
                callback2.replace(list);
            }
            for (ConstraintController callback3 : this.mConstraintControllers) {
                callback3.setCallback(this);
            }
        }
    }

    public void reset() {
        synchronized (this.mLock) {
            for (ConstraintController reset : this.mConstraintControllers) {
                reset.reset();
            }
        }
    }

    public boolean areAllConstraintsMet(String str) {
        synchronized (this.mLock) {
            for (ConstraintController isWorkSpecConstrained : this.mConstraintControllers) {
                if (isWorkSpecConstrained.isWorkSpecConstrained(str)) {
                    Logger.get().debug(TAG, String.format("Work %s constrained by %s", new Object[]{str, r1[r4].getClass().getSimpleName()}), new Throwable[0]);
                    return false;
                }
            }
            return true;
        }
    }

    public void onConstraintMet(List<String> list) {
        synchronized (this.mLock) {
            ArrayList arrayList = new ArrayList();
            for (String str : list) {
                if (areAllConstraintsMet(str)) {
                    Logger.get().debug(TAG, String.format("Constraints met for %s", new Object[]{str}), new Throwable[0]);
                    arrayList.add(str);
                }
            }
            if (this.mCallback != null) {
                this.mCallback.onAllConstraintsMet(arrayList);
            }
        }
    }

    public void onConstraintNotMet(List<String> list) {
        synchronized (this.mLock) {
            if (this.mCallback != null) {
                this.mCallback.onAllConstraintsNotMet(list);
            }
        }
    }
}
