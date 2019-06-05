package org.mozilla.telemetry;

public class TelemetryHolder {
    private static Telemetry telemetry;

    public static void set(Telemetry telemetry) {
        telemetry = telemetry;
    }

    public static Telemetry get() {
        if (telemetry != null) {
            return telemetry;
        }
        throw new IllegalStateException("You need to call set() on TelemetryHolder in your application class");
    }
}
