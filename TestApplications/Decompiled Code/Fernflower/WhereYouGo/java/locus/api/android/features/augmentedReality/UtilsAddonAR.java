package locus.api.android.features.augmentedReality;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import java.security.NoSuchAlgorithmException;
import java.util.List;
import locus.api.android.ActionDisplay;
import locus.api.android.utils.LocusUtils;
import locus.api.objects.Storable;
import locus.api.objects.extra.Location;
import locus.api.utils.Logger;

public class UtilsAddonAR {
   private static final String BROADCAST_DATA = "locus.api.android.addon.ar.NEW_DATA";
   private static final String EXTRA_GUIDING_ID = "EXTRA_GUIDING_ID";
   private static final String EXTRA_LOCATION = "EXTRA_LOCATION";
   private static final String INTENT_VIEW = "locus.api.android.addon.ar.ACTION_VIEW";
   private static final int REQUEST_ADDON_AR = 30001;
   public static final int REQUIRED_VERSION = 11;
   public static final String RESULT_WPT_ID = "RESULT_WPT_ID";
   private static final String TAG = "UtilsAddonAR";
   private static Location mLastLocation;

   public static boolean isInstalled(Context var0) {
      return LocusUtils.isAppAvailable(var0, "menion.android.locus.addon.ar", 11);
   }

   public static boolean showPoints(Activity var0, List var1, Location var2, long var3) {
      boolean var5 = false;
      if (!isInstalled(var0)) {
         Logger.logW("UtilsAddonAR", "missing required version 11");
      } else {
         Intent var6 = new Intent("locus.api.android.addon.ar.ACTION_VIEW");
         var6.putExtra("INTENT_EXTRA_POINTS_DATA_ARRAY", Storable.getAsBytes(var1));
         var6.putExtra("EXTRA_LOCATION", var2.getAsBytes());
         var6.putExtra("EXTRA_GUIDING_ID", var3);
         if (!ActionDisplay.hasData(var6)) {
            Logger.logW("UtilsAddonAR", "Intent 'null' or not contain any data");
         } else {
            mLastLocation = var2;
            var0.startActivityForResult(var6, 30001);
            var5 = true;
         }
      }

      return var5;
   }

   public static void showTracks(Context var0, List var1) throws NoSuchAlgorithmException {
      throw new NoSuchAlgorithmException("Not yet implemented");
   }

   public static void updateLocation(Context var0, Location var1) {
      long var2 = var1.getTime();
      long var4 = mLastLocation.getTime();
      double var6 = (double)var1.distanceTo(mLastLocation);
      double var8 = Math.abs(var1.getAltitude() - mLastLocation.getAltitude());
      if (var2 - var4 >= 5000L && (var6 >= 5.0D || var8 >= 10.0D)) {
         mLastLocation = var1;
         Intent var10 = new Intent("locus.api.android.addon.ar.NEW_DATA");
         var10.putExtra("EXTRA_LOCATION", mLastLocation.getAsBytes());
         var0.sendBroadcast(var10);
      }

   }
}
