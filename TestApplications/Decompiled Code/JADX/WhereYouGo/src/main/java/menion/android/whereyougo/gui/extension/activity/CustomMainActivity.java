package menion.android.whereyougo.gui.extension.activity;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog.Builder;
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.os.Bundle;
import android.os.Debug;
import android.os.StatFs;
import android.view.KeyEvent;
import java.io.File;
import java.io.InputStream;
import java.io.StringReader;
import menion.android.whereyougo.C0254R;
import menion.android.whereyougo.MainApplication;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.utils.C0322A;
import menion.android.whereyougo.utils.FileSystem;
import menion.android.whereyougo.utils.Logger;
import menion.android.whereyougo.utils.Utils;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

public abstract class CustomMainActivity extends CustomActivity {
    public static final int CLOSE_DESTROY_APP_DIALOG_ADDITIONAL_TEXT = 2;
    public static final int CLOSE_DESTROY_APP_DIALOG_NO_TEXT = 1;
    public static final int CLOSE_DESTROY_APP_NO_DIALOG = 0;
    public static final int CLOSE_HIDE_APP = 3;
    private static final String[] DIRS = new String[]{FileSystem.CACHE};
    public static final int FINISH_EXIT = 0;
    public static final int FINISH_EXIT_FORCE = 1;
    public static final int FINISH_NONE = -1;
    public static final int FINISH_REINSTALL = 5;
    public static final int FINISH_RESTART = 2;
    public static final int FINISH_RESTART_FACTORY_RESET = 4;
    public static final int FINISH_RESTART_FORCE = 3;
    private static final String TAG = "CustomMain";
    private static boolean callRegisterOnly;
    private static boolean callSecondInit;
    private boolean finish = false;
    private int finishType = -1;

    /* renamed from: menion.android.whereyougo.gui.extension.activity.CustomMainActivity$1 */
    class C02921 implements Runnable {
        C02921() {
        }

        public void run() {
            try {
                ActivityManager aM = (ActivityManager) CustomMainActivity.this.getApplicationContext().getSystemService("activity");
                Thread.sleep(1250);
                aM.killBackgroundProcesses(CustomMainActivity.this.getPackageName());
            } catch (Exception e) {
                Logger.m22e(CustomMainActivity.TAG, "clearPackageFromMemory()", e);
            }
        }
    }

    /* renamed from: menion.android.whereyougo.gui.extension.activity.CustomMainActivity$2 */
    class C02942 implements Runnable {

        /* renamed from: menion.android.whereyougo.gui.extension.activity.CustomMainActivity$2$1 */
        class C02931 implements OnClickListener {
            C02931() {
            }

            public void onClick(DialogInterface dialog, int which) {
                if (CustomMainActivity.this.finishType == 0 || CustomMainActivity.this.finishType == 1) {
                    CustomMainActivity.this.finish = true;
                    CustomMainActivity.this.finish();
                } else if (CustomMainActivity.this.finishType == 2 || CustomMainActivity.this.finishType == 3 || CustomMainActivity.this.finishType == 4) {
                    CustomMainActivity.this.finish = true;
                    CustomMainActivity.this.finish();
                } else if (CustomMainActivity.this.finishType == 5) {
                    CustomMainActivity.this.showDialogFinish(1);
                }
            }
        }

        C02942() {
        }

        public void run() {
            boolean cancelable = true;
            String title = Locale.getString(C0254R.string.question);
            String message = "";
            if (CustomMainActivity.this.finishType == 3 || CustomMainActivity.this.finishType == 4 || CustomMainActivity.this.finishType == 5 || CustomMainActivity.this.finishType == 1) {
                cancelable = false;
            }
            switch (CustomMainActivity.this.finishType) {
                case 0:
                    message = Locale.getString(C0254R.string.do_you_really_want_to_exit);
                    break;
                case 1:
                    title = Locale.getString(C0254R.string.info);
                    message = Locale.getString(C0254R.string.you_have_to_exit_app_force);
                    break;
                case 2:
                    message = Locale.getString(C0254R.string.you_have_to_restart_app_recommended);
                    break;
                case 3:
                    title = Locale.getString(C0254R.string.info);
                    message = Locale.getString(C0254R.string.you_have_to_restart_app_force);
                    break;
                case 4:
                    title = Locale.getString(C0254R.string.info);
                    message = Locale.getString(C0254R.string.you_have_to_restart_app_force);
                    break;
                case 5:
                    title = Locale.getString(C0254R.string.info);
                    message = Locale.getString(C0254R.string.new_version_will_be_installed);
                    break;
            }
            Builder b = new Builder(CustomMainActivity.this);
            b.setCancelable(cancelable);
            b.setTitle(title);
            b.setIcon(C0254R.C0252drawable.ic_question_alt);
            b.setMessage(message);
            b.setPositiveButton(C0254R.string.f48ok, new C02931());
            if (cancelable) {
                b.setNegativeButton(C0254R.string.cancel, null);
            }
            b.show();
        }
    }

    /* renamed from: menion.android.whereyougo.gui.extension.activity.CustomMainActivity$3 */
    class C02953 implements OnClickListener {
        C02953() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    /* renamed from: menion.android.whereyougo.gui.extension.activity.CustomMainActivity$4 */
    class C02964 implements OnClickListener {
        C02964() {
        }

        public void onClick(DialogInterface dialog, int which) {
        }
    }

    public abstract void eventCreateLayout();

    public abstract void eventDestroyApp();

    public abstract void eventFirstInit();

    public abstract void eventRegisterOnly();

    public abstract void eventSecondInit();

    public abstract String getCloseAdditionalText();

