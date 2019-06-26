package org.telegram.messenger;

import android.annotation.SuppressLint;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.res.Configuration;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.NetworkInfo.State;
import android.os.Build.VERSION;
import android.os.Handler;
import android.os.PowerManager;
import android.util.Log;
import java.io.File;
import org.telegram.p004ui.Components.ForegroundDetector;
import org.telegram.tgnet.ConnectionsManager;
import org.telegram.tgnet.TLRPC.User;

public class ApplicationLoader extends Application {
    @SuppressLint({"StaticFieldLeak"})
    public static volatile Context applicationContext = null;
    public static volatile Handler applicationHandler = null;
    private static volatile boolean applicationInited = false;
    private static ConnectivityManager connectivityManager = null;
    public static volatile NetworkInfo currentNetworkInfo = null;
    public static volatile boolean externalInterfacePaused = true;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    private static PendingIntent pendingIntent;
    public static volatile boolean unableGetCurrentNetwork;

    /* renamed from: org.telegram.messenger.ApplicationLoader$1 */
    static class C10181 extends BroadcastReceiver {
        C10181() {
        }

        public void onReceive(Context context, Intent intent) {
            try {
                ApplicationLoader.currentNetworkInfo = ApplicationLoader.connectivityManager.getActiveNetworkInfo();
            } catch (Throwable unused) {
            }
            boolean isConnectionSlow = ApplicationLoader.isConnectionSlow();
            for (int i = 0; i < 3; i++) {
                ConnectionsManager.getInstance(i).checkConnection();
                FileLoader.getInstance(i).onNetworkChanged(isConnectionSlow);
            }
        }
    }

    public static File getFilesDirFixed() {
        File filesDir;
        for (int i = 0; i < 10; i++) {
            filesDir = applicationContext.getFilesDir();
            if (filesDir != null) {
                return filesDir;
            }
        }
        try {
            filesDir = new File(applicationContext.getApplicationInfo().dataDir, "files");
            filesDir.mkdirs();
            return filesDir;
        } catch (Exception e) {
            FileLog.m30e(e);
            return new File("/data/data/org.telegram.messenger/files");
        }
    }

    public static void postInitApplication() {
        if (!applicationInited) {
            applicationInited = true;
            try {
                LocaleController.getInstance();
            } catch (Exception e) {
                e.printStackTrace();
            }
            try {
                connectivityManager = (ConnectivityManager) applicationContext.getSystemService("connectivity");
                applicationContext.registerReceiver(new C10181(), new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
            } catch (Exception e2) {
                e2.printStackTrace();
            }
            try {
                IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
                intentFilter.addAction("android.intent.action.SCREEN_OFF");
                applicationContext.registerReceiver(new ScreenReceiver(), intentFilter);
            } catch (Exception e22) {
                e22.printStackTrace();
            }
            try {
                isScreenOn = ((PowerManager) applicationContext.getSystemService("power")).isScreenOn();
                if (BuildVars.LOGS_ENABLED) {
                    StringBuilder stringBuilder = new StringBuilder();
                    stringBuilder.append("screen state = ");
                    stringBuilder.append(isScreenOn);
                    FileLog.m27d(stringBuilder.toString());
                }
            } catch (Exception e3) {
                FileLog.m30e(e3);
            }
            SharedConfig.loadConfig();
            for (int i = 0; i < 3; i++) {
                UserConfig.getInstance(i).loadConfig();
                MessagesController.getInstance(i);
                ConnectionsManager.getInstance(i);
                User currentUser = UserConfig.getInstance(i).getCurrentUser();
                if (currentUser != null) {
                    MessagesController.getInstance(i).putUser(currentUser, true);
                    MessagesController.getInstance(i).getBlockedUsers(true);
                    SendMessagesHelper.getInstance(i).checkUnsentMessages();
                }
            }
            ApplicationLoader applicationLoader = (ApplicationLoader) applicationContext;
            if (BuildVars.LOGS_ENABLED) {
                FileLog.m27d("app initied");
            }
            MediaController.getInstance();
            for (int i2 = 0; i2 < 3; i2++) {
                ContactsController.getInstance(i2).checkAppAccount();
                DownloadController.getInstance(i2);
            }
        }
    }

    public void onCreate() {
        try {
            applicationContext = getApplicationContext();
        } catch (Throwable unused) {
        }
        super.onCreate();
        if (applicationContext == null) {
            applicationContext = getApplicationContext();
        }
        NativeLoader.initNativeLibs(applicationContext);
        ConnectionsManager.native_setJava(false);
        ForegroundDetector foregroundDetector = new ForegroundDetector(this);
        applicationHandler = new Handler(applicationContext.getMainLooper());
        AndroidUtilities.runOnUIThread(C1015-$$Lambda$r7IJ1lCIETKiJtvF21QqYn99iv8.INSTANCE);
    }

    public static void startPushService() {
        if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
            String str = "TFOSS";
            Log.d(str, "Trying to start push service every 10 minutes");
            AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService("alarm");
            pendingIntent = PendingIntent.getBroadcast(applicationContext, 0, new Intent(applicationContext, ApplicationLoader.class), 0);
            alarmManager.cancel(pendingIntent);
            alarmManager.setRepeating(0, System.currentTimeMillis(), 60000, pendingIntent);
            try {
                Log.d(str, "Starting push service...");
                if (VERSION.SDK_INT >= 26) {
                    applicationContext.startForegroundService(new Intent(applicationContext, NotificationsService.class));
                    return;
                } else {
                    applicationContext.startService(new Intent(applicationContext, NotificationsService.class));
                    return;
                }
            } catch (Throwable unused) {
                Log.d(str, "Failed to start push service");
                return;
            }
        }
        stopPushService();
    }

