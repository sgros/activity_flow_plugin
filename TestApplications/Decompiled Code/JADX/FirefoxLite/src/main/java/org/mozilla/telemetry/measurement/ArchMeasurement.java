package org.mozilla.telemetry.measurement;

import android.os.Build;
import android.os.Build.VERSION;

public class ArchMeasurement extends TelemetryMeasurement {
    public ArchMeasurement() {
        super("arch");
    }

    public Object flush() {
        return getArchitecture();
    }

    /* Access modifiers changed, original: 0000 */
    public String getArchitecture() {
        if (VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS[0];
        }
        return Build.CPU_ABI;
    }
}
