package menion.android.whereyougo.gui.utils;

import android.app.Activity;
import android.app.AlertDialog.Builder;
import android.content.Context;
import android.content.DialogInterface.OnClickListener;
import android.text.Html;
import android.text.TextUtils;
import android.view.Window;
import android.view.View.OnLongClickListener;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import android.widget.ListView;
import cz.matejcik.openwig.Engine;
import java.util.ArrayList;
import menion.android.whereyougo.gui.extension.IconedListAdapter;
import menion.android.whereyougo.utils.Const;
import menion.android.whereyougo.utils.Utils;

public class UtilsGUI {
   public static final int DIALOG_EDIT_TEXT_ID = 10005;
   public static final int DIALOG_SPINNER_ID = 10006;

   public static ListView createListView(Context var0, boolean var1, ArrayList var2) {
      ListView var3 = new ListView(var0);
      setListView(var0, var3, var1, var2);
      return var3;
   }

   public static void dialogDoItem(final Activity var0, final CharSequence var1, final int var2, final CharSequence var3, final String var4, final OnClickListener var5, final String var6, final OnClickListener var7) {
      var0.runOnUiThread(new Runnable() {
         public void run() {
            if (!var0.isFinishing()) {
               Builder var1x = new Builder(var0);
               var1x.setCancelable(false);
               var1x.setTitle(var1);
               var1x.setIcon(var2);
               var1x.setMessage(var3);
               if (!TextUtils.isEmpty(var4)) {
                  var1x.setPositiveButton(var4, var5);
               }

               if (!TextUtils.isEmpty(var6)) {
                  var1x.setNegativeButton(var6, var7);
               }

               if (!var0.isFinishing()) {
                  var1x.show();
               }
            }

         }
      });
   }

   public static void dialogDoItem(final Activity var0, final CharSequence var1, final int var2, final CharSequence var3, final String var4, final OnClickListener var5, final String var6, final OnClickListener var7, final String var8, final OnClickListener var9) {
      var0.runOnUiThread(new Runnable() {
         public void run() {
            if (!var0.isFinishing()) {
               Builder var1x = new Builder(var0);
               var1x.setCancelable(true);
               var1x.setTitle(var1);
               var1x.setIcon(var2);
               var1x.setMessage(var3);
               if (!TextUtils.isEmpty(var4)) {
                  var1x.setPositiveButton(var4, var5);
               }

               if (!TextUtils.isEmpty(var6)) {
                  var1x.setNegativeButton(var6, var7);
               }

               if (!TextUtils.isEmpty(var8)) {
                  var1x.setNeutralButton(var8, var9);
               }

               if (!var0.isFinishing()) {
                  var1x.show();
               }
            }

         }
      });
   }

   public static int getDialogWidth() {
      return -1;
   }

   public static WebView getFilledWebView(Activity var0, String var1) {
      WebView var3 = new WebView(var0);

      try {
         var3.loadDataWithBaseURL((String)null, var1.replaceAll("\\+", " "), "text/html", "utf-8", (String)null);
      } catch (Exception var2) {
      }

      var3.setLayoutParams(new LayoutParams(getDialogWidth(), -2));
      var3.setBackgroundColor(-1);
      return var3;
   }

   public static int getUniqueId() {
      return (int)(Math.random() * 2.147483647E9D);
   }

   public static CharSequence html(String var0, boolean var1) {
      if (var0 == null) {
         var0 = null;
      } else {
         String var2 = var0;
         if (var1) {
            var2 = var0.replaceAll("\\n", "<br>").replaceAll("  ", "&nbsp;&nbsp;");
         }

         var0 = Html.fromHtml(var2).toString();
      }

      return var0;
   }

   public static void setButtons(Activity var0, int[] var1, android.view.View.OnClickListener var2, OnLongClickListener var3) {
      int var4 = var1.length;

      for(int var5 = 0; var5 < var4; ++var5) {
         int var6 = var1[var5];
         if (var2 != null) {
            var0.findViewById(var6).setOnClickListener(var2);
         }

         if (var3 != null) {
            var0.findViewById(var6).setOnLongClickListener(var3);
         }
      }

   }

   public static void setListView(Context var0, ListView var1, boolean var2, ArrayList var3) {
      if (var2) {
         var1.setChoiceMode(2);
      }

      IconedListAdapter var4 = new IconedListAdapter(var0, var3, var1);
      var4.setTextView02Visible(0, true);
      if (var3.size() > 50) {
         var1.setFastScrollEnabled(true);
      }

      var1.setAdapter(var4);
   }