    public abstract int getCloseValue();

    public static String getNewsFromTo(int lastVersion, int actualVersion) {
        String versionInfo = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
        String data = loadAssetString("news.xml");
        if (data == null || data.length() == 0) {
            data = loadAssetString("news.xml");
        }
        if (data != null && data.length() > 0) {
            try {
                XmlPullParser parser = XmlPullParserFactory.newInstance().newPullParser();
                parser.setInput(new StringReader(data));
                boolean correct = false;
                while (true) {
                    int event = parser.nextToken();
                    String tagName;
                    if (event == 2) {
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                            String name = parser.getAttributeValue(null, "name");
                            int id = Utils.parseInt(parser.getAttributeValue(null, "id"));
                            if (id <= lastVersion || id > actualVersion) {
                                correct = false;
                            } else {
                                correct = true;
                                versionInfo = versionInfo + "<h4>" + name + "</h4><ul>";
                            }
                        } else if (tagName.equalsIgnoreCase("li") && correct) {
                            versionInfo = versionInfo + "<li>" + parser.nextText() + "</li>";
                        }
                    } else if (event == 3) {
                        tagName = parser.getName();
                        if (tagName.equalsIgnoreCase("update")) {
                            if (correct) {
                                correct = false;
                                versionInfo = versionInfo + "</ul>";
                            }
                        } else if (tagName.equals("document")) {
                            break;
                        }
                    } else {
                        continue;
                    }
                }
            } catch (Exception e) {
                Logger.m22e(TAG, "getNews()", e);
            }
        }
        return versionInfo + "</body></html>";
    }

    public static String loadAssetString(String name) {
        String str;
        InputStream is = null;
        try {
            is = C0322A.getMain().getAssets().open(name);
            byte[] buffer = new byte[is.available()];
            is.read(buffer);
            is.close();
            str = new String(buffer);
        } catch (Exception e) {
            Logger.m22e(TAG, "loadAssetString(" + name + ")", e);
            str = "";
        } finally {
            Utils.closeStream(is);
        }
        return str;
    }

    private void clearPackageFromMemory() {
        new Thread(new C02921()).start();
    }

    public boolean dispatchKeyEvent(KeyEvent event) {
        if (event.getAction() == 0 && event.getKeyCode() == 4) {
            switch (getCloseValue()) {
                case 0:
                    this.finish = true;
                    finish();
                    return true;
                case 1:
                    showDialogFinish(0);
                    return true;
                case 2:
                    showDialogFinish(0);
                    return true;
            }
        }
        return super.dispatchKeyEvent(event);
    }

    public void finishForceSilent() {
        this.finish = true;
        finish();
    }

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        C0322A.registerMain(this);
        callSecondInit = false;
        callRegisterOnly = false;
        if (C0322A.getApp() == null) {
            C0322A.registerApp((MainApplication) getApplication());
            testFileSystem();
            if (Utils.isPermissionAllowed("android.permission.ACCESS_FINE_LOCATION") && (Preferences.GPS || Preferences.GPS_START_AUTOMATICALLY)) {
                LocationState.setGpsOn(this);
            } else {
                LocationState.setGpsOff(this);
            }
            eventFirstInit();
            CustomActivity.setScreenBasic(this);
            eventCreateLayout();
            callSecondInit = true;
            return;
        }
        CustomActivity.setScreenBasic(this);
        eventCreateLayout();
        callRegisterOnly = true;
    }

    public void onDestroy() {
        if (this.finish) {
            Debug.stopMethodTracing();
            boolean clearPackageAllowed = Utils.isPermissionAllowed("android.permission.KILL_BACKGROUND_PROCESSES");
            eventDestroyApp();
            PreferenceValues.disableWakeLock();
            PreferenceValues.setLastKnownLocation();
            LocationState.destroy(this);
            C0322A.destroy();
            super.onDestroy();
            if (clearPackageAllowed) {
                clearPackageFromMemory();
                return;
            }
            return;
        }
        super.onDestroy();
    }

    public void onResumeExtra() {
        if (callSecondInit) {
            callSecondInit = false;
            eventSecondInit();
        }
        if (callRegisterOnly) {
            callRegisterOnly = false;
            eventRegisterOnly();
        }
    }

    public void showDialogFinish(int typeOfFinish) {
        if (typeOfFinish != -1) {
            this.finishType = typeOfFinish;
            runOnUiThread(new C02942());
        }
    }

    /* Access modifiers changed, original: protected */
    public boolean testFileSystem() {
        int i = 0;
        if (DIRS == null || DIRS.length == 0) {
            return true;
        }
        if (FileSystem.createRoot(MainApplication.APP_NAME)) {
            String[] strArr = DIRS;
            int length = strArr.length;
            while (i < length) {
                new File(strArr[i]).mkdirs();
                i++;
            }
            return true;
        }
        UtilsGUI.showDialogError((Activity) this, (int) C0254R.string.filesystem_cannot_create_root, new C02953());
        return false;
    }

    /* Access modifiers changed, original: protected */
    public boolean testFreeSpace() {
        if (DIRS == null || DIRS.length == 0) {
            return true;
        }
        try {
            StatFs stat = new StatFs(FileSystem.ROOT);
            long megFree = (((long) stat.getBlockSize()) * ((long) stat.getAvailableBlocks())) / 1048576;
            if (megFree <= 0 || megFree >= 5) {
                return true;
            }
            UtilsGUI.showDialogError((Activity) this, getString(C0254R.string.not_enough_disk_space_x, new Object[]{FileSystem.ROOT, megFree + "MB"}), new C02964());
            return false;
        } catch (Exception e) {
            return false;
        }
    }
}
