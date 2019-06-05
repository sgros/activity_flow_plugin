package org.mozilla.telemetry.measurement;

public class StaticMeasurement extends TelemetryMeasurement {
    private final Object value;

    public StaticMeasurement(String str, Object obj) {
        super(str);
        this.value = obj;
    }

    public Object flush() {
        return this.value;
    }
}
