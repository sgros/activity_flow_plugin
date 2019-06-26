// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import org.telegram.ui.Components.ForegroundDetector;
import android.content.res.Configuration;
import android.os.Build$VERSION;
import android.app.AlarmManager;
import android.util.Log;
import org.telegram.tgnet.TLRPC;
import android.os.PowerManager;
import android.content.IntentFilter;
import org.telegram.tgnet.ConnectionsManager;
import android.content.Intent;
import android.content.BroadcastReceiver;
import android.net.NetworkInfo$State;
import java.io.File;
import android.app.PendingIntent;
import android.net.NetworkInfo;
import android.net.ConnectivityManager;
import android.os.Handler;
import android.annotation.SuppressLint;
import android.content.Context;
import android.app.Application;

public class ApplicationLoader extends Application
{
    @SuppressLint({ "StaticFieldLeak" })
    public static volatile Context applicationContext;
    public static volatile Handler applicationHandler;
    private static volatile boolean applicationInited = false;
    private static ConnectivityManager connectivityManager;
    public static volatile NetworkInfo currentNetworkInfo;
    public static volatile boolean externalInterfacePaused = true;
    public static volatile boolean isScreenOn = false;
    public static volatile boolean mainInterfacePaused = true;
    public static volatile boolean mainInterfacePausedStageQueue = true;
    public static volatile long mainInterfacePausedStageQueueTime;
    private static PendingIntent pendingIntent;
    public static volatile boolean unableGetCurrentNetwork;
    
    public static int getCurrentNetworkType() {
        if (isConnectedOrConnectingToWiFi()) {
            return 1;
        }
        if (isRoaming()) {
            return 2;
        }
        return 0;
    }
    
    public static File getFilesDirFixed() {
        for (int i = 0; i < 10; ++i) {
            final File filesDir = ApplicationLoader.applicationContext.getFilesDir();
            if (filesDir != null) {
                return filesDir;
            }
        }
        try {
            final File file = new File(ApplicationLoader.applicationContext.getApplicationInfo().dataDir, "files");
            file.mkdirs();
            return file;
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return new File("/data/data/org.telegram.messenger/files");
        }
    }
    
