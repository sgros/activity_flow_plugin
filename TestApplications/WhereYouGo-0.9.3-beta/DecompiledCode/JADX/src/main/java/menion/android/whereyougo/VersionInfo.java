package menion.android.whereyougo;

import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import menion.android.whereyougo.gui.extension.activity.CustomMainActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.utils.C0322A;

public class VersionInfo {
    private static boolean stage01Completed = false;

    public static void afterStartAction() {
        if (!stage01Completed) {
            int lastVersion = PreferenceValues.getApplicationVersionLast();
            final int actualVersion = PreferenceValues.getApplicationVersionActual();
            if (lastVersion == 0 || actualVersion != lastVersion) {
                String news = getNews(lastVersion, actualVersion);
                if (news == null || news.length() <= 0) {
                    stage01Completed = true;
                    return;
                }
                Builder b = new Builder(C0322A.getMain());
                b.setCancelable(false);
                b.setTitle(MainApplication.APP_NAME);
                b.setIcon(C0254R.C0252drawable.icon);
                b.setView(UtilsGUI.getFilledWebView(C0322A.getMain(), news));
                b.setNeutralButton(C0254R.string.yes, new OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        VersionInfo.stage01Completed = true;
                        PreferenceValues.setApplicationVersionLast(actualVersion);
                    }
                });
                b.show();
                return;
            }
            stage01Completed = true;
        }
    }

    public static String getNews(int lastVersion, int actualVersion) {
        String newsInfo = "";
        if (lastVersion != 0) {
            return CustomMainActivity.getNewsFromTo(lastVersion, actualVersion);
        }
        return ((newsInfo + "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>") + CustomMainActivity.loadAssetString(PreferenceValues.getLanguageCode() + "_first.html")) + "</body></html>";
    }
}
