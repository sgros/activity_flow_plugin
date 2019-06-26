// 
// Decompiled by Procyon v0.5.34
// 

package org.telegram.messenger;

import android.app.PendingIntent;
import org.telegram.ui.LaunchActivity;
import android.os.IBinder;
import android.content.Intent;
import org.telegram.tgnet.TLRPC;
import androidx.core.app.NotificationManagerCompat;
import java.util.Collection;
import java.util.ArrayList;
import android.os.Handler;
import androidx.core.app.NotificationCompat;
import android.app.Service;

public class LocationSharingService extends Service implements NotificationCenterDelegate
{
    private NotificationCompat.Builder builder;
    private Handler handler;
    private Runnable runnable;
    
    public LocationSharingService() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
    }
    
    private ArrayList<LocationController.SharingLocationInfo> getInfos() {
        final ArrayList<LocationController.SharingLocationInfo> list = new ArrayList<LocationController.SharingLocationInfo>();
        for (int i = 0; i < 3; ++i) {
            final ArrayList<LocationController.SharingLocationInfo> sharingLocationsUI = LocationController.getInstance(i).sharingLocationsUI;
            if (!sharingLocationsUI.isEmpty()) {
                list.addAll((Collection<? extends LocationController.SharingLocationInfo>)sharingLocationsUI);
            }
        }
        return list;
    }
    
    private void updateNotification(final boolean b) {
        if (this.builder == null) {
            return;
        }
        final ArrayList<LocationController.SharingLocationInfo> infos = this.getInfos();
        String s;
        if (infos.size() == 1) {
            final LocationController.SharingLocationInfo sharingLocationInfo = infos.get(0);
            final int i = (int)sharingLocationInfo.messageObject.getDialogId();
            final int currentAccount = sharingLocationInfo.messageObject.currentAccount;
            if (i > 0) {
                s = UserObject.getFirstName(MessagesController.getInstance(currentAccount).getUser(i));
            }
            else {
                final TLRPC.Chat chat = MessagesController.getInstance(currentAccount).getChat(-i);
                if (chat != null) {
                    s = chat.title;
                }
                else {
                    s = "";
                }
            }
        }
        else {
            s = LocaleController.formatPluralString("Chats", infos.size());
        }
        final String format = String.format(LocaleController.getString("AttachLiveLocationIsSharing", 2131558722), LocaleController.getString("AttachLiveLocation", 2131558721), s);
        this.builder.setTicker(format);
        this.builder.setContentText(format);
        if (b) {
            NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(6, this.builder.build());
        }
    }
    
    public void didReceivedNotification(final int n, final int n2, final Object... array) {
        if (n == NotificationCenter.liveLocationsChanged) {
            final Handler handler = this.handler;
            if (handler != null) {
                handler.post((Runnable)new Runnable() {
                    @Override
                    public void run() {
                        if (LocationSharingService.this.getInfos().isEmpty()) {
                            LocationSharingService.this.stopSelf();
                        }
                        else {
                            LocationSharingService.this.updateNotification(true);
                        }
                    }
                });
            }
        }
    }
    
    public IBinder onBind(final Intent intent) {
        return null;
    }
    
    public void onCreate() {
        super.onCreate();
        this.handler = new Handler();
        this.runnable = new Runnable() {
            @Override
            public void run() {
                LocationSharingService.this.handler.postDelayed(LocationSharingService.this.runnable, 60000L);
                Utilities.stageQueue.postRunnable(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 3; ++i) {
                            LocationController.getInstance(i).update();
                        }
                    }
                });
            }
        };
        this.handler.postDelayed(this.runnable, 60000L);
    }
    
    public void onDestroy() {
        final Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        this.stopForeground(true);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
    }
    
    public int onStartCommand(Intent intent, final int n, final int n2) {
        if (this.getInfos().isEmpty()) {
            this.stopSelf();
        }
        if (this.builder == null) {
            intent = new Intent(ApplicationLoader.applicationContext, (Class)LaunchActivity.class);
            intent.setAction("org.tmessages.openlocations");
            intent.addCategory("android.intent.category.LAUNCHER");
            final PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 0);
            (this.builder = new NotificationCompat.Builder(ApplicationLoader.applicationContext)).setWhen(System.currentTimeMillis());
            this.builder.setSmallIcon(2131165534);
            this.builder.setContentIntent(activity);
            NotificationsController.checkOtherNotificationsChannel();
            this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            this.builder.setContentTitle(LocaleController.getString("AppName", 2131558635));
            intent = new Intent(ApplicationLoader.applicationContext, (Class)StopLiveLocationReceiver.class);
            this.builder.addAction(0, LocaleController.getString("StopLiveLocation", 2131560823), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, intent, 134217728));
        }
        this.updateNotification(false);
        this.startForeground(6, this.builder.build());
        return 2;
    }
}
