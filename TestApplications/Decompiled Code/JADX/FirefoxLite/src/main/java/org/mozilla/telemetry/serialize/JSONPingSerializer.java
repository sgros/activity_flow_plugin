package org.mozilla.telemetry.serialize;

import java.util.Map.Entry;
import org.json.JSONException;
import org.json.JSONObject;
import org.mozilla.telemetry.ping.TelemetryPing;

public class JSONPingSerializer implements TelemetryPingSerializer {
    public String serialize(TelemetryPing telemetryPing) {
        try {
            JSONObject jSONObject = new JSONObject();
            for (Entry entry : telemetryPing.getMeasurementResults().entrySet()) {
                jSONObject.put((String) entry.getKey(), entry.getValue());
            }
            return jSONObject.toString();
        } catch (JSONException e) {
            throw new AssertionError("Can't serialize ping", e);
        }
    }
}
