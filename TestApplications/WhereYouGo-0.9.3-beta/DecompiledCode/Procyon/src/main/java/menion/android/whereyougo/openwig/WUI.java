// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.openwig;

import menion.android.whereyougo.maps.utils.MapHelper;
import menion.android.whereyougo.gui.activity.wherigo.ListTargetsActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListActionsActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListTasksActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListZonesActivity;
import menion.android.whereyougo.gui.activity.wherigo.ListThingsActivity;
import menion.android.whereyougo.gui.activity.wherigo.DetailsActivity;
import menion.android.whereyougo.gui.activity.CartridgeDetailsActivity;
import menion.android.whereyougo.gui.activity.wherigo.MainMenuActivity;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.utils.ManagerNotify;
import menion.android.whereyougo.gui.IRefreshable;
import menion.android.whereyougo.gui.activity.wherigo.InputScreenActivity;
import android.os.Vibrator;
import android.content.Intent;
import java.util.Arrays;
import se.krka.kahlua.vm.LuaClosure;
import cz.matejcik.openwig.Media;
import cz.matejcik.openwig.EventTable;
import cz.matejcik.openwig.Engine;
import menion.android.whereyougo.audio.UtilsAudio;
import menion.android.whereyougo.preferences.Locale;
import android.content.Context;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.A;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.gui.extension.activity.CustomActivity;
import menion.android.whereyougo.gui.activity.GuidingActivity;
import menion.android.whereyougo.gui.activity.wherigo.PushDialogActivity;
import android.app.Activity;
import android.app.ProgressDialog;
import cz.matejcik.openwig.platform.UI;

public class WUI implements UI
{
    public static final int SCREEN_ACTIONS = 12;
    public static final int SCREEN_CART_DETAIL = 11;
    public static final int SCREEN_MAIN = 10;
    public static final int SCREEN_MAP = 14;
    public static final int SCREEN_TARGETS = 13;
    private static final String TAG = "WUI";
    private static ProgressDialog progressDialog;
    public static boolean saving;
    private Runnable onSavingFinished;
    private Runnable onSavingStarted;
    
    static {
        WUI.saving = false;
    }
    
    private static void closeActivity(final Activity activity) {
        if (activity instanceof PushDialogActivity || activity instanceof GuidingActivity) {
            activity.finish();
        }
    }
    
    private static CustomActivity getParentActivity() {
        final Activity currentActivity = PreferenceValues.getCurrentActivity();
        if (currentActivity != null) {
            final Activity main = currentActivity;
            if (currentActivity instanceof CustomActivity) {
                return (CustomActivity)main;
            }
        }
        final Activity main = A.getMain();
        return (CustomActivity)main;
    }
    
    public static void showTextProgress(final String str) {
        Logger.i("WUI", "showTextProgress(" + str + ")");
    }
    
    public static void startProgressDialog() {
        (WUI.progressDialog = new ProgressDialog((Context)A.getMain())).setMessage((CharSequence)Locale.getString(2131165343));
        WUI.progressDialog.show();
    }
    
    @Override
    public void blockForSaving() {
        Logger.w("WUI", "blockForSaving()");
        WUI.saving = true;
        if (this.onSavingStarted != null) {
            this.onSavingStarted.run();
        }
    }
    
    @Override
    public void command(final String s) {
        if ("StopSound".equals(s)) {
            UtilsAudio.stopSound();
        }
        else if ("Alert".equals(s)) {
            UtilsAudio.playBeep(1);
        }
    }
    
    @Override
    public void debugMsg(final String s) {
        Logger.w("WUI", "debugMsg(" + s.trim() + ")");
    }
    
    @Override
    public void end() {
        while (true) {
            if (WUI.progressDialog == null || !WUI.progressDialog.isShowing()) {
                break Label_0021;
            }
            try {
                WUI.progressDialog.dismiss();
                Engine.kill();
                this.showScreen(10, null);
            }
            catch (Exception ex) {
                Logger.e("WUI", "end(): dismiss progressDialog", ex);
                continue;
            }
            break;
        }
    }
    
    public String getDeviceId() {
        return String.format("%s %s", A.getAppName(), A.getAppVersion());
    }
    
    @Override
    public void playSound(final byte[] array, final String s) {
        UtilsAudio.playSound(array, s);
    }
    
    @Override
    public void pushDialog(final String[] a, final Media[] a2, final String str, final String str2, final LuaClosure obj) {
        Logger.w("WUI", "pushDialog(" + Arrays.toString(a) + ", " + Arrays.toString(a2) + ", " + str + ", " + str2 + ", " + obj + ")");
        final CustomActivity parentActivity = getParentActivity();
        PushDialogActivity.setDialog(a, a2, str, str2, obj);
        final Intent intent = new Intent((Context)parentActivity, (Class)PushDialogActivity.class);
        intent.setFlags(65536);
        parentActivity.startActivity(intent);
        parentActivity.overridePendingTransition(0, 0);
        closeActivity(parentActivity);
        ((Vibrator)A.getMain().getSystemService("vibrator")).vibrate(25L);
    }
    
