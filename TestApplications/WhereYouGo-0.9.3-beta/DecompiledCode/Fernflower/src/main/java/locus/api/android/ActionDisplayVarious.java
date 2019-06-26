package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.util.List;
import locus.api.android.utils.LocusUtils;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;

public class ActionDisplayVarious extends ActionDisplay {
   public static void removeCirclesSilent(Context var0, LocusUtils.LocusVersion var1, long[] var2) throws RequiredVersionMissingException {
      removeSpecialDataSilently(var0, var1, "INTENT_EXTRA_CIRCLES_MULTI", var2);
   }

   public static boolean sendCirclesSilent(Context var0, List var1, boolean var2) throws RequiredVersionMissingException {
      return sendCirclesSilent("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, false, var2);
   }

   private static boolean sendCirclesSilent(String var0, Context var1, List var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      if (var2 != null && var2.size() != 0) {
         Intent var5 = new Intent();
         var5.putExtra("INTENT_EXTRA_CIRCLES_MULTI", Storable.getAsBytes(var2));
         var3 = sendData(var0, var1, var5, var3, var4, LocusUtils.VersionCode.UPDATE_02);
      } else {
         var3 = false;
      }

      return var3;
   }
}
