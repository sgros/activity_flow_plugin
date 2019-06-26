package locus.api.android;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import java.io.File;
import locus.api.android.utils.LocusUtils;
import locus.api.utils.Logger;

public class ActionFiles {
   private static final String TAG = ActionFiles.class.getSimpleName();

   private static String getMimeType(File var0) {
      String var2 = var0.getName();
      int var1 = var2.lastIndexOf(".");
      if (var1 == -1) {
         var2 = "*/*";
      } else {
         var2 = "application/" + var2.substring(var1 + 1);
      }

      return var2;
   }

   public static boolean importFileLocus(Context var0, File var1) {
      return importFileLocus(var0, LocusUtils.getActiveVersion(var0), var1, true);
   }

   public static boolean importFileLocus(Context var0, LocusUtils.LocusVersion var1, File var2, boolean var3) {
      if (!isReadyForImport(var0, var1, var2)) {
         Logger.logE(TAG, "importFileLocus(" + var0 + ", " + var1 + ", " + var2 + ", " + var3 + "), " + "invalid input parameters. Import cannot be performed!");
         var3 = false;
      } else {
         Intent var4 = new Intent("android.intent.action.VIEW");
         var4.setClassName(var1.getPackageName(), "menion.android.locus.core.MainActivity");
         var4.setDataAndType(Uri.fromFile(var2), getMimeType(var2));
         var4.putExtra("INTENT_EXTRA_CALL_IMPORT", var3);
         var0.startActivity(var4);
         var3 = true;
      }

      return var3;
   }

   public static boolean importFileSystem(Context var0, File var1) {
      boolean var2;
      if (var1 != null && var1.exists()) {
         Intent var3 = new Intent("android.intent.action.VIEW");
         var3.setDataAndType(Uri.fromFile(var1), getMimeType(var1));
         var0.startActivity(var3);
         var2 = true;
      } else {
         var2 = false;
      }

      return var2;
   }

   private static boolean isReadyForImport(Context var0, LocusUtils.LocusVersion var1, File var2) {
      boolean var3;
      if (var2 != null && var2.exists() && var1 != null) {
         var3 = true;
      } else {
         var3 = false;
      }

      return var3;
   }
}
