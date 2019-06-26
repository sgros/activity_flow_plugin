package menion.android.whereyougo;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.A;

public class VersionInfo {
   private static boolean stage01Completed = false;

   public static void afterStartAction() {
      if (!stage01Completed) {
         int var0 = PreferenceValues.getApplicationVersionLast();
         final int var1 = PreferenceValues.getApplicationVersionActual();
         if (var0 != 0 && var1 == var0) {
            stage01Completed = true;
         } else {
            String var2 = getNews(var0, var1);
            if (var2 != null && var2.length() > 0) {
               Builder var3 = new Builder(A.getMain());
               var3.setCancelable(false);
               var3.setTitle("WhereYouGo");
               var3.setIcon(2130837553);
               var3.setView(UtilsGUI.getFilledWebView(A.getMain(), var2));
               var3.setNeutralButton(2131165317, new OnClickListener() {
                  public void onClick(DialogInterface var1x, int var2) {
                     VersionInfo.stage01Completed = true;
                     PreferenceValues.setApplicationVersionLast(var1);
                  }
               });
               var3.show();
            } else {
               stage01Completed = true;
            }
         }
      }

   }

   public static String getNews(int var0, int var1) {
      String var2;
      if (var0 == 0) {
         var2 = "" + "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
         var2 = var2 + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html");
         var2 = var2 + "</body></html>";
      } else {
         var2 = CustomMainActivity.getNewsFromTo(var0, var1);
      }

      return var2;
   }
}
