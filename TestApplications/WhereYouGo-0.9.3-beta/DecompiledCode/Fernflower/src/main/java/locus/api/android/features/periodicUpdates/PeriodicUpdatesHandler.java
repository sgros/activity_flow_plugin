package locus.api.android.features.periodicUpdates;

import android.content.Context;
import android.content.Intent;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.extra.Location;

public class PeriodicUpdatesHandler {
   private static final String TAG = "PeriodicUpdatesHandler";
   private static PeriodicUpdatesHandler mInstance;
   protected Location mLastGps;
   protected Location mLastMapCenter;
   protected int mLastZoomLevel = -1;
   protected double mLocMinDistance = 1.0D;

   private PeriodicUpdatesHandler() {
   }

   public static PeriodicUpdatesHandler getInstance() {
      // $FF: Couldn't be decompiled
   }

   public void onReceive(Context var1, Intent var2, PeriodicUpdatesHandler.OnUpdate var3) {
      if (var3 == null) {
         throw new IllegalArgumentException("Incorrect arguments");
      } else {
         if (var1 != null && var2 != null) {
            UpdateContainer var4 = PeriodicUpdatesFiller.intentToUpdate(var2, this);
            var3.onUpdate(LocusUtils.createLocusVersion(var1, var2), var4);
         } else {
            var3.onIncorrectData();
         }

      }
   }

   public void setLocNotificationLimit(double var1) {
      this.mLocMinDistance = var1;
   }

   public interface OnUpdate {
      void onIncorrectData();

      void onUpdate(LocusUtils.LocusVersion var1, UpdateContainer var2);
   }
}
