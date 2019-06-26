// 
// Decompiled by Procyon v0.5.34
// 

package menion.android.whereyougo.gui.extension.activity;

import java.io.InputStream;
import android.os.StatFs;
import menion.android.whereyougo.gui.utils.UtilsGUI;
import java.io.File;
import android.content.DialogInterface;
import android.content.DialogInterface$OnClickListener;
import android.app.AlertDialog$Builder;
import menion.android.whereyougo.preferences.Locale;
import menion.android.whereyougo.preferences.PreferenceValues;
import android.os.Debug;
import android.app.Activity;
import android.content.Context;
import menion.android.whereyougo.geo.location.LocationState;
import menion.android.whereyougo.preferences.Preferences;
import menion.android.whereyougo.MainApplication;
import android.os.Bundle;
import android.view.KeyEvent;
import java.io.Closeable;
import menion.android.whereyougo.utils.A;
import org.xmlpull.v1.XmlPullParser;
import menion.android.whereyougo.utils.Utils;
import java.io.Reader;
import java.io.StringReader;
import org.xmlpull.v1.XmlPullParserFactory;
import menion.android.whereyougo.utils.Logger;
import android.app.ActivityManager;
import menion.android.whereyougo.utils.FileSystem;

public abstract class CustomMainActivity extends CustomActivity
{
    public static final int CLOSE_DESTROY_APP_DIALOG_ADDITIONAL_TEXT = 2;
    public static final int CLOSE_DESTROY_APP_DIALOG_NO_TEXT = 1;
    public static final int CLOSE_DESTROY_APP_NO_DIALOG = 0;
    public static final int CLOSE_HIDE_APP = 3;
    private static final String[] DIRS;
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
    private boolean finish;
    private int finishType;
    
    static {
        DIRS = new String[] { FileSystem.CACHE };
    }
    
    public CustomMainActivity() {
        this.finishType = -1;
        this.finish = false;
    }
    
