package org.mozilla.focus.telemetry;

import android.content.Context;
import android.os.Bundle;
import java.util.HashMap;
import java.util.Objects;
import org.mozilla.rocket.util.LoggerWrapper;

class FirebaseEvent {
    private static HashMap<String, String> prefKeyWhitelist = new HashMap();
    private String eventName;
    private Bundle eventParam;

    FirebaseEvent(String str, String str2, String str3, String str4) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(str2);
        stringBuilder.append("__");
        stringBuilder.append(str3);
        stringBuilder.append("__");
        str = stringBuilder.toString();
        StringBuilder stringBuilder2 = new StringBuilder();
        stringBuilder2.append(str);
        stringBuilder2.append(str4);
        this.eventName = stringBuilder2.toString();
        int length = str.length();
        if (this.eventName.length() > 40) {
            if (length > 20) {
                StringBuilder stringBuilder3 = new StringBuilder();
                stringBuilder3.append("Event[");
                stringBuilder3.append(this.eventName);
                stringBuilder3.append("]'s prefixLength too long  ");
                stringBuilder3.append(length);
                stringBuilder3.append(" of ");
                stringBuilder3.append(20);
                LoggerWrapper.throwOrWarn("FirebaseEvent", stringBuilder3.toString());
            }
            StringBuilder stringBuilder4 = new StringBuilder();
            stringBuilder4.append("Event[");
            stringBuilder4.append(this.eventName);
            stringBuilder4.append("] exceeds Firebase event name limit ");
            stringBuilder4.append(this.eventName.length());
            stringBuilder4.append(" of ");
            stringBuilder4.append(40);
            LoggerWrapper.throwOrWarn("FirebaseEvent", stringBuilder4.toString());
            if (str4 != null) {
                int length2 = 40 - str.length();
                length = str4.length();
                stringBuilder4 = new StringBuilder();
                stringBuilder4.append(str);
                stringBuilder4.append(str4.substring(length - length2, length));
                this.eventName = stringBuilder4.toString();
            }
        }
    }

    public static FirebaseEvent create(String str, String str2, String str3, String str4) {
        return new FirebaseEvent(str, str2, str3, str4);
    }

    public FirebaseEvent param(String str, String str2) {
        if (this.eventParam == null) {
            this.eventParam = new Bundle();
        }
        if (this.eventParam.size() >= 25) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Firebase event[");
            stringBuilder.append(this.eventName);
            stringBuilder.append("] has too many parameters");
            LoggerWrapper.throwOrWarn("FirebaseEvent", stringBuilder.toString());
        }
        this.eventParam.putString(safeParamLength(str, 40), safeParamLength(str2, 100));
        return this;
    }

    private static String safeParamLength(String str, int i) {
        if (str.length() > i) {
            StringBuilder stringBuilder = new StringBuilder();
            stringBuilder.append("Exceeding limit of param content length:");
            stringBuilder.append(str.length());
            stringBuilder.append(" of ");
            stringBuilder.append(i);
            LoggerWrapper.throwOrWarn("FirebaseEvent", stringBuilder.toString());
        }
        return str.substring(0, Math.min(i, str.length()));
    }

    public void event(Context context) {
        if (context != null && TelemetryWrapper.isTelemetryEnabled(context)) {
            FirebaseWrapper.event(context.getApplicationContext(), this.eventName, this.eventParam);
        }
    }

    static String getValidPrefKey(String str) {
        return (String) prefKeyWhitelist.get(str);
    }

    static boolean isInitialized() {
        return prefKeyWhitelist.size() != 0;
    }

    static void setPrefKeyWhitelist(HashMap<String, String> hashMap) {
        prefKeyWhitelist = hashMap;
    }

    public boolean equals(Object obj) {
        boolean z = false;
        if (obj == null) {
            return false;
        }
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof FirebaseEvent)) {
            return false;
        }
        FirebaseEvent firebaseEvent = (FirebaseEvent) obj;
        if (this.eventName.equals(firebaseEvent.eventName) && equalBundles(this.eventParam, firebaseEvent.eventParam)) {
            z = true;
        }
        return z;
    }

    private boolean equalBundles(Bundle bundle, Bundle bundle2) {
        if (bundle == bundle2) {
            return true;
        }
        if (bundle == null || bundle.size() != bundle2.size() || !bundle.keySet().containsAll(bundle2.keySet())) {
            return false;
        }
        for (String str : bundle.keySet()) {
            String str2 = (String) bundle.get(str);
            String str3 = (String) bundle2.get(str3);
            if (str2 != null) {
                if (!str2.equals(str3)) {
                }
            }
            return false;
        }
        return true;
    }

    public int hashCode() {
        return Objects.hash(new Object[]{this.eventName, this.eventParam});
    }
}
