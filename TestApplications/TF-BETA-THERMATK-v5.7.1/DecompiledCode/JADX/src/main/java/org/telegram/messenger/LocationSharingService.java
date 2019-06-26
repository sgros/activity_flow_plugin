package org.telegram.messenger;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.Handler;
import android.os.IBinder;
import androidx.core.app.NotificationCompat.Builder;
import androidx.core.app.NotificationManagerCompat;
import java.util.ArrayList;
import org.telegram.messenger.LocationController.SharingLocationInfo;
import org.telegram.messenger.NotificationCenter.NotificationCenterDelegate;
import org.telegram.p004ui.LaunchActivity;
import org.telegram.tgnet.TLRPC.Chat;

public class LocationSharingService extends Service implements NotificationCenterDelegate {
    private Builder builder;
    private Handler handler;
    private Runnable runnable;

    /* renamed from: org.telegram.messenger.LocationSharingService$1 */
    class C10391 implements Runnable {

        /* renamed from: org.telegram.messenger.LocationSharingService$1$1 */
        class C10381 implements Runnable {
            C10381() {
            }

            public void run() {
                for (int i = 0; i < 3; i++) {
                    LocationController.getInstance(i).update();
                }
            }
        }

        C10391() {
        }

        public void run() {
            LocationSharingService.this.handler.postDelayed(LocationSharingService.this.runnable, 60000);
            Utilities.stageQueue.postRunnable(new C10381());
        }
    }

    /* renamed from: org.telegram.messenger.LocationSharingService$2 */
    class C10402 implements Runnable {
        C10402() {
        }

        public void run() {
            if (LocationSharingService.this.getInfos().isEmpty()) {
                LocationSharingService.this.stopSelf();
            } else {
                LocationSharingService.this.updateNotification(true);
            }
        }
    }

    public IBinder onBind(Intent intent) {
        return null;
    }

    public LocationSharingService() {
        NotificationCenter.getGlobalInstance().addObserver(this, NotificationCenter.liveLocationsChanged);
    }

    public void onCreate() {
        super.onCreate();
        this.handler = new Handler();
        this.runnable = new C10391();
        this.handler.postDelayed(this.runnable, 60000);
    }

    public void onDestroy() {
        Handler handler = this.handler;
        if (handler != null) {
            handler.removeCallbacks(this.runnable);
        }
        stopForeground(true);
        NotificationCenter.getGlobalInstance().removeObserver(this, NotificationCenter.liveLocationsChanged);
    }

    public void didReceivedNotification(int i, int i2, Object... objArr) {
        if (i == NotificationCenter.liveLocationsChanged) {
            Handler handler = this.handler;
            if (handler != null) {
                handler.post(new C10402());
            }
        }
    }

    private ArrayList<SharingLocationInfo> getInfos() {
        ArrayList arrayList = new ArrayList();
        for (int i = 0; i < 3; i++) {
            ArrayList arrayList2 = LocationController.getInstance(i).sharingLocationsUI;
            if (!arrayList2.isEmpty()) {
                arrayList.addAll(arrayList2);
            }
        }
        return arrayList;
    }

    private void updateNotification(boolean z) {
        if (this.builder != null) {
            String firstName;
            ArrayList infos = getInfos();
            if (infos.size() == 1) {
                SharingLocationInfo sharingLocationInfo = (SharingLocationInfo) infos.get(0);
                int dialogId = (int) sharingLocationInfo.messageObject.getDialogId();
                int i = sharingLocationInfo.messageObject.currentAccount;
                if (dialogId > 0) {
                    firstName = UserObject.getFirstName(MessagesController.getInstance(i).getUser(Integer.valueOf(dialogId)));
                } else {
                    Chat chat = MessagesController.getInstance(i).getChat(Integer.valueOf(-dialogId));
                    firstName = chat != null ? chat.title : "";
                }
            } else {
                firstName = LocaleController.formatPluralString("Chats", infos.size());
            }
            firstName = String.format(LocaleController.getString("AttachLiveLocationIsSharing", C1067R.string.AttachLiveLocationIsSharing), new Object[]{LocaleController.getString("AttachLiveLocation", C1067R.string.AttachLiveLocation), firstName});
            this.builder.setTicker(firstName);
            this.builder.setContentText(firstName);
            if (z) {
                NotificationManagerCompat.from(ApplicationLoader.applicationContext).notify(6, this.builder.build());
            }
        }
    }

    public int onStartCommand(Intent intent, int i, int i2) {
        if (getInfos().isEmpty()) {
            stopSelf();
        }
        if (this.builder == null) {
            intent = new Intent(ApplicationLoader.applicationContext, LaunchActivity.class);
            intent.setAction("org.tmessages.openlocations");
            intent.addCategory("android.intent.category.LAUNCHER");
            PendingIntent activity = PendingIntent.getActivity(ApplicationLoader.applicationContext, 0, intent, 0);
            this.builder = new Builder(ApplicationLoader.applicationContext);
            this.builder.setWhen(System.currentTimeMillis());
            this.builder.setSmallIcon(C1067R.C1065drawable.live_loc);
            this.builder.setContentIntent(activity);
            NotificationsController.checkOtherNotificationsChannel();
            this.builder.setChannelId(NotificationsController.OTHER_NOTIFICATIONS_CHANNEL);
            this.builder.setContentTitle(LocaleController.getString("AppName", C1067R.string.AppName));
            this.builder.addAction(0, LocaleController.getString("StopLiveLocation", C1067R.string.StopLiveLocation), PendingIntent.getBroadcast(ApplicationLoader.applicationContext, 2, new Intent(ApplicationLoader.applicationContext, StopLiveLocationReceiver.class), 134217728));
        }
        updateNotification(false);
        startForeground(6, this.builder.build());
        return 2;
    }
}