   public static void setWindowDialogCorrectWidth(Window var0) {
      android.view.WindowManager.LayoutParams var1 = var0.getAttributes();
      var1.width = getDialogWidth();
      var0.setAttributes(var1);
   }

   public static void setWindowFloatingRight(Activity var0) {
      int var1 = Math.min(Const.SCREEN_WIDTH, Const.SCREEN_HEIGHT);
      android.view.WindowManager.LayoutParams var2 = var0.getWindow().getAttributes();
      var2.width = getDialogWidth();
      var2.height = var1;
      var2.x = (int)((float)(Const.SCREEN_WIDTH - var2.width) - Utils.getDpPixels(10.0F));
      var0.getWindow().setAttributes(var2);
   }

   public static void showDialogDeleteItem(Activity var0, String var1, OnClickListener var2) {
      String var3 = var0.getString(2131165302);
      Object var4;
      if (var1 != null) {
         var4 = var0.getString(2131165197, new Object[]{Html.fromHtml(var1)});
      } else {
         var4 = var0.getText(2131165196);
      }

      dialogDoItem(var0, var3, 2130837547, (CharSequence)var4, var0.getString(2131165317), var2, var0.getString(2131165224), (OnClickListener)null);
   }

   public static void showDialogError(Activity var0, int var1, OnClickListener var2) {
      showDialogError(var0, var0.getText(var1), var2);
   }

   public static void showDialogError(Activity var0, CharSequence var1) {
      showDialogError(var0, var1, (OnClickListener)null);
   }

   public static void showDialogError(Activity var0, CharSequence var1, OnClickListener var2) {
      dialogDoItem(var0, var0.getText(2131165200), 2130837531, var1, (String)null, (OnClickListener)null, var0.getString(2131165192), var2);
   }

   public static void showDialogInfo(Activity var0, int var1) {
      showDialogInfo(var0, var0.getText(var1));
   }

   public static void showDialogInfo(Activity var0, int var1, OnClickListener var2) {
      dialogDoItem(var0, var0.getText(2131165213), 2130837552, var0.getText(var1), (String)null, (OnClickListener)null, var0.getString(2131165192), var2);
   }

   public static void showDialogInfo(Activity var0, CharSequence var1) {
      dialogDoItem(var0, var0.getText(2131165213), 2130837552, var1, (String)null, (OnClickListener)null, var0.getString(2131165192), (OnClickListener)null);
   }

   public static void showDialogQuestion(Activity var0, int var1, OnClickListener var2) {
      showDialogQuestion(var0, var0.getText(var1), var2, (OnClickListener)null);
   }

   public static void showDialogQuestion(Activity var0, int var1, OnClickListener var2, OnClickListener var3) {
      showDialogQuestion(var0, var0.getText(var1), var2, var3);
   }

   public static void showDialogQuestion(Activity var0, int var1, OnClickListener var2, OnClickListener var3, OnClickListener var4) {
      showDialogQuestion(var0, var0.getText(var1), var2, var3, var4);
   }

   public static void showDialogQuestion(Activity var0, CharSequence var1, OnClickListener var2) {
      showDialogQuestion(var0, var1, var2, (OnClickListener)null);
   }

   public static void showDialogQuestion(Activity var0, CharSequence var1, OnClickListener var2, OnClickListener var3) {
      dialogDoItem(var0, var0.getText(2131165302), 2130837578, var1, var0.getString(2131165317), var2, var0.getString(2131165224), var3);
   }

   public static void showDialogQuestion(Activity var0, CharSequence var1, OnClickListener var2, OnClickListener var3, OnClickListener var4) {
      dialogDoItem(var0, var0.getText(2131165302), 2130837578, var1, var0.getString(2131165317), var2, var0.getString(2131165224), var3, var0.getString(2131165190), var4);
   }

   public static void showDialogWebView(Activity var0, int var1, String var2) {
      showDialogWebView(var0, var0.getString(var1), var2);
   }

   public static void showDialogWebView(final Activity var0, final String var1, final String var2) {
      var0.runOnUiThread(new Runnable() {
         public void run() {
            if (!var0.isFinishing()) {
               Builder var1x = new Builder(var0);
               var1x.setCancelable(false);
               var1x.setTitle(var1);
               var1x.setView(UtilsGUI.getFilledWebView(var0, var2));
               var1x.setPositiveButton(2131165192, (OnClickListener)null);
               if (!var0.isFinishing()) {
                  var1x.show();
               }
            }

         }
      });
   }

   public static CharSequence simpleHtml(String var0) {
      if (var0 != null) {
         var0 = Engine.removeHtml(var0);
      }

      return var0;
   }
}
