package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class BatteryChargingController extends ConstraintController {
   public BatteryChargingController(Context var1) {
      super(Trackers.getInstance(var1).getBatteryChargingTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      return var1.constraints.requiresCharging();
   }

   boolean isConstrained(Boolean var1) {
      return var1 ^ true;
   }
}
