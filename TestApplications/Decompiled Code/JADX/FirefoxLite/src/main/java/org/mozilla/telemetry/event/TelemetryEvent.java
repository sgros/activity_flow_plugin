package org.mozilla.telemetry.event;

import android.os.SystemClock;
import java.util.HashMap;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONObject;
import org.mozilla.telemetry.TelemetryHolder;
import org.mozilla.telemetry.util.StringUtils;

public class TelemetryEvent {
    private static final long startTime = SystemClock.elapsedRealtime();
    private final String category;
    private final Map<String, Object> extras;
    private final String method;
    private final String object;
    private final long timestamp = (SystemClock.elapsedRealtime() - startTime);
    private String value;

    public static TelemetryEvent create(String str, String str2, String str3, String str4) {
        return new TelemetryEvent(str, str2, str3, str4);
    }

    private TelemetryEvent(String str, String str2, String str3, String str4) {
        this.category = StringUtils.safeSubstring(str, 0, 30);
        this.method = StringUtils.safeSubstring(str2, 0, 20);
        str2 = null;
        if (str3 == null) {
            str = null;
        } else {
            str = StringUtils.safeSubstring(str3, 0, 20);
        }
        this.object = str;
        if (str4 != null) {
            str2 = StringUtils.safeSubstring(str4, 0, 80);
        }
        this.value = str2;
        this.extras = new HashMap();
    }

    public TelemetryEvent extra(String str, String str2) {
        if (this.extras.size() <= 200) {
            this.extras.put(StringUtils.safeSubstring(str, 0, 15), StringUtils.safeSubstring(str2, 0, 80));
            return this;
        }
        throw new IllegalArgumentException("Exceeding limit of 200 extra keys");
    }

    public void queue() {
        TelemetryHolder.get().queueEvent(this);
    }

    public String toJSON() {
        JSONArray jSONArray = new JSONArray();
        jSONArray.put(this.timestamp);
        jSONArray.put(this.category);
        jSONArray.put(this.method);
        jSONArray.put(this.object);
        if (this.value != null) {
            jSONArray.put(this.value);
        }
        if (!(this.extras == null || this.extras.isEmpty())) {
            if (this.value == null) {
                jSONArray.put(null);
            }
            jSONArray.put(new JSONObject(this.extras));
        }
        return jSONArray.toString();
    }
}
