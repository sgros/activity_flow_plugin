// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.dialog;

import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.app.AlertDialog$Builder;
import android.view.ViewGroup$LayoutParams;
import android.content.Context;
import android.webkit.WebView;
import menion.android.whereyougo.VersionInfo;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.A;
import android.app.Dialog;
import android.os.Bundle;
import menion.android.whereyougo.gui.extension.dialog.CustomDialogFragment;

public class AboutDialog extends CustomDialogFragment
{
    @Override
    public Dialog createDialog(final Bundle bundle) {
        final String string = "<div align=\"center\"><h2><b>" + A.getAppName() + "</b></h2></div><div align=\"center\"><h3><b>" + A.getAppVersion() + "</b></h3></div>" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html") + "<br /><br />" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_about.html") + "<br /><br />" + VersionInfo.getNews(1, PreferenceValues.getApplicationVersionActual());
        final WebView view = new WebView((Context)A.getMain());
        view.loadData(string, "text/html; charset=utf-8", "utf-8");
        view.setLayoutParams(new ViewGroup$LayoutParams(-1, -2));
        view.setBackgroundColor(-1);
        return (Dialog)new AlertDialog$Builder((Context)this.getActivity()).setTitle(2131165184).setIcon(2130837551).setView((View)view).setNeutralButton(2131165192, (DialogInterface$OnClickListener)null).create();
    }
}
