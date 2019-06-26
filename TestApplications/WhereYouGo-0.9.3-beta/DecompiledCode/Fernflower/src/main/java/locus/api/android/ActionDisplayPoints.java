package locus.api.android;

import android.content.Context;
import android.content.Intent;
import java.util.ArrayList;
import java.util.List;
import locus.api.android.objects.PackWaypoints;
import locus.api.android.utils.exceptions.RequiredVersionMissingException;
import locus.api.objects.Storable;

public class ActionDisplayPoints extends ActionDisplay {
   private static final String TAG = "ActionDisplayPoints";

   public static List readDataWriteOnCard(String param0) {
      // $FF: Couldn't be decompiled
   }

   private static boolean sendDataWriteOnCard(List param0, String param1) {
      // $FF: Couldn't be decompiled
   }

   public static boolean sendPack(Context var0, PackWaypoints var1, ActionDisplay.ExtraAction var2) throws RequiredVersionMissingException {
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

      return sendPack("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var4, var3);
   }

   private static boolean sendPack(String var0, Context var1, PackWaypoints var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      if (var2 == null) {
         var3 = false;
      } else {
         Intent var5 = new Intent();
         var5.putExtra("INTENT_EXTRA_POINTS_DATA", var2.getAsBytes());
         var3 = sendData(var0, var1, var5, var3, var4);
      }

      return var3;
   }

   public static boolean sendPackSilent(Context var0, PackWaypoints var1, boolean var2) throws RequiredVersionMissingException {
      return sendPack("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, false, var2);
   }

   public static boolean sendPacks(Context var0, List var1, ActionDisplay.ExtraAction var2) throws RequiredVersionMissingException {
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

      return sendPacks("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var4, var3);
   }

   private static boolean sendPacks(String var0, Context var1, List var2, boolean var3, boolean var4) throws RequiredVersionMissingException {
      if (var2 == null) {
         var3 = false;
      } else {
         Intent var5 = new Intent();
         var5.putExtra("INTENT_EXTRA_POINTS_DATA_ARRAY", Storable.getAsBytes(var2));
         var3 = sendData(var0, var1, var5, var3, var4);
      }

      return var3;
   }

   public static boolean sendPacksFile(Context var0, ArrayList var1, String var2, ActionDisplay.ExtraAction var3) throws RequiredVersionMissingException {
      boolean var4;
      if (var3 == ActionDisplay.ExtraAction.IMPORT) {
         var4 = true;
      } else {
         var4 = false;
      }

      boolean var5;
      if (var3 == ActionDisplay.ExtraAction.CENTER) {
         var5 = true;
      } else {
         var5 = false;
      }

      return sendPacksFile("locus.api.android.ACTION_DISPLAY_DATA", var0, var1, var2, var4, var5);
   }

   private static boolean sendPacksFile(String var0, Context var1, List var2, String var3, boolean var4, boolean var5) throws RequiredVersionMissingException {
      if (sendDataWriteOnCard(var2, var3)) {
         Intent var6 = new Intent();
         var6.putExtra("INTENT_EXTRA_POINTS_FILE_PATH", var3);
         var4 = sendData(var0, var1, var6, var4, var5);
      } else {
         var4 = false;
      }

      return var4;
   }

   public static boolean sendPacksFileSilent(Context var0, ArrayList var1, String var2, boolean var3) throws RequiredVersionMissingException {
      return sendPacksFile("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, var2, false, var3);
   }

   public static boolean sendPacksSilent(Context var0, List var1, boolean var2) throws RequiredVersionMissingException {
      return sendPacks("locus.api.android.ACTION_DISPLAY_DATA_SILENTLY", var0, var1, false, var2);
   }

   public void removePackFromLocus(Context var1, String var2) throws RequiredVersionMissingException {
      if (var2 != null && var2.length() != 0) {
         PackWaypoints var3 = new PackWaypoints(var2);
         (new Intent()).putExtra("INTENT_EXTRA_POINTS_DATA", var3.getAsBytes());
         sendPackSilent(var1, var3, false);
      }

   }
}