    private void clearPackageFromMemory() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    final ActivityManager activityManager = (ActivityManager)CustomMainActivity.this.getApplicationContext().getSystemService("activity");
                    Thread.sleep(1250L);
                    activityManager.killBackgroundProcesses(CustomMainActivity.this.getPackageName());
                }
                catch (Exception ex) {
                    Logger.e("CustomMain", "clearPackageFromMemory()", ex);
                }
            }
        }).start();
    }
    
    public static String getNewsFromTo(final int n, final int n2) {
        String str = "<html><head><meta http-equiv=\"Content-Type\" content=\"text/html; charset=utf-8\" /></head><body>";
        final String loadAssetString = loadAssetString("news.xml");
        String loadAssetString2 = null;
        Label_0030: {
            if (loadAssetString != null) {
                loadAssetString2 = loadAssetString;
                if (loadAssetString.length() != 0) {
                    break Label_0030;
                }
            }
            loadAssetString2 = loadAssetString("news.xml");
        }
        String s = str;
        Label_0378: {
            if (loadAssetString2 == null) {
                break Label_0378;
            }
            s = str;
            if (loadAssetString2.length() <= 0) {
                break Label_0378;
            }
            s = str;
            try {
                final XmlPullParser pullParser = XmlPullParserFactory.newInstance().newPullParser();
                s = str;
                s = str;
                final StringReader input = new StringReader(loadAssetString2);
                s = str;
                pullParser.setInput((Reader)input);
                int n3 = 0;
                while (true) {
                    s = str;
                    final int nextToken = pullParser.nextToken();
                    if (nextToken == 2) {
                        s = str;
                        final String name = pullParser.getName();
                        s = str;
                        if (name.equalsIgnoreCase("update")) {
                            s = str;
                            final String attributeValue = pullParser.getAttributeValue((String)null, "name");
                            s = str;
                            final int int1 = Utils.parseInt(pullParser.getAttributeValue((String)null, "id"));
                            if (int1 > n && int1 <= n2) {
                                n3 = 1;
                                s = str;
                                s = str;
                                final StringBuilder sb = new StringBuilder();
                                s = str;
                                str = sb.append(str).append("<h4>").append(attributeValue).append("</h4><ul>").toString();
                            }
                            else {
                                n3 = 0;
                            }
                        }
                        else {
                            s = str;
                            if (!name.equalsIgnoreCase("li") || n3 == 0) {
                                continue;
                            }
                            s = str;
                            s = str;
                            final StringBuilder sb2 = new StringBuilder();
                            s = str;
                            str = sb2.append(str).append("<li>").append(pullParser.nextText()).append("</li>").toString();
                        }
                    }
                    else {
                        if (nextToken != 3) {
                            continue;
                        }
                        s = str;
                        final String name2 = pullParser.getName();
                        s = str;
                        if (name2.equalsIgnoreCase("update")) {
                            if (n3 == 0) {
                                continue;
                            }
                            n3 = 0;
                            s = str;
                            s = str;
                            final StringBuilder sb3 = new StringBuilder();
                            s = str;
                            str = sb3.append(str).append("</ul>").toString();
                        }
                        else {
                            s = str;
                            if (name2.equals("document")) {
                                break;
                            }
                            continue;
                        }
                    }
                }
                s = str;
                return s + "</body></html>";
            }
            catch (Exception ex) {
                Logger.e("CustomMain", "getNews()", ex);
                return s + "</body></html>";
            }
        }
    }
    
    public static String loadAssetString(String str) {
        Closeable closeable = null;
        Closeable open = null;
        try {
            final Closeable closeable2 = closeable = (open = A.getMain().getAssets().open(str));
            final byte[] array = new byte[((InputStream)closeable2).available()];
            open = closeable2;
            closeable = closeable2;
            ((InputStream)closeable2).read(array);
            open = closeable2;
            closeable = closeable2;
            ((InputStream)closeable2).close();
            open = closeable2;
            closeable = closeable2;
            open = closeable2;
            closeable = closeable2;
            final String s = new String(array);
            Utils.closeStream(closeable2);
            str = s;
            return str;
        }
        catch (Exception ex) {
            closeable = open;
            closeable = open;
            final StringBuilder sb = new StringBuilder();
            closeable = open;
            Logger.e("CustomMain", sb.append("loadAssetString(").append(str).append(")").toString(), ex);
            str = "";
            Utils.closeStream(open);
            return str;
        }
        finally {
            Utils.closeStream(closeable);
        }
    }
    
    public boolean dispatchKeyEvent(final KeyEvent keyEvent) {
        boolean dispatchKeyEvent = true;
        if (keyEvent.getAction() == 0 && keyEvent.getKeyCode() == 4) {
            switch (this.getCloseValue()) {
                case 0: {
                    this.finish = true;
                    this.finish();
                    return dispatchKeyEvent;
                }
                case 1: {
                    this.showDialogFinish(0);
                    return dispatchKeyEvent;
                }
                case 2: {
                    this.showDialogFinish(0);
                    return dispatchKeyEvent;
                }
            }
        }
        dispatchKeyEvent = super.dispatchKeyEvent(keyEvent);
        return dispatchKeyEvent;
    }
    
    protected abstract void eventCreateLayout();
    
    protected abstract void eventDestroyApp();
    
    protected abstract void eventFirstInit();
    
    protected abstract void eventRegisterOnly();
    
    protected abstract void eventSecondInit();
    
    public void finishForceSilent() {
        this.finish = true;
        this.finish();
    }
    
    protected abstract String getCloseAdditionalText();
    
    protected abstract int getCloseValue();
    
    @Override
    public void onCreate(final Bundle bundle) {
        super.onCreate(bundle);
        A.registerMain(this);
        CustomMainActivity.callSecondInit = false;
        CustomMainActivity.callRegisterOnly = false;
        if (A.getApp() == null) {
            A.registerApp((MainApplication)this.getApplication());
            this.testFileSystem();
            if (Utils.isPermissionAllowed("android.permission.ACCESS_FINE_LOCATION") && (Preferences.GPS || Preferences.GPS_START_AUTOMATICALLY)) {
                LocationState.setGpsOn((Context)this);
            }
            else {
                LocationState.setGpsOff((Context)this);
            }
            this.eventFirstInit();
            CustomActivity.setScreenBasic(this);
            this.eventCreateLayout();
            CustomMainActivity.callSecondInit = true;
        }
        else {
            CustomActivity.setScreenBasic(this);
            this.eventCreateLayout();
            CustomMainActivity.callRegisterOnly = true;
        }
    }
    
    @Override
    public void onDestroy() {
        if (this.finish) {
            Debug.stopMethodTracing();
            final boolean permissionAllowed = Utils.isPermissionAllowed("android.permission.KILL_BACKGROUND_PROCESSES");
            this.eventDestroyApp();
            PreferenceValues.disableWakeLock();
            PreferenceValues.setLastKnownLocation();
            LocationState.destroy((Context)this);
            A.destroy();
            super.onDestroy();
            if (permissionAllowed) {
                this.clearPackageFromMemory();
            }
        }
        else {
            super.onDestroy();
        }
    }
    
    public void onResumeExtra() {
        if (CustomMainActivity.callSecondInit) {
            CustomMainActivity.callSecondInit = false;
            this.eventSecondInit();
        }
        if (CustomMainActivity.callRegisterOnly) {
            CustomMainActivity.callRegisterOnly = false;
            this.eventRegisterOnly();
        }
    }
    
    public void showDialogFinish(final int finishType) {
        if (finishType != -1) {
            this.finishType = finishType;
            this.runOnUiThread((Runnable)new Runnable() {
                @Override
                public void run() {
                    boolean cancelable = true;
                    String title = Locale.getString(2131165302);
                    String message = "";
                    if (CustomMainActivity.this.finishType == 3 || CustomMainActivity.this.finishType == 4 || CustomMainActivity.this.finishType == 5 || CustomMainActivity.this.finishType == 1) {
                        cancelable = false;
                    }
                    switch (CustomMainActivity.this.finishType) {
                        case 0: {
                            message = Locale.getString(2131165198);
                            break;
                        }
                        case 1: {
                            title = Locale.getString(2131165213);
                            message = Locale.getString(2131165318);
                            break;
                        }
                        case 2: {
                            message = Locale.getString(2131165320);
                            break;
                        }
                        case 3: {
                            title = Locale.getString(2131165213);
                            message = Locale.getString(2131165319);
                            break;
                        }
                        case 4: {
                            title = Locale.getString(2131165213);
                            message = Locale.getString(2131165319);
                            break;
                        }
                        case 5: {
                            title = Locale.getString(2131165213);
                            message = Locale.getString(2131165223);
                            break;
                        }
                    }
                    final AlertDialog$Builder alertDialog$Builder = new AlertDialog$Builder((Context)CustomMainActivity.this);
                    alertDialog$Builder.setCancelable(cancelable);
                    alertDialog$Builder.setTitle((CharSequence)title);
                    alertDialog$Builder.setIcon(2130837546);
                    alertDialog$Builder.setMessage((CharSequence)message);
                    alertDialog$Builder.setPositiveButton(2131165230, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                            if (CustomMainActivity.this.finishType == 0 || CustomMainActivity.this.finishType == 1) {
                                CustomMainActivity.this.finish = true;
                                CustomMainActivity.this.finish();
                            }
                            else if (CustomMainActivity.this.finishType == 2 || CustomMainActivity.this.finishType == 3 || CustomMainActivity.this.finishType == 4) {
                                CustomMainActivity.this.finish = true;
                                CustomMainActivity.this.finish();
                            }
                            else if (CustomMainActivity.this.finishType == 5) {
                                CustomMainActivity.this.showDialogFinish(1);
                            }
                        }
                    });
                    if (cancelable) {
                        alertDialog$Builder.setNegativeButton(2131165190, (DialogInterface$OnClickListener)null);
                    }
                    alertDialog$Builder.show();
                }
            });
        }
    }
    
    protected boolean testFileSystem() {
        final boolean b = true;
        int n = 0;
        boolean b2 = b;
        if (CustomMainActivity.DIRS != null) {
            if (CustomMainActivity.DIRS.length == 0) {
                b2 = b;
            }
            else if (FileSystem.createRoot("WhereYouGo")) {
                final String[] dirs = CustomMainActivity.DIRS;
                final int length = dirs.length;
                while (true) {
                    b2 = b;
                    if (n >= length) {
                        break;
                    }
                    new File(dirs[n]).mkdirs();
                    ++n;
                }
            }
            else {
                UtilsGUI.showDialogError(this, 2131165203, (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                    public void onClick(final DialogInterface dialogInterface, final int n) {
                    }
                });
                b2 = false;
            }
        }
        return b2;
    }
    
    protected boolean testFreeSpace() {
        boolean b = false;
        if (CustomMainActivity.DIRS == null || CustomMainActivity.DIRS.length == 0) {
            b = true;
        }
        else {
            Label_0125: {
                try {
                    final StatFs statFs = new StatFs(FileSystem.ROOT);
                    final long lng = statFs.getBlockSize() * (long)statFs.getAvailableBlocks() / 1048576L;
                    if (lng <= 0L || lng >= 5L) {
                        break Label_0125;
                    }
                    UtilsGUI.showDialogError(this, this.getString(2131165229, new Object[] { FileSystem.ROOT, lng + "MB" }), (DialogInterface$OnClickListener)new DialogInterface$OnClickListener() {
                        public void onClick(final DialogInterface dialogInterface, final int n) {
                        }
                    });
                }
                catch (Exception ex) {}
                return b;
            }
            b = true;
        }
        return b;
    }
}
