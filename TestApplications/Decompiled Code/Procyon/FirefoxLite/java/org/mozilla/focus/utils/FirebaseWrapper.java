// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.utils;

import java.util.HashMap;
import android.os.Bundle;
import android.content.Context;

abstract class FirebaseWrapper
{
    private static FirebaseWrapper instance;
    
    static void enableAnalytics(final Context context, final boolean b) {
    }
    
    static void enableCloudMessaging(final Context context, final String s, final boolean b) {
    }
    
    static void enableCrashlytics(final Context context, final boolean b) {
    }
    
    static void enableRemoteConfig(final Context context, final boolean b, final RemoteConfigFetchCallback remoteConfigFetchCallback) {
    }
    
    public static void event(final Context context, final String s, final Bundle bundle) {
    }
    
    public static String getFcmToken() {
        return "";
    }
    
    static FirebaseWrapper getInstance() {
        return FirebaseWrapper.instance;
    }
    
    static boolean getRcBoolean(final Context context, final String key) {
        if (FirebaseWrapper.instance == null) {
            return false;
        }
        final Boolean value = FirebaseWrapper.instance.getRemoteConfigDefault(context).get(key);
        return value instanceof Boolean && value;
    }
    
    static long getRcLong(final Context context, final String key) {
        if (FirebaseWrapper.instance == null) {
            return 0L;
        }
        final Long value = FirebaseWrapper.instance.getRemoteConfigDefault(context).get(key);
        if (value instanceof Integer) {
            return value;
        }
        if (value instanceof Long) {
            return value;
        }
        return 0L;
    }
    
    static String getRcString(final Context context, final String key) {
        if (FirebaseWrapper.instance == null) {
            return "";
        }
        final String value = FirebaseWrapper.instance.getRemoteConfigDefault(context).get(key);
        if (value instanceof String) {
            return value;
        }
        return "";
    }
    
    static void initInternal(final FirebaseWrapper instance) {
        if (FirebaseWrapper.instance != null) {
            return;
        }
        FirebaseWrapper.instance = instance;
    }
    
    static String prettify(final String s) {
        return s.replace("<BR>", "\n");
    }
    
    static void setDeveloperModeEnabled(final boolean b) {
    }
    
    static void updateInstanceId(final Context context, final boolean b) {
    }
    
    abstract HashMap<String, Object> getRemoteConfigDefault(final Context p0);
    
    abstract void refreshRemoteConfigDefault(final Context p0, final RemoteConfigFetchCallback p1);
    
    public interface RemoteConfigFetchCallback
    {
    }
}
