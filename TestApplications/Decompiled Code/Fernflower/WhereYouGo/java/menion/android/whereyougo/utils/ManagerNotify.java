package menion.android.whereyougo.utils;

import android.app.Activity;
import android.content.Context;
import android.widget.Toast;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;

public class ManagerNotify {
   private static final String TAG = "ManagerNotify";

   public static void toastInternetProblem() {
      toastLongMessage(Locale.getString(2131165301));
   }

   public static void toastLongMessage(int var0) {
      toastLongMessage(Locale.getString(var0));
   }

   public static void toastLongMessage(Context var0, String var1) {
      toastMessage(var0, var1, 1);
   }

   public static void toastLongMessage(String var0) {
      toastLongMessage(PreferenceValues.getCurrentActivity(), var0);
   }

   private static void toastMessage(final Context var0, final String var1, final int var2) {
      Logger.d("ManagerNotify", "toastMessage(" + var0 + ", " + var1 + ", " + var2 + ")");
      if (var0 != null && var1 != null && var1.length() != 0) {
         try {
            if (var0 instanceof Activity) {
               Activity var3 = (Activity)var0;
               Runnable var4 = new Runnable() {
                  public void run() {
                     Toast.makeText(var0, var1, var2).show();
                  }
               };
               var3.runOnUiThread(var4);
            } else {
               Toast.makeText(var0, var1, var2).show();
            }
         } catch (Exception var5) {
            Logger.e("ManagerNotify", "toastMessage(" + var0 + ", " + var1 + ", " + var2 + ")", var5);
         }
      }

   }

   public static void toastShortMessage(int var0) {
      toastShortMessage(Locale.getString(var0));
   }

   public static void toastShortMessage(Context var0, String var1) {
      toastMessage(var0, var1, 0);
   }

   public static void toastShortMessage(String var0) {
      toastShortMessage(PreferenceValues.getCurrentActivity(), var0);
   }

   public static void toastUnexpectedProblem() {
      toastLongMessage(Locale.getString(2131165312));
   }
}
