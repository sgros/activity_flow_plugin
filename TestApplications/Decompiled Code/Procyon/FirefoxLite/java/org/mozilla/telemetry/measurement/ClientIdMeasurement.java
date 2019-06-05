// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.content.SharedPreferences;
import java.util.UUID;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public class ClientIdMeasurement extends TelemetryMeasurement
{
    private String clientId;
    private TelemetryConfiguration configuration;
    
    public ClientIdMeasurement(final TelemetryConfiguration configuration) {
        super("clientId");
        this.configuration = configuration;
    }
    
    private static String generateClientId(final TelemetryConfiguration telemetryConfiguration) {
        synchronized (ClientIdMeasurement.class) {
            final SharedPreferences sharedPreferences = telemetryConfiguration.getSharedPreferences();
            if (sharedPreferences.contains("client_id")) {
                return sharedPreferences.getString("client_id", (String)null);
            }
            final String string = UUID.randomUUID().toString();
            sharedPreferences.edit().putString("client_id", string).apply();
            return string;
        }
    }
    
    @Override
    public Object flush() {
        if (this.clientId == null) {
            this.clientId = generateClientId(this.configuration);
        }
        return this.clientId;
    }
}
