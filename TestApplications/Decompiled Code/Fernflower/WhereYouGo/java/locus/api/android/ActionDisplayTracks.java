package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.util.List;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;
import locus.api.objects.extra.Track;
import locus.api.utils.Logger;

public class ActionDisplayTracks extends ActionDisplay {
   private static final String TAG = ActionDisplayTracks.class.getSimpleName();

   public static boolean sendTrack(Context var0, Track var1, ActionDisplay.ExtraAction var2) throws RequiredVersionMissingException {
      boolean var3;
      if (var2 == ActionDisplay.ExtraAction.IMPORT) {
         var3 = true;
      } else {
         var3 = false;
      }

      boolean var4;
      if (var2 == ActionDisplay.ExtraAction.CENTER) {
         var4 = true;
      } else {
         var4 = false;
      }

      return sendTrack("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var3, var4, false);
   }

   public static boolean sendTrack(Context var0, Track var1, ActionDisplay.ExtraAction var2, boolean var3) throws RequiredVersionMissingException {
      boolean var4;
      if (var2 == ActionDisplay.ExtraAction.IMPORT) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var5;
      if (var2 == ActionDisplay.ExtraAction.CENTER) {
         var5 = true;
      } else {
         var5 = false;
      }

      return sendTrack("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var4, var5, var3);
   }

   private static boolean sendTrack(String var0, Context var1, Track var2, boolean var3, boolean var4, boolean var5) throws RequiredVersionMissingException {
      if (var2 != null && var2.getPoints().size() != 0) {
         Intent var6 = new Intent();
         var6.putExtra("INTENT_EXTRA_TRACKS_SINGLE", var2.getAsBytes());
         var6.putExtra("INTENT_EXTRA_START_NAVIGATION", var5);
         var3 = sendData(var0, var1, var6, var3, var4);
      } else {
         Logger.logE(TAG, "sendTrack(" + var0 + ", " + var1 + ", " + var2 + ", " + var3 + ", " + var4 + ", " + var5 + "), " + "track is null or contain no points");
         var3 = false;
      }

      return var3;
   }

   public static boolean sendTrackSilent(Context var0, Track var1, boolean var2) throws RequiredVersionMissingException {
      return sendTrack("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, false, var2, false);
   }

   public static boolean sendTracks(Context var0, List var1, ActionDisplay.ExtraAction var2) throws RequiredVersionMissingException {
      boolean var3 = true;
      boolean var4;
      if (var2 == ActionDisplay.ExtraAction.IMPORT) {
         var4 = true;
      } else {
         var4 = false;
      }

      if (var2 != ActionDisplay.ExtraAction.CENTER) {
         var3 = false;
      }

      return sendTracks("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var4, var3);
   }

   private static boolean sendTracks(String var0, Context var1, List var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      if (var2 != null && var2.size() != 0) {
         Intent var5 = new Intent();
         var5.putExtra("INTENT_EXTRA_TRACKS_MULTI", Storable.getAsBytes(var2));
         var3 = sendData(var0, var1, var5, var3, var4);
      } else {
         var3 = false;
      }

      return var3;
   }

   public static boolean sendTracksSilent(Context var0, List var1, boolean var2) throws RequiredVersionMissingException {
      return sendTracks("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, false, var2);
   }
}
