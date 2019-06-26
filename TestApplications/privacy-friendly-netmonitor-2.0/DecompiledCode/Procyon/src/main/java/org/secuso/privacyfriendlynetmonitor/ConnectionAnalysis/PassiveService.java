// 
// Decompiled by Procyon v0.5.34
// 

package org.secuso.privacyfriendlynetmonitor.ConnectionAnalysis;

import android.os.Binder;
import android.util.Log;
import org.secuso.privacyfriendlynetmonitor.Assistant.KnownPorts;
import android.app.NotificationManager;
import android.app.TaskStackBuilder;
import android.content.Intent;
import org.secuso.privacyfriendlynetmonitor.Activities.MainActivity;
import android.graphics.BitmapFactory;
import android.os.Build$VERSION;
import android.annotation.TargetApi;
import org.secuso.privacyfriendlynetmonitor.Assistant.RunStore;
import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.app.NotificationCompat;
import android.os.IBinder;
import android.app.Service;

public class PassiveService extends Service
{
    private static final int SERVICE_IDENTIFIER = 1;
    public static boolean mInterrupt;
    private final IBinder mBinder;
    NotificationCompat.Builder mBuilder;
    private Bitmap mIcon;
    private Thread mThread;
    
    public PassiveService() {
        this.mBinder = (IBinder)new AnalyzerBinder();
        this.mBuilder = new NotificationCompat.Builder((Context)this).setSmallIcon(2131230826).setContentTitle(this.getVersionString(2131623976)).setContentText(this.getVersionString(2131623979));
    }
    
    @TargetApi(9)
    private static String getStringNew(final int n) {
        return RunStore.getContext().getResources().getString(n);
    }
    
    @TargetApi(21)
    private static String getStringOld(final int n) {
        return RunStore.getContext().getString(n);
    }
    
    private String getVersionString(final int n) {
        if (Build$VERSION.SDK_INT >= 21) {
            return getStringNew(n);
        }
        return getStringOld(n);
    }
    
    private void interrupt() {
        PassiveService.mInterrupt = true;
        this.stopSelf();
    }
    
    private void loadNotificationBitmaps() {
        this.mIcon = BitmapFactory.decodeResource(this.getResources(), 2131558400);
    }
    
    private void showAppNotification() {
        this.mBuilder.setSmallIcon(2131230826);
        this.mBuilder.setLargeIcon(this.mIcon);
        final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
        final TaskStackBuilder create = TaskStackBuilder.create((Context)this);
        create.addParentStack((Class)MainActivity.class);
        create.addNextIntent(intent);
        this.mBuilder.setContentIntent(create.getPendingIntent(0, 134217728));
        this.startForeground(1, this.mBuilder.build());
    }
    
    private void showWarningNotification() {
        final Intent intent = new Intent((Context)this, (Class)MainActivity.class);
        final TaskStackBuilder create = TaskStackBuilder.create((Context)this);
        create.addParentStack((Class)MainActivity.class);
        create.addNextIntent(intent);
        this.mBuilder.setContentIntent(create.getPendingIntent(0, 134217728));
        ((NotificationManager)this.getSystemService("notification")).notify("NetMonitor", 1, this.mBuilder.build());
    }
    
    public IBinder onBind(final Intent intent) {
        this.startThread();
        return this.mBinder;
    }
    
    public void onCreate() {
        PassiveService.mInterrupt = false;
        this.loadNotificationBitmaps();
        this.showAppNotification();
        KnownPorts.initPortMap();
    }
    
    public void onDestroy() {
        this.interrupt();
        super.onDestroy();
    }
    
    public int onStartCommand(final Intent intent, final int n, final int n2) {
        this.startThread();
        return 1;
    }
    
    public void onTaskRemoved(final Intent intent) {
        this.stopSelf();
    }
    
    public boolean onUnbind(final Intent intent) {
        this.interrupt();
        return super.onUnbind(intent);
    }
    
    public void startThread() {
        Log.i("NetMonitor", "PassiveService - Thread started");
        if (this.mThread != null) {
            this.mThread.interrupt();
        }
        (this.mThread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (!PassiveService.mInterrupt) {
                        Detector.updateReportMap();
                        Collector.updateSettings();
                        if (Collector.isCertVal) {
                            Collector.updateCertVal();
                        }
                        Thread.sleep(1000L);
                    }
                }
                catch (InterruptedException ex) {
                    ex.printStackTrace();
                }
                if (PassiveService.mInterrupt) {
                    PassiveService.this.mThread.interrupt();
                }
                PassiveService.this.stopSelf();
            }
        }, "AnalyzerThreadRunnable")).start();
    }
    
    public class AnalyzerBinder extends Binder
    {
        PassiveService getService() {
            return PassiveService.this;
        }
    }
}
