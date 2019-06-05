package org.mozilla.telemetry.measurement;

import android.os.Build.VERSION;

public class OperatingSystemVersionMeasurement extends StaticMeasurement {
    public OperatingSystemVersionMeasurement() {
        super("osversion", Integer.toString(VERSION.SDK_INT));
    }
}
