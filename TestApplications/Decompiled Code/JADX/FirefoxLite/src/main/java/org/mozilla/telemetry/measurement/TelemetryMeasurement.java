package org.mozilla.telemetry.measurement;

public abstract class TelemetryMeasurement {
    private final String fieldName;

    public abstract Object flush();

    public TelemetryMeasurement(String str) {
        this.fieldName = str;
    }

    public String getFieldName() {
        return this.fieldName;
    }
}
