package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class BatteryNotLowController extends ConstraintController {
   public BatteryNotLowController(Context var1) {
      super(Trackers.getInstance(var1).getBatteryNotLowTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      return var1.constraints.requiresBatteryNotLow();
   }

   boolean isConstrained(Boolean var1) {
      return var1 ^ true;
   }
}