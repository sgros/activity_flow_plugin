package org.mozilla.focus.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build.VERSION;
import android.support.p001v4.app.NotificationCompat.Builder;
import android.support.p001v4.content.ContextCompat;
import android.text.TextUtils;
import org.mozilla.rocket.C0769R;

public class NotificationUtil {

    public enum Channel {
        IMPORTANT,
        PRIVATE
    }

    public static Builder baseBuilder(Context context, Channel channel) {
        String channelD = getChannelD(channel);
        if (TextUtils.isEmpty(channelD)) {
            throw new IllegalStateException("No such channel");
        }
        Builder autoCancel = new Builder(context, channelD).setSmallIcon(2131230896).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), C0769R.mipmap.ic_launcher)).setColor(ContextCompat.getColor(context, C0769R.color.surveyNotificationAccent)).setAutoCancel(true);
        if (VERSION.SDK_INT >= 24) {
            autoCancel.setShowWhen(false);
        }
        return autoCancel;
    }

    public static Builder importantBuilder(Context context) {
        return baseBuilder(context, Channel.IMPORTANT).setDefaults(2).setPriority(1);
    }

    public static void sendNotification(Context context, int i, Builder builder) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.notify(i, builder.build());
        }
    }

    public static void init(Context context) {
        if (VERSION.SDK_INT >= 26) {
            createNotificationChannel(context, "default_channel_id", C0769R.string.app_name, 4);
            createNotificationChannel(context, "private_mode_channel_id", C0769R.string.private_browsing_title, 2);
        }
    }

    private static void createNotificationChannel(Context context, String str, int i, int i2) {
        NotificationManager notificationManager = (NotificationManager) context.getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.createNotificationChannel(new NotificationChannel(str, context.getString(i), i2));
        }
    }

    private static String getChannelD(Channel channel) {
        switch (channel) {
            case IMPORTANT:
                return "default_channel_id";
            case PRIVATE:
                return "private_mode_channel_id";
            default:
                return "";
        }
    }
}
