package org.mozilla.telemetry.ping;

import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import org.mozilla.telemetry.config.TelemetryConfiguration;
import org.mozilla.telemetry.measurement.ClientIdMeasurement;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import org.mozilla.telemetry.measurement.VersionMeasurement;

public abstract class TelemetryPingBuilder {
    private TelemetryConfiguration configuration;
    private final List<TelemetryMeasurement> measurements = new LinkedList();
    private final String type;

    public boolean canBuild() {
        return true;
    }

    public TelemetryPingBuilder(TelemetryConfiguration telemetryConfiguration, String str, int i) {
        this.configuration = telemetryConfiguration;
        this.type = str;
        addMeasurement(new VersionMeasurement(i));
        addMeasurement(new ClientIdMeasurement(telemetryConfiguration));
    }

    public TelemetryConfiguration getConfiguration() {
        return this.configuration;
    }

    public String getType() {
        return this.type;
    }

    /* Access modifiers changed, original: protected */
    public void addMeasurement(TelemetryMeasurement telemetryMeasurement) {
        this.measurements.add(telemetryMeasurement);
    }

    public TelemetryPing build() {
        String generateDocumentId = generateDocumentId();
        return new TelemetryPing(getType(), generateDocumentId, getUploadPath(generateDocumentId), flushMeasurements());
    }

    /* Access modifiers changed, original: protected */
    public String getUploadPath(String str) {
        TelemetryConfiguration configuration = getConfiguration();
        return String.format("/submit/telemetry/%s/%s/%s/%s/%s/%s", new Object[]{str, getType(), configuration.getAppName(), configuration.getAppVersion(), configuration.getUpdateChannel(), configuration.getBuildId()});
    }

    private Map<String, Object> flushMeasurements() {
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        for (TelemetryMeasurement telemetryMeasurement : this.measurements) {
            linkedHashMap.put(telemetryMeasurement.getFieldName(), telemetryMeasurement.flush());
        }
        return linkedHashMap;
    }

    public String generateDocumentId() {
        return UUID.randomUUID().toString();
    }
}
