// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.os.IBinder;
import android.content.Intent;
import androidx.core.app.NotificationManagerCompat;
import androidx.core.app.NotificationCompat;
import android.app.Service;

public class VideoEncodingService extends Service implements NotificationCenterDelegate
{
    private NotificationCompat.Builder builder;
    private int currentAccount;
    private int currentProgress;
    private String path;
    
    public VideoEncodingService() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.stopEncodingService);
    }
    
    public void didReceivedNotification(int currentProgress, final int n, final Object... array) {
        final int fileUploadProgressChanged = NotificationCenter.FileUploadProgressChanged;
        boolean b = true;
        if (currentProgress == fileUploadProgressChanged) {
            final String anObject = (String)array[0];
            if (n == this.currentAccount) {
                final String path = this.path;
                if (path != null && path.equals(anObject)) {
                    final Float n2 = (Float)array[1];
                    final Boolean b2 = (Boolean)array[2];
                    this.currentProgress = (int)(n2 * 100.0f);
                    final NotificationCompat.Builder builder = this.builder;
                    currentProgress = this.currentProgress;
                    if (currentProgress != 0) {
                        b = false;
                    }
                    builder.setProgress(100, currentProgress, b);
                    try {
                        NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(4, this.builder.build());
                    }
                    catch (Throwable t) {
                        FileLog.e(t);
                    }
                }
            }
        }
        else if (currentProgress == NotificationCenter.stopEncodingService) {
            final String s = (String)array[0];
            if ((int)array[1] == this.currentAccount && (s == null || s.equals(this.path))) {
                this.stopSelf();
            }
        }
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onDestroy() {
        this.stopForeground(true);
        NotificationManagerCompat.from(ApplicationLoader.applicationContext).cancel(4);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.stopEncodingService);
        NotificationCenter.getInstance(this.currentAccount).removeObserver(this, NotificationCenter.FileUploadProgressChanged);
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("destroy video service");
        }
    }
    
    public int onStartCommand(final Intent intent, int n, final int n2) {
        this.path = intent.getStringExtra("path");
        n = this.currentAccount;
        this.currentAccount = intent.getIntExtra("currentAccount", UserConfig.selectedAccount);
        if (n != this.currentAccount) {
            NotificationCenter.getInstance(n).removeObserver(this, NotificationCenter.FileUploadProgressChanged);
            NotificationCenter.getInstance(this.currentAccount).addObserver(this, NotificationCenter.FileUploadProgressChanged);
        }
        boolean b = false;
        final boolean booleanExtra = intent.getBooleanExtra("gif", false);
        if (this.path == null) {
            this.stopSelf();
            return 2;
        }
        if (BuildVars.LOGS_ENABLED) {
            FileLog.d("start video service");
        }
        if (this.builder == null) {
            NotificationsController.checkOtherNotificationsChannel();
            (this.builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)).setSmallIcon(17301640);
            this.builder.setWhen(System.currentTimeMillis());
            this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            this.builder.setContentTitle(LocaleController.getString("AppName", 2131558635));
            if (booleanExtra) {
                this.builder.setTicker(LocaleController.getString("SendingGif", 2131560712));
                this.builder.setContentText(LocaleController.getString("SendingGif", 2131560712));
            }
            else {
                this.builder.setTicker(LocaleController.getString("SendingVideo", 2131560715));
                this.builder.setContentText(LocaleController.getString("SendingVideo", 2131560715));
            }
        }
        this.currentProgress = 0;
        final NotificationCompat.Builder builder = this.builder;
        n = this.currentProgress;
        if (n == 0) {
            b = true;
        }
        builder.setProgress(100, n, b);
        this.startForeground(4, this.builder.build());
        NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(4, this.builder.build());
        return 2;
    }
}
