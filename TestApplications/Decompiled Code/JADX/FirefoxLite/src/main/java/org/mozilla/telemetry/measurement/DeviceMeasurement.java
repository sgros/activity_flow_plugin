package org.mozilla.telemetry.measurement;

import android.os.Build;
import org.mozilla.telemetry.util.StringUtils;

public class DeviceMeasurement extends TelemetryMeasurement {
    public DeviceMeasurement() {
        super("device");
    }

    public Object flush() {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(StringUtils.safeSubstring(getManufacturer(), 0, 12));
        stringBuilder.append('-');
        stringBuilder.append(StringUtils.safeSubstring(getModel(), 0, 19));
        return stringBuilder.toString();
    }

    /* Access modifiers changed, original: 0000 */
    public String getManufacturer() {
        return Build.MANUFACTURER;
    }

    /* Access modifiers changed, original: 0000 */
    public String getModel() {
        return Build.MODEL;
    }
}
