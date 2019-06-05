// 
// Decompiled by Procyon v0.5.34
// 

package org.mozilla.telemetry.measurement;

import android.os.Build;
import android.os.Build$VERSION;

public class ArchMeasurement extends TelemetryMeasurement
{
    public ArchMeasurement() {
        super("arch");
    }
    
    @Override
    public Object flush() {
        return this.getArchitecture();
    }
    
    String getArchitecture() {
        if (Build$VERSION.SDK_INT >= 21) {
            return Build.SUPPORTED_ABIS[0];
        }
        return Build.CPU_ABI;
    }
}
