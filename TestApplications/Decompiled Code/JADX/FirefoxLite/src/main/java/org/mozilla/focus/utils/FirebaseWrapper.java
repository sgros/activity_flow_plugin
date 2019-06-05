package org.mozilla.focus.utils;

import android.content.Context;
import android.os.Bundle;
import java.util.HashMap;

abstract class FirebaseWrapper {
    private static FirebaseWrapper instance;

    public interface RemoteConfigFetchCallback {
    }

    static void enableAnalytics(Context context, boolean z) {
    }

    static void enableCloudMessaging(Context context, String str, boolean z) {
    }

    static void enableCrashlytics(Context context, boolean z) {
    }

    static void enableRemoteConfig(Context context, boolean z, RemoteConfigFetchCallback remoteConfigFetchCallback) {
    }

    public static void event(Context context, String str, Bundle bundle) {
    }

    public static String getFcmToken() {
        return "";
    }

    static void setDeveloperModeEnabled(boolean z) {
    }

    static void updateInstanceId(Context context, boolean z) {
    }

    public abstract HashMap<String, Object> getRemoteConfigDefault(Context context);

    public abstract void refreshRemoteConfigDefault(Context context, RemoteConfigFetchCallback remoteConfigFetchCallback);

    FirebaseWrapper() {
    }

    static FirebaseWrapper getInstance() {
        return instance;
    }

    static long getRcLong(Context context, String str) {
        if (instance == null) {
            return 0;
        }
        Object obj = instance.getRemoteConfigDefault(context).get(str);
        if (obj instanceof Integer) {
            return ((Integer) obj).longValue();
        }
        if (obj instanceof Long) {
            return ((Long) obj).longValue();
        }
        return 0;
    }

    static String prettify(String str) {
        return str.replace("<BR>", "\n");
    }

    static String getRcString(Context context, String str) {
        if (instance == null) {
            return "";
        }
        Object obj = instance.getRemoteConfigDefault(context).get(str);
        return obj instanceof String ? (String) obj : "";
    }

    static boolean getRcBoolean(Context context, String str) {
        if (instance == null) {
            return false;
        }
        Object obj = instance.getRemoteConfigDefault(context).get(str);
        if (obj instanceof Boolean) {
            return ((Boolean) obj).booleanValue();
        }
        return false;
    }

    static void initInternal(FirebaseWrapper firebaseWrapper) {
        if (instance == null) {
            instance = firebaseWrapper;
        }
    }
}
