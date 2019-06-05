// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.os.Build;
import org.mozilla.telemetry.util.StringUtils;

public class DeviceMeasurement extends TelemetryMeasurement
{
    public DeviceMeasurement() {
        super("device");
    }
    
    @Override
    public Object flush() {
        final StringBuilder sb = new StringBuilder();
        sb.append(StringUtils.safeSubstring(this.getManufacturer(), 0, 12));
        sb.append('-');
        sb.append(StringUtils.safeSubstring(this.getModel(), 0, 19));
        return sb.toString();
    }
    
    String getManufacturer() {
        return Build.MANUFACTURER;
    }
    
    String getModel() {
        return Build.MODEL;
    }
}
