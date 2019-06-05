package menion.android.whereyougo.openwig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Vibrator;
import cz.matejcik.openwig.Engine;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.platform.UI;
import java.util.Arrays;
import menion.android.whereyougo.audio.UtilsAudio;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.CartridgeDetailsActivity;
import menion.android.whereyougo.gui.activity.GuidingActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.activity.wherigo.InputScreenActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListActionsActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListTargetsActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListTasksActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListThingsActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListZonesActivity;
import menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity;
import menion.android.whereyougo.gui.activity.wherigo.PushDialogActivity;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import se.krka.kahlua.vm.LuaClosure;

public class WUI implements UI {
   public static final int SCREEN_ACTIONS = 12;
   public static final int SCREEN_CART_DETAIL = 11;
   public static final int SCREEN_MAIN = 10;
   public static final int SCREEN_MAP = 14;
   public static final int SCREEN_TARGETS = 13;
   private static final String TAG = "WUI";
   private static ProgressDialog progressDialog;
   public static boolean saving = false;
   private Runnable onSavingFinished;
   private Runnable onSavingStarted;

   private static void closeActivity(Activity var0) {
      if (var0 instanceof PushDialogActivity || var0 instanceof GuidingActivity) {
         var0.finish();
      }

   }

   private static CustomActivity getParentActivity() {
      Activity var0 = PreferenceValues.getCurrentActivity();
      Object var1;
      if (var0 != null) {
         var1 = var0;
         if (var0 instanceof CustomActivity) {
            return (CustomActivity)var1;
         }
      }

      var1 = A.getMain();
      return (CustomActivity)var1;
   }

   public static void showTextProgress(String var0) {
      Logger.i("WUI", "showTextProgress(" + var0 + ")");
   }

   public static void startProgressDialog() {
      progressDialog = new ProgressDialog(A.getMain());
      progressDialog.setMessage(Locale.getString(2131165343));
      progressDialog.show();
   }

   public void blockForSaving() {
      Logger.w("WUI", "blockForSaving()");
      saving = true;
      if (this.onSavingStarted != null) {
         this.onSavingStarted.run();
      }

   }

   public void command(String var1) {
      if ("StopSound".equals(var1)) {
         UtilsAudio.stopSound();
      } else if ("Alert".equals(var1)) {
         UtilsAudio.playBeep(1);
      }

   }

   public void debugMsg(String var1) {
      Logger.w("WUI", "debugMsg(" + var1.trim() + ")");
   }

   public void end() {
      if (progressDialog != null && progressDialog.isShowing()) {
         try {
            progressDialog.dismiss();
         } catch (Exception var2) {
            Logger.e("WUI", "end(): dismiss progressDialog", var2);
         }
      }

      Engine.kill();
      this.showScreen(10, (EventTable)null);
   }

   public String getDeviceId() {
      return String.format("%s %s", A.getAppName(), A.getAppVersion());
   }

   public void playSound(byte[] var1, String var2) {
      UtilsAudio.playSound(var1, var2);
   }

   public void pushDialog(String[] var1, Media[] var2, String var3, String var4, LuaClosure var5) {
      Logger.w("WUI", "pushDialog(" + Arrays.toString(var1) + ", " + Arrays.toString(var2) + ", " + var3 + ", " + var4 + ", " + var5 + ")");
      CustomActivity var6 = getParentActivity();
      PushDialogActivity.setDialog(var1, var2, var3, var4, var5);
      Intent var7 = new Intent(var6, PushDialogActivity.class);
      var7.setFlags(65536);
      var6.startActivity(var7);
      var6.overridePendingTransition(0, 0);
      closeActivity(var6);
      ((Vibrator)A.getMain().getSystemService("vibrator")).vibrate(25L);
   }

