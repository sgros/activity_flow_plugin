// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.focus.telemetry;

import java.util.Objects;
import android.content.Context;
import java.util.Iterator;
import java.util.Collection;
import org.mozilla.rocket.util.LoggerWrapper;
import android.os.Bundle;
import java.util.HashMap;

class FirebaseEvent
{
    private static HashMap<String, String> prefKeyWhitelist;
    private String eventName;
    private Bundle eventParam;
    
    static {
        FirebaseEvent.prefKeyWhitelist = new HashMap<String, String>();
    }
    
    FirebaseEvent(String string, final String str, final String str2, final String str3) {
        final StringBuilder sb = new StringBuilder();
        sb.append(str);
        sb.append("__");
        sb.append(str2);
        sb.append("__");
        string = sb.toString();
        final StringBuilder sb2 = new StringBuilder();
        sb2.append(string);
        sb2.append(str3);
        this.eventName = sb2.toString();
        final int length = string.length();
        if (this.eventName.length() > 40) {
            if (length > 20) {
                final StringBuilder sb3 = new StringBuilder();
                sb3.append("Event[");
                sb3.append(this.eventName);
                sb3.append("]'s prefixLength too long  ");
                sb3.append(length);
                sb3.append(" of ");
                sb3.append(20);
                LoggerWrapper.throwOrWarn("FirebaseEvent", sb3.toString());
            }
            final StringBuilder sb4 = new StringBuilder();
            sb4.append("Event[");
            sb4.append(this.eventName);
            sb4.append("] exceeds Firebase event name limit ");
            sb4.append(this.eventName.length());
            sb4.append(" of ");
            sb4.append(40);
            LoggerWrapper.throwOrWarn("FirebaseEvent", sb4.toString());
            if (str3 != null) {
                final int length2 = string.length();
                final int length3 = str3.length();
                final StringBuilder sb5 = new StringBuilder();
                sb5.append(string);
                sb5.append(str3.substring(length3 - (40 - length2), length3));
                this.eventName = sb5.toString();
            }
        }
    }
    
    public static FirebaseEvent create(final String s, final String s2, final String s3, final String s4) {
        return new FirebaseEvent(s, s2, s3, s4);
    }
    
    private boolean equalBundles(final Bundle bundle, final Bundle bundle2) {
        if (bundle == bundle2) {
            return true;
        }
        if (bundle == null) {
            return false;
        }
        if (bundle.size() != bundle2.size()) {
            return false;
        }
        if (!bundle.keySet().containsAll(bundle2.keySet())) {
            return false;
        }
        for (final String s : bundle.keySet()) {
            final String s2 = (String)bundle.get(s);
            final String anObject = (String)bundle2.get(s);
            if (s2 == null || !s2.equals(anObject)) {
                return false;
            }
        }
        return true;
    }
    
    static String getValidPrefKey(final String key) {
        return FirebaseEvent.prefKeyWhitelist.get(key);
    }
    
    static boolean isInitialized() {
        return FirebaseEvent.prefKeyWhitelist.size() != 0;
    }
    
    private static String safeParamLength(final String s, final int n) {
        if (s.length() > n) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Exceeding limit of param content length:");
            sb.append(s.length());
            sb.append(" of ");
            sb.append(n);
            LoggerWrapper.throwOrWarn("FirebaseEvent", sb.toString());
        }
        return s.substring(0, Math.min(n, s.length()));
    }
    
    static void setPrefKeyWhitelist(final HashMap<String, String> prefKeyWhitelist) {
        FirebaseEvent.prefKeyWhitelist = prefKeyWhitelist;
    }
    
    @Override
    public boolean equals(final Object o) {
        final boolean b = false;
        if (o == null) {
            return false;
        }
        if (o == this) {
            return true;
        }
        if (!(o instanceof FirebaseEvent)) {
            return false;
        }
        final FirebaseEvent firebaseEvent = (FirebaseEvent)o;
        boolean b2 = b;
        if (this.eventName.equals(firebaseEvent.eventName)) {
            b2 = b;
            if (this.equalBundles(this.eventParam, firebaseEvent.eventParam)) {
                b2 = true;
            }
        }
        return b2;
    }
    
    public void event(final Context context) {
        if (context == null) {
            return;
        }
        if (TelemetryWrapper.isTelemetryEnabled(context)) {
            FirebaseWrapper.event(context.getApplicationContext(), this.eventName, this.eventParam);
        }
    }
    
    @Override
    public int hashCode() {
        return Objects.hash(this.eventName, this.eventParam);
    }
    
    public FirebaseEvent param(final String s, final String s2) {
        if (this.eventParam == null) {
            this.eventParam = new Bundle();
        }
        if (this.eventParam.size() >= 25) {
            final StringBuilder sb = new StringBuilder();
            sb.append("Firebase event[");
            sb.append(this.eventName);
            sb.append("] has too many parameters");
            LoggerWrapper.throwOrWarn("FirebaseEvent", sb.toString());
        }
        this.eventParam.putString(safeParamLength(s, 40), safeParamLength(s2, 100));
        return this;
    }
}
