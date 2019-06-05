package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.annotation.TargetApi;
import android.app.NotificationManager;
import android.app.Service;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Binder;
import android.os.Build.VERSION;
import android.os.IBinder;
import android.support.p000v4.app.NotificationCompat.Builder;
import android.util.Log;
import org.secuso.privacyfriendlynetmonitor.Activities.MainActivity;
import org.secuso.privacyfriendlynetmonitor.Assistant.Const;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import org.secuso.privacyfriendlynetmonitor.C0501R;

public class PassiveService extends Service {
    private static final int SERVICE_IDENTIFIER = 1;
    public static boolean mInterrupt;
    private final IBinder mBinder = new AnalyzerBinder();
    Builder mBuilder = new Builder(this).setSmallIcon(C0501R.C0498drawable.ic_notification).setContentTitle(getVersionString(C0501R.string.app_name)).setContentText(getVersionString(C0501R.string.bg_desc));
    private Bitmap mIcon;
    private Thread mThread;

    /* renamed from: org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis.PassiveService$1 */
    class C04961 implements Runnable {
        C04961() {
        }

        public void run() {
            while (!PassiveService.mInterrupt) {
                try {
                    Detector.updateReportMap();
                    Collector.updateSettings();
                    if (Collector.isCertVal.booleanValue()) {
                        Collector.updateCertVal();
                    }
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            if (PassiveService.mInterrupt) {
                PassiveService.this.mThread.interrupt();
            }
            PassiveService.this.stopSelf();
        }
    }

    public class AnalyzerBinder extends Binder {
        /* Access modifiers changed, original: 0000 */
        public PassiveService getService() {
            return PassiveService.this;
        }
    }

    private String getVersionString(int i) {
        if (VERSION.SDK_INT >= 21) {
            return getStringNew(i);
        }
        return getStringOld(i);
    }

    @TargetApi(9)
    private static String getStringNew(int i) {
        return RunStore.getContext().getResources().getString(i);
    }

    @TargetApi(21)
    private static String getStringOld(int i) {
        return RunStore.getContext().getString(i);
    }

    public void onCreate() {
        mInterrupt = false;
        loadNotificationBitmaps();
        showAppNotification();
        KnownPorts.initPortMap();
    }

    private void loadNotificationBitmaps() {
        this.mIcon = BitmapFactory.decodeResource(getResources(), C0501R.mipmap.ic_drawer);
    }

    public void startThread() {
        Log.i(Const.LOG_TAG, "PassiveService - Thread started");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
        this.mThread = new Thread(new C04961(), "AnalyzerThreadRunnable");
        this.mThread.start();
    }

    private void interrupt() {
        mInterrupt = true;
        stopSelf();
    }

    public IBinder onBind(Intent intent) {
        startThread();
        return this.mBinder;
    }

    public boolean onUnbind(Intent intent) {
        interrupt();
        return super.onUnbind(intent);
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        startThread();
        return 1;
    }

    public void onDestroy() {
        interrupt();
        super.onDestroy();
    }

    public void onTaskRemoved(Intent intent) {
        stopSelf();
    }

    private void showAppNotification() {
        this.mBuilder.setSmallIcon(C0501R.C0498drawable.ic_notification);
        this.mBuilder.setLargeIcon(this.mIcon);
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder create = TaskStackBuilder.create(this);
        create.addParentStack(MainActivity.class);
        create.addNextIntent(intent);
        this.mBuilder.setContentIntent(create.getPendingIntent(0, 134217728));
        startForeground(1, this.mBuilder.build());
    }

    private void showWarningNotification() {
        Intent intent = new Intent(this, MainActivity.class);
        TaskStackBuilder create = TaskStackBuilder.create(this);
        create.addParentStack(MainActivity.class);
        create.addNextIntent(intent);
        this.mBuilder.setContentIntent(create.getPendingIntent(0, 134217728));
        ((NotificationManager) getSystemService("notification")).notify(Const.LOG_TAG, 1, this.mBuilder.build());
    }
}