    public static void stopPushService() {
        applicationContext.stopService(new Intent(applicationContext, NotificationsService.class));
        PendingIntent service = PendingIntent.getService(applicationContext, 0, new Intent(applicationContext, NotificationsService.class), 0);
        AlarmManager alarmManager = (AlarmManager) applicationContext.getSystemService("alarm");
        alarmManager.cancel(service);
        service = pendingIntent;
        if (service != null) {
            alarmManager.cancel(service);
        }
    }

    public void onConfigurationChanged(Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            LocaleController.getInstance().onDeviceConfigurationChange(configuration);
            AndroidUtilities.checkDisplaySize(applicationContext, configuration);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static boolean isRoaming() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isRoaming();
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        return false;
    }

    public static boolean isConnectedOrConnectingToWiFi() {
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            State state = networkInfo.getState();
            if (networkInfo != null && (state == State.CONNECTED || state == State.CONNECTING || state == State.SUSPENDED)) {
                return true;
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        return false;
    }

    public static boolean isConnectedToWiFi() {
        try {
            NetworkInfo networkInfo = ((ConnectivityManager) applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            if (networkInfo != null && networkInfo.getState() == State.CONNECTED) {
                return true;
            }
        } catch (Exception e) {
            FileLog.m30e(e);
        }
        return false;
    }

    public static int getCurrentNetworkType() {
        if (isConnectedOrConnectingToWiFi()) {
            return 1;
        }
        return isRoaming() ? 2 : 0;
    }

    public static boolean isConnectionSlow() {
        try {
            NetworkInfo activeNetworkInfo = ((ConnectivityManager) applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo.getType() == 0) {
                int subtype = activeNetworkInfo.getSubtype();
                if (subtype == 1 || subtype == 2 || subtype == 4 || subtype == 7 || subtype == 11) {
                    return true;
                }
            }
        } catch (Throwable unused) {
        }
        return false;
    }

    public static boolean isNetworkOnline() {
        try {
            ConnectivityManager connectivityManager = (ConnectivityManager) applicationContext.getSystemService("connectivity");
            NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && (activeNetworkInfo.isConnectedOrConnecting() || activeNetworkInfo.isAvailable())) {
                return true;
            }
            NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
            NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
            if (networkInfo2 == null || !networkInfo2.isConnectedOrConnecting()) {
                return false;
            }
            return true;
        } catch (Exception e) {
            FileLog.m30e(e);
            return true;
        }
    }
}
