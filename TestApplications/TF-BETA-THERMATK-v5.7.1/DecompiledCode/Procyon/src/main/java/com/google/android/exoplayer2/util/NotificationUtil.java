// 
// Decompiled by Procyon v0.5.34
// 

package com.google.android.exoplayer2.util;

import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Retention;
import java.lang.annotation.Documented;
import java.lang.annotation.Annotation;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.annotation.SuppressLint;

@SuppressLint({ "InlinedApi" })
public final class NotificationUtil
{
    public static final int IMPORTANCE_DEFAULT = 3;
    public static final int IMPORTANCE_HIGH = 4;
    public static final int IMPORTANCE_LOW = 2;
    public static final int IMPORTANCE_MIN = 1;
    public static final int IMPORTANCE_NONE = 0;
    public static final int IMPORTANCE_UNSPECIFIED = -1000;
    
    private NotificationUtil() {
    }
    
    public static void createNotificationChannel(final Context context, final String s, final int n, final int n2) {
        if (Util.SDK_INT >= 26) {
            ((NotificationManager)context.getSystemService("notification")).createNotificationChannel(new NotificationChannel(s, (CharSequence)context.getString(n), n2));
        }
    }
    
    public static void setNotification(final Context context, final int n, final Notification notification) {
        final NotificationManager notificationManager = (NotificationManager)context.getSystemService("notification");
        if (notification != null) {
            notificationManager.notify(n, notification);
        }
        else {
            notificationManager.cancel(n);
        }
    }
    
    @Documented
    @Retention(RetentionPolicy.SOURCE)
    public @interface Importance {
    }
}
