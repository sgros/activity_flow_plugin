package menion.android.whereyougo.gui.dialog;

import android.app.Dialog;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import menion.android.whereyougo.VersionInfo;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.A;

public class AboutDialog extends CustomDialogFragment {
   public Dialog createDialog(Bundle var1) {
      String var3 = "<div align=\"center\"><h2><b>" + A.getAppName() + "</b></h2></div><div align=\"center\"><h3><b>" + A.getAppVersion() + "</b></h3></div>" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html") + "<br /><br />" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_about.html") + "<br /><br />" + VersionInfo.getNews(1, PreferenceValues.getApplicationVersionActual());
      WebView var2 = new WebView(A.getMain());
      var2.loadData(var3, "text/html; charset=utf-8", "utf-8");
      var2.setLayoutParams(new LayoutParams(-1, -2));
      var2.setBackgroundColor(-1);
      return (new Builder(this.getActivity())).setTitle(2131165184).setIcon(2130837551).setView(var2).setNeutralButton(2131165192, (OnClickListener)null).create();
   }
}
