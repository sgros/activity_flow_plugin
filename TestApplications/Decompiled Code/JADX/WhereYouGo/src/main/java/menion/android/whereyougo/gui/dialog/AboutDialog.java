package menion.android.whereyougo.gui.dialog;

import android.app.AlertDialog.Builder;
import android.app.Dialog;
import android.os.Bundle;
import android.view.ViewGroup.LayoutParams;
import android.webkit.WebView;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.VersionInfo;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.C0322A;

public class AboutDialog extends CustomDialogFragment {
    public Dialog createDialog(Bundle savedInstanceState) {
        String buffer = "<div align=\"center\"><h2><b>" + C0322A.getAppName() + "</b></h2></div><div align=\"center\"><h3><b>" + C0322A.getAppVersion() + "</b></h3></div>" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html") + "<br /><br />" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_about.html") + "<br /><br />" + VersionInfo.getNews(1, PreferenceValues.getApplicationVersionActual());
        WebView webView = new WebView(C0322A.getMain());
        webView.loadData(buffer, "text/html; charset=utf-8", "utf-8");
        webView.setLayoutParams(new LayoutParams(-1, -2));
        webView.setBackgroundColor(-1);
        return new Builder(getActivity()).setTitle(C0254R.string.about_application).setIcon(C0254R.C0252drawable.ic_title_logo).setView(webView).setNeutralButton(C0254R.string.close, null).create();
    }
}
