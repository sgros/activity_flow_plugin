// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.serialize;

import java.util.Iterator;
import org.json.JSONException;
import java.util.Map;
import org.json.JSONObject;
import org.mozilla.telemetry.ping.TelemetryPing;

public class JSONPingSerializer implements TelemetryPingSerializer
{
    @Override
    public String serialize(final TelemetryPing telemetryPing) {
        try {
            final JSONObject jsonObject = new JSONObject();
            for (final Map.Entry<String, Object> entry : telemetryPing.getMeasurementResults().entrySet()) {
                jsonObject.put((String)entry.getKey(), entry.getValue());
            }
            return jsonObject.toString();
        }
        catch (JSONException cause) {
            throw new AssertionError("Can't serialize ping", (Throwable)cause);
        }
    }
}
