// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.event;

import org.json.JSONObject;
import org.json.JSONArray;
import org.mozilla.telemetry.TelemetryHolder;
import java.util.HashMap;
import org.mozilla.telemetry.util.StringUtils;
import android.os.SystemClock;
import java.util.Map;

public class TelemetryEvent
{
    private static final long startTime;
    private final String category;
    private final Map<String, Object> extras;
    private final String method;
    private final String object;
    private final long timestamp;
    private String value;
    
    static {
        startTime = SystemClock.elapsedRealtime();
    }
    
    private TelemetryEvent(String s, final String s2, final String s3, final String s4) {
        this.timestamp = SystemClock.elapsedRealtime() - TelemetryEvent.startTime;
        this.category = StringUtils.safeSubstring(s, 0, 30);
        this.method = StringUtils.safeSubstring(s2, 0, 20);
        final String s5 = null;
        if (s3 == null) {
            s = null;
        }
        else {
            s = StringUtils.safeSubstring(s3, 0, 20);
        }
        this.object = s;
        if (s4 == null) {
            s = s5;
        }
        else {
            s = StringUtils.safeSubstring(s4, 0, 80);
        }
        this.value = s;
        this.extras = new HashMap<String, Object>();
    }
    
    public static TelemetryEvent create(final String s, final String s2, final String s3, final String s4) {
        return new TelemetryEvent(s, s2, s3, s4);
    }
    
    public TelemetryEvent extra(final String s, final String s2) {
        if (this.extras.size() <= 200) {
            this.extras.put(StringUtils.safeSubstring(s, 0, 15), StringUtils.safeSubstring(s2, 0, 80));
            return this;
        }
        throw new IllegalArgumentException("Exceeding limit of 200 extra keys");
    }
    
    public void queue() {
        TelemetryHolder.get().queueEvent(this);
    }
    
    public String toJSON() {
        final JSONArray jsonArray = new JSONArray();
        jsonArray.put(this.timestamp);
        jsonArray.put((Object)this.category);
        jsonArray.put((Object)this.method);
        jsonArray.put((Object)this.object);
        if (this.value != null) {
            jsonArray.put((Object)this.value);
        }
        if (this.extras != null && !this.extras.isEmpty()) {
            if (this.value == null) {
                jsonArray.put((Object)null);
            }
            jsonArray.put((Object)new JSONObject((Map)this.extras));
        }
        return jsonArray.toString();
    }
}
