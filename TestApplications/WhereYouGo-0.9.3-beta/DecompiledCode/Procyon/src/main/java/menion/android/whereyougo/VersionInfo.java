// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo;

import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.view.View;
import android.app.Activity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import android.content.Context;
import android.app.AlertDialog$Builder;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.preferences.PreferenceValues;

public class VersionInfo
{
    private static boolean stage01Completed;
    
    static {
        VersionInfo.stage01Completed = false;
    }
    
    public static void afterStartAction() {
        if (!VersionInfo.stage01Completed) {
            final int applicationVersionLast = PreferenceValues.getApplicationVersionLast();
            final int applicationVersionActual = PreferenceValues.getApplicationVersionActual();
            if (applicationVersionLast == 0 || applicationVersionActual != applicationVersionLast) {
                final String news = getNews(applicationVersionLast, applicationVersionActual);
                if (news != null && news.length() > 0) {
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)A.getMain());
                    alertDialog$Builder.setCancelable(false);
                    alertDialog$Builder.setTitle((CharSequence)"WhereYouGo");
                    alertDialog$Builder.setIcon(2130837553);
                    alertDialog$Builder.setView((View)UtilsGUI.getFilledWebView(A.getMain(), news));
                    alertDialog$Builder.setNeutralButton(2131165317, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            VersionInfo.stage01Completed = true;
                            PreferenceValues.setApplicationVersionLast(applicationVersionActual);
                        }
                    });
                    alertDialog$Builder.show();
                }
                else {
                    VersionInfo.stage01Completed = true;
                }
            }
            else {
                VersionInfo.stage01Completed = true;
            }
        }
    }
    
    public static String getNews(final int n, final int n2) {
        String s;
        if (n == 0) {
            s = "" + "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>" + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html") + "</body></html>";
        }
        else {
            s = CustomMainActivity.getNewsFromTo(n, n2);
        }
        return s;
    }
}
