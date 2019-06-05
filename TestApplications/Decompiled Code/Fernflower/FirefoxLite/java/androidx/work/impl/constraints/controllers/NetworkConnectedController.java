package androidx.work.impl.constraints.controllers;

import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.NetworkType;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class NetworkConnectedController extends ConstraintController {
   public NetworkConnectedController(Context var1) {
      super(Trackers.getInstance(var1).getNetworkStateTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      boolean var2;
      if (var1.constraints.getRequiredNetworkType() == NetworkType.CONNECTED) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean isConstrained(NetworkState var1) {
      int var2 = VERSION.SDK_INT;
      boolean var3 = true;
      if (var2 >= 26) {
         boolean var4 = var3;
         if (var1.isConnected()) {
            if (!var1.isValidated()) {
               var4 = var3;
            } else {
               var4 = false;
            }
         }

         return var4;
      } else {
         return var1.isConnected() ^ true;
      }
   }
}
