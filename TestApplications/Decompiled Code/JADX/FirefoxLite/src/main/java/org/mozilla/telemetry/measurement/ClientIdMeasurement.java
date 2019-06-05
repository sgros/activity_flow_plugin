package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.UUID;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class ClientIdMeasurement extends TelemetryMeasurement {
    private String clientId;
    private TelemetryConfiguration configuration;

    public ClientIdMeasurement(TelemetryConfiguration telemetryConfiguration) {
        super("clientId");
        this.configuration = telemetryConfiguration;
    }

    public Object flush() {
        if (this.clientId == null) {
            this.clientId = generateClientId(this.configuration);
        }
        return this.clientId;
    }

    private static synchronized String generateClientId(TelemetryConfiguration telemetryConfiguration) {
        synchronized (ClientIdMeasurement.class) {
            SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
            if (sharedPreferences.contains("client_id")) {
                String string = sharedPreferences.getString("client_id", null);
                return string;
            }
            String uuid = UUID.randomUUID().toString();
            sharedPreferences.edit().putString("client_id", uuid).apply();
            return uuid;
        }
    }
}