    @Override
    public void pushInput(final EventTable eventTable) {
        Logger.w("WUI", "pushInput(" + eventTable + ")");
        final CustomActivity parentActivity = getParentActivity();
        InputScreenActivity.setInput(eventTable);
        final Intent intent = new Intent((Context)parentActivity, (Class)InputScreenActivity.class);
        intent.setFlags(65536);
        parentActivity.startActivity(intent);
        parentActivity.overridePendingTransition(0, 0);
        closeActivity(parentActivity);
    }
    
    @Override
    public void refresh() {
        final Activity currentActivity = PreferenceValues.getCurrentActivity();
        Logger.w("WUI", "refresh(), currentActivity:" + currentActivity);
        if (currentActivity != null && currentActivity instanceof IRefreshable) {
            ((IRefreshable)currentActivity).refresh();
        }
    }
    
    public void setOnSavingFinished(final Runnable onSavingFinished) {
        this.onSavingFinished = onSavingFinished;
    }
    
    public void setOnSavingStarted(final Runnable onSavingStarted) {
        this.onSavingStarted = onSavingStarted;
    }
    
    @Override
    public void setStatusText(final String str) {
        Logger.w("WUI", "setStatus(" + str + ")");
        if (str != null && str.length() != 0) {
            ManagerNotify.toastShortMessage((Context)getParentActivity(), str);
        }
    }
    
    @Override
    public void showError(final String s) {
        Logger.e("WUI", "showError(" + s.trim() + ")");
        if (PreferenceValues.getCurrentActivity() != null) {
            UtilsGUI.showDialogError(PreferenceValues.getCurrentActivity(), s);
        }
    }
    
    @Override
    public void showScreen(final int i, final EventTable eventTable) {
        final CustomActivity parentActivity = getParentActivity();
        Logger.w("WUI", "showScreen(" + i + "), parent:" + parentActivity + ", param:" + eventTable);
        PreferenceValues.setCurrentActivity(null);
        switch (i) {
            default: {
                closeActivity(parentActivity);
                break;
            }
            case 0: {
                parentActivity.startActivity(new Intent((Context)parentActivity, (Class)MainMenuActivity.class));
                break;
            }
            case 11: {
                parentActivity.startActivity(new Intent((Context)parentActivity, (Class)CartridgeDetailsActivity.class));
                break;
            }
            case 1: {
                DetailsActivity.et = eventTable;
                final Intent intent = new Intent((Context)parentActivity, (Class)DetailsActivity.class);
                intent.addFlags(131072);
                parentActivity.startActivity(intent);
                break;
            }
            case 2: {
                final Intent intent2 = new Intent((Context)parentActivity, (Class)ListThingsActivity.class);
                intent2.putExtra("title", Locale.getString(2131165216));
                intent2.putExtra("mode", 0);
                parentActivity.startActivity(intent2);
                break;
            }
            case 3: {
                final Intent intent3 = new Intent((Context)parentActivity, (Class)ListThingsActivity.class);
                intent3.putExtra("title", Locale.getString(2131165321));
                intent3.putExtra("mode", 1);
                parentActivity.startActivity(intent3);
                break;
            }
            case 4: {
                final Intent intent4 = new Intent((Context)parentActivity, (Class)ListZonesActivity.class);
                intent4.putExtra("title", Locale.getString(2131165219));
                parentActivity.startActivity(intent4);
                break;
            }
            case 5: {
                final Intent intent5 = new Intent((Context)parentActivity, (Class)ListTasksActivity.class);
                intent5.putExtra("title", Locale.getString(2131165310));
                parentActivity.startActivity(intent5);
                break;
            }
            case 12: {
                final Intent intent6 = new Intent((Context)parentActivity, (Class)ListActionsActivity.class);
                if (eventTable != null) {
                    intent6.putExtra("title", eventTable.name);
                }
                parentActivity.startActivity(intent6);
                break;
            }
            case 13: {
                final Intent intent7 = new Intent((Context)parentActivity, (Class)ListTargetsActivity.class);
                if (eventTable != null) {
                    intent7.putExtra("title", eventTable.name);
                }
                parentActivity.startActivity(intent7);
                break;
            }
            case 14: {
                MapHelper.showMap(parentActivity, eventTable);
                break;
            }
        }
    }
    
    @Override
    public void start() {
        A.getMain().runOnUiThread((Runnable)new Runnable() {
            @Override
            public void run() {
                if (WUI.progressDialog == null || !WUI.progressDialog.isShowing()) {
                    return;
                }
                try {
                    WUI.progressDialog.dismiss();
                }
                catch (Exception ex) {
                    Logger.e("WUI", "start(): dismiss progressDialog", ex);
                }
            }
        });
        this.showScreen(0, null);
    }
    
    @Override
    public void unblock() {
        Logger.w("WUI", "unblock()");
        WUI.saving = false;
        if (this.onSavingFinished != null) {
            this.onSavingFinished.run();
        }
    }
}
