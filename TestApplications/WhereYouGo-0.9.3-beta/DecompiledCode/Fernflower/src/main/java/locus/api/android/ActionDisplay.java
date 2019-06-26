package locus.api.android;

import android.content.Context;
import android.content.Intent;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.utils.Logger;

public class ActionDisplay {
   private static final String TAG = "ActionDisplay";

   public static boolean hasData(Intent var0) {
      boolean var1 = false;
      if (var0 != null && (var0.getByteArrayExtra("INTENT_EXTRA_POINTS_DATA") != null || var0.getByteArrayExtra("INTENT_EXTRA_POINTS_DATA_ARRAY") != null || var0.getStringExtra("INTENT_EXTRA_POINTS_FILE_PATH") != null || var0.getByteArrayExtra("INTENT_EXTRA_TRACKS_SINGLE") != null || var0.getByteArrayExtra("INTENT_EXTRA_TRACKS_MULTI") != null || var0.getByteArrayExtra("INTENT_EXTRA_CIRCLES_MULTI") != null)) {
         var1 = true;
      }

      return var1;
   }

   protected static boolean removeSpecialDataSilently(Context var0, LocusUtils.LocusVersion var1, String var2, long[] var3) throws RequiredVersionMissingException {
      if (!var1.isVersionValid(LocusUtils.VersionCode.UPDATE_02)) {
         throw new RequiredVersionMissingException(LocusUtils.VersionCode.UPDATE_02);
      } else {
         boolean var4;
         if (var3 != null && var3.length != 0) {
            Intent var5 = new Intent("locus.api.android.ACTION_REMOVE_DATA_SILENTLY");
            var5.setPackage(var1.getPackageName());
            var5.putExtra(var2, var3);
            var0.sendBroadcast(var5);
            var4 = true;
         } else {
            Logger.logW("ActionDisplay", "Intent 'null' or not contain any data");
            var4 = false;
         }

         return var4;
      }
   }

   protected static boolean sendData(String var0, Context var1, Intent var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      return sendData(var0, var1, var2, var3, var4, LocusUtils.VersionCode.UPDATE_01);
   }

   protected static boolean sendData(String var0, Context var1, Intent var2, boolean var3, boolean var4, LocusUtils.VersionCode var5) throws RequiredVersionMissingException {
      boolean var6 = false;
      if (!LocusUtils.isLocusAvailable(var1, var5.vcFree, var5.vcPro, 0)) {
         throw new RequiredVersionMissingException(var5.vcFree);
      } else {
         if (!hasData(var2)) {
            Logger.logW("ActionDisplay", "Intent 'null' or not contain any data");
            var3 = var6;
         } else {
            var2.setAction(var0);
            var2.putExtra("INTENT_EXTRA_CENTER_ON_DATA", var4);
            if (var0.equals("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY")) {
               var1.sendBroadcast(var2);
            } else {
               var2.putExtra("INTENT_EXTRA_CALL_IMPORT", var3);
               var1.startActivity(var2);
            }

            var3 = true;
         }

         return var3;
      }
   }

   public static enum ExtraAction {
      CENTER,
      IMPORT,
      NONE;
   }
}
