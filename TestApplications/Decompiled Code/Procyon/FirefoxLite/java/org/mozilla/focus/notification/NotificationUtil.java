// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.notification;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build$VERSION;
import android.support.v4.content.ContextCompat;
import android.graphics.BitmapFactory;
import android.text.TextUtils;
import android.support.v4.app.NotificationCompat;
import android.content.Context;

public class NotificationUtil
{
    public static NotificationCompat.Builder baseBuilder(final Context context, final Channel channel) {
        final String channelD = getChannelD(channel);
        if (!TextUtils.isEmpty((CharSequence)channelD)) {
            final NotificationCompat.Builder setAutoCancel = new NotificationCompat.Builder(context, channelD).setSmallIcon(2131230896).setLargeIcon(BitmapFactory.decodeResource(context.getResources(), 2131623936)).setColor(ContextCompat.getColor(context, 2131099879)).setAutoCancel(true);
            if (Build$VERSION.SDK_INT >= 24) {
                setAutoCancel.setShowWhen(false);
            }
            return setAutoCancel;
        }
        throw new IllegalStateException("No such channel");
    }
    
    private static void createNotificationChannel(final Context context, final String s, final int n, final int n2) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (notificationManager == null) {
            return;
        }
        notificationManager.createNotificationChannel(new NotificationChannel(s, (CharSequence)context.getString(n), n2));
    }
    
    private static String getChannelD(final Channel channel) {
        switch (NotificationUtil$1.$SwitchMap$org$mozilla$focus$notification$NotificationUtil$Channel[channel.ordinal()]) {
            default: {
                return "";
            }
            case 2: {
                return "private_mode_channel_id";
            }
            case 1: {
                return "default_channel_id";
            }
        }
    }
    
    public static NotificationCompat.Builder importantBuilder(final Context context) {
        return baseBuilder(context, Channel.IMPORTANT).setDefaults(2).setPriority(1);
    }
    
    public static void init(final Context context) {
        if (Build$VERSION.SDK_INT < 26) {
            return;
        }
        createNotificationChannel(context, "default_channel_id", 2131755062, 4);
        createNotificationChannel(context, "private_mode_channel_id", 2131755362, 2);
    }
    
    public static void sendNotification(final Context context, final int n, final NotificationCompat.Builder builder) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (notificationManager != null) {
            notificationManager.notify(n, builder.build());
        }
    }
    
    public enum Channel
    {
        IMPORTANT, 
        PRIVATE;
    }
}
