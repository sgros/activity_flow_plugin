package androidx.work.impl.constraints.controllers;

import android.content.Context;
import androidx.work.NetworkType;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class NetworkUnmeteredController extends ConstraintController {
   public NetworkUnmeteredController(Context var1) {
      super(Trackers.getInstance(var1).getNetworkStateTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      boolean var2;
      if (var1.constraints.getRequiredNetworkType() == NetworkType.UNMETERED) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean isConstrained(NetworkState var1) {
      boolean var2;
      if (var1.isConnected() && !var1.isMetered()) {
         var2 = false;
      } else {
         var2 = true;
      }

      return var2;
   }
}
