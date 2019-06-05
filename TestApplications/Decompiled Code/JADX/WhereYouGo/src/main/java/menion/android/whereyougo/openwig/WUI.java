package menion.android.whereyougo.openwig;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Vibrator;
import java.util.Arrays;
import menion.android.whereyougo.C0254R;
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
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.ManagerNotify;
import p005cz.matejcik.openwig.Engine;
import p005cz.matejcik.openwig.EventTable;
import p005cz.matejcik.openwig.Media;
import p005cz.matejcik.openwig.platform.C0237UI;
import p009se.krka.kahlua.p010vm.LuaClosure;

public class WUI implements C0237UI {
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

    /* renamed from: menion.android.whereyougo.openwig.WUI$1 */
    class C03211 implements Runnable {
        C03211() {
        }

        public void run() {
            if (WUI.progressDialog != null && WUI.progressDialog.isShowing()) {
                try {
                    WUI.progressDialog.dismiss();
                } catch (Exception e) {
                    Logger.m22e(WUI.TAG, "start(): dismiss progressDialog", e);
                }
            }
        }
    }

    private static void closeActivity(Activity activity) {
        if ((activity instanceof PushDialogActivity) || (activity instanceof GuidingActivity)) {
            activity.finish();
        }
    }

    private static CustomActivity getParentActivity() {
        Activity activity = PreferenceValues.getCurrentActivity();
        if (activity == null || !(activity instanceof CustomActivity)) {
            activity = C0322A.getMain();
        }
        return (CustomActivity) activity;
    }

    public static void showTextProgress(String text) {
        Logger.m24i(TAG, "showTextProgress(" + text + ")");
    }

    public static void startProgressDialog() {
        progressDialog = new ProgressDialog(C0322A.getMain());
        progressDialog.setMessage(Locale.getString(C0254R.string.loading));
        progressDialog.show();
    }

    public void blockForSaving() {
        Logger.m26w(TAG, "blockForSaving()");
        saving = true;
        if (this.onSavingStarted != null) {
            this.onSavingStarted.run();
        }
    }

    public void debugMsg(String msg) {
        Logger.m26w(TAG, "debugMsg(" + msg.trim() + ")");
    }

    public void end() {
        if (progressDialog != null && progressDialog.isShowing()) {
            try {
                progressDialog.dismiss();
            } catch (Exception e) {
                Logger.m22e(TAG, "end(): dismiss progressDialog", e);
            }
        }
        Engine.kill();
        showScreen(10, null);
    }

    public String getDeviceId() {
        return String.format("%s %s", new Object[]{C0322A.getAppName(), C0322A.getAppVersion()});
    }

    public void playSound(byte[] data, String mime) {
        UtilsAudio.playSound(data, mime);
    }

    public void command(String cmd) {
        if ("StopSound".equals(cmd)) {
            UtilsAudio.stopSound();
        } else if ("Alert".equals(cmd)) {
            UtilsAudio.playBeep(1);
        }
    }

    public void pushDialog(String[] texts, Media[] media, String button1, String button2, LuaClosure callback) {
        Logger.m26w(TAG, "pushDialog(" + Arrays.toString(texts) + ", " + Arrays.toString(media) + ", " + button1 + ", " + button2 + ", " + callback + ")");
        Activity activity = getParentActivity();
        PushDialogActivity.setDialog(texts, media, button1, button2, callback);
        Intent intent = new Intent(activity, PushDialogActivity.class);
        intent.setFlags(65536);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        closeActivity(activity);
        ((Vibrator) C0322A.getMain().getSystemService("vibrator")).vibrate(25);
    }

    public void pushInput(EventTable input) {
        Logger.m26w(TAG, "pushInput(" + input + ")");
        Activity activity = getParentActivity();
        InputScreenActivity.setInput(input);
        Intent intent = new Intent(activity, InputScreenActivity.class);
        intent.setFlags(65536);
        activity.startActivity(intent);
        activity.overridePendingTransition(0, 0);
        closeActivity(activity);
    }

    public void refresh() {
        Activity activity = PreferenceValues.getCurrentActivity();
        Logger.m26w(TAG, "refresh(), currentActivity:" + activity);
        if (activity != null && (activity instanceof IRefreshable)) {
            ((IRefreshable) activity).refresh();
        }
    }

    public void setStatusText(String text) {
        Logger.m26w(TAG, "setStatus(" + text + ")");
        if (text != null && text.length() != 0) {
            ManagerNotify.toastShortMessage(getParentActivity(), text);
        }
    }

    public void showError(String msg) {
        Logger.m21e(TAG, "showError(" + msg.trim() + ")");
        if (PreferenceValues.getCurrentActivity() != null) {
            UtilsGUI.showDialogError(PreferenceValues.getCurrentActivity(), msg);
        }
    }

    public void showScreen(int screenId, EventTable details) {
        Activity activity = getParentActivity();
        Logger.m26w(TAG, "showScreen(" + screenId + "), parent:" + activity + ", param:" + details);
        PreferenceValues.setCurrentActivity(null);
        switch (screenId) {
            case 0:
                activity.startActivity(new Intent(activity, MainMenuActivity.class));
                return;
            case 1:
                DetailsActivity.f101et = details;
                Intent intent03 = new Intent(activity, DetailsActivity.class);
                intent03.addFlags(131072);
                activity.startActivity(intent03);
                return;
            case 2:
                Intent intent04 = new Intent(activity, ListThingsActivity.class);
                intent04.putExtra("title", Locale.getString(C0254R.string.inventory));
                intent04.putExtra("mode", 0);
                activity.startActivity(intent04);
                return;
            case 3:
                Intent intent05 = new Intent(activity, ListThingsActivity.class);
                intent05.putExtra("title", Locale.getString(C0254R.string.you_see));
                intent05.putExtra("mode", 1);
                activity.startActivity(intent05);
                return;
            case 4:
                Intent intent06 = new Intent(activity, ListZonesActivity.class);
                intent06.putExtra("title", Locale.getString(C0254R.string.locations));
                activity.startActivity(intent06);
                return;
            case 5:
                Intent intent07 = new Intent(activity, ListTasksActivity.class);
                intent07.putExtra("title", Locale.getString(C0254R.string.tasks));
                activity.startActivity(intent07);
                return;
            case 11:
                activity.startActivity(new Intent(activity, CartridgeDetailsActivity.class));
                return;
            case 12:
                Intent intent09 = new Intent(activity, ListActionsActivity.class);
                if (details != null) {
                    intent09.putExtra("title", details.name);
                }
                activity.startActivity(intent09);
                return;
            case 13:
                Intent intent10 = new Intent(activity, ListTargetsActivity.class);
                if (details != null) {
                    intent10.putExtra("title", details.name);
                }
                activity.startActivity(intent10);
                return;
            case 14:
                MapHelper.showMap(activity, details);
                return;
            default:
                closeActivity(activity);
                return;
        }
    }

    public void start() {
        C0322A.getMain().runOnUiThread(new C03211());
        showScreen(0, null);
    }

    public void unblock() {
        Logger.m26w(TAG, "unblock()");
        saving = false;
        if (this.onSavingFinished != null) {
            this.onSavingFinished.run();
        }
    }

    public void setOnSavingStarted(Runnable onSavingStarted) {
        this.onSavingStarted = onSavingStarted;
    }

    public void setOnSavingFinished(Runnable onSavingFinished) {
        this.onSavingFinished = onSavingFinished;
    }
}
