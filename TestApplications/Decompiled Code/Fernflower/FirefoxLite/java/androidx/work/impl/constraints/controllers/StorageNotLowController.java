package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class StorageNotLowController extends ConstraintController {
   public StorageNotLowController(Context var1) {
      super(Trackers.getInstance(var1).getStorageNotLowTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      return var1.constraints.requiresStorageNotLow();
   }

   boolean isConstrained(Boolean var1) {
      return var1 ^ true;
   }
}