    public static boolean isConnectedOrConnectingToWiFi() {
        try {
            final NetworkInfo networkInfo = ((ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            final NetworkInfo$State state = networkInfo.getState();
            if (networkInfo != null && (state == NetworkInfo$State.CONNECTED || state == NetworkInfo$State.CONNECTING || state == NetworkInfo$State.SUSPENDED)) {
                return true;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return false;
    }
    
    public static boolean isConnectedToWiFi() {
        try {
            final NetworkInfo networkInfo = ((ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity")).getNetworkInfo(1);
            if (networkInfo != null && networkInfo.getState() == NetworkInfo$State.CONNECTED) {
                return true;
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return false;
    }
    
    public static boolean isConnectionSlow() {
        try {
            final NetworkInfo activeNetworkInfo = ((ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo.getType() == 0) {
                final int subtype = activeNetworkInfo.getSubtype();
                if (subtype == 1 || subtype == 2 || subtype == 4 || subtype == 7 || subtype == 11) {
                    return true;
                }
            }
            return false;
        }
        catch (Throwable t) {
            return false;
        }
    }
    
    public static boolean isNetworkOnline() {
        try {
            final ConnectivityManager connectivityManager = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
            final NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
            if (activeNetworkInfo != null && (activeNetworkInfo.isConnectedOrConnecting() || activeNetworkInfo.isAvailable())) {
                return true;
            }
            final NetworkInfo networkInfo = connectivityManager.getNetworkInfo(0);
            if (networkInfo != null && networkInfo.isConnectedOrConnecting()) {
                return true;
            }
            final NetworkInfo networkInfo2 = connectivityManager.getNetworkInfo(1);
            return networkInfo2 != null && networkInfo2.isConnectedOrConnecting();
        }
        catch (Exception ex) {
            FileLog.e(ex);
            return true;
        }
    }
    
    public static boolean isRoaming() {
        try {
            final NetworkInfo activeNetworkInfo = ((ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity")).getActiveNetworkInfo();
            if (activeNetworkInfo != null) {
                return activeNetworkInfo.isRoaming();
            }
        }
        catch (Exception ex) {
            FileLog.e(ex);
        }
        return false;
    }
    
    public static void postInitApplication() {
        if (ApplicationLoader.applicationInited) {
            return;
        }
        ApplicationLoader.applicationInited = true;
        try {
            LocaleController.getInstance();
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
        try {
            ApplicationLoader.connectivityManager = (ConnectivityManager)ApplicationLoader.applicationContext.getSystemService("connectivity");
            ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)new BroadcastReceiver() {
                public void onReceive(final Context context, final Intent intent) {
                    while (true) {
                        try {
                            ApplicationLoader.currentNetworkInfo = ApplicationLoader.connectivityManager.getActiveNetworkInfo();
                            final boolean connectionSlow = ApplicationLoader.isConnectionSlow();
                            for (int i = 0; i < 3; ++i) {
                                ConnectionsManager.getInstance(i).checkConnection();
                                FileLoader.getInstance(i).onNetworkChanged(connectionSlow);
                            }
                        }
                        catch (Throwable t) {
                            continue;
                        }
                        break;
                    }
                }
            }, new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE"));
        }
        catch (Exception ex2) {
            ex2.printStackTrace();
        }
        try {
            final IntentFilter intentFilter = new IntentFilter("android.intent.action.SCREEN_ON");
            intentFilter.addAction("android.intent.action.SCREEN_OFF");
            ApplicationLoader.applicationContext.registerReceiver((BroadcastReceiver)new ScreenReceiver(), intentFilter);
        }
        catch (Exception ex3) {
            ex3.printStackTrace();
        }
        try {
            ApplicationLoader.isScreenOn = ((PowerManager)ApplicationLoader.applicationContext.getSystemService("power")).isScreenOn();
            if (BuildVars.LOGS_ENABLED) {
                final StringBuilder sb = new StringBuilder();
                sb.append("screen state = ");
                sb.append(ApplicationLoader.isScreenOn);
                FileLog.d(sb.toString());
            }
        }
        catch (Exception ex4) {
            FileLog.e(ex4);
        }
        SharedConfig.loadConfig();
        final int n = 0;
        for (int i = 0; i < 3; ++i) {
            UserConfig.getInstance(i).loadConfig();
            MessagesController.getInstance(i);
            ConnectionsManager.getInstance(i);
            final TLRPC.User currentUser = UserConfig.getInstance(i).getCurrentUser();
            if (currentUser != null) {
                MessagesController.getInstance(i).putUser(currentUser, true);
                MessagesController.getInstance(i).getBlockedUsers(true);
                SendMessagesHelper.getInstance(i).checkUnsentMessages();
            }
        }
        final ApplicationLoader applicationLoader = (ApplicationLoader)ApplicationLoader.applicationContext;
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("app initied");
        }
        MediaController.getInstance();
        for (int j = n; j < 3; ++j) {
            ContactsController.getInstance(j).checkAppAccount();
            DownloadController.getInstance(j);
        }
    }
    
    public static void startPushService() {
        if (MessagesController.getGlobalNotificationsSettings().getBoolean("pushService", true)) {
            Log.d("TFOSS", "Trying to start push service every 10 minutes");
            final AlarmManager alarmManager = (AlarmManager)ApplicationLoader.applicationContext.getSystemService("alarm");
            alarmManager.cancel(ApplicationLoader.pendingIntent = PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class)ApplicationLoader.class), 0));
            alarmManager.setRepeating(0, System.currentTimeMillis(), 60000L, ApplicationLoader.pendingIntent);
            try {
                Log.d("TFOSS", "Starting push service...");
                if (Build$VERSION.SDK_INT >= 26) {
                    ApplicationLoader.applicationContext.startForegroundService(new Intent(ApplicationLoader.applicationContext, (Class)NotificationsService.class));
                }
                else {
                    ApplicationLoader.applicationContext.startService(new Intent(ApplicationLoader.applicationContext, (Class)NotificationsService.class));
                }
            }
            catch (Throwable t) {
                Log.d("TFOSS", "Failed to start push service");
            }
        }
        else {
            stopPushService();
        }
    }
    
    public static void stopPushService() {
        ApplicationLoader.applicationContext.stopService(new Intent(ApplicationLoader.applicationContext, (Class)NotificationsService.class));
        final PendingIntent service = PendingIntent.getService(ApplicationLoader.applicationContext, 0, new Intent(ApplicationLoader.applicationContext, (Class)NotificationsService.class), 0);
        final AlarmManager alarmManager = (AlarmManager)ApplicationLoader.applicationContext.getSystemService("alarm");
        alarmManager.cancel(service);
        final PendingIntent pendingIntent = ApplicationLoader.pendingIntent;
        if (pendingIntent != null) {
            alarmManager.cancel(pendingIntent);
        }
    }
    
    public void onConfigurationChanged(final Configuration configuration) {
        super.onConfigurationChanged(configuration);
        try {
            LocaleController.getInstance().onDeviceConfigurationChange(configuration);
            AndroidUtilities.checkDisplaySize(ApplicationLoader.applicationContext, configuration);
        }
        catch (Exception ex) {
            ex.printStackTrace();
        }
    }
    
    public void onCreate() {
        while (true) {
            try {
                ApplicationLoader.applicationContext = this.getApplicationContext();
                super.onCreate();
                if (ApplicationLoader.applicationContext == null) {
                    ApplicationLoader.applicationContext = this.getApplicationContext();
                }
                NativeLoader.initNativeLibs(ApplicationLoader.applicationContext);
                ConnectionsManager.native_setJava(false);
                new ForegroundDetector(this);
                ApplicationLoader.applicationHandler = new Handler(ApplicationLoader.applicationContext.getMainLooper());
                AndroidUtilities.runOnUIThread((Runnable)_$$Lambda$r7IJ1lCIETKiJtvF21QqYn99iv8.INSTANCE);
            }
            catch (Throwable t) {
                continue;
            }
            break;
        }
    }
}
