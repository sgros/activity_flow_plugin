// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.ping;

import java.util.UUID;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import org.mozilla.telemetry.measurement.ClientIdMeasurement;
import org.mozilla.telemetry.measurement.VersionMeasurement;
import java.util.LinkedList;
import org.mozilla.telemetry.measurement.TelemetryMeasurement;
import java.util.List;
import org.mozilla.telemetry.config.TelemetryConfiguration;

public abstract class TelemetryPingBuilder
{
    private TelemetryConfiguration configuration;
    private final List<TelemetryMeasurement> measurements;
    private final String type;
    
    public TelemetryPingBuilder(final TelemetryConfiguration configuration, final String type, final int n) {
        this.configuration = configuration;
        this.type = type;
        this.measurements = new LinkedList<TelemetryMeasurement>();
        this.addMeasurement(new VersionMeasurement(n));
        this.addMeasurement(new ClientIdMeasurement(configuration));
    }
    
    private Map<String, Object> flushMeasurements() {
        final LinkedHashMap<String, Object> linkedHashMap = new LinkedHashMap<String, Object>();
        for (final TelemetryMeasurement telemetryMeasurement : this.measurements) {
            linkedHashMap.put(telemetryMeasurement.getFieldName(), telemetryMeasurement.flush());
        }
        return linkedHashMap;
    }
    
    protected void addMeasurement(final TelemetryMeasurement telemetryMeasurement) {
        this.measurements.add(telemetryMeasurement);
    }
    
    public TelemetryPing build() {
        final String generateDocumentId = this.generateDocumentId();
        return new TelemetryPing(this.getType(), generateDocumentId, this.getUploadPath(generateDocumentId), this.flushMeasurements());
    }
    
    public boolean canBuild() {
        return true;
    }
    
    public String generateDocumentId() {
        return UUID.randomUUID().toString();
    }
    
    public TelemetryConfiguration getConfiguration() {
        return this.configuration;
    }
    
    public String getType() {
        return this.type;
    }
    
    protected String getUploadPath(final String s) {
        final TelemetryConfiguration configuration = this.getConfiguration();
        return String.format("/submit/telemetry/%s/%s/%s/%s/%s/%s", s, this.getType(), configuration.getAppName(), configuration.getAppVersion(), configuration.getUpdateChannel(), configuration.getBuildId());
    }
}