   public void pushInput(EventTable var1) {
      Logger.w("WUI", "pushInput(" + var1 + ")");
      CustomActivity var2 = getParentActivity();
      InputScreenActivity.setInput(var1);
      Intent var3 = new Intent(var2, InputScreenActivity.class);
      var3.setFlags(65536);
      var2.startActivity(var3);
      var2.overridePendingTransition(0, 0);
      closeActivity(var2);
   }

   public void refresh() {
      Activity var1 = PreferenceValues.getCurrentActivity();
      Logger.w("WUI", "refresh(), currentActivity:" + var1);
      if (var1 != null && var1 instanceof IRefreshable) {
         ((IRefreshable)var1).refresh();
      }

   }

   public void setOnSavingFinished(Runnable var1) {
      this.onSavingFinished = var1;
   }

   public void setOnSavingStarted(Runnable var1) {
      this.onSavingStarted = var1;
   }

   public void setStatusText(String var1) {
      Logger.w("WUI", "setStatus(" + var1 + ")");
      if (var1 != null && var1.length() != 0) {
         ManagerNotify.toastShortMessage(getParentActivity(), var1);
      }

   }

   public void showError(String var1) {
      Logger.e("WUI", "showError(" + var1.trim() + ")");
      if (PreferenceValues.getCurrentActivity() != null) {
         UtilsGUI.showDialogError(PreferenceValues.getCurrentActivity(), var1);
      }

   }

   public void showScreen(int var1, EventTable var2) {
      CustomActivity var3 = getParentActivity();
      Logger.w("WUI", "showScreen(" + var1 + "), parent:" + var3 + ", param:" + var2);
      PreferenceValues.setCurrentActivity((Activity)null);
      Intent var4;
      Intent var5;
      switch(var1) {
      case 0:
         var3.startActivity(new Intent(var3, MainMenuActivity.class));
         break;
      case 1:
         DetailsActivity.et = var2;
         var5 = new Intent(var3, DetailsActivity.class);
         var5.addFlags(131072);
         var3.startActivity(var5);
         break;
      case 2:
         var5 = new Intent(var3, ListThingsActivity.class);
         var5.putExtra("title", Locale.getString(2131165216));
         var5.putExtra("mode", 0);
         var3.startActivity(var5);
         break;
      case 3:
         var5 = new Intent(var3, ListThingsActivity.class);
         var5.putExtra("title", Locale.getString(2131165321));
         var5.putExtra("mode", 1);
         var3.startActivity(var5);
         break;
      case 4:
         var5 = new Intent(var3, ListZonesActivity.class);
         var5.putExtra("title", Locale.getString(2131165219));
         var3.startActivity(var5);
         break;
      case 5:
         var5 = new Intent(var3, ListTasksActivity.class);
         var5.putExtra("title", Locale.getString(2131165310));
         var3.startActivity(var5);
         break;
      case 6:
      case 7:
      case 8:
      case 9:
      case 10:
      default:
         closeActivity(var3);
         break;
      case 11:
         var3.startActivity(new Intent(var3, CartridgeDetailsActivity.class));
         break;
      case 12:
         var4 = new Intent(var3, ListActionsActivity.class);
         if (var2 != null) {
            var4.putExtra("title", var2.name);
         }

         var3.startActivity(var4);
         break;
      case 13:
         var4 = new Intent(var3, ListTargetsActivity.class);
         if (var2 != null) {
            var4.putExtra("title", var2.name);
         }

         var3.startActivity(var4);
         break;
      case 14:
         MapHelper.showMap(var3, var2);
      }

   }

   public void start() {
      A.getMain().runOnUiThread(new Runnable() {
         public void run() {
            if (WUI.progressDialog != null && WUI.progressDialog.isShowing()) {
               try {
                  WUI.progressDialog.dismiss();
               } catch (Exception var2) {
                  Logger.e("WUI", "start(): dismiss progressDialog", var2);
               }
            }

         }
      });
      this.showScreen(0, (EventTable)null);
   }

   public void unblock() {
      Logger.w("WUI", "unblock()");
      saving = false;
      if (this.onSavingFinished != null) {
         this.onSavingFinished.run();
      }

   }
}
