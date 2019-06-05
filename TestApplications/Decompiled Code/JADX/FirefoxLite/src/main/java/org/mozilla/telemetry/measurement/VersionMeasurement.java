package org.mozilla.telemetry.measurement;

public class VersionMeasurement extends StaticMeasurement {
    public VersionMeasurement(int i) {
        super("v", Integer.valueOf(i));
        if (i <= 0) {
            throw new IllegalArgumentException("Version should be a positive integer > 0");
        }
    }
}
