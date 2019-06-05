package androidx.work.impl.constraints.controllers;

import android.content.Context;
import android.os.Build.VERSION;
import androidx.work.Logger;
import androidx.work.NetworkType;
import androidx.work.impl.constraints.NetworkState;
import androidx.work.impl.constraints.trackers.Trackers;
import androidx.work.impl.model.WorkSpec;

public class NetworkMeteredController extends ConstraintController {
   private static final String TAG = Logger.tagWithPrefix("NetworkMeteredCtrlr");

   public NetworkMeteredController(Context var1) {
      super(Trackers.getInstance(var1).getNetworkStateTracker());
   }

   boolean hasConstraint(WorkSpec var1) {
      boolean var2;
      if (var1.constraints.getRequiredNetworkType() == NetworkType.METERED) {
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   boolean isConstrained(NetworkState var1) {
      int var2 = VERSION.SDK_INT;
      boolean var3 = true;
      if (var2 < 26) {
         Logger.get().debug(TAG, "Metered network constraint is not supported before API 26, only checking for connected state.");
         return var1.isConnected() ^ true;
      } else {
         boolean var4 = var3;
         if (var1.isConnected()) {
            if (!var1.isMetered()) {
               var4 = var3;
            } else {
               var4 = false;
            }
         }

         return var4;
      }
   }
}
